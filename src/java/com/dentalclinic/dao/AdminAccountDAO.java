package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class AdminAccountDAO {

    public static final class OpResult {
        public final boolean success;
        public final String message;
        public final int taiKhoanId;

        public OpResult(boolean success, String message, int taiKhoanId) {
            this.success = success;
            this.message = message;
            this.taiKhoanId = taiKhoanId;
        }
    }

    private static String mapRoleToDb(String roleUi) {
        if (roleUi == null) return null;
        switch (roleUi.trim()) {
            case "doctor": return "Bác sĩ";
            case "staff": return "Lễ tân";
            case "admin": return "Quản trị viên";
            case "customer": return "Bệnh nhân";
            default: return null;
        }
    }

    private static String mapStatusToDb(String statusUi) {
        if (statusUi == null) return "Hoạt động";
        return "inactive".equalsIgnoreCase(statusUi.trim()) ? "Bị khóa" : "Hoạt động";
    }

    private static String mapSpecialtyUiToDbName(String specialtyUi) {
        if (specialtyUi == null || specialtyUi.isBlank()) {
            return "Khám & Chẩn đoán";
        }
        switch (specialtyUi.trim()) {
            case "Răng tổng quát": return "Khám & Chẩn đoán";
            case "Chỉnh nha": return "Chỉnh nha";
            case "Phục hình": return "Chỉnh nha";
            case "Thẩm mỹ nha": return "Thẩm mỹ";
            case "Phẫu thuật miệng": return "Trẻ em";
            default: return specialtyUi.trim();
        }
    }

    private static Boolean mapGenderToBit(String genderUi) {
        if (genderUi == null || genderUi.isBlank()) return null;
        switch (genderUi.trim()) {
            case "male": return Boolean.TRUE;
            case "female": return Boolean.FALSE;
            default: return null;
        }
    }

    private static String sanitizeAvatar(String avatar) {
        if (avatar == null) return null;
        String trimmed = avatar.trim();
        if (trimmed.isEmpty()) return null;
        if (trimmed.startsWith("data:") && trimmed.length() > 255) {
            return null;
        }
        return trimmed.length() > 255 ? trimmed.substring(0, 255) : trimmed;
    }

    private int resolveChuyenKhoaId(Connection conn, String specialtyUi) throws SQLException {
        String ten = mapSpecialtyUiToDbName(specialtyUi);
        String sql = "SELECT TOP 1 ChuyenKhoa_ID FROM ChuyenKhoa WHERE TenChuyenKhoa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, ten);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 1;
    }

    private java.sql.Date parseDob(String dob) {
        if (dob == null || dob.isBlank()) return null;
        try {
            return java.sql.Date.valueOf(dob.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean accountExists(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM TaiKhoan WHERE TaiKhoan_ID = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public OpResult createStaffAccount(String hoTen, String phone, String password, String roleUi,
            String dob, String genderUi, String specialtyUi, String degree, String avatar) {
        String vaiTroDb = mapRoleToDb(roleUi);
        if (vaiTroDb == null || (!"doctor".equals(roleUi.trim()) && !"staff".equals(roleUi.trim()))) {
            return new OpResult(false, "Loại tài khoản không hợp lệ (chỉ hỗ trợ Bác sĩ hoặc Nhân viên).", 0);
        }
        if (hoTen == null || hoTen.isBlank()) {
            return new OpResult(false, "Họ tên không được để trống.", 0);
        }
        if (phone == null || phone.isBlank()) {
            return new OpResult(false, "Số điện thoại không được để trống.", 0);
        }
        if (password == null || password.isBlank()) {
            return new OpResult(false, "Mật khẩu không được để trống.", 0);
        }

        BenhNhanDAO benhNhanDAO = new BenhNhanDAO();
        if (benhNhanDAO.phoneExists(phone.trim())) {
            return new OpResult(false, "Số điện thoại đã được sử dụng.", 0);
        }

        java.sql.Date ngaySinh = parseDob(dob);
        if (dob != null && !dob.isBlank() && ngaySinh == null) {
            return new OpResult(false, "Ngày sinh không hợp lệ.", 0);
        }

        Boolean gioiTinh = mapGenderToBit(genderUi);
        String avatarDb = sanitizeAvatar(avatar);
        if (avatarDb == null) avatarDb = "";
        String trinhDo = degree == null ? "" : degree.trim();

        String insertTk = "INSERT INTO TaiKhoan (SoDienThoai, MatKhau, VaiTro, TrangThai, HoTen, NgaySinh, GioiTinh) "
                + "VALUES (?, ?, ?, N'Hoạt động', ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int taiKhoanId;
                try (PreparedStatement ps = conn.prepareStatement(insertTk, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, phone.trim());
                    ps.setString(2, password);
                    ps.setNString(3, vaiTroDb);
                    ps.setNString(4, hoTen.trim());
                    if (ngaySinh != null) {
                        ps.setDate(5, ngaySinh);
                    } else {
                        ps.setNull(5, Types.DATE);
                    }
                    if (gioiTinh != null) {
                        ps.setBoolean(6, gioiTinh);
                    } else {
                        ps.setNull(6, Types.BIT);
                    }
                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return new OpResult(false, "Không thể tạo tài khoản.", 0);
                    }
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            conn.rollback();
                            return new OpResult(false, "Không lấy được ID tài khoản mới.", 0);
                        }
                        taiKhoanId = keys.getInt(1);
                    }
                }

                if ("doctor".equals(roleUi.trim())) {
                    insertBacSi(conn, taiKhoanId, avatarDb, trinhDo, resolveChuyenKhoaId(conn, specialtyUi));
                } else {
                    insertLeTan(conn, taiKhoanId);
                }

                conn.commit();
                return new OpResult(true, "Đã thêm tài khoản.", taiKhoanId);
            } catch (SQLException e) {
                conn.rollback();
                return opResultFromSql(e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new OpResult(false, "Không kết nối được cơ sở dữ liệu.", 0);
        }
    }

    public OpResult updateAccount(int taiKhoanId, String hoTen, String phone, String password, String roleUi,
            String statusUi, String dob, String genderUi, String specialtyUi, String degree, String avatar) {
        if (taiKhoanId < 1) {
            return new OpResult(false, "ID tài khoản không hợp lệ.", 0);
        }
        if (hoTen == null || hoTen.isBlank()) {
            return new OpResult(false, "Họ tên không được để trống.", 0);
        }
        if (phone == null || phone.isBlank()) {
            return new OpResult(false, "Số điện thoại không được để trống.", 0);
        }

        BenhNhanDAO benhNhanDAO = new BenhNhanDAO();
        if (benhNhanDAO.phoneExistsOtherThanTaiKhoan(phone.trim(), taiKhoanId)) {
            return new OpResult(false, "Số điện thoại đã được sử dụng.", 0);
        }

        java.sql.Date ngaySinh = parseDob(dob);
        if (dob != null && !dob.isBlank() && ngaySinh == null) {
            return new OpResult(false, "Ngày sinh không hợp lệ.", 0);
        }

        Boolean gioiTinh = mapGenderToBit(genderUi);
        String trangThaiDb = mapStatusToDb(statusUi);
        String avatarDb = sanitizeAvatar(avatar);
        String trinhDo = degree == null ? "" : degree.trim();
        String vaiTroDb = mapRoleToDb(roleUi);

        try (Connection conn = DBConnection.getConnection()) {
            if (!accountExists(conn, taiKhoanId)) {
                return new OpResult(false, "Không tìm thấy tài khoản.", 0);
            }

            conn.setAutoCommit(false);
            try {
                String updateTk = "UPDATE TaiKhoan SET HoTen = ?, SoDienThoai = ?, TrangThai = ?, NgaySinh = ?, GioiTinh = ?"
                        + (vaiTroDb != null ? ", VaiTro = ?" : "")
                        + " WHERE TaiKhoan_ID = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateTk)) {
                    int i = 1;
                    ps.setNString(i++, hoTen.trim());
                    ps.setString(i++, phone.trim());
                    ps.setNString(i++, trangThaiDb);
                    if (ngaySinh != null) {
                        ps.setDate(i++, ngaySinh);
                    } else {
                        ps.setNull(i++, Types.DATE);
                    }
                    if (gioiTinh != null) {
                        ps.setBoolean(i++, gioiTinh);
                    } else {
                        ps.setNull(i++, Types.BIT);
                    }
                    if (vaiTroDb != null) {
                        ps.setNString(i++, vaiTroDb);
                    }
                    ps.setInt(i, taiKhoanId);
                    ps.executeUpdate();
                }

                if (password != null && !password.isBlank()) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE TaiKhoan SET MatKhau = ? WHERE TaiKhoan_ID = ?")) {
                        ps.setString(1, password);
                        ps.setInt(2, taiKhoanId);
                        ps.executeUpdate();
                    }
                }

                if ("doctor".equals(roleUi != null ? roleUi.trim() : "")) {
                    int ckId = resolveChuyenKhoaId(conn, specialtyUi);
                    if (bacSiExists(conn, taiKhoanId)) {
                        String sql = "UPDATE BacSi SET TrinhDo = ?, ChuyenKhoa_ID = ?"
                                + (avatarDb != null ? ", AnhDaiDien = ?" : "")
                                + " WHERE TaiKhoan_ID = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            int i = 1;
                            ps.setNString(i++, trinhDo);
                            ps.setInt(i++, ckId);
                            if (avatarDb != null) {
                                ps.setString(i++, avatarDb);
                            }
                            ps.setInt(i, taiKhoanId);
                            ps.executeUpdate();
                        }
                    } else {
                        deleteLeTan(conn, taiKhoanId);
                        insertBacSi(conn, taiKhoanId, avatarDb != null ? avatarDb : "", trinhDo, ckId);
                    }
                } else if ("staff".equals(roleUi != null ? roleUi.trim() : "")) {
                    deleteBacSi(conn, taiKhoanId);
                    insertLeTan(conn, taiKhoanId);
                }

                conn.commit();
                return new OpResult(true, "Đã cập nhật tài khoản.", taiKhoanId);
            } catch (SQLException e) {
                conn.rollback();
                return opResultFromSql(e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new OpResult(false, "Không kết nối được cơ sở dữ liệu.", 0);
        }
    }

    public OpResult toggleStatus(int taiKhoanId, String statusUi) {
        if (taiKhoanId < 1) {
            return new OpResult(false, "ID tài khoản không hợp lệ.", 0);
        }
        String trangThaiDb = mapStatusToDb(statusUi);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE TaiKhoan SET TrangThai = ? WHERE TaiKhoan_ID = ?")) {
            ps.setNString(1, trangThaiDb);
            ps.setInt(2, taiKhoanId);
            int n = ps.executeUpdate();
            if (n == 0) {
                return new OpResult(false, "Không tìm thấy tài khoản.", 0);
            }
            return new OpResult(true, "Đã cập nhật trạng thái.", taiKhoanId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new OpResult(false, "Không kết nối được cơ sở dữ liệu.", 0);
        }
    }

    public OpResult deleteAccount(int taiKhoanId) {
        if (taiKhoanId < 1) {
            return new OpResult(false, "ID tài khoản không hợp lệ.", 0);
        }
        try (Connection conn = DBConnection.getConnection()) {
            if (!accountExists(conn, taiKhoanId)) {
                return new OpResult(false, "Không tìm thấy tài khoản.", 0);
            }

            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM LichLamViec WHERE TaiKhoan_ID = ?")) {
                    ps.setInt(1, taiKhoanId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM TinNhan WHERE NguoiGui_ID = ? OR NguoiNhan_ID = ?")) {
                    ps.setInt(1, taiKhoanId);
                    ps.setInt(2, taiKhoanId);
                    ps.executeUpdate();
                } catch (SQLException ignored) {
                    // Bảng TinNhan có thể chưa tồn tại trên một số môi trường
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM TaiKhoan WHERE TaiKhoan_ID = ?")) {
                    ps.setInt(1, taiKhoanId);
                    if (ps.executeUpdate() == 0) {
                        conn.rollback();
                        return new OpResult(false, "Không xóa được tài khoản.", 0);
                    }
                }
                conn.commit();
                return new OpResult(true, "Đã xóa tài khoản.", taiKhoanId);
            } catch (SQLException e) {
                conn.rollback();
                String msg = e.getMessage();
                if (msg != null && msg.toLowerCase().contains("reference")) {
                    return new OpResult(false, "Không thể xóa: tài khoản đang được sử dụng ở dữ liệu khác.", 0);
                }
                return opResultFromSql(e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new OpResult(false, "Không kết nối được cơ sở dữ liệu.", 0);
        }
    }

    private void insertBacSi(Connection conn, int taiKhoanId, String avatar, String trinhDo, int chuyenKhoaId)
            throws SQLException {
        String sql = "INSERT INTO BacSi (TaiKhoan_ID, AnhDaiDien, TrinhDo, ChuyenKhoa_ID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taiKhoanId);
            ps.setString(2, avatar);
            ps.setNString(3, trinhDo);
            ps.setInt(4, chuyenKhoaId);
            ps.executeUpdate();
        }
    }

    private void insertLeTan(Connection conn, int taiKhoanId) throws SQLException {
        if (leTanExists(conn, taiKhoanId)) return;
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO LeTan (TaiKhoan_ID) VALUES (?)")) {
            ps.setInt(1, taiKhoanId);
            ps.executeUpdate();
        }
    }

    private boolean bacSiExists(Connection conn, int taiKhoanId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM BacSi WHERE TaiKhoan_ID = ?")) {
            ps.setInt(1, taiKhoanId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean leTanExists(Connection conn, int taiKhoanId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM LeTan WHERE TaiKhoan_ID = ?")) {
            ps.setInt(1, taiKhoanId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void deleteBacSi(Connection conn, int taiKhoanId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM BacSi WHERE TaiKhoan_ID = ?")) {
            ps.setInt(1, taiKhoanId);
            ps.executeUpdate();
        }
    }

    private void deleteLeTan(Connection conn, int taiKhoanId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM LeTan WHERE TaiKhoan_ID = ?")) {
            ps.setInt(1, taiKhoanId);
            ps.executeUpdate();
        }
    }

    private OpResult opResultFromSql(SQLException e) {
        String msg = e.getMessage();
        if (msg != null && (msg.contains("UQ_TaiKhoan_SoDienThoai") || msg.contains("UNIQUE"))) {
            return new OpResult(false, "Số điện thoại đã được sử dụng.", 0);
        }
        e.printStackTrace();
        return new OpResult(false, "Lỗi cơ sở dữ liệu: " + (msg != null ? msg : "không xác định"), 0);
    }
}
