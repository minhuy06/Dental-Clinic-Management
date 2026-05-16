package com.dentalclinic.dao;

import com.dentalclinic.model.DichVu;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DichVuDAO {
    
    public List<DichVu> layDanhSachDichVu() {
        List<DichVu> list = new ArrayList<>();
        // Truy vấn tối giản để tránh lỗi thiếu cột TrangThai
        String sql = "SELECT DichVu_ID, TenDichVu, GiaTien, TinhTheoRang FROM DichVu";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setDichVuID(rs.getInt("DichVu_ID"));
                dv.setTenDichVu(rs.getNString("TenDichVu"));
                dv.setGiaTien(rs.getDouble("GiaTien"));
                dv.setTinhTheoRang(rs.getBoolean("TinhTheoRang"));
                list.add(dv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
