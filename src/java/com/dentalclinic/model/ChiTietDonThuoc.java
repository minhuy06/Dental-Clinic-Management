package com.dentalclinic.model;

public class ChiTietDonThuoc {
    private int chiTietThuocID;
    private PhieuKham phieuKham;
    private Thuoc thuoc;
    private int soLuong;
    private String cachSuDung;

    public ChiTietDonThuoc() {}

    public ChiTietDonThuoc(int chiTietThuocID, PhieuKham phieuKham, Thuoc thuoc, int soLuong, String cachSuDung) {
        this.chiTietThuocID = chiTietThuocID;
        this.phieuKham = phieuKham;
        this.thuoc = thuoc;
        this.soLuong = soLuong;
        this.cachSuDung = cachSuDung;
    }

    public int getChiTietThuocID() {
        return chiTietThuocID;
    }

    public void setChiTietThuocID(int chiTietThuocID) {
        this.chiTietThuocID = chiTietThuocID;
    }

    public PhieuKham getPhieuKham() {
        return phieuKham;
    }

    public void setPhieuKham(PhieuKham phieuKham) {
        this.phieuKham = phieuKham;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getCachSuDung() {
        return cachSuDung;
    }

    public void setCachSuDung(String cachSuDung) {
        this.cachSuDung = cachSuDung;
    }
}