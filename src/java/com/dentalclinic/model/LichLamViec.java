package com.dentalclinic.model;

import java.sql.Date;

public class LichLamViec {
    private int lichID;
    private int taiKhoanID;
    private int caID;
    private Date ngayLam;
    private String trangThai;
    private int phongID;

    public LichLamViec() {}

    public LichLamViec(int lichID, int taiKhoanID, int caID, Date ngayLam, String trangThai, int phongID) {
        this.lichID = lichID;
        this.taiKhoanID = taiKhoanID;
        this.caID = caID;
        this.ngayLam = ngayLam;
        this.trangThai = trangThai;
        this.phongID = phongID;
    }

    // Getters and Setters
    public int getLichID() { return lichID; }
    public void setLichID(int lichID) { this.lichID = lichID; }

    public int getTaiKhoanID() { return taiKhoanID; }
    public void setTaiKhoanID(int taiKhoanID) { this.taiKhoanID = taiKhoanID; }

    public int getCaID() { return caID; }
    public void setCaID(int caID) { this.caID = caID; }

    public Date getNgayLam() { return ngayLam; }
    public void setNgayLam(Date ngayLam) { this.ngayLam = ngayLam; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public int getPhongID() { return phongID; }
    public void setPhongID(int phongID) { this.phongID = phongID; }
}