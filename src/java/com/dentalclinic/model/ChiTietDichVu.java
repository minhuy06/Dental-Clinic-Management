package com.dentalclinic.model;

public class ChiTietDichVu {
    private int chiTietDichVuID;
    private PhieuKham phieuKham; 
    private DichVu dichVu;       
    private double donGia;       
    private int viTriRang;

    public ChiTietDichVu() {}

    public ChiTietDichVu(int chiTietDichVuID, PhieuKham phieuKham, DichVu dichVu, double donGia, int viTriRang) {
        this.chiTietDichVuID = chiTietDichVuID;
        this.phieuKham = phieuKham;
        this.dichVu = dichVu;
        this.donGia = donGia;
        this.viTriRang = viTriRang;
    }

    public int getChiTietDichVuID() {
        return chiTietDichVuID;
    }

    public void setChiTietDichVuID(int chiTietDichVuID) {
        this.chiTietDichVuID = chiTietDichVuID;
    }

    public PhieuKham getPhieuKham() {
        return phieuKham;
    }

    public void setPhieuKham(PhieuKham phieuKham) {
        this.phieuKham = phieuKham;
    }

    public DichVu getDichVu() {
        return dichVu;
    }

    public void setDichVu(DichVu dichVu) {
        this.dichVu = dichVu;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getViTriRang() {
        return viTriRang;
    }

    public void setViTriRang(int viTriRang) {
        this.viTriRang = viTriRang;
    }
}