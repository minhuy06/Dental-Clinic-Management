package com.dentalclinic.model;

import java.util.List;

public class LeTan {
    private int leTanID;        // Khớp cột LeTan_ID (PK)
    private int taiKhoanID;     // Khớp cột TaiKhoan_ID (FK)

    // Đối tượng quan hệ (Dùng để lấy Họ Tên, Số điện thoại của Lễ tân khi hiển thị lên giao diện)
    private TaiKhoan taiKhoan;
    private List<HoaDon> danhSachHoaDon;
    public LeTan() {}

    public LeTan(int leTanID, int taiKhoanID) {
        this.leTanID = leTanID;
        this.taiKhoanID = taiKhoanID;
    }

    // --- Getters và Setters cho các thuộc tính cơ bản ---
    public int getLeTanID() { return leTanID; }
    public void setLeTanID(int leTanID) { this.leTanID = leTanID; }

    public int getTaiKhoanID() { return taiKhoanID; }
    public void setTaiKhoanID(int taiKhoanID) { this.taiKhoanID = taiKhoanID; }

    // --- Getters và Setters cho Đối tượng quan hệ ---
    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }
    
    public List<HoaDon> getDanhSachHoaDon() { return danhSachHoaDon; }
    public void setDanhSachHoaDon(List<HoaDon> danhSachHoaDon) { this.danhSachHoaDon = danhSachHoaDon; }
}