package com.dentalclinic.dao;

import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.BenhNhan;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichHenDAO {

    /**
     * Lấy toàn bộ lịch hẹn cho Admin (Gộp các dịch vụ thành chuỗi để hiển thị)
     */
    public List<LichHen> getAllLichHen() {
        List<LichHen> list = new ArrayList<>();
        // Sử dụng STRING_AGG để gộp nhiều dịch vụ của 1 lịch hẹn thành 1 dòng duy nhất
        String sql = "SELECT lh.LichHen_ID, lh.BenhNhan_ID, lh.BacSi_ID, " +
                     "tkBN.HoTen AS TenBN, tkBS.HoTen AS TenBS, " +
                     "STRING_AGG(dv.TenDichVu, ', ') AS TenDichVu, " +
                     "lh.NgayKham, lh.GioKham, lh.TrangThai, lh.GhiChu " +
                     "FROM LichHen lh " +
                     "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID " +
                     "JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID " +
                     "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +
                     "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID " +
                     "LEFT JOIN ChiTietLichHen ctlh ON lh.LichHen_ID = ctlh.LichHen_ID " +
                     "LEFT JOIN DichVu dv ON ctlh.DichVu_ID = dv.DichVu_ID " +
                     "GROUP BY lh.LichHen_ID, lh.BenhNhan_ID, lh.BacSi_ID, " +
                     "tkBN.HoTen, tkBS.HoTen, lh.NgayKham, lh.GioKham, lh.TrangThai, lh.GhiChu " +
                     "ORDER BY lh.NgayKham DESC, lh.GioKham DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LichHen lh = new LichHen();
                lh.setLichHenID(rs.getInt("LichHen_ID"));
                lh.setNgayKham(rs.getDate("NgayKham"));
                lh.setGioKham(rs.getTime("GioKham"));
                lh.setTrangThai(rs.getString("TrangThai"));
                lh.setGhiChu(rs.getString("GhiChu"));

                // Gán tên bệnh nhân
                BenhNhan bn = new BenhNhan();
                TaiKhoan tkBN = new TaiKhoan();
                tkBN.setHoTen(rs.getString("TenBN"));
                bn.setTaiKhoan(tkBN);
                lh.setBenhNhan(bn);

                // Gán tên bác sĩ
                BacSi bs = new BacSi();
                TaiKhoan tkBS = new TaiKhoan();
                tkBS.setHoTen(rs.getString("TenBS"));
                bs.setTaiKhoan(tkBS);
                lh.setBacSi(bs);

                // Gán chuỗi tên dịch vụ (ví dụ: "Nhổ răng, Trám răng")
                lh.setTenDichVuChung(rs.getString("TenDichVu")); 

                list.add(lh);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Lấy lịch sử đặt lịch của một bệnh nhân cụ thể
     */
    public List<LichHen> getLichHenByBenhNhan(int benhNhanID) {
        List<LichHen> list = new ArrayList<>();
        String sql = "SELECT lh.LichHen_ID, tkBS.HoTen AS TenBS, " +
                     "STRING_AGG(dv.TenDichVu, ', ') AS TenDichVu, " +
                     "lh.NgayKham, lh.GioKham, lh.TrangThai " +
                     "FROM LichHen lh " +
                     "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +
                     "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID " +
                     "LEFT JOIN ChiTietLichHen ctlh ON lh.LichHen_ID = ctlh.LichHen_ID " +
                     "LEFT JOIN DichVu dv ON ctlh.DichVu_ID = dv.DichVu_ID " +
                     "WHERE lh.BenhNhan_ID = ? " +
                     "GROUP BY lh.LichHen_ID, tkBS.HoTen, lh.NgayKham, lh.GioKham, lh.TrangThai " +
                     "ORDER BY lh.NgayKham DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, benhNhanID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichHen lh = new LichHen();
                    lh.setLichHenID(rs.getInt("LichHen_ID"));
                    lh.setNgayKham(rs.getDate("NgayKham"));
                    lh.setGioKham(rs.getTime("GioKham"));
                    lh.setTrangThai(rs.getString("TrangThai"));

                    BacSi bs = new BacSi();
                    TaiKhoan tk = new TaiKhoan();
                    tk.setHoTen(rs.getString("TenBS"));
                    bs.setTaiKhoan(tk);
                    lh.setBacSi(bs);

                    lh.setTenDichVuChung(rs.getString("TenDichVu"));
                    list.add(lh);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Tạo lịch hẹn mới (Dùng Transaction để chèn vào 2 bảng LichHen và ChiTietLichHen)
     */
    public boolean taoLichHen(LichHen model, List<Integer> listDichVuIDs) {
        String sqlLH = "INSERT INTO LichHen (BenhNhan_ID, BacSi_ID, NgayKham, GioKham, GhiChu, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlCT = "INSERT INTO ChiTietLichHen (LichHen_ID, DichVu_ID) VALUES (?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // 1. Chèn vào bảng LichHen
            PreparedStatement psLH = conn.prepareStatement(sqlLH, Statement.RETURN_GENERATED_KEYS);
            psLH.setInt(1, model.getBenhNhanID());
            psLH.setInt(2, model.getBacSiID());
            psLH.setDate(3, model.getNgayKham());
            psLH.setTime(4, model.getGioKham());
            psLH.setString(5, model.getGhiChu());
            psLH.setString(6, "Chờ xác nhận");
            psLH.executeUpdate();

            // Lấy LichHen_ID vừa tạo
            ResultSet rs = psLH.getGeneratedKeys();
            int lichHenID = 0;
            if (rs.next()) lichHenID = rs.getInt(1);

            // 2. Chèn các dịch vụ vào bảng ChiTietLichHen
            PreparedStatement psCT = conn.prepareStatement(sqlCT);
            for (int dvID : listDichVuIDs) {
                psCT.setInt(1, lichHenID);
                psCT.setInt(2, dvID);
                psCT.addBatch();
            }
            psCT.executeBatch();

            conn.commit(); // Thành công thì commit
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    /**
     * Cập nhật trạng thái lịch hẹn (Xác nhận/Hủy)
     */
    public boolean updateTrangThai(int lichHenID, String trangThaiMoi) {
        String sql = "UPDATE LichHen SET TrangThai = ? WHERE LichHen_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThaiMoi);
            ps.setInt(2, lichHenID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}