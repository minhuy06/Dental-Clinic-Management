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
        // Query JOIN 3 bảng để lấy đầy đủ thông tin hiển thị
        String sql = "SELECT b.*, t.HoTen, c.TenChuyenKhoa " +
                     "FROM BacSi b " +
                     "JOIN TaiKhoan t ON b.TaiKhoan_ID = t.TaiKhoan_ID " +
                     "JOIN ChuyenKhoa c ON b.ChuyenKhoa_ID = c.ChuyenKhoa_ID " +
                     "WHERE t.TrangThai = N'Hoạt động'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapFullInfo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
                if (rs.next()) return mapFullInfo(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm Helper để đóng gói dữ liệu từ SQL vào Object Model
    private BacSi mapFullInfo(ResultSet rs) throws SQLException {
        BacSi bs = new BacSi();
        bs.setBacSiID(rs.getInt("BacSi_ID"));
        bs.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
        bs.setChuyenKhoaID(rs.getInt("ChuyenKhoa_ID"));
        bs.setAnhDaiDien(rs.getString("AnhDaiDien"));
        bs.setTrinhDo(rs.getNString("TrinhDo"));
        
        // Mapping thông tin từ bảng TaiKhoan (Lấy HoTen)
        TaiKhoan tk = new TaiKhoan();
        tk.setHoTen(rs.getNString("HoTen"));
        bs.setTaiKhoan(tk);
        
        // Mapping thông tin Chuyên Khoa
        ChuyenKhoa ck = new ChuyenKhoa();
        ck.setTenChuyenKhoa(rs.getNString("TenChuyenKhoa"));
        bs.setChuyenKhoa(ck);
        
        return bs;
    }
}