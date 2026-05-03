package com.dentalclinic.model;

public class ChiTietLichHen {
    
    // 1. Các thuộc tính mapping trực tiếp với các cột trong CSDL
    private int chiTietLichHenId;
    private int lichHenId;
    private int dichVuId;
    private int soLuong;
    
    // 2. Thuộc tính mở rộng (Object Mapping)
    // Dùng để lưu trữ thông tin của Dịch Vụ khi dùng câu lệnh JOIN SQL
    private DichVu dichVu; 
    private LichHen lichHen;

    // Constructor rỗng
    public ChiTietLichHen() {
    }

    // Constructor cơ bản (Dành cho chức năng Thêm/Insert)
    public ChiTietLichHen(int lichHenId, int dichVuId, int soLuong) {
        this.lichHenId = lichHenId;
        this.dichVuId = dichVuId;
        this.soLuong = soLuong;
    }

    // Constructor đầy đủ (Dành cho chức năng Lấy/Select dữ liệu lên)
    public ChiTietLichHen(int chiTietLichHenId, int lichHenId, int dichVuId, int soLuong) {
        this.chiTietLichHenId = chiTietLichHenId;
        this.lichHenId = lichHenId;
        this.dichVuId = dichVuId;
        this.soLuong = soLuong;
    }

    // GETTER & SETTER

    public int getChiTietLichHenId() {
        return chiTietLichHenId;
    }

    public void setChiTietLichHenId(int chiTietLichHenId) {
        this.chiTietLichHenId = chiTietLichHenId;
    }

    public int getLichHenId() {
        return lichHenId;
    }

    public void setLichHenId(int lichHenId) {
        this.lichHenId = lichHenId;
    }

    public int getDichVuId() {
        return dichVuId;
    }

    public void setDichVuId(int dichVuId) {
        this.dichVuId = dichVuId;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    // Getter & Setter cho đối tượng DichVu mở rộng
    public DichVu getDichVu() {
        return dichVu;
    }

    public void setDichVu(DichVu dichVu) {
        this.dichVu = dichVu;
    }

    public LichHen getLichHen(){
        return lichHen;
    }
    public void setLichHen(LichHen lichHen){
        this.lichHen = lichHen;
    }

}