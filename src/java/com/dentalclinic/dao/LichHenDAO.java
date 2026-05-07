package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.model.BenhNhan;
import com.dentalclinic.model.BacSi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichHenDAO {

    // Lấy lịch hẹn cho hồ sơ cá nhân (Dùng List)
    public List<LichHen> getLichHenByBenhNhan(int taiKhoanID) {
        List<LichHen> list = new ArrayList<>();
        String sql = "SELECT lh.*, tkBS.HoTen AS TenBacSi " +
                     "FROM LichHen lh " +
                     "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID " +
                     "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +
                     "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID " +
                     "WHERE bn.TaiKhoan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taiKhoanID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichHen lh = mapResultSetToLichHen(rs);

                    // 1. Tạo TaiKhoan chứa tên Bác sĩ
                    TaiKhoan tkBs = new TaiKhoan();
                    tkBs.setHoTen(rs.getNString("TenBacSi"));

                    // 2. Tạo đối tượng BacSi và bỏ TaiKhoan vào
                    BacSi bs = new BacSi();
                    bs.setTaiKhoan(tkBs);

                    // 3. Gán BacSi vào LichHen (Lúc này lh.setBacSi nhận vào kiểu BacSi)
                    lh.setBacSi(bs); 

                    list.add(lh);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Lấy tất cả lịch hẹn cho Lễ tân (index.jsp)
    public List<LichHen> getAllLichHen() {
        List<LichHen> list = new ArrayList<>();
        String sql = "SELECT lh.*, tkBN.HoTen AS TenBenhNhan " +
                     "FROM LichHen lh " +
                     "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID " +
                     "JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID " +
                     "ORDER BY lh.NgayKham DESC, lh.GioKham DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LichHen lh = mapResultSetToLichHen(rs);

                // 1. Tạo TaiKhoan chứa tên Bệnh nhân
                TaiKhoan tkBn = new TaiKhoan();
                tkBn.setHoTen(rs.getNString("TenBenhNhan"));

                // 2. Tạo đối tượng BenhNhan và bỏ TaiKhoan vào
                BenhNhan bn = new BenhNhan();
                bn.setTaiKhoan(tkBn);

                // 3. Gán BenhNhan vào LichHen
                lh.setBenhNhan(bn); 

                list.add(lh);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Lưu lịch hẹn mới (Fix lỗi N"" và kiểu dữ liệu)
    public int insertBooking(LichHen lh, List<Integer> dichVuIds) {
        String sql = "INSERT INTO LichHen (BenhNhan_ID, BacSi_ID, NgayKham, GioKham, GhiChu, TrangThai, Phong_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, lh.getBenhNhanID());
                ps.setInt(2, lh.getBacSiID());
                ps.setDate(3, lh.getNgayKham());
                ps.setTime(4, lh.getGioKham());
                ps.setNString(5, lh.getGhiChu());
                ps.setNString(6, lh.getTrangThai()); // Truyền trực tiếp chuỗi "Chờ duyệt"
                ps.setInt(7, lh.getPhongID());
                
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int lhID = rs.getInt(1);
                    // Lưu chi tiết dịch vụ
                    String sqlDV = "INSERT INTO ChiTietLichHen (LichHen_ID, DichVu_ID) VALUES (?, ?)";
                    try (PreparedStatement psDV = conn.prepareStatement(sqlDV)) {
                        for (Integer dvID : dichVuIds) {
                            psDV.setInt(1, lhID);
                            psDV.setInt(2, dvID);
                            psDV.addBatch();
                        }
                        psDV.executeBatch();
                    }
                    conn.commit();
                    return lhID;
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    private LichHen mapResultSetToLichHen(ResultSet rs) throws SQLException {
        LichHen lh = new LichHen();
        lh.setLichHenID(rs.getInt("LichHen_ID"));
        lh.setBenhNhanID(rs.getInt("BenhNhan_ID"));
        lh.setBacSiID(rs.getInt("BacSi_ID"));
        lh.setNgayKham(rs.getDate("NgayKham"));
        lh.setGioKham(rs.getTime("GioKham"));
        lh.setGhiChu(rs.getNString("GhiChu"));
        lh.setTrangThai(rs.getNString("TrangThai"));
        return lh;
    }
}