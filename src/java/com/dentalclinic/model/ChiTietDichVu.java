package com.dentalclinic.model;

public class ChiTietDichVu {
    private int chiTietDichVuID;
    private int phieuKhamID;   // FK -> PhieuKham
    private int dichVuID;      // FK -> DichVu
    private double donGia;
    private int viTriRang;
    private int soLuong;

    // Đối tượng quan hệ
    private PhieuKham phieuKham;
    private DichVu dichVu;

    public ChiTietDichVu() {}

    public ChiTietDichVu(int chiTietDichVuID, int phieuKhamID, int dichVuID,
                          double donGia, int viTriRang, int soLuong) {
        this.chiTietDichVuID = chiTietDichVuID;
        this.phieuKhamID = phieuKhamID;
        this.dichVuID = dichVuID;
        this.donGia = donGia;
        this.viTriRang = viTriRang;
        this.soLuong = soLuong;
    }

    public int getChiTietDichVuID() { return chiTietDichVuID; }
    public void setChiTietDichVuID(int chiTietDichVuID) { this.chiTietDichVuID = chiTietDichVuID; }

    public int getPhieuKhamID() { return phieuKhamID; }
    public void setPhieuKhamID(int phieuKhamID) { this.phieuKhamID = phieuKhamID; }

    public int getDichVuID() { return dichVuID; }
    public void setDichVuID(int dichVuID) { this.dichVuID = dichVuID; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public int getViTriRang() { return viTriRang; }
    public void setViTriRang(int viTriRang) { this.viTriRang = viTriRang; }
    
    public int getSoLuong(){return soLuong;}
    public void setSoLuong(int soLuong){this.soLuong = soLuong;}

    public PhieuKham getPhieuKham() { return phieuKham; }
    public void setPhieuKham(PhieuKham phieuKham) { this.phieuKham = phieuKham; }

    public DichVu getDichVu() { return dichVu; }
    public void setDichVu(DichVu dichVu) { this.dichVu = dichVu; }
}