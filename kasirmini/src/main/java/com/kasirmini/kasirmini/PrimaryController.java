package com.kasirmini.kasirmini;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrimaryController {

    @FXML
    private Label labelWelcome;

    @FXML
    private Button btnBarang;

    @FXML
    public void initialize() {
        labelWelcome.setText("Selamat Datang di KasirMini!");
    }

    @FXML
    private void goBarang() throws IOException {
        App.setRoot("barang");
    }

    @FXML
    private void goTransaksi() {
        System.out.println("Menu Transaksi dipilih");
    }

    @FXML
    private void goLaporan() {
        System.out.println("Menu Laporan dipilih");
    }

    @FXML
    private void logout() throws IOException {
        App.setRoot("login");
    }
}
