package com.dentalclinic.model;

public class PhongKham {
    private int phongID;
    private String tenPhong;
    private String trangThai;

    public PhongKham() {}

    public PhongKham(int phongID, String tenPhong, String trangThai) {
        this.phongID = phongID;
        this.tenPhong = tenPhong;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getPhongID() { return phongID; }
    public void setPhongID(int phongID) { this.phongID = phongID; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}