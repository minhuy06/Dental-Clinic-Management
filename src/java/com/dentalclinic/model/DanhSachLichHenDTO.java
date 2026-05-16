package com.dentalclinic.model;

public class DanhSachLichHenDTO {
    private int lichHenID;
    private String tenBenhNhan;
    private String soDienThoai;
    private String gioHen;
    private String tenBacSi;
    private String lyDoKham;
    private String trangThai;

    public DanhSachLichHenDTO() {}

    public int getLichHenID() { return lichHenID; }
    public void setLichHenID(int lichHenID) { this.lichHenID = lichHenID; }

    public String getTenBenhNhan() { return tenBenhNhan; }
    public void setTenBenhNhan(String tenBenhNhan) { this.tenBenhNhan = tenBenhNhan; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getGioHen() { return gioHen; }
    public void setGioHen(String gioHen) { this.gioHen = gioHen; }

    public String getTenBacSi() { return tenBacSi; }
    public void setTenBacSi(String tenBacSi) { this.tenBacSi = tenBacSi; }

    public String getLyDoKham() { return lyDoKham; }
    public void setLyDoKham(String lyDoKham) { this.lyDoKham = lyDoKham; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
