package com.dentalclinic.model;

import java.util.List;

public class PhongKham {
    private int phongID;
    private String tenPhong;

    // Đối tượng quan hệ
    private List<LichLamViec> danhSachLichLamViec;
    private List<PhieuKham> danhSachPhieuKham;

    public PhongKham() {}

    public PhongKham(int phongID, String tenPhong) {
        this.phongID = phongID;
        this.tenPhong = tenPhong;
    }

    public int getPhongID() { return phongID; }
    public void setPhongID(int phongID) { this.phongID = phongID; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    
    public List<LichLamViec> getDanhSachLichLamViec() { return danhSachLichLamViec; }
    public void setDanhSachLichLamViec(List<LichLamViec> danhSachLichLamViec) { this.danhSachLichLamViec = danhSachLichLamViec; }

    public List<PhieuKham> getDanhSachPhieuKham() { return danhSachPhieuKham; }
    public void setDanhSachPhieuKham(List<PhieuKham> danhSachPhieuKham) { this.danhSachPhieuKham = danhSachPhieuKham; }
}