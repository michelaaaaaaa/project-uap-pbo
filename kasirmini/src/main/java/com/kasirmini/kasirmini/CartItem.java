package com.kasirmini.kasirmini;

public class CartItem {

    private BarangModel barang;
    private int qty;
    private double hargaSatuan;
    private double subtotal;

    public CartItem(BarangModel barang, int qty) {
        this.barang = barang;
        this.qty = qty;
        this.hargaSatuan = barang.getHarga();
        this.subtotal = this.hargaSatuan * qty;
    }

    public BarangModel getBarang() {
        return barang;
    }

    public int getQty() {
        return qty;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public String getNamaBarang() {
        return barang.getNamaBarang();
    }
}
