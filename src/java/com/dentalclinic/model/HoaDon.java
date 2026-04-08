package com.dentalclinic.model;

import java.time.LocalDate;

public class HoaDon {
    private int hoaDonID;
    private PhieuKham phieuKham;
    private LeTan leTan; 
    private double tongTien;
    private LocalDate ngayThanhToan;
    private String trangThai;

    public HoaDon() {}

    public HoaDon(int hoaDonID, PhieuKham phieuKham, LeTan leTan, double tongTien, LocalDate ngayThanhToan, String trangThai) {
        this.hoaDonID = hoaDonID;
        this.phieuKham = phieuKham;
        this.leTan = leTan;
        this.tongTien = tongTien;
        this.ngayThanhToan = ngayThanhToan;
        this.trangThai = trangThai;
    }

    public int getHoaDonID() {
        return hoaDonID;
    }

    public void setHoaDonID(int hoaDonID) {
        this.hoaDonID = hoaDonID;
    }

    public PhieuKham getPhieuKham() {
        return phieuKham;
    }

    public void setPhieuKham(PhieuKham phieuKham) {
        this.phieuKham = phieuKham;
    }

    public LeTan getLeTan() {
        return leTan;
    }

    public void setLeTan(LeTan leTan) {
        this.leTan = leTan;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public LocalDate getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDate ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}