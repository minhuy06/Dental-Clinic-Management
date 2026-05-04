package com.dentalclinic.model;

import java.util.List;

public class DichVu {
    private int dichVuID;
    private String tenDichVu;
    private double giaTien;
    private int thoiLuongDuKien;
    private int chuyenKhoaID;  // FK -> ChuyenKhoa

    // Đối tượng quan hệ
    private ChuyenKhoa chuyenKhoa;
    private List<PhongKhamDichVu> danhSachPhongKham;

    private List<ChiTietLichHen> lichSuDatDichVu;
    private List<ChiTietDichVu> danhSachChiTietDichVu;

    public DichVu() {}

    public DichVu(int dichVuID, String tenDichVu, double giaTien, 
                  int thoiLuongDuKien, int chuyenKhoaID) {
        this.dichVuID = dichVuID;
        this.tenDichVu = tenDichVu;
        this.giaTien = giaTien;
        this.thoiLuongDuKien = thoiLuongDuKien;
        this.chuyenKhoaID = chuyenKhoaID;
    }

    public int getDichVuID() { return dichVuID; }
    public void setDichVuID(int dichVuID) { this.dichVuID = dichVuID; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public double getGiaTien() { return giaTien; }
    public void setGiaTien(double giaTien) { this.giaTien = giaTien; }

    public int getThoiLuongDuKien() { return thoiLuongDuKien; }
    public void setThoiLuongDuKien(int thoiLuongDuKien) { 
        this.thoiLuongDuKien = thoiLuongDuKien; 
    }

    public int getChuyenKhoaID() { return chuyenKhoaID; }
    public void setChuyenKhoaID(int chuyenKhoaID) { this.chuyenKhoaID = chuyenKhoaID; }

    public ChuyenKhoa getChuyenKhoa() { return chuyenKhoa; }
    public void setChuyenKhoa(ChuyenKhoa chuyenKhoa) { this.chuyenKhoa = chuyenKhoa; }

    public List<PhongKhamDichVu> getDanhSachPhongKham() { return danhSachPhongKham; }
    public void setDanhSachPhongKham(List<PhongKhamDichVu> danhSachPhongKham) { 
        this.danhSachPhongKham = danhSachPhongKham; 
    }
    public List<ChiTietLichHen> getLichSuDatDichVu() {
        return lichSuDatDichVu;
    }

    public void setLichSuDatDichVu(List<ChiTietLichHen> lichSuDatDichVu) {
        this.lichSuDatDichVu = lichSuDatDichVu;
    }

    public List<ChiTietDichVu> getDanhSachChiTietDichVu() { return danhSachChiTietDichVu; }
    public void setDanhSachChiTietDichVu(List<ChiTietDichVu> danhSachChiTietDichVu) { this.danhSachChiTietDichVu = danhSachChiTietDichVu; }
}