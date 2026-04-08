package com.dentalclinic.model;

import java.time.LocalDate;

public class BenhNhan {
    private int benhNhanID;
    private TaiKhoan taiKhoan; 
    private String hoTen;
    private LocalDate ngaySinh;
    private boolean gioiTinh;
    private String tienSuBenh;
    private String nhomMau;

    public BenhNhan() {}

    public BenhNhan(int benhNhanID, TaiKhoan taiKhoan, String hoTen, LocalDate ngaySinh, boolean gioiTinh, String tienSuBenh, String nhomMau) {
        this.benhNhanID = benhNhanID;
        this.taiKhoan = taiKhoan;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.tienSuBenh = tienSuBenh;
        this.nhomMau = nhomMau;
    }

    public int getBenhNhanID() {
        return benhNhanID;
    }

    public void setBenhNhanID(int benhNhanID) {
        this.benhNhanID = benhNhanID;
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

    public String getTienSuBenh() {
        return tienSuBenh;
    }

    public void setTienSuBenh(String tienSuBenh) {
        this.tienSuBenh = tienSuBenh;
    }

    public String getNhomMau() {
        return nhomMau;
    }

    public void setNhomMau(String nhomMau) {
        this.nhomMau = nhomMau;
    }
}