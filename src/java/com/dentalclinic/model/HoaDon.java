package com.dentalclinic.model;

import java.sql.Timestamp;

public class HoaDon {
    private int hoaDonID;           // Khớp cột HoaDon_ID (PK)
    private int phieuKhamID;        // Khớp cột PhieuKham_ID (FK)
    private int leTanID;            // Khớp cột LeTan_ID (FK) - Người lập hóa đơn
    private double tongTien;        // Khớp cột TongTien
    private Timestamp ngayThanhToan; // Khớp cột NgayThanhToan
    private String trangThai;       // Khớp cột TrangThai (VD: Đã thanh toán, Chờ thanh toán)

    // Đối tượng quan hệ (Dùng để hiển thị thông tin chi tiết trên giao diện)
    private PhieuKham phieuKham;
    private LeTan leTan;

    public HoaDon() {}

    public HoaDon(int hoaDonID, int phieuKhamID, int leTanID, double tongTien, Timestamp ngayThanhToan, String trangThai) {
        this.hoaDonID = hoaDonID;
        this.phieuKhamID = phieuKhamID;
        this.leTanID = leTanID;
        this.tongTien = tongTien;
        this.ngayThanhToan = ngayThanhToan;
        this.trangThai = trangThai;
    }

    // --- Getters và Setters cho các thuộc tính cơ bản ---
    public int getHoaDonID() { return hoaDonID; }
    public void setHoaDonID(int hoaDonID) { this.hoaDonID = hoaDonID; }

    public int getPhieuKhamID() { return phieuKhamID; }
    public void setPhieuKhamID(int phieuKhamID) { this.phieuKhamID = phieuKhamID; }

    public int getLeTanID() { return leTanID; }
    public void setLeTanID(int leTanID) { this.leTanID = leTanID; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public Timestamp getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(Timestamp ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    // --- Getters và Setters cho Đối tượng quan hệ ---
    public PhieuKham getPhieuKham() { return phieuKham; }
    public void setPhieuKham(PhieuKham phieuKham) { this.phieuKham = phieuKham; }

    public LeTan getLeTan() { return leTan; }
    public void setLeTan(LeTan leTan) { this.leTan = leTan; }
}