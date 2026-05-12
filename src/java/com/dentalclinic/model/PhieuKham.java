package com.dentalclinic.model;

import java.sql.Timestamp;
import java.util.List; // Phục vụ cho List<ChiTietDichVu>

public class PhieuKham {
    // --- Các thuộc tính cơ bản (Dùng để thao tác DB) ---
    private int phieuKhamID;      // Khớp cột PhieuKham_ID (PK)
    private int lichHenID;        // Khớp cột LichHen_ID (FK)    
    private String lyDoKham;      // Khớp cột LyDoKham
    private String chanDoan;      // Khớp cột ChanDoan
    private String ghiChu;        // Khớp cột GhiChu
    private Timestamp ngayTao;    // Khớp cột NgayTao (Nên dùng Timestamp để lấy cả giờ, phút, giây)

    // --- Các đối tượng quan hệ (Phục vụ lấy thông tin liên kết hiển thị lên UI) ---
    private LichHen lichHen;      // Để từ Phiếu Khám lấy ngược ra Tên Bệnh Nhân
    private HoaDon hoaDon;

    private List<ChiTietDichVu> danhSachDichVu; 
    // Constructor mặc định
    public PhieuKham() {}

    // Constructor tham số cơ bản (Dành cho INSERT)
    public PhieuKham(int phieuKhamID, int lichHenID, String lyDoKham
                     , String chanDoan, String ghiChu, Timestamp ngayTao) {
        this.phieuKhamID = phieuKhamID;
        this.lichHenID = lichHenID;
        this.lyDoKham = lyDoKham;
        this.chanDoan = chanDoan;
        this.ghiChu = ghiChu;
        this.ngayTao = ngayTao;
    }

    // ================= GETTERS VÀ SETTERS =================
    
    public int getPhieuKhamID() { return phieuKhamID; }
    public void setPhieuKhamID(int phieuKhamID) { this.phieuKhamID = phieuKhamID; }

    public int getLichHenID() { return lichHenID; }
    public void setLichHenID(int lichHenID) { this.lichHenID = lichHenID; }

    public String getLyDoKham() { return lyDoKham; }
    public void setLyDoKham(String lyDoKham) { this.lyDoKham = lyDoKham; }

    public String getChanDoan() { return chanDoan; }
    public void setChanDoan(String chanDoan) { this.chanDoan = chanDoan; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public Timestamp getNgayTao() { return ngayTao; }
    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }

    // --- Getters / Setters cho Đối tượng quan hệ ---
    
    public LichHen getLichHen() { return lichHen; }
    public void setLichHen(LichHen lichHen) { this.lichHen = lichHen; }

    public HoaDon getHoaDon(){return hoaDon;}
    public void setHoaDon(HoaDon hoaDon){this.hoaDon = hoaDon;}

    public List<ChiTietDichVu> getDanhSachDichVu() { return danhSachDichVu; }
    public void setDanhSachDichVu(List<ChiTietDichVu> danhSachDichVu) { this.danhSachDichVu = danhSachDichVu; }
}