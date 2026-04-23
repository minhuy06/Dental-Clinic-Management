package com.dentalclinic.dao;

import com.dentalclinic.model.BenhNhan;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BenhNhanDAO {

    public BenhNhan getBenhNhanByTaiKhoanID(int taiKhoanID) {
        String sql = "SELECT bn.BenhNhan_ID, bn.TaiKhoan_ID, bn.NhomMau, bn.TienSuBenh, " +
                     "tk.HoTen, tk.NgaySinh, tk.GioiTinh, tk.SoDienThoai " +
                     "FROM BenhNhan bn " +
                     "JOIN TaiKhoan tk ON bn.TaiKhoan_ID = tk.TaiKhoan_ID " +
                     "WHERE bn.TaiKhoan_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taiKhoanID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BenhNhan bn = new BenhNhan();
                    bn.setBenhNhanID(rs.getInt("BenhNhan_ID"));
                    bn.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                    bn.setNhomMau(rs.getString("NhomMau"));
                    bn.setTienSuBenh(rs.getString("TienSuBenh"));

                    TaiKhoan tk = new TaiKhoan();
                    tk.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                    tk.setHoTen(rs.getString("HoTen"));
                    tk.setNgaySinh(rs.getDate("NgaySinh"));
                    tk.setGioiTinh(rs.getBoolean("GioiTinh"));
                    tk.setSoDienThoai(rs.getString("SoDienThoai"));
                    bn.setTaiKhoan(tk);

                    return bn;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
