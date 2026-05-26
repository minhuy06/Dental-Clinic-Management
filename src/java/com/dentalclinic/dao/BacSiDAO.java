package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.model.ChuyenKhoa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

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
    private static String toSqlTimeString(java.time.LocalTime time) {
        if (time == null) return "00:00:00";
        return String.format("%02d:%02d:00", time.getHour(), time.getMinute());
    }

    private static java.time.LocalTime parseSqlTime(String gioKham) {
        if (gioKham == null || gioKham.isBlank()) return null;
        String raw = gioKham.trim();
        if (raw.length() == 5) raw = raw + ":00";
        return java.time.LocalTime.parse(raw);
    }

    /**
     * Tìm bác sĩ rảnh: có ca làm (LichLamViec + CaLam) trong ngày, khung giờ khám nằm trọn trong ca,
     * và không trùng lịch hẹn đã có.
     */
    public List<Integer> findAvailableDoctorIdsForBooking(String ngayKham, java.time.LocalTime start, int durationMinutes) {
        List<Integer> available = new ArrayList<>();
        if (ngayKham == null || ngayKham.isBlank() || start == null) return available;

        int blockMinutes = Math.max(durationMinutes, 15);
        java.time.LocalTime end = start.plusMinutes(blockMinutes);
        java.util.LinkedHashSet<Integer> candidateIds = new java.util.LinkedHashSet<>();

        String sql = "SELECT DISTINCT b.BacSi_ID "
                + "FROM BacSi b "
                + "INNER JOIN LichLamViec lv ON lv.TaiKhoan_ID = b.TaiKhoan_ID "
                + "INNER JOIN CaLam cl ON cl.Ca_ID = lv.Ca_ID "
                + "INNER JOIN TaiKhoan tk ON tk.TaiKhoan_ID = b.TaiKhoan_ID "
                + "WHERE lv.NgayLam = ? "
                + "AND tk.VaiTro = N'Bác sĩ' "
                + "AND ? >= cl.GioBatDau AND ? <= cl.GioKetThuc "
                + "AND ? >= cl.GioBatDau AND ? <= cl.GioKetThuc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            Date ngay = java.sql.Date.valueOf(ngayKham);
            Time startTime = java.sql.Time.valueOf(toSqlTimeString(start));
            Time endTime = java.sql.Time.valueOf(toSqlTimeString(end));
            ps.setDate(1, ngay);
            ps.setTime(2, startTime);
            ps.setTime(3, endTime);
            ps.setTime(4, startTime);
            ps.setTime(5, endTime);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    candidateIds.add(rs.getInt("BacSi_ID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (candidateIds.isEmpty()) {
            String fallback = "SELECT DISTINCT b.BacSi_ID FROM BacSi b "
                    + "INNER JOIN LichLamViec lv ON lv.TaiKhoan_ID = b.TaiKhoan_ID "
                    + "INNER JOIN TaiKhoan tk ON tk.TaiKhoan_ID = b.TaiKhoan_ID "
                    + "WHERE lv.NgayLam = ? AND tk.VaiTro = N'Bác sĩ'";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(fallback)) {
                ps.setDate(1, java.sql.Date.valueOf(ngayKham));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        candidateIds.add(rs.getInt("BacSi_ID"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (Integer bacSiId : candidateIds) {
            if (bacSiId != null && bacSiId > 0 && isDoctorFreeForBooking(bacSiId, ngayKham, start, durationMinutes)) {
                available.add(bacSiId);
            }
        }
        return available;
    }

    public List<Integer> getAvailableDoctorIds(String ngayKham, String gioKham, int phongId) {
        java.time.LocalTime start = parseSqlTime(gioKham);
        if (start == null) return new ArrayList<>();
        return findAvailableDoctorIdsForBooking(ngayKham, start, 30);
    }

    public boolean isDoctorFreeForBooking(int bacSiId, String ngayKham, java.time.LocalTime start, int durationMinutes) {
        return isDoctorFreeForBooking(bacSiId, ngayKham, start, durationMinutes, null);
    }

    public boolean isDoctorFreeForBooking(int bacSiId, String ngayKham, java.time.LocalTime start,
            int durationMinutes, Integer excludeLichHenId) {
        if (bacSiId < 1 || ngayKham == null || start == null) return false;
        int blockMinutes = Math.max(durationMinutes, 15);
        java.time.LocalTime end = start.plusMinutes(blockMinutes);

        List<TimeRange> shifts = getDoctorShiftsOnDate(bacSiId, ngayKham);
        if (shifts.isEmpty()) return false;

        boolean inShift = false;
        for (TimeRange sh : shifts) {
            if (!start.isBefore(sh.getStart()) && !end.isAfter(sh.getEnd())) {
                inShift = true;
                break;
            }
        }
        if (!inShift) return false;

        List<TimeRange> busySlots = getDoctorBusySlotsOnDate(bacSiId, ngayKham, excludeLichHenId);
        for (TimeRange busy : busySlots) {
            if (start.isBefore(busy.getEnd()) && end.isAfter(busy.getStart())) {
                return false;
            }
        }
        return true;
    }

    /** Bác sĩ có ca làm tại phòng trong ngày (theo LichLamViec). */
    public boolean isDoctorScheduledInRoomOnDate(int bacSiId, int phongId, String ngayKham) {
        if (bacSiId < 1 || phongId < 1 || ngayKham == null || ngayKham.isBlank()) {
            return false;
        }
        String sql = "SELECT 1 FROM LichLamViec lv "
                + "INNER JOIN BacSi b ON b.TaiKhoan_ID = lv.TaiKhoan_ID "
                + "WHERE b.BacSi_ID = ? AND lv.NgayLam = ? AND lv.Phong_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bacSiId);
            ps.setDate(2, java.sql.Date.valueOf(ngayKham));
            ps.setInt(3, phongId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int findPhongIdForDoctorOnDate(int bacSiId, String ngayKham) {
        String sql = "SELECT TOP 1 lv.Phong_ID FROM LichLamViec lv "
                + "JOIN BacSi b ON b.TaiKhoan_ID = lv.TaiKhoan_ID "
                + "WHERE b.BacSi_ID = ? AND lv.NgayLam = ? AND lv.Phong_ID IS NOT NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bacSiId);
            ps.setDate(2, java.sql.Date.valueOf(ngayKham));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Phong_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public boolean isDoctorAvailable(int bacSiId, String ngayKham, String gioKham, int phongId) {
        String sql = "SELECT 1 FROM BacSi b " +
                     "WHERE b.BacSi_ID = ? " +
                     "AND NOT EXISTS ( " +
                     "  SELECT 1 FROM LichHen lh " +
                     "  WHERE lh.BacSi_ID = b.BacSi_ID AND lh.NgayKham = ? AND lh.GioKham = ? AND lh.TrangThai <> N'Đã hủy' " +
                     ")";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            Date ngay = java.sql.Date.valueOf(ngayKham);
            Time gio = java.sql.Time.valueOf(gioKham);
            ps.setInt(1, bacSiId);
            ps.setDate(2, ngay);
            ps.setTime(3, gio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class TimeRange {
        private final LocalTime start;
        private final LocalTime end;
        public TimeRange(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }
        public LocalTime getStart() { return start; }
        public LocalTime getEnd() { return end; }
    }

    public List<TimeRange> getDoctorShiftsOnDate(int bacSiId, String ngayKham) {
        List<TimeRange> ranges = new ArrayList<>();
        String sql = "SELECT cl.GioBatDau, cl.GioKetThuc " +
                     "FROM BacSi b " +
                     "JOIN LichLamViec lv ON lv.TaiKhoan_ID = b.TaiKhoan_ID " +
                     "JOIN CaLam cl ON cl.Ca_ID = lv.Ca_ID " +
                     "WHERE b.BacSi_ID = ? AND lv.NgayLam = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bacSiId);
            ps.setDate(2, java.sql.Date.valueOf(ngayKham));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Time s = rs.getTime("GioBatDau");
                    Time e = rs.getTime("GioKetThuc");
                    if (s != null && e != null) {
                        ranges.add(new TimeRange(s.toLocalTime(), e.toLocalTime()));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ranges;
    }

    public List<TimeRange> getDoctorBusySlotsOnDate(int bacSiId, String ngayKham) {
        return getDoctorBusySlotsOnDate(bacSiId, ngayKham, null);
    }

    public List<TimeRange> getDoctorBusySlotsOnDate(int bacSiId, String ngayKham, Integer excludeLichHenId) {
        List<TimeRange> ranges = new ArrayList<>();
        String sql = "SELECT lh.LichHen_ID, lh.GioKham AS StartTime, " +
                     "DATEADD(MINUTE, ISNULL(( " +
                     "   SELECT SUM(ISNULL(dv.ThoiLuongDuKien, 30) * ISNULL(ctlh.SoLuong, 1)) " +
                     "   FROM ChiTietLichHen ctlh " +
                     "   JOIN DichVu dv ON dv.DichVu_ID = ctlh.DichVu_ID " +
                     "   WHERE ctlh.LichHen_ID = lh.LichHen_ID " +
                     "), 30) + 15, CAST(lh.GioKham AS DATETIME)) AS EndTime " +
                     "FROM LichHen lh " +
                     "WHERE lh.BacSi_ID = ? AND lh.NgayKham = ? AND lh.TrangThai <> N'Đã hủy'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bacSiId);
            ps.setDate(2, java.sql.Date.valueOf(ngayKham));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (excludeLichHenId != null && excludeLichHenId > 0
                            && rs.getInt("LichHen_ID") == excludeLichHenId) {
                        continue;
                    }
                    Time start = rs.getTime("StartTime");
                    Timestamp end = rs.getTimestamp("EndTime");
                    if (start != null && end != null) {
                        ranges.add(new TimeRange(start.toLocalTime(), end.toLocalDateTime().toLocalTime()));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ranges;
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

    /** Mã bác sĩ theo tài khoản đăng nhập (null nếu không phải bác sĩ). */
    public Integer findBacSiIdByTaiKhoanId(int taiKhoanId) {
        if (taiKhoanId <= 0) return null;
        String sql = "SELECT BacSi_ID FROM BacSi WHERE TaiKhoan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taiKhoanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("BacSi_ID");
                }
            }
        } catch (SQLException e) {
            System.err.println("[BacSiDAO] findBacSiIdByTaiKhoanId: " + e.getMessage());
        }
        return null;
    }
}