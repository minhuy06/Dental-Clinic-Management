package com.dentalclinic.model;

import java.util.List;

public class PhongKham {
    private int phongID;
    private String tenPhong;
    private String trangThai;

    // Đối tượng quan hệ
    private List<PhongKhamDichVu> danhSachDichVu;
    private List<LichLamViec> danhSachLichLamViec;
    private List<PhieuKham> danhSachPhieuKham;

    public PhongKham() {}

    public PhongKham(int phongID, String tenPhong, String trangThai) {
        this.phongID = phongID;
        this.tenPhong = tenPhong;
        this.trangThai = trangThai;
    }

    public int getPhongID() { return phongID; }
    public void setPhongID(int phongID) { this.phongID = phongID; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public List<PhongKhamDichVu> getDanhSachDichVu() { return danhSachDichVu; }
    public void setDanhSachDichVu(List<PhongKhamDichVu> danhSachDichVu) { 
        this.danhSachDichVu = danhSachDichVu; 
    }
    
    public List<LichLamViec> getDanhSachLichLamViec() { return danhSachLichLamViec; }
    public void setDanhSachLichLamViec(List<LichLamViec> danhSachLichLamViec) { this.danhSachLichLamViec = danhSachLichLamViec; }

    public List<PhieuKham> getDanhSachPhieuKham() { return danhSachPhieuKham; }
    public void setDanhSachPhieuKham(List<PhieuKham> danhSachPhieuKham) { this.danhSachPhieuKham = danhSachPhieuKham; }
}