package com.dentalclinic.model;

import java.sql.Time;

public class CaLam {
    private int caID;
    private String tenCa;
    private Time gioBatDau;
    private Time gioKetThuc;

    public CaLam() {}

    public CaLam(int caID, String tenCa, Time gioBatDau, Time gioKetThuc) {
        this.caID = caID;
        this.tenCa = tenCa;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
    }

    // Getters and Setters
    public int getCaID() { return caID; }
    public void setCaID(int caID) { this.caID = caID; }

    public String getTenCa() { return tenCa; }
    public void setTenCa(String tenCa) { this.tenCa = tenCa; }

    public Time getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(Time gioBatDau) { this.gioBatDau = gioBatDau; }

    public Time getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(Time gioKetThuc) { this.gioKetThuc = gioKetThuc; }
}