package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.DichVu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DichVuDAO {

    public List<DichVu> getAll() {
        List<DichVu> list = new ArrayList<>();
        String sql = "SELECT * FROM DichVu"; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setDichVuID(rs.getInt("DichVu_ID"));
                dv.setTenDichVu(rs.getNString("TenDichVu"));
                dv.setGiaTien(rs.getDouble("Gia"));
                dv.setThoiLuongDuKien(rs.getInt("ThoiLuongDuKien"));
                dv.setChuyenKhoaID(rs.getInt("ChuyenKhoa_ID"));
                dv.setTinhTheoRang(rs.getBoolean("TinhTheoRang"));
                list.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}