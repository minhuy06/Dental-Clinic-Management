package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.LichHen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BenhNhanDAO {

    /**
     * Lấy danh sách lịch hẹn của 1 bệnh nhân dựa theo trạng thái
     * @param benhNhanID ID của bệnh nhân đang đăng nhập
     * @param trangThai "Chờ xác nhận", "Đã khám", "Đã hủy",...
     */
    public List<LichHen> getLichSuDatLich(int benhNhanID, String trangThai) {
        List<LichHen> list = new ArrayList<>();
        // Query JOIN với bảng TaiKhoan (của bác sĩ) để lấy tên bác sĩ khám
        String sql = "SELECT lh.*, tk.HoTen AS TenBacSi " +
                     "FROM LichHen lh " +
                     "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +
                     "JOIN TaiKhoan tk ON bs.TaiKhoan_ID = tk.TaiKhoan_ID " +
                     "WHERE lh.BenhNhan_ID = ? AND lh.TrangThai = ? " +
                     "ORDER BY lh.NgayKham DESC, lh.GioKham DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, benhNhanID);
            ps.setNString(2, trangThai);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichHen lh = new LichHen();
                    lh.setLichHenID(rs.getInt("LichHen_ID"));
                    lh.setNgayKham(rs.getDate("NgayKham"));
                    lh.setGioKham(rs.getTime("GioKham"));
                    lh.setTrangThai(rs.getNString("TrangThai"));
                    lh.setGhiChu(rs.getNString("GhiChu"));
                    // Bạn có thể lưu tên bác sĩ vào một trường tạm hoặc object BacSi trong Model
                    list.add(lh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int resolveOrCreateDeskPatient(String hoTen, String soDienThoai) {
        String ten = hoTen == null ? "" : hoTen.trim();
        String sdt = soDienThoai == null ? "" : soDienThoai.trim();
        if (ten.isEmpty() || sdt.isEmpty()) return -1;

        String findSql = "SELECT bn.BenhNhan_ID, tk.TaiKhoan_ID FROM BenhNhan bn " +
                         "JOIN TaiKhoan tk ON tk.TaiKhoan_ID = bn.TaiKhoan_ID " +
                         "WHERE tk.SoDienThoai = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(findSql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int taiKhoanId = rs.getInt("TaiKhoan_ID");
                    int benhNhanId = rs.getInt("BenhNhan_ID");
                    try (PreparedStatement ups = conn.prepareStatement("UPDATE TaiKhoan SET HoTen = ? WHERE TaiKhoan_ID = ?")) {
                        ups.setNString(1, ten);
                        ups.setInt(2, taiKhoanId);
                        ups.executeUpdate();
                    }
                    return benhNhanId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insertTk = "INSERT INTO TaiKhoan (SoDienThoai, MatKhau, VaiTro, TrangThai, HoTen) VALUES (?, ?, N'Bệnh nhân', N'Hoạt động', ?)";
        String insertBn = "INSERT INTO BenhNhan (TaiKhoan_ID) VALUES (?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psTk = conn.prepareStatement(insertTk, Statement.RETURN_GENERATED_KEYS)) {
                psTk.setString(1, sdt);
                psTk.setString(2, "123456");
                psTk.setNString(3, ten);
                psTk.executeUpdate();
                int taiKhoanId = -1;
                try (ResultSet keys = psTk.getGeneratedKeys()) {
                    if (keys.next()) taiKhoanId = keys.getInt(1);
                }
                if (taiKhoanId <= 0) {
                    conn.rollback();
                    return -1;
                }
                try (PreparedStatement psBn = conn.prepareStatement(insertBn, Statement.RETURN_GENERATED_KEYS)) {
                    psBn.setInt(1, taiKhoanId);
                    psBn.executeUpdate();
                    int benhNhanId = -1;
                    try (ResultSet keysBn = psBn.getGeneratedKeys()) {
                        if (keysBn.next()) benhNhanId = keysBn.getInt(1);
                    }
                    if (benhNhanId <= 0) {
                        conn.rollback();
                        return -1;
                    }
                    conn.commit();
                    return benhNhanId;
                }
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}