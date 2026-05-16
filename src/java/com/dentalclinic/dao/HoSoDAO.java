package com.dentalclinic.dao;

import com.dentalclinic.model.HoSo;
import com.dentalclinic.model.BenhNhan;
import com.dentalclinic.model.TaiKhoan;
import java.sql.Connection;
import com.dentalclinic.utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HoSoDAO {
    public HoSo layHoSoTheoBenhNhan(int benhNhanID) {
        String sql = "SELECT * FROM HoSo WHERE BenhNhan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, benhNhanID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HoSo hs = new HoSo();
                    hs.setHoSoID(rs.getInt("HoSo_ID"));
                    hs.setDiUngThuoc(rs.getNString("DiUngThuoc"));
                    hs.setTienSuBenh(rs.getNString("TienSuBenh"));
                    return hs;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
