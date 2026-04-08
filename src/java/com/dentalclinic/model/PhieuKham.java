package com.dentalclinic.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class PhieuKham {
    private int phieuKhamID;
    private LichHen lichHen;
    private String chanDoan;
    private LocalDate ngayTao;
    private String trangThai;
    private List<ChiTietDichVu> danhSachDichVu;
    private List<ChiTietDonThuoc> danhSachThuoc;

    public PhieuKham() {
        this.danhSachDichVu = new ArrayList<>();
        this.danhSachThuoc = new ArrayList<>();
    }

    public PhieuKham(int phieuKhamID, LichHen lichHen, String chanDoan, LocalDate ngayTao, String trangThai, List<ChiTietDichVu> danhSachDichVu, List<ChiTietDonThuoc> danhSachThuoc) {
        this.phieuKhamID = phieuKhamID;
        this.lichHen = lichHen;
        this.chanDoan = chanDoan;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
        this.danhSachDichVu = danhSachDichVu;
        this.danhSachThuoc = danhSachThuoc;
    }

    public int getPhieuKhamID() {
        return phieuKhamID;
    }

    public void setPhieuKhamID(int phieuKhamID) {
        this.phieuKhamID = phieuKhamID;
    }

    public LichHen getLichHen() {
        return lichHen;
    }

    public void setLichHen(LichHen lichHen) {
        this.lichHen = lichHen;
    }

    public String getChanDoan() {
        return chanDoan;
    }

    public void setChanDoan(String chanDoan) {
        this.chanDoan = chanDoan;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public List<ChiTietDichVu> getDanhSachDichVu() {
        return danhSachDichVu;
    }

    public void setDanhSachDichVu(List<ChiTietDichVu> danhSachDichVu) {
        this.danhSachDichVu = danhSachDichVu;
    }

    public List<ChiTietDonThuoc> getDanhSachThuoc() {
        return danhSachThuoc;
    }

    public void setDanhSachThuoc(List<ChiTietDonThuoc> danhSachThuoc) {
        this.danhSachThuoc = danhSachThuoc;
    }
}