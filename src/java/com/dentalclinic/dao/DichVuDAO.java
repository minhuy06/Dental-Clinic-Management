package com.dentalclinic.dao;

import com.dentalclinic.model.DichVu;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DichVuDAO {

    public List<DichVu> getAllDichVu() {
        List<DichVu> list = new ArrayList<>();
        String sql = "SELECT DichVu_ID, TenDichVu, GiaTien, ThoiLuongDuKien FROM DichVu ORDER BY DichVu_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setDichVuID(rs.getInt("DichVu_ID"));
                dv.setTenDichVu(rs.getString("TenDichVu"));
                dv.setGiaTien(rs.getDouble("GiaTien"));
                dv.setThoiLuongDuKien(String.valueOf(rs.getInt("ThoiLuongDuKien")));
                list.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
