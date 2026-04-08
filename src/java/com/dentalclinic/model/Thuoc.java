package com.dentalclinic.model;

public class Thuoc {
    private int thuocID;
    private String tenThuoc;
    private String donViTinh;
    private double giaTien;

    public Thuoc() {}

    public Thuoc(int thuocID, String tenThuoc, String donViTinh, double giaTien) {
        this.thuocID = thuocID;
        this.tenThuoc = tenThuoc;
        this.donViTinh = donViTinh;
        this.giaTien = giaTien;
    }

    public int getThuocID() {
        return thuocID;
    }

    public void setThuocID(int thuocID) {
        this.thuocID = thuocID;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }
}