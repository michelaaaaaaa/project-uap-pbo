package com.kasirmini.kasirmini;

import javafx.beans.property.*;

public class TransaksiRiwayatModel {

    private final IntegerProperty idTransaksi;
    private final StringProperty tanggal;
    private final StringProperty namaKasir;
    private final DoubleProperty total;

    public TransaksiRiwayatModel(int idTransaksi, String tanggal, String namaKasir, double total) {
        this.idTransaksi = new SimpleIntegerProperty(idTransaksi);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.namaKasir = new SimpleStringProperty(namaKasir);
        this.total = new SimpleDoubleProperty(total);
    }

    public int getIdTransaksi() { return idTransaksi.get(); }
    public IntegerProperty idTransaksiProperty() { return idTransaksi; }

    public String getTanggal() { return tanggal.get(); }
    public StringProperty tanggalProperty() { return tanggal; }

    public String getNamaKasir() { return namaKasir.get(); }
    public StringProperty namaKasirProperty() { return namaKasir; }

    public double getTotal() { return total.get(); }
    public DoubleProperty totalProperty() { return total; }
}
