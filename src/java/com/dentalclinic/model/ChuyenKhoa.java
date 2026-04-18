package com.dentalclinic.model;

public class ChuyenKhoa {
    private int chuyenKhoaID;
    private String tenChuyenKhoa;

    public ChuyenKhoa() {}

    public ChuyenKhoa(int chuyenKhoaID, String tenChuyenKhoa) {
        this.chuyenKhoaID = chuyenKhoaID;
        this.tenChuyenKhoa = tenChuyenKhoa;
    }

    public int getChuyenKhoaID() { return chuyenKhoaID; }
    public void setChuyenKhoaID(int chuyenKhoaID) { this.chuyenKhoaID = chuyenKhoaID; }

    public String getTenChuyenKhoa() { return tenChuyenKhoa; }
    public void setTenChuyenKhoa(String tenChuyenKhoa) { this.tenChuyenKhoa = tenChuyenKhoa; }
}