package com.dentalclinic.model;

public class TaiKhoanBsLtDTO {
    private int taiKhoanID;
    private String hoTen;
    private String vaiTro;
    private String soDienThoai;
    private String ngaySinh;
    private boolean gioiTinh;
    private String trangThai;
    private String matKhau;
    
    // Dành cho Bác sĩ
    private int chuyenKhoaID;
    private String tenChuyenKhoa;
    private String trinhDo;
    private String anhDaiDien;
   
    public TaiKhoanBsLtDTO() {}

    // --- GETTERS & SETTERS ---
    public int getTaiKhoanID(){return taiKhoanID;}
    public void setTaiKhoanID(int taiKhoanID){this.taiKhoanID = taiKhoanID;}
    
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public boolean isGioiTinh() { return gioiTinh; }
    public void setGioiTinh(boolean gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public int getChuyenKhoaID() { return chuyenKhoaID; }
    public void setChuyenKhoaID(int chuyenKhoaID) { this.chuyenKhoaID = chuyenKhoaID; }
    
    public String getTenChuyenKhoa() { return tenChuyenKhoa; }
    public void setTenChuyenKhoa(String tenChuyenKhoa) { this.tenChuyenKhoa = tenChuyenKhoa; }

    public String getTrinhDo() { return trinhDo; }
    public void setTrinhDo(String trinhDo) { this.trinhDo = trinhDo; }

    public String getAnhDaiDien() { return anhDaiDien; }
    public void setAnhDaiDien(String anhDaiDien) { this.anhDaiDien = anhDaiDien; }
}