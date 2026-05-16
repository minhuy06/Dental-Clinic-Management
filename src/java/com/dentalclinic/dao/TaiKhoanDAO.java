package com.dentalclinic.dao;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {
    public TaiKhoan getTaiKhoanByPhone(String phone) {
        TaiKhoan tk = null;
        String sql = "SELECT * FROM TAIKHOAN WHERE soDienThoai=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoan();
                    tk.setSoDienThoai(rs.getString("soDienThoai"));
                    tk.setTaiKhoanID(rs.getInt("taiKhoan_ID"));
                    tk.setHoTen(rs.getString("hoTen"));
                    tk.setGioiTinh(rs.getBoolean("gioiTinh"));
                    tk.setMatKhau(rs.getString("matKhau"));
                    tk.setVaiTro(rs.getString("vaiTro"));
                    tk.setTrangThai(rs.getString("trangThai"));
                    tk.setNgaySinh(rs.getDate("ngaySinh"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tk;
    }

    public boolean updatePassword(int taiKhoanId, String newPassword) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TaiKhoan_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, taiKhoanId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi đổi mật khẩu TaiKhoanDAO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInfo(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET SoDienThoai = ?, NgaySinh = ?, GioiTinh = ? WHERE TaiKhoan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getSoDienThoai());
            ps.setDate(2, tk.getNgaySinh() != null ? new java.sql.Date(tk.getNgaySinh().getTime()) : null);
            ps.setObject(3, tk.isGioiTinh());
            ps.setInt(4, tk.getTaiKhoanID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
