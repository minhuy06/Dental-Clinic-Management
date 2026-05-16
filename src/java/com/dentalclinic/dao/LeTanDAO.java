package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeTanDAO {

    /** Mã lễ tân theo tài khoản đăng nhập (null nếu không phải lễ tân). */
    public Integer findLeTanIdByTaiKhoanId(int taiKhoanId) {
        if (taiKhoanId <= 0) {
            return null;
        }
        String sql = "SELECT LeTan_ID FROM LeTan WHERE TaiKhoan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taiKhoanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("LeTan_ID");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LeTanDAO] findLeTanIdByTaiKhoanId: " + e.getMessage());
        }
        return null;
    }
}
