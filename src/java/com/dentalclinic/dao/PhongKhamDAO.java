package com.dentalclinic.dao;

import com.dentalclinic.model.PhongKham;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhongKhamDAO {

    public List<PhongKham> getAll() {
        List<PhongKham> list = new ArrayList<>();
        String sql = "SELECT Phong_ID, TenPhong FROM PhongKham ORDER BY Phong_ID";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhongKham phong = new PhongKham();
                phong.setPhongID(rs.getInt("Phong_ID"));
                phong.setTenPhong(rs.getNString("TenPhong"));
                list.add(phong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
