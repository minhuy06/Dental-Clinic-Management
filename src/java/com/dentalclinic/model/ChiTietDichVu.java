package com.dentalclinic.model;

public class ChiTietDichVu {
    private int chiTietDichVuID;
    private int phieuKhamID;   // FK -> PhieuKham
    private int dichVuID;      // FK -> DichVu
    private double donGia;
    private String viTriRang;
    private int bacSiID;       // FK -> BacSi

    // Đối tượng quan hệ
    private PhieuKham phieuKham;
    private DichVu dichVu;
    private BacSi bacSi;

    public ChiTietDichVu() {}

    public ChiTietDichVu(int chiTietDichVuID, int phieuKhamID, int dichVuID,
                          double donGia, String viTriRang, int bacSiID) {
        this.chiTietDichVuID = chiTietDichVuID;
        this.phieuKhamID = phieuKhamID;
        this.dichVuID = dichVuID;
        this.donGia = donGia;
        this.viTriRang = viTriRang;
        this.bacSiID = bacSiID;
    }

    public int getChiTietDichVuID() { return chiTietDichVuID; }
    public void setChiTietDichVuID(int chiTietDichVuID) { this.chiTietDichVuID = chiTietDichVuID; }

    public int getPhieuKhamID() { return phieuKhamID; }
    public void setPhieuKhamID(int phieuKhamID) { this.phieuKhamID = phieuKhamID; }

    public int getDichVuID() { return dichVuID; }
    public void setDichVuID(int dichVuID) { this.dichVuID = dichVuID; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getViTriRang() { return viTriRang; }
    public void setViTriRang(String viTriRang) { this.viTriRang = viTriRang; }

    public int getBacSiID() { return bacSiID; }
    public void setBacSiID(int bacSiID) { this.bacSiID = bacSiID; }

    public PhieuKham getPhieuKham() { return phieuKham; }
    public void setPhieuKham(PhieuKham phieuKham) { this.phieuKham = phieuKham; }

    public DichVu getDichVu() { return dichVu; }
    public void setDichVu(DichVu dichVu) { this.dichVu = dichVu; }

    public BacSi getBacSi() { return bacSi; }
    public void setBacSi(BacSi bacSi) { this.bacSi = bacSi; }
}