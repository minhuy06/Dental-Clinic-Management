package com.dentalclinic.model;

import java.sql.Date;
import java.sql.Time;

public class LichHen {
    // --- Các thuộc tính cơ bản (Khớp 100% với cột trong Database) ---
    private int lichHenID;      // Khớp cột LichHen_ID (PK)
    private int benhNhanID;     // Khớp cột BenhNhan_ID (FK)
    private int bacSiID;        // Khớp cột BacSi_ID (FK)
    private int dichVuID;       // Khớp cột DichVu_ID (FK)
    
    private Date ngayKham;      // Khớp cột NgayKham
    private Time gioKham;       // Khớp cột GioKham
    private String ghiChu;      // Khớp cột GhiChu (Triệu chứng, yêu cầu thêm...)
    private String trangThai;   // Khớp cột TrangThai (Chờ xác nhận, Đã xác nhận, Đã hủy, Hoàn thành)

    // --- Các đối tượng quan hệ (Dùng để JOIN và hiển thị thông tin lên UI) ---
    private BenhNhan benhNhan;
    private BacSi bacSi;
    private DichVu dichVu;
    // Constructor mặc định
    public LichHen() {}

    // Constructor đầy đủ tham số cơ bản (Dành cho lúc INSERT/UPDATE)
    public LichHen(int lichHenID, int benhNhanID, int bacSiID, int dichVuID, Date ngayKham, Time gioKham, String ghiChu, String trangThai) {
        this.lichHenID = lichHenID;
        this.benhNhanID = benhNhanID;
        this.bacSiID = bacSiID;
        this.dichVuID = dichVuID;
        this.ngayKham = ngayKham;
        this.gioKham = gioKham;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    // ================= GETTERS VÀ SETTERS =================
    
    public int getLichHenID() { return lichHenID; }
    public void setLichHenID(int lichHenID) { this.lichHenID = lichHenID; }

    public int getBenhNhanID() { return benhNhanID; }
    public void setBenhNhanID(int benhNhanID) { this.benhNhanID = benhNhanID; }

    public int getBacSiID() { return bacSiID; }
    public void setBacSiID(int bacSiID) { this.bacSiID = bacSiID; }

    public int getDichVuID() { return dichVuID; }
    public void setDichVuID(int dichVuID) { this.dichVuID = dichVuID; }

    public Date getNgayKham() { return ngayKham; }
    public void setNgayKham(Date ngayKham) { this.ngayKham = ngayKham; }

    public Time getGioKham() { return gioKham; }
    public void setGioKham(Time gioKham) { this.gioKham = gioKham; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    // --- Getters / Setters cho Đối tượng quan hệ ---
    
    public BenhNhan getBenhNhan() { return benhNhan; }
    public void setBenhNhan(BenhNhan benhNhan) { this.benhNhan = benhNhan; }

    public BacSi getBacSi() { return bacSi; }
    public void setBacSi(BacSi bacSi) { this.bacSi = bacSi; }

    public DichVu getDichVu() { return dichVu; }
    public void setDichVu(DichVu dichVu) { this.dichVu = dichVu; }
}