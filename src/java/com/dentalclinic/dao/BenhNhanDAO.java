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
}