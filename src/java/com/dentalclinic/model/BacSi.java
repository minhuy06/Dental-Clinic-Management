package com.dentalclinic.model;

import java.util.List;

public class BacSi {
    private int bacSiID;      // Khớp với BacSi_ID (PK)
    private int taiKhoanID;   // Khớp với TaiKhoan_ID (FK)
    private String anhDaiDien;
    private String trinhDo;
    
    // Đối tượng quan hệ (Dùng để chứa dữ liệu sau khi JOIN)
    private TaiKhoan taiKhoan; 
    private List<BacSiChuyenKhoa> danhSachChuyenKhoa;
    private List<LichHen> danhSachLichHen;
    private List<PhieuKham> danhSachPhieuKham;
    private List<ChiTietDichVu> danhSachChiTietDichVu;
    
    public BacSi() {}

    public BacSi(int bacSiID, int taiKhoanID, String anhDaiDien, String trinhDo) {
        this.bacSiID = bacSiID;
        this.taiKhoanID = taiKhoanID;
        this.anhDaiDien = anhDaiDien;
        this.trinhDo = trinhDo;
    }

    // Getters and Setters
    public int getBacSiID() { return bacSiID; }
    public void setBacSiID(int bacSiID) { this.bacSiID = bacSiID; }

    public int getTaiKhoanID() { return taiKhoanID; }
    public void setTaiKhoanID(int taiKhoanID) { this.taiKhoanID = taiKhoanID; }

    public String getAnhDaiDien() { return anhDaiDien; }
    public void setAnhDaiDien(String anhDaiDien) { this.anhDaiDien = anhDaiDien; }

    public String getTrinhDo() { return trinhDo; }
    public void setTrinhDo(String trinhDo) { this.trinhDo = trinhDo; }

    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }

    public List<BacSiChuyenKhoa> getDanhSachChuyenKhoa() { return danhSachChuyenKhoa; }
    public void setDanhSachChuyenKhoa(List<BacSiChuyenKhoa> danhSachChuyenKhoa) { 
    this.danhSachChuyenKhoa = danhSachChuyenKhoa; 
    }
    public List<LichHen> getDanhSachLichHen() { return danhSachLichHen; }
    public void setDanhSachLichHen(List<LichHen> danhSachLichHen) { this.danhSachLichHen = danhSachLichHen; }

    public List<PhieuKham> getDanhSachPhieuKham() { return danhSachPhieuKham; }
    public void setDanhSachPhieuKham(List<PhieuKham> danhSachPhieuKham) { this.danhSachPhieuKham = danhSachPhieuKham; }

    public List<ChiTietDichVu> getDanhSachChiTietDichVu() { return danhSachChiTietDichVu; }
    public void setDanhSachChiTietDichVu(List<ChiTietDichVu> danhSachChiTietDichVu) { this.danhSachChiTietDichVu = danhSachChiTietDichVu; }
}