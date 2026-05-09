package com.dentalclinic.model;

import java.util.List;

public class BacSi {
    private int bacSiID;      // Khớp với BacSi_ID (PK)
    private int taiKhoanID;   // Khớp với TaiKhoan_ID (FK)
    private String anhDaiDien;
    private String trinhDo;
    private int chuyenKhoaID;
    
    // Đối tượng quan hệ (Dùng để chứa dữ liệu sau khi JOIN)
    private TaiKhoan taiKhoan;
    private ChuyenKhoa chuyenKhoa;
    private List<LichHen> danhSachLichHen;
    
    public BacSi() {}

    public BacSi(int bacSiID, int taiKhoanID, String anhDaiDien, String trinhDo, int chuyenKhoaID) {
        this.bacSiID = bacSiID;
        this.taiKhoanID = taiKhoanID;
        this.anhDaiDien = anhDaiDien;
        this.trinhDo = trinhDo;
        this.chuyenKhoaID = chuyenKhoaID;
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
    
    public int getChuyenKhoaID(){return chuyenKhoaID;}
    public void setChuyenKhoaID(int chuyenKhoaID){this.chuyenKhoaID = chuyenKhoaID;}

    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }
    
    public ChuyenKhoa getChuyenKhoa(){return chuyenKhoa;}
    public void setChuyenKhoa(ChuyenKhoa chuyenKhoa){this.chuyenKhoa = chuyenKhoa;}

    public List<LichHen> getDanhSachLichHen() { return danhSachLichHen; }
    public void setDanhSachLichHen(List<LichHen> danhSachLichHen) { this.danhSachLichHen = danhSachLichHen; }
}