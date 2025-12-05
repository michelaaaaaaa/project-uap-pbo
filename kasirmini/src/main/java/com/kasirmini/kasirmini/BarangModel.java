package com.kasirmini.kasirmini;

import javafx.beans.property.*;

public class BarangModel {

    private final IntegerProperty idBarang;
    private final StringProperty namaBarang;
    private final DoubleProperty harga;
    private final IntegerProperty stok;

    public BarangModel(int idBarang, String namaBarang, double harga, int stok) {
        this.idBarang = new SimpleIntegerProperty(idBarang);
        this.namaBarang = new SimpleStringProperty(namaBarang);
        this.harga = new SimpleDoubleProperty(harga);
        this.stok = new SimpleIntegerProperty(stok);
    }

    public int getIdBarang() {
        return idBarang.get();
    }

    public IntegerProperty idBarangProperty() {
        return idBarang;
    }

    public String getNamaBarang() {
        return namaBarang.get();
    }

    public StringProperty namaBarangProperty() {
        return namaBarang;
    }

    public double getHarga() {
        return harga.get();
    }

    public DoubleProperty hargaProperty() {
        return harga;
    }

    public int getStok() {
        return stok.get();
    }

    public IntegerProperty stokProperty() {
        return stok;
    }

    public void setStok(int s) {
        stok.set(s);
    }
}
