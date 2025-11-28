package com.kasirmini.kasirmini;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TransaksiController {

    @FXML
    private TableView<BarangModel> tblBarang;

    @FXML
    private TableColumn<BarangModel, String> colNamaBarang;

    @FXML
    private TableColumn<BarangModel, Number> colHargaBarang;

    @FXML
    private TableColumn<BarangModel, Number> colStokBarang;

    @FXML
    private TableView<CartItem> tblKeranjang;

    @FXML
    private TableColumn<CartItem, String> colNamaKeranjang;

    @FXML
    private TableColumn<CartItem, Number> colQtyKeranjang;

    @FXML
    private TableColumn<CartItem, Number> colHargaKeranjang;

    @FXML
    private TableColumn<CartItem, Number> colSubtotalKeranjang;

    @FXML
    private TextField txtQty;

    @FXML
    private Label lblTotal;

    private ObservableList<BarangModel> listBarang = FXCollections.observableArrayList();
    private ObservableList<CartItem> listKeranjang = FXCollections.observableArrayList();
    private double total = 0.0;

    @FXML
    public void initialize() {
        colNamaBarang.setCellValueFactory(data -> data.getValue().namaBarangProperty());
        colHargaBarang.setCellValueFactory(data -> data.getValue().hargaProperty());
        colStokBarang.setCellValueFactory(data -> data.getValue().stokProperty());

        colNamaKeranjang.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaBarang()));
        colQtyKeranjang.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQty()));
        colHargaKeranjang.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getHargaSatuan()));
        colSubtotalKeranjang.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getSubtotal()));

        tblBarang.setItems(listBarang);
        tblKeranjang.setItems(listKeranjang);

        loadBarang();
        updateTotalLabel();
    }

    private void loadBarang() {
        listBarang.clear();
        try (Connection conn = DBConnect.getKoneksi();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM barang")) {

            while (rs.next()) {
                BarangModel b = new BarangModel(
                        rs.getInt("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getDouble("harga"),
                        rs.getInt("stok")
                );
                listBarang.add(b);
            }

        } catch (SQLException e) {
            showAlert("Error DB", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void tambahKeKeranjang() {
        BarangModel selected = tblBarang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih barang terlebih dahulu", Alert.AlertType.WARNING);
            return;
        }

        if (txtQty.getText().isEmpty()) {
            showAlert("Peringatan", "Masukkan qty", Alert.AlertType.WARNING);
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(txtQty.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Qty harus angka", Alert.AlertType.ERROR);
            return;
        }

        if (qty <= 0) {
            showAlert("Error", "Qty harus lebih dari 0", Alert.AlertType.ERROR);
            return;
        }

        if (qty > selected.getStok()) {
            showAlert("Error", "Stok tidak mencukupi", Alert.AlertType.ERROR);
            return;
        }

        CartItem item = new CartItem(selected, qty);
        listKeranjang.add(item);
        total += item.getSubtotal();
        updateTotalLabel();

        txtQty.clear();
    }

    @FXML
    private void hapusDariKeranjang() {
        CartItem selected = tblKeranjang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih item di keranjang", Alert.AlertType.WARNING);
            return;
        }
        total -= selected.getSubtotal();
        listKeranjang.remove(selected);
        updateTotalLabel();
    }

    @FXML
    private void simpanTransaksi() {
        if (listKeranjang.isEmpty()) {
            showAlert("Peringatan", "Keranjang masih kosong", Alert.AlertType.WARNING);
            return;
        }

        // Konfirmasi
        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
        konfirmasi.setTitle("Konfirmasi");
        konfirmasi.setHeaderText(null);
        konfirmasi.setContentText("Simpan transaksi dan kurangi stok?");
        Optional<ButtonType> result = konfirmasi.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try (Connection conn = DBConnect.getKoneksi()) {

            if (conn == null) {
                showAlert("Error", "Koneksi database gagal", Alert.AlertType.ERROR);
                return;
            }

            conn.setAutoCommit(false);

            // Insert transaksi
            String sqlTrans = "INSERT INTO transaksi (tanggal, total, id_users) VALUES (?, ?, ?)";
            PreparedStatement psTrans = conn.prepareStatement(sqlTrans, Statement.RETURN_GENERATED_KEYS);
            psTrans.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            psTrans.setDouble(2, total);
            psTrans.setInt(3, SessionData.getIdUsers());
            psTrans.executeUpdate();

            ResultSet rsKey = psTrans.getGeneratedKeys();
            int idTransaksi = 0;
            if (rsKey.next()) {
                idTransaksi = rsKey.getInt(1);
            }

            // Insert detail + update stok
            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_barang, qty, harga_satuan, subtotal) VALUES (?, ?, ?, ?, ?)";
            String sqlUpdateStok = "UPDATE barang SET stok = stok - ? WHERE id_barang = ?";

            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            PreparedStatement psStok = conn.prepareStatement(sqlUpdateStok);

            for (CartItem item : listKeranjang) {
                BarangModel b = item.getBarang();

                // detail
                psDetail.setInt(1, idTransaksi);
                psDetail.setInt(2, b.getIdBarang());
                psDetail.setInt(3, item.getQty());
                psDetail.setDouble(4, item.getHargaSatuan());
                psDetail.setDouble(5, item.getSubtotal());
                psDetail.addBatch();

                // stok
                psStok.setInt(1, item.getQty());
                psStok.setInt(2, b.getIdBarang());
                psStok.addBatch();
            }

            psDetail.executeBatch();
            psStok.executeBatch();

            conn.commit();

            showAlert("Sukses", "Transaksi berhasil disimpan", Alert.AlertType.INFORMATION);

            listKeranjang.clear();
            total = 0.0;
            updateTotalLabel();
            loadBarang();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error DB", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void kembaliMenuKasir() {
        try {
            App.setRoot("kasir_menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTotalLabel() {
        lblTotal.setText(String.format("Total: Rp %.0f", total));
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
