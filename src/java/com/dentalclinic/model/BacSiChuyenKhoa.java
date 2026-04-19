package com.dentalclinic.model;

public class BacSiChuyenKhoa {
    private int bacSiID;       // FK -> BacSi
    private int chuyenKhoaID;  // FK -> ChuyenKhoa

    // Đối tượng quan hệ (dùng sau khi JOIN)
    private BacSi bacSi;
    private ChuyenKhoa chuyenKhoa;

    public BacSiChuyenKhoa() {}

    public BacSiChuyenKhoa(int bacSiID, int chuyenKhoaID) {
        this.bacSiID = bacSiID;
        this.chuyenKhoaID = chuyenKhoaID;
    }

    public int getBacSiID() { return bacSiID; }
    public void setBacSiID(int bacSiID) { this.bacSiID = bacSiID; }

    public int getChuyenKhoaID() { return chuyenKhoaID; }
    public void setChuyenKhoaID(int chuyenKhoaID) { this.chuyenKhoaID = chuyenKhoaID; }

    public BacSi getBacSi() { return bacSi; }
    public void setBacSi(BacSi bacSi) { this.bacSi = bacSi; }

    public ChuyenKhoa getChuyenKhoa() { return chuyenKhoa; }
    public void setChuyenKhoa(ChuyenKhoa chuyenKhoa) { this.chuyenKhoa = chuyenKhoa; }
}