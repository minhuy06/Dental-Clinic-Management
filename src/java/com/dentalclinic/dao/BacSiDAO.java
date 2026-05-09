package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.model.ChuyenKhoa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BacSiDAO {

    /**
     * 1. Lấy danh sách tất cả bác sĩ kèm thông tin Họ tên, Chuyên khoa để hiển thị lên Website.
     * Tương ứng với phần <section id="bacsi"> trong file JSP của bạn.
     */
    public List<BacSi> getAllForDisplay() {
        List<BacSi> list = new ArrayList<>();
        String sql = "SELECT b.*, t.HoTen, c.TenChuyenKhoa " +
                     "FROM BacSi b " +
                     "JOIN TaiKhoan t ON b.TaiKhoan_ID = t.TaiKhoan_ID " +
                     "JOIN ChuyenKhoa c ON b.ChuyenKhoa_ID = c.ChuyenKhoa_ID " +
                     "WHERE t.TrangThai = N'Hoạt động'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                list.add(mapFullInfo(rs, md));
            }
            return list;
        } catch (SQLException e) {
            System.err.println("[BacSiDAO] getAllForDisplay fallback (không JOIN ChuyenKhoa hoặc thiếu ChuyenKhoa_ID): " + e.getMessage());
            e.printStackTrace();
        }
        String fallback = "SELECT b.BacSi_ID, b.TaiKhoan_ID, b.AnhDaiDien, b.TrinhDo, t.HoTen, CAST(N'—' AS NVARCHAR(100)) AS TenChuyenKhoa, CAST(0 AS INT) AS ChuyenKhoa_ID "
                + "FROM BacSi b "
                + "JOIN TaiKhoan t ON b.TaiKhoan_ID = t.TaiKhoan_ID "
                + "WHERE t.TrangThai = N'Hoạt động'";
        if (bacSiHasChuyenKhoaIdColumn()) {
            fallback = "SELECT b.BacSi_ID, b.TaiKhoan_ID, b.ChuyenKhoa_ID, b.AnhDaiDien, b.TrinhDo, t.HoTen, "
                    + "COALESCE(c.TenChuyenKhoa, N'—') AS TenChuyenKhoa FROM BacSi b "
                    + "JOIN TaiKhoan t ON b.TaiKhoan_ID = t.TaiKhoan_ID "
                    + "LEFT JOIN ChuyenKhoa c ON b.ChuyenKhoa_ID = c.ChuyenKhoa_ID "
                    + "WHERE t.TrangThai = N'Hoạt động'";
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(fallback);
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                list.add(mapFullInfo(rs, md));
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        return list;
    }

    private boolean bacSiHasChuyenKhoaIdColumn() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'BacSi' AND COLUMN_NAME = N'ChuyenKhoa_ID'");
             ResultSet rs = ps.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 2. Tìm danh sách ID bác sĩ rảnh dựa trên ngày và giờ.
     * Đây là logic chạy ngầm sau khi người dùng nhấn "Lưu lịch hẹn".
     */
    public List<Integer> getAvailableDoctorIds(String ngayKham, String gioKham) {
        List<Integer> availableIds = new ArrayList<>();
        
        // Logic: 
        // 1. Phải có lịch làm việc (LichLamViec) vào ngày đó.
        // 2. Không được có lịch hẹn (LichHen) nào trùng giờ đó mà trạng thái chưa hủy.
        String sql = "SELECT BacSi_ID FROM BacSi " +
                     "WHERE BacSi_ID IN (SELECT BacSi_ID FROM LichLamViec WHERE NgayLamViec = ?) " +
                     "AND BacSi_ID NOT IN (SELECT BacSi_ID FROM LichHen WHERE NgayKham = ? AND GioKham = ? AND TrangThai <> N'Đã hủy')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ngayKham);
            ps.setString(2, ngayKham);
            ps.setString(3, gioKham);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    availableIds.add(rs.getInt("BacSi_ID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableIds;
    }

    /**
     * 3. Lấy thông tin chi tiết 1 bác sĩ theo ID
     */
    public BacSi getById(int id) {
        String sql = "SELECT b.*, t.HoTen, c.TenChuyenKhoa FROM BacSi b " +
                     "JOIN TaiKhoan t ON b.TaiKhoan_ID = t.TaiKhoan_ID " +
                     "JOIN ChuyenKhoa c ON b.ChuyenKhoa_ID = c.ChuyenKhoa_ID " +
                     "WHERE b.BacSi_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                if (rs.next()) return mapFullInfo(rs, md);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean mdHas(ResultSetMetaData md, String col) throws SQLException {
        int n = md.getColumnCount();
        for (int i = 1; i <= n; i++) {
            String l = md.getColumnLabel(i);
            if (l != null && l.equalsIgnoreCase(col)) return true;
        }
        return false;
    }

    private BacSi mapFullInfo(ResultSet rs, ResultSetMetaData md) throws SQLException {
        BacSi bs = new BacSi();
        bs.setBacSiID(rs.getInt("BacSi_ID"));
        bs.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
        if (mdHas(md, "ChuyenKhoa_ID")) {
            bs.setChuyenKhoaID(rs.getInt("ChuyenKhoa_ID"));
        } else {
            bs.setChuyenKhoaID(0);
        }
        if (mdHas(md, "AnhDaiDien")) {
            bs.setAnhDaiDien(rs.getString("AnhDaiDien"));
        }
        bs.setTrinhDo(rs.getNString("TrinhDo"));

        TaiKhoan tk = new TaiKhoan();
        tk.setHoTen(rs.getNString("HoTen"));
        bs.setTaiKhoan(tk);

        ChuyenKhoa ck = new ChuyenKhoa();
        ck.setTenChuyenKhoa(rs.getNString("TenChuyenKhoa"));
        bs.setChuyenKhoa(ck);

        return bs;
    }
}