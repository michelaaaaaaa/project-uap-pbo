package com.kasirmini.kasirmini;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminController {

    @FXML
    private Label lblWelcomeAdmin;

    @FXML
    public void initialize() {
        String nama = SessionData.getNama();
        lblWelcomeAdmin.setText("Selamat datang, " + (nama != null ? nama : "Admin"));
    }

    @FXML
    private void goKelolaBarang() {
        try {
            App.setRoot("barang");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goLaporan() {
        try {
            App.setRoot("laporan_admin"); // buka layar laporan admin
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
