package com.kasirmini.kasirmini;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BarangController {

    @FXML
    private TextField txtNamaBarang;

    @FXML
    private TextField txtHarga;

    @FXML
    private TextField txtStok;

    @FXML
    private TableView<BarangModel> tblBarang;

    @FXML
    private TableColumn<BarangModel, Number> colId;

    @FXML
    private TableColumn<BarangModel, String> colNama;

    @FXML
    private TableColumn<BarangModel, Number> colHarga;

    @FXML
    private TableColumn<BarangModel, Number> colStok;

    private ObservableList<BarangModel> listBarang = FXCollections.observableArrayList();
    private BarangModel selectedBarang = null;

    //inisialisaisi uinyaa yaang menghubungkan kolom tabel dengan properti di barang model
    
    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> data.getValue().idBarangProperty());
        colNama.setCellValueFactory(data -> data.getValue().namaBarangProperty());
        colHarga.setCellValueFactory(data -> data.getValue().hargaProperty());
        colStok.setCellValueFactory(data -> data.getValue().stokProperty());

        tblBarang.setItems(listBarang);

        tblBarang.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedBarang = newVal;
                txtNamaBarang.setText(newVal.getNamaBarang());
                txtHarga.setText(String.valueOf(newVal.getHarga()));
                txtStok.setText(String.valueOf(newVal.getStok()));
            }
        });

        loadBarangFromDB();
    }

    //mengambil data dari database
    private void loadBarangFromDB() {
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
    private void tambahBarang() {
        String nama = txtNamaBarang.getText();
        String hargaStr = txtHarga.getText();
        String stokStr = txtStok.getText();

        if (nama.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty()) {
            showAlert("Peringatan", "Semua field harus diisi", Alert.AlertType.WARNING);
            return;
        }

        try {
            double harga = Double.parseDouble(hargaStr);
            int stok = Integer.parseInt(stokStr);

            String sql = "INSERT INTO barang (nama_barang, harga, stok) VALUES (?, ?, ?)";

            try (Connection conn = DBConnect.getKoneksi();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nama);
                ps.setDouble(2, harga);
                ps.setInt(3, stok);
                ps.executeUpdate();
            }

            clearForm();
            loadBarangFromDB();

        } catch (NumberFormatException e) {
            showAlert("Error", "Harga dan stok harus angka", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Error DB", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateBarang() {
        if (selectedBarang == null) {
            showAlert("Peringatan", "Pilih barang yang akan diupdate", Alert.AlertType.WARNING);
            return;
        }

        String nama = txtNamaBarang.getText();
        String hargaStr = txtHarga.getText();
        String stokStr = txtStok.getText();

        if (nama.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty()) {
            showAlert("Peringatan", "Semua field harus diisi", Alert.AlertType.WARNING);
            return;
        }

        try {
            double harga = Double.parseDouble(hargaStr);
            int stok = Integer.parseInt(stokStr);

            String sql = "UPDATE barang SET nama_barang = ?, harga = ?, stok = ? WHERE id_barang = ?";

            try (Connection conn = DBConnect.getKoneksi();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nama);
                ps.setDouble(2, harga);
                ps.setInt(3, stok);
                ps.setInt(4, selectedBarang.getIdBarang());
                ps.executeUpdate();
            }

            clearForm();
            loadBarangFromDB();

        } catch (NumberFormatException e) {
            showAlert("Error", "Harga dan stok harus angka", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Error DB", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void hapusBarang() {
        if (selectedBarang == null) {
            showAlert("Peringatan", "Pilih barang yang akan dihapus", Alert.AlertType.WARNING);
            return;
        }

        String sql = "DELETE FROM barang WHERE id_barang = ?";

        try (Connection conn = DBConnect.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, selectedBarang.getIdBarang());
            ps.executeUpdate();

            clearForm();
            loadBarangFromDB();

        } catch (SQLException e) {
            // Kode error 1451 = foreign key constraint (MySQL)
            if (e.getErrorCode() == 1451) {
                showAlert(
                    "Error DB",
                    "Barang ini sudah pernah dipakai dalam transaksi,\n" +
                    "jadi tidak boleh dihapus untuk menjaga riwayat data.",
                    Alert.AlertType.ERROR
                );
            } else {
                showAlert("Error DB", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void clearForm() {
        selectedBarang = null;
        txtNamaBarang.clear();
        txtHarga.clear();
        txtStok.clear();
        tblBarang.getSelectionModel().clearSelection();
    }

    @FXML
    private void kembaliMenuAdmin() {
        try {
            App.setRoot("admin_menu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
