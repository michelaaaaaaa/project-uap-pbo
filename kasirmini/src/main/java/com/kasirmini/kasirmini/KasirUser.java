package com.kasirmini.kasirmini;

public class KasirUser extends UserBase {

    public KasirUser(int id, String username, String nama, String role) {
        super(id, username, nama, role);
    }

    @Override
    public String getHomeFxml() {
        return "kasir_menu";
    }
}
