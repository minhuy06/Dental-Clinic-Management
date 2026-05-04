package com.dentalclinic.model;

import java.sql.Date;
import java.util.List;// Hoặc dùng java.time.LocalDate tùy thói quen của nhóm

public class TaiKhoan {
    private int taiKhoanID;      // Khớp cột TaiKhoan_ID (PK)
    private String soDienThoai;  // Khớp cột SoDienThoai (Tên đăng nhập)
    private String matKhau;      // Khớp cột MatKhau
    private String vaiTro;       // Khớp cột VaiTro (Quyết định người này là ai)
    private String trangThai;    // Khớp cột TrangThai (Hoạt động, Bị khóa...)
    private String hoTen;        // Khớp cột HoTen
    private Date ngaySinh;       // Khớp cột NgaySinh
    private boolean gioiTinh;    // Khớp cột GioiTinh (Ví dụ: true = Nam, false = Nữ)

    private List<LichLamViec> danhSachLichLamViec;
    // Constructor mặc định
    public TaiKhoan() {}

    // Constructor đầy đủ tham số
    public TaiKhoan(int taiKhoanID, String soDienThoai, String matKhau, String vaiTro, 
                    String trangThai, String hoTen, Date ngaySinh, boolean gioiTinh) {
        this.taiKhoanID = taiKhoanID;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
    }

    // ================= GETTERS VÀ SETTERS =================
    
    public int getTaiKhoanID() { return taiKhoanID; }
    public void setTaiKhoanID(int taiKhoanID) { this.taiKhoanID = taiKhoanID; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public boolean isGioiTinh() { return gioiTinh; } // Chú ý: boolean dùng 'is' thay vì 'get'
    public void setGioiTinh(boolean gioiTinh) { this.gioiTinh = gioiTinh; }
    
    public List<LichLamViec> getDanhSachLichLamViec() { return danhSachLichLamViec; }
    public void setDanhSachLichLamViec(List<LichLamViec> danhSachLichLamViec) { this.danhSachLichLamViec = danhSachLichLamViec; }
}