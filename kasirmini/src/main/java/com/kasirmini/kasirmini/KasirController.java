package com.kasirmini.kasirmini;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KasirController {

    @FXML
    private Label lblWelcomeKasir;

    @FXML
    public void initialize() {
        String nama = SessionData.getNama();
        lblWelcomeKasir.setText("Selamat datang, " + (nama != null ? nama : "Kasir"));
    }

    @FXML
    private void goTransaksi() {
        try {
            App.setRoot("transaksi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goRiwayat() {
        try {
            App.setRoot("riwayat_transaksi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        try {
            SessionData.clear();
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
