package com.dentalclinic.dao;

import com.dentalclinic.model.DanhSachLichHenDTO;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LichHenDAO {
    public List<DanhSachLichHenDTO> layDanhSachChoKhamNgayHienTai(){
        List<DanhSachLichHenDTO> list = new ArrayList<>();
        String sql = "Select lh.LichHen_ID, tk_bn.HoTen as TenBenhNhan, tk_bn.SoDienThoai, CONVERT(VARCHAR(5), lh.GioKham, 108) as GioHen, tk_bs.HoTen as TenBacSi, lh.GhiChu as LyDoKham, lh.TrangThai "+
                "from LichHen as lh "+
                "join BenhNhan bn on lh.BenhNhan_ID = bn.BenhNhan_ID "+
                "join TaiKhoan tk_bn on bn.TaiKhoan_ID = tk_bn.TaiKhoan_ID "+
                "JOIN BacSi bs on lh.BacSi_ID = bs.BacSi_ID "+
                "JOIN TaiKhoan tk_bs ON bs.TaiKhoan_ID = tk_bs.TaiKhoan_ID " +
                "WHERE CONVERT(DATE, lh.NgayKham) = CONVERT(DATE, GETDATE()) and lh.TrangThai in (N'Đã đến', N'Đang khám') "+
                "ORDER BY lh.GioKham ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
                while(rs.next()){
                    DanhSachLichHenDTO ds = new DanhSachLichHenDTO();
                    ds.setLichHenID(rs.getInt("LichHen_ID"));
                    ds.setTenBenhNhan(rs.getNString("TenBenhNhan"));
                    ds.setSoDienThoai(rs.getString("SoDienThoai"));
                    ds.setGioHen(rs.getString("GioHen"));
                    ds.setTenBacSi(rs.getNString("TenBacSi"));
                    ds.setLyDoKham(rs.getNString("LyDoKham"));
                    
                    String trangThaiDB = rs.getNString("TrangThai");
                    if ("Đã đến".equalsIgnoreCase(trangThaiDB)) ds.setTrangThai("waiting");
                    else if ("Đang khám".equalsIgnoreCase(trangThaiDB)) ds.setTrangThai("examining");
                    else if ("Đã hoàn thành".equalsIgnoreCase(trangThaiDB)) ds.setTrangThai("completed");
                    else ds.setTrangThai("cancelled");
                    
                    list.add(ds);
                }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    
    // Hàm cập nhật trạng thái thành "Đang khám" khi bác sĩ ấn "Khám ngay"
    public boolean capNhatTrangThaiDangKham(int lichHenID){
        String sql = "Update LichHen Set TrangThai = N'Đang khám' Where LichHen_ID = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setInt(1, lichHenID);
            return ps.executeUpdate() > 0;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
