package com.dentalclinic.model;

import java.sql.Date;

public class LichLamViec {
    // --- Các thuộc tính cơ bản (Dùng để INSERT/UPDATE xuống Database) ---
    private int lichID;         // Khớp cột Lich_ID (PK)
    private int taiKhoanID;     // Khớp cột TaiKhoan_ID (FK) - Lưu ý: dùng Tài khoản, không dùng Bác sĩ
    private int caID;           // Khớp cột Ca_Id (FK)
    private Date ngayLam;       // Khớp cột NgayLam
    private int phongID;        // Khớp cột Phong_ID (FK)
    private String trangThai;   // Khớp cột TrangThai (VD: Sắp tới, Đã hoàn thành, Nghỉ phép)

    // --- Các đối tượng quan hệ (Dùng để hứng dữ liệu JOIN và hiển thị lên giao diện Admin) ---
    private TaiKhoan taiKhoan;  
    private CaLam caLam;        
    private PhongKham phongKham;

    // Constructor mặc định
    public LichLamViec() {}

    // Constructor đầy đủ tham số cơ bản
    public LichLamViec(int lichID, int taiKhoanID, int caID, Date ngayLam, int phongID, String trangThai) {
        this.lichID = lichID;
        this.taiKhoanID = taiKhoanID;
        this.caID = caID;
        this.ngayLam = ngayLam;
        this.phongID = phongID;
        this.trangThai = trangThai;
    }

    // ================= GETTERS VÀ SETTERS =================
    
    public int getLichID() { return lichID; }
    public void setLichID(int lichID) { this.lichID = lichID; }

    public int getTaiKhoanID() { return taiKhoanID; }
    public void setTaiKhoanID(int taiKhoanID) { this.taiKhoanID = taiKhoanID; }

    public int getCaID() { return caID; }
    public void setCaID(int caID) { this.caID = caID; }

    public Date getNgayLam() { return ngayLam; }
    public void setNgayLam(Date ngayLam) { this.ngayLam = ngayLam; }

    public int getPhongID() { return phongID; }
    public void setPhongID(int phongID) { this.phongID = phongID; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    // --- Getters / Setters cho Đối tượng quan hệ ---
    
    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }

    public CaLam getCaLam() { return caLam; }
    public void setCaLam(CaLam caLam) { this.caLam = caLam; }

    public PhongKham getPhongKham() { return phongKham; }
    public void setPhongKham(PhongKham phongKham) { this.phongKham = phongKham; }
}