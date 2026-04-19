package com.dentalclinic.model;

import java.sql.Timestamp;

public class TinNhan {
    // --- Các thuộc tính cơ bản (Khớp 100% với cột trong Database) ---
    private int tinNhanID;         // Khớp cột TinNhan_ID (PK)
    private int nguoiGuiID;        // Khớp cột NguoiGui_ID (FK trỏ tới TaiKhoan_ID)
    private int nguoiNhanID;       // Khớp cột NguoiNhan_ID (FK trỏ tới TaiKhoan_ID)
    private String noiDung;        // Khớp cột NoiDung
    private Timestamp thoiGianGui; // Khớp cột ThoiGianGui (Dùng Timestamp để lưu chính xác từng giây)
    private String trangThai;      // Khớp cột TrangThai (VD: "Đã gửi", "Đã xem")

    // --- Các đối tượng quan hệ (Giải quyết bài toán 2 khóa ngoại cùng trỏ về 1 bảng) ---
    private TaiKhoan nguoiGui;     // Chứa thông tin của người gửi
    private TaiKhoan nguoiNhan;    // Chứa thông tin của người nhận

    // Constructor mặc định
    public TinNhan() {}

    // Constructor tham số cơ bản (Dùng khi tạo tin nhắn mới đẩy xuống DB)
    public TinNhan(int tinNhanID, int nguoiGuiID, int nguoiNhanID, String noiDung, Timestamp thoiGianGui, String trangThai) {
        this.tinNhanID = tinNhanID;
        this.nguoiGuiID = nguoiGuiID;
        this.nguoiNhanID = nguoiNhanID;
        this.noiDung = noiDung;
        this.thoiGianGui = thoiGianGui;
        this.trangThai = trangThai;
    }

    // ================= GETTERS VÀ SETTERS =================
    
    public int getTinNhanID() { return tinNhanID; }
    public void setTinNhanID(int tinNhanID) { this.tinNhanID = tinNhanID; }

    public int getNguoiGuiID() { return nguoiGuiID; }
    public void setNguoiGuiID(int nguoiGuiID) { this.nguoiGuiID = nguoiGuiID; }

    public int getNguoiNhanID() { return nguoiNhanID; }
    public void setNguoiNhanID(int nguoiNhanID) { this.nguoiNhanID = nguoiNhanID; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public Timestamp getThoiGianGui() { return thoiGianGui; }
    public void setThoiGianGui(Timestamp thoiGianGui) { this.thoiGianGui = thoiGianGui; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    // --- Getters / Setters cho Đối tượng quan hệ ---
    
    public TaiKhoan getNguoiGui() { return nguoiGui; }
    public void setNguoiGui(TaiKhoan nguoiGui) { this.nguoiGui = nguoiGui; }

    public TaiKhoan getNguoiNhan() { return nguoiNhan; }
    public void setNguoiNhan(TaiKhoan nguoiNhan) { this.nguoiNhan = nguoiNhan; }
}