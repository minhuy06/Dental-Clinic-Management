package com.dentalclinic.model;

import java.util.List;

public class BenhNhan {
    private int benhNhanID;     // Khớp cột BenhNhan_ID
    private int taiKhoanID;     // Khớp cột TaiKhoan_ID (Khóa ngoại)

    // Đối tượng quan hệ (Dùng để hứng dữ liệu chứa Tên, Tuổi, Giới tính khi JOIN)
    private TaiKhoan taiKhoan;
    private List<LichHen> danhSachLichHen;

    public BenhNhan() {}

    public BenhNhan(int benhNhanID, int taiKhoanID) {
        this.benhNhanID = benhNhanID;
        this.taiKhoanID = taiKhoanID;
    }

    // --- Getters và Setters ---
    public int getBenhNhanID() { return benhNhanID; }
    public void setBenhNhanID(int benhNhanID) { this.benhNhanID = benhNhanID; }

    public int getTaiKhoanID() { return taiKhoanID; }
    public void setTaiKhoanID(int taiKhoanID) { this.taiKhoanID = taiKhoanID; }

    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }
    
    public List<LichHen> getDanhSachLichHen() { return danhSachLichHen; }
    public void setDanhSachLichHen(List<LichHen> danhSachLichHen) { this.danhSachLichHen = danhSachLichHen; }
}