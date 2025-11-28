package com.kasirmini.kasirmini;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RiwayatTransaksiController {

    @FXML
    private TableView<TransaksiRiwayatModel> tblRiwayat;

    @FXML
    private TableColumn<TransaksiRiwayatModel, Number> colIdTrans;

    @FXML
    private TableColumn<TransaksiRiwayatModel, String> colTanggal;

    @FXML
    private TableColumn<TransaksiRiwayatModel, String> colKasir;

    @FXML
    private TableColumn<TransaksiRiwayatModel, Number> colTotal;

    private ObservableList<TransaksiRiwayatModel> listRiwayat = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colIdTrans.setCellValueFactory(data -> data.getValue().idTransaksiProperty());
        colTanggal.setCellValueFactory(data -> data.getValue().tanggalProperty());
        colKasir.setCellValueFactory(data -> data.getValue().namaKasirProperty());
        colTotal.setCellValueFactory(data -> data.getValue().totalProperty());

        tblRiwayat.setItems(listRiwayat);

        loadRiwayatFromDB();
    }

    private void loadRiwayatFromDB() {
        listRiwayat.clear();
        try (Connection conn = DBConnect.getKoneksi();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT t.id_transaksi, t.tanggal, t.total, u.nama " +
                "FROM transaksi t JOIN users u ON t.id_users = u.id_users " +
                "ORDER BY t.tanggal DESC"
             )) {

            while (rs.next()) {
                TransaksiRiwayatModel tr = new TransaksiRiwayatModel(
                        rs.getInt("id_transaksi"),
                        rs.getTimestamp("tanggal").toString(),
                        rs.getString("nama"),
                        rs.getDouble("total")
                );
                listRiwayat.add(tr);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error DB", "Gagal ambil data riwayat: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void exportRiwayat() {
        String filename = "riwayat_transaksi.txt";

        try (Connection conn = DBConnect.getKoneksi();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT t.id_transaksi, t.tanggal, t.total, u.nama " +
                 "FROM transaksi t JOIN users u ON t.id_users = u.id_users " +
                 "ORDER BY t.tanggal DESC"
             );
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename))
        ) {

            writer.write("=== RIWAYAT TRANSAKSI ===");
            writer.newLine();
            writer.newLine();

            while (rs.next()) {
                writer.write("ID Transaksi : " + rs.getInt("id_transaksi"));
                writer.newLine();
                writer.write("Tanggal      : " + rs.getTimestamp("tanggal"));
                writer.newLine();
                writer.write("Kasir        : " + rs.getString("nama"));
                writer.newLine();
                writer.write("Total        : Rp " + rs.getDouble("total"));
                writer.newLine();
                writer.write("---------------------------------------------");
                writer.newLine();
            }

            writer.flush();

            showAlert("Export Berhasil",
                    "Riwayat transaksi berhasil diexport ke file:\n" + filename,
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error Export", "Terjadi kesalahan: " + e.getMessage(), Alert.AlertType.ERROR);
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

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
