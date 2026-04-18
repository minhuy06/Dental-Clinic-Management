package com.dentalclinic.model;

import java.sql.Timestamp;

public class TinNhan {
    private int tinNhanID;
    private int nguoiGuiID;
    private int nguoiNhanID;
    private String noiDung;
    private Timestamp thoiGianGui;
    private String trangThai;

    public TinNhan() {}

    public TinNhan(int tinNhanID, int nguoiGuiID, int nguoiNhanID, String noiDung, Timestamp thoiGianGui, String trangThai) {
        this.tinNhanID = tinNhanID;
        this.nguoiGuiID = nguoiGuiID;
        this.nguoiNhanID = nguoiNhanID;
        this.noiDung = noiDung;
        this.thoiGianGui = thoiGianGui;
        this.trangThai = trangThai;
    }

    // Getters and Setters (Rút gọn)
    public int getTinNhanID() { return tinNhanID; }
    public void setTinNhanID(int tinNhanID) { this.tinNhanID = tinNhanID; }
    // ... Tương tự cho các trường còn lại
}