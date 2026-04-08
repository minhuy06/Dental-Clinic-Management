package com.dentalclinic.model;

import java.time.LocalDate;

public class LeTan {
    private int leTanID;
    private TaiKhoan taiKhoan;
    private String hoTen;
    private LocalDate ngaySinh;
    private boolean gioiTinh;
    private String caLamViec;

    public LeTan() {}

    public LeTan(int leTanID, TaiKhoan taiKhoan, String hoTen, LocalDate ngaySinh, boolean gioiTinh, String caLamViec) {
        this.leTanID = leTanID;
        this.taiKhoan = taiKhoan;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.caLamViec = caLamViec;
    }

    public int getLeTanID() {
        return leTanID;
    }

    public void setLeTanID(int leTanID) {
        this.leTanID = leTanID;
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

    public String getCaLamViec() {
        return caLamViec;
    }

    public void setCaLamViec(String caLamViec) {
        this.caLamViec = caLamViec;
    }
}