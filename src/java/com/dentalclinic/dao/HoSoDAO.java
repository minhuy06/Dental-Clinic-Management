package com.dentalclinic.dao;

import com.dentalclinic.model.HoSo;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.model.BenhNhan;
import java.sql.Connection;
import com.dentalclinic.utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HoSoDAO {
    // Truy xuất dữ liệu từ HoSo để đổ ra phiếu khám
    public HoSo layThongTinHoSo(int lichHenID){
        String sql = "select tk.HoTen, tk.GioiTinh, tk.SoDienThoai, tk.NgaySinh, hs.DiUngThuoc, hs.TienSuBenh, lh.BenhNhan_ID " +
                "from LichHen as lh " + 
                "join BenhNhan bn on lh.BenhNhan_ID = bn.BenhNhan_ID " + 
                "join TaiKhoan tk on bn.TaiKhoan_ID = tk.TaiKhoan_ID " +
                "left join HoSo hs on bn.BenhNhan_ID = hs.BenhNhan_ID " +
                "where lh.LichHen_ID = ?";
        
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setInt(1, lichHenID);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    TaiKhoan tk = new TaiKhoan();
                    tk.setHoTen(rs.getNString("HoTen"));
                    tk.setGioiTinh(rs.getBoolean("GioiTinh"));
                    tk.setNgaySinh(rs.getDate("NgaySinh"));
                    tk.setSoDienThoai(rs.getString("SoDienThoai"));
                    
                    BenhNhan bn = new BenhNhan();
                    bn.setBenhNhanID(rs.getInt("BenhNhan_ID"));
                    bn.setTaiKhoan(tk);
                    
                    HoSo hs = new HoSo();
                    hs.setDiUngThuoc(rs.getNString("DiUngThuoc") != null ? rs.getNString("DiUngThuoc") : "Không có cảnh báo");
                    hs.setTienSuBenh(rs.getNString("TienSuBenh") != null ? rs.getNString("TienSuBenh") : "Chưa ghi nhận");
                    hs.setBenhNhan(bn);
                    
                    return hs;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
