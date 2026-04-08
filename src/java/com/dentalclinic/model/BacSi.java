package com.dentalclinic.model;

import java.time.LocalDate;

public class BacSi {
    private int bacSiID;
    private TaiKhoan taiKhoan;
    private String hoTen;
    private LocalDate ngaySinh;
    private boolean gioiTinh;
    private String chuyenKhoa;
    private String anhDaiDien;
    private String trinhDo;

    public BacSi() {}

    public BacSi(int bacSiID, TaiKhoan taiKhoan, String hoTen, LocalDate ngaySinh, boolean gioiTinh, String chuyenKhoa, String anhDaiDien, String trinhDo) {
        this.bacSiID = bacSiID;
        this.taiKhoan = taiKhoan;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.chuyenKhoa = chuyenKhoa;
        this.anhDaiDien = anhDaiDien;
        this.trinhDo = trinhDo;
    }

    public int getBacSiID() {
        return bacSiID;
    }

    public void setBacSiID(int bacSiID) {
        this.bacSiID = bacSiID;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getChuyenKhoa() {
        return chuyenKhoa;
    }

    public void setChuyenKhoa(String chuyenKhoa) {
        this.chuyenKhoa = chuyenKhoa;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public String getTrinhDo() {
        return trinhDo;
    }

    public void setTrinhDo(String trinhDo) {
        this.trinhDo = trinhDo;
    }
}