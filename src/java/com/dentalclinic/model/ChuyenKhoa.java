package com.dentalclinic.model;

import java.util.List;

public class ChuyenKhoa {
    private int chuyenKhoaID;
    private String tenChuyenKhoa;

    // Đối tượng quan hệ (dùng sau khi JOIN)
    private List<BacSiChuyenKhoa> danhSachBacSi;
    private List<DichVu> danhSachDichVu;
    public ChuyenKhoa() {}

    public ChuyenKhoa(int chuyenKhoaID, String tenChuyenKhoa) {
        this.chuyenKhoaID = chuyenKhoaID;
        this.tenChuyenKhoa = tenChuyenKhoa;
    }

    public int getChuyenKhoaID() { return chuyenKhoaID; }
    public void setChuyenKhoaID(int chuyenKhoaID) { this.chuyenKhoaID = chuyenKhoaID; }

    public String getTenChuyenKhoa() { return tenChuyenKhoa; }
    public void setTenChuyenKhoa(String tenChuyenKhoa) { this.tenChuyenKhoa = tenChuyenKhoa; }

    public List<BacSiChuyenKhoa> getDanhSachBacSi() { return danhSachBacSi; }
    public void setDanhSachBacSi(List<BacSiChuyenKhoa> danhSachBacSi) { 
        this.danhSachBacSi = danhSachBacSi; 
    }
    
    public List<DichVu> getDanhSachDichVu(){return danhSachDichVu;}
    public void setDanhSachDichVu(List<DichVu> danhSachDichVu){
        this.danhSachDichVu = danhSachDichVu;
    }
}