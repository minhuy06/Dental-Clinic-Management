package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.LichLamViec;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.model.CaLam;
import com.dentalclinic.model.PhongKham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichLamViecDAO {

    /**
     * Lấy toàn bộ danh sách lịch làm việc để hiển thị trên giao diện Admin
     * Thực hiện JOIN 3 bảng để lấy thông tin chi tiết
     */
    public List<LichLamViec> getAllForAdmin() {
        List<LichLamViec> list = new ArrayList<>();
        String sql = "SELECT lv.*, tk.HoTen, cl.TenCa, cl.GioBatDau, cl.GioKetThuc, pk.TenPhong " +
                     "FROM LichLamViec lv " +
                     "JOIN TaiKhoan tk ON lv.TaiKhoan_ID = tk.TaiKhoan_ID " +
                     "JOIN CaLam cl ON lv.Ca_ID = cl.Ca_ID " +
                     "JOIN PhongKham pk ON lv.Phong_ID = pk.Phong_ID " +
                     "ORDER BY lv.NgayLam DESC, cl.GioBatDau ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LichLamViec lv = new LichLamViec();
                lv.setLichID(rs.getInt("Lich_ID"));
                lv.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                lv.setCaID(rs.getInt("Ca_ID"));
                lv.setNgayLam(rs.getDate("NgayLam"));
                lv.setPhongID(rs.getInt("Phong_ID"));

                // 1. Set thông tin Tài khoản (Bác sĩ)
                TaiKhoan tk = new TaiKhoan();
                tk.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                tk.setHoTen(rs.getNString("HoTen"));
                lv.setTaiKhoan(tk);

                // 2. Set thông tin Ca làm
                CaLam cl = new CaLam();
                cl.setCaID(rs.getInt("Ca_ID"));
                cl.setTenCa(rs.getNString("TenCa"));
                // Giả sử Model CaLam của bạn có các trường này để hiển thị giờ
                // cl.setGioBatDau(rs.getTime("GioBatDau")); 
                lv.setCaLam(cl);

                // 3. Set thông tin Phòng khám
                PhongKham pk = new PhongKham();
                pk.setPhongID(rs.getInt("Phong_ID"));
                pk.setTenPhong(rs.getNString("TenPhong"));
                lv.setPhongKham(pk);

                list.add(lv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm mới lịch làm việc
     */
    public boolean insert(LichLamViec lv) {
        String sql = "INSERT INTO LichLamViec (TaiKhoan_ID, Ca_ID, NgayLam, Phong_ID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, lv.getTaiKhoanID());
            ps.setInt(2, lv.getCaID());
            ps.setDate(3, lv.getNgayLam()); // Vì model dùng java.sql.Date nên truyền trực tiếp được
            ps.setInt(4, lv.getPhongID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa lịch làm việc theo ID
     */
    public boolean delete(int lichID) {
        String sql = "DELETE FROM LichLamViec WHERE Lich_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(LichLamViec lv) {
        String sql = "UPDATE LichLamViec SET TaiKhoan_ID = ?, Ca_ID = ?, NgayLam = ?, Phong_ID = ? WHERE Lich_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lv.getTaiKhoanID());
            ps.setInt(2, lv.getCaID());
            ps.setDate(3, lv.getNgayLam());
            ps.setInt(4, lv.getPhongID());
            ps.setInt(5, lv.getLichID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra trùng lịch: Một bác sĩ không thể trực 2 nơi/2 ca trong cùng 1 thời điểm
     */
    public boolean isDuplicate(int taiKhoanID, Date ngay, int caID) {
        String sql = "SELECT 1 FROM LichLamViec WHERE TaiKhoan_ID = ? AND NgayLam = ? AND Ca_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taiKhoanID);
            ps.setDate(2, ngay);
            ps.setInt(3, caID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDuplicateExcludingId(int taiKhoanID, Date ngay, int caID, int lichID) {
        String sql = "SELECT 1 FROM LichLamViec WHERE TaiKhoan_ID = ? AND NgayLam = ? AND Ca_ID = ? AND Lich_ID <> ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taiKhoanID);
            ps.setDate(2, ngay);
            ps.setInt(3, caID);
            ps.setInt(4, lichID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}