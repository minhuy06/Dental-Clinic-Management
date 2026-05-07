package com.dentalclinic.model;

import java.sql.Date;

public class TaiKhoanBsLtDTO {
    
    private String hoTen;
    private String vaiTro;
    private String soDienThoai;
    private Date ngaySinh;
    private String gioiTinh;
    private String trangThai;
    private String matKhau;
    
    // Dành cho Bác sĩ
    private String chuyenKhoa;
    private String trinhDo;
    private String anhDaiDien;
   
    public TaiKhoanBsLtDTO() {}

    // --- GETTERS & SETTERS ---
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getChuyenKhoa() { return chuyenKhoa; }
    public void setChuyenKhoa(String chuyenKhoa) { this.chuyenKhoa = chuyenKhoa; }

    public String getTrinhDo() { return trinhDo; }
    public void setTrinhDo(String trinhDo) { this.trinhDo = trinhDo; }

    public String getAnhDaiDien() { return anhDaiDien; }
    public void setAnhDaiDien(String anhDaiDien) { this.anhDaiDien = anhDaiDien; }
}