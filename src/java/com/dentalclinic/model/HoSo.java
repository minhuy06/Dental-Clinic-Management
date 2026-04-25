package com.dentalclinic.model;

import java.sql.Date;

public class HoSo {
    private int hoSoID;
    private String diaChi;
    private String diUngThuoc;
    private String tienSuBenh;
    private String trangThai;
    private Date ngayDangKy;
    private String ghiChu;
    
    private BenhNhan benhNhan;
    // Constructor không tham số
    public HoSo() {
    }

    // Constructor đầy đủ tham số
    public HoSo(int hoSoID, String diaChi, String diUngThuoc, 
                String tienSuBenh, String trangThai, Date ngayDangKy, String ghiChu) {
        this.hoSoID = hoSoID;
        this.diaChi = diaChi;
        this.diUngThuoc = diUngThuoc;
        this.tienSuBenh = tienSuBenh;
        this.trangThai = trangThai;
        this.ngayDangKy = ngayDangKy;
        this.ghiChu = ghiChu;
    }

    // Getter và Setter
    public int getHoSoID() { return hoSoID; }
    public void setHoSoID(int hoSoID) { this.hoSoID = hoSoID; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getDiUngThuoc() { return diUngThuoc; }
    public void setDiUngThuoc(String diUngThuoc) { this.diUngThuoc = diUngThuoc; }

    public String getTienSuBenh() { return tienSuBenh; }
    public void setTienSuBenh(String tienSuBenh) { this.tienSuBenh = tienSuBenh; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Date getNgayDangKy() { return ngayDangKy; }
    public void setNgayDangKy(Date ngayDangKy) { this.ngayDangKy = ngayDangKy; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public BenhNhan getBenhNhan() {
        return benhNhan;
    }

    public void setBenhNhan(BenhNhan benhNhan) {
        this.benhNhan = benhNhan;
    }
}