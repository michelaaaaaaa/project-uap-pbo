package com.kasirmini.kasirmini;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LaporanAdminController {

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

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> data.getValue().idBarangProperty());
        colNama.setCellValueFactory(data -> data.getValue().namaBarangProperty());
        colHarga.setCellValueFactory(data -> data.getValue().hargaProperty());
        colStok.setCellValueFactory(data -> data.getValue().stokProperty());

        tblBarang.setItems(listBarang);

        loadBarangFromDB();
    }

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
            showAlert("Error DB", "Gagal ambil data barang: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void exportBarangToText() {
        String filename = "laporan_barang.txt";

        try (Connection conn = DBConnect.getKoneksi();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT nama_barang, harga, stok FROM barang");
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

            writer.write("LAPORAN DATA BARANG");
            writer.newLine();
            writer.write("=========================");
            writer.newLine();

            while (rs.next()) {
                String line = rs.getString("nama_barang") + " | " +
                              rs.getDouble("harga") + " | stok: " +
                              rs.getInt("stok");
                writer.write(line);
                writer.newLine();
            }

            writer.flush();

            showAlert("Laporan", "Laporan berhasil disimpan ke file:\n" + filename,
                    Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            showAlert("Error DB", "Gagal ambil data barang: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            showAlert("Error File", "Gagal menulis file: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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
