package com.dentalclinic.model;

import java.sql.Time;
import java.util.List;

public class CaLam {
    // --- Các thuộc tính cơ bản (Khớp 100% với cột trong Database) ---
    private int caID;            // Khớp cột Ca_Id (PK)
    private String tenCa;        // Khớp cột TenCa (VD: "Ca Sáng", "Ca Chiều")
    private Time gioBatDau;      // Khớp cột GioBatDau (VD: 07:30:00)
    private Time gioKetThuc;     // Khớp cột GioKetThuc (VD: 11:30:00)
    
    private List<LichLamViec> danhSachLichLamViec;
    // Constructor mặc định
    public CaLam() {}

    // Constructor đầy đủ tham số
    public CaLam(int caID, String tenCa, Time gioBatDau, Time gioKetThuc) {
        this.caID = caID;
        this.tenCa = tenCa;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
    }

    // ================= GETTERS VÀ SETTERS =================
    
    public int getCaID() { return caID; }
    public void setCaID(int caID) { this.caID = caID; }

    public String getTenCa() { return tenCa; }
    public void setTenCa(String tenCa) { this.tenCa = tenCa; }

    public Time getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(Time gioBatDau) { this.gioBatDau = gioBatDau; }

    public Time getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(Time gioKetThuc) { this.gioKetThuc = gioKetThuc; }
    
    public List<LichLamViec> getDanhSachLichLamViec() { return danhSachLichLamViec; }
    public void setDanhSachLichLamViec(List<LichLamViec> danhSachLichLamViec) { this.danhSachLichLamViec = danhSachLichLamViec; }
}