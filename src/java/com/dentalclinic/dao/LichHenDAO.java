package com.dentalclinic.dao;

import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.BenhNhan;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.HoSo;
import com.dentalclinic.model.PhongKham;
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

    // Lấy thông tin đầy đủ của Lịch Hẹn (bao gồm Bệnh nhân, Hồ sơ, Bác sĩ, Phòng khám)
    public LichHen layChiTietLichHen(int lichHenID){
        String sql = "select tk_bn.HoTen, tk_bn.GioiTinh, tk_bn.SoDienThoai, tk_bn.NgaySinh, " +
                "hs.DiUngThuoc, hs.TienSuBenh, lh.BenhNhan_ID, " +
                "tk_bs.HoTen as TenBacSi, pk.TenPhong " +
                "from LichHen as lh " + 
                "join BenhNhan bn on lh.BenhNhan_ID = bn.BenhNhan_ID " + 
                "join TaiKhoan tk_bn on bn.TaiKhoan_ID = tk_bn.TaiKhoan_ID " +
                "left join HoSo hs on bn.BenhNhan_ID = hs.BenhNhan_ID " +
                "left join BacSi bs on lh.BacSi_ID = bs.BacSi_ID " +
                "left join TaiKhoan tk_bs on bs.TaiKhoan_ID = tk_bs.TaiKhoan_ID " +
                "left join PhongKham pk on lh.Phong_ID = pk.Phong_ID " +
                "where lh.LichHen_ID = ?";
        
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setInt(1, lichHenID);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    // 1. Thông tin tài khoản bệnh nhân
                    TaiKhoan tkBN = new TaiKhoan();
                    tkBN.setHoTen(rs.getNString("HoTen"));
                    tkBN.setGioiTinh(rs.getBoolean("GioiTinh"));
                    tkBN.setNgaySinh(rs.getDate("NgaySinh"));
                    tkBN.setSoDienThoai(rs.getString("SoDienThoai"));
                    
                    // 2. Thông tin hồ sơ bệnh nhân
                    HoSo hs = new HoSo();
                    hs.setDiUngThuoc(rs.getNString("DiUngThuoc") != null ? rs.getNString("DiUngThuoc") : "Không có cảnh báo");
                    hs.setTienSuBenh(rs.getNString("TienSuBenh") != null ? rs.getNString("TienSuBenh") : "Chưa ghi nhận");
                    
                    // 3. Ghép vào Bệnh nhân
                    BenhNhan bn = new BenhNhan();
                    bn.setBenhNhanID(rs.getInt("BenhNhan_ID"));
                    bn.setTaiKhoan(tkBN);
                    bn.setHoSo(hs);
                    
                    // 4. Thông tin bác sĩ
                    TaiKhoan tkBS = new TaiKhoan();
                    tkBS.setHoTen(rs.getNString("TenBacSi"));
                    BacSi bs = new BacSi();
                    bs.setTaiKhoan(tkBS);
                    
                    // 5. Thông tin phòng khám
                    PhongKham pk = new PhongKham();
                    pk.setTenPhong(rs.getNString("TenPhong") != null ? rs.getNString("TenPhong") : "Chưa xếp phòng");
                    
                    // 6. Ghép tất cả vào Lịch Hẹn
                    LichHen lh = new LichHen();
                    lh.setLichHenID(lichHenID);
                    lh.setBenhNhan(bn);
                    lh.setBacSi(bs);
                    lh.setPhongKham(pk);
                    
                    return lh;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
