package com.dentalclinic.model;

import java.time.LocalDate;
import java.sql.Time;

public class LichHen {
    private int lichHenID;
    private BenhNhan benhNhan; 
    private BacSi bacSi;       
    private LocalDate ngayKham;
    private Time gioKham;
    private String ghiChu;
    private String trangThai;

    public LichHen() {}

    public LichHen(int lichHenID, BenhNhan benhNhan, BacSi bacSi, LocalDate ngayKham, Time gioKham, String ghiChu, String trangThai) {
        this.lichHenID = lichHenID;
        this.benhNhan = benhNhan;
        this.bacSi = bacSi;
        this.ngayKham = ngayKham;
        this.gioKham = gioKham;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    public int getLichHenID() {
        return lichHenID;
    }

    public void setLichHenID(int lichHenID) {
        this.lichHenID = lichHenID;
    }

    public BenhNhan getBenhNhan() {
        return benhNhan;
    }

    public void setBenhNhan(BenhNhan benhNhan) {
        this.benhNhan = benhNhan;
    }

    public BacSi getBacSi() {
        return bacSi;
    }

    public void setBacSi(BacSi bacSi) {
        this.bacSi = bacSi;
    }

    public LocalDate getNgayKham() {
        return ngayKham;
    }

    public void setNgayKham(LocalDate ngayKham) {
        this.ngayKham = ngayKham;
    }

    public Time getGioKham() {
        return gioKham;
    }

    public void setGioKham(Time gioKham) {
        this.gioKham = gioKham;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}