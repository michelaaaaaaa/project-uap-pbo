package com.kasirmini.kasirmini;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
            showAlert("Peringatan", "Username dan password tidak boleh kosong", Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = DBConnect.getKoneksi()) {

            if (conn == null) {
                showAlert("Error", "Koneksi database gagal!", Alert.AlertType.ERROR);
                return;
            }

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUsers = rs.getInt("id_users");
                String nama = rs.getString("nama");
                String role = rs.getString("role");

                // ==== INHERITANCE + POLYMORPHISM DI SINI ====
                UserBase userObj;

                if (role.equalsIgnoreCase("admin")) {
                    userObj = new AdminUser(idUsers, user, nama, role);
                } else {
                    userObj = new KasirUser(idUsers, user, nama, role);
                }

                // simpan informasi user di session (optional tapi berguna)
                SessionData.setIdUsers(idUsers);
                SessionData.setUsername(user);
                SessionData.setNama(nama);
                SessionData.setRole(role);

                // POLYMORPHISM: panggil method override
                String targetFxml = userObj.getHomeFxml();
                App.setRoot(targetFxml);

            } else {
                showAlert("Login Gagal", "Username atau password salah!", Alert.AlertType.ERROR);
                txtPassword.clear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error DB", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
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
