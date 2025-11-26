package com.kasirmini.kasirmini;

public class AdminUser extends UserBase {

    public AdminUser(int id, String username, String nama, String role) {
        super(id, username, nama, role);
    }

    @Override
    public String getHomeFxml() {
        return "admin_menu";
    }
}
