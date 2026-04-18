package com.dentalclinic.model;

public class PhongKhamDichVu {
    private int phongID;    // FK -> PhongKham
    private int dichVuID;   // FK -> DichVu

    private PhongKham phongKham;
    private DichVu dichVu;

    public PhongKhamDichVu() {}

    public PhongKhamDichVu(int phongID, int dichVuID) {
        this.phongID = phongID;
        this.dichVuID = dichVuID;
    }

    public int getPhongID() { return phongID; }
    public void setPhongID(int phongID) { this.phongID = phongID; }

    public int getDichVuID() { return dichVuID; }
    public void setDichVuID(int dichVuID) { this.dichVuID = dichVuID; }

    public PhongKham getPhongKham() { return phongKham; }
    public void setPhongKham(PhongKham phongKham) { this.phongKham = phongKham; }

    public DichVu getDichVu() { return dichVu; }
    public void setDichVu(DichVu dichVu) { this.dichVu = dichVu; }
}