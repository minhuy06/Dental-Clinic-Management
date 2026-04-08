package com.dentalclinic.model;

public class DichVu {
    private int dichVuID;
    private String tenDichVu;
    private double giaTien;

    public DichVu() {}

    public DichVu(int dichVuID, String tenDichVu, double giaTien) {
        this.dichVuID = dichVuID;
        this.tenDichVu = tenDichVu;
        this.giaTien = giaTien;
    }

    public int getDichVuID() {
        return dichVuID;
    }

    public void setDichVuID(int dichVuID) {
        this.dichVuID = dichVuID;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }
}