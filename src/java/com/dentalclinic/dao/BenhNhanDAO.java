package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.LichHen;
import com.google.gson.annotations.SerializedName;
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

    /** Dòng bệnh nhân cho trang quản trị tiếp tân — map JSON của benhnhan.js */
    public static class ReceptionPatientRow {
        public int id;
        public String fullName;
        public String gender;
        public String phone;
        public String birthDate;
        public String address;
        public String allergy;
        public String medicalHistory;
        /** active | completed | followup */
        public String status;
        public String registerDate;
        public String notes;
        public int taiKhoanId;
    }

    public List<ReceptionPatientRow> listReceptionPatients() {
        List<ReceptionPatientRow> list = new ArrayList<>();
        boolean hasHoSo = tableExists("HoSo");
        String sql = "SELECT bn.BenhNhan_ID, bn.TaiKhoan_ID, tk.HoTen, tk.SoDienThoai, tk.NgaySinh, tk.GioiTinh, "
                + "lastLh.LastTrangThai AS LastLhTrangThai ";
        if (hasHoSo) {
            sql += ", hs.DiaChi, hs.DiUngThuoc, hs.TienSuBenh, hs.NgayDangKy, hs.GhiChu ";
        } else {
            sql += ", CAST(NULL AS NVARCHAR(255)) AS DiaChi, CAST(NULL AS NVARCHAR(500)) AS DiUngThuoc, "
                    + "CAST(NULL AS NVARCHAR(500)) AS TienSuBenh, CAST(NULL AS DATE) AS NgayDangKy, CAST(NULL AS NVARCHAR(1000)) AS GhiChu ";
        }
        sql += "FROM BenhNhan bn "
                + "LEFT JOIN TaiKhoan tk ON tk.TaiKhoan_ID = bn.TaiKhoan_ID "
                + (hasHoSo ? "OUTER APPLY (SELECT TOP 1 DiaChi, DiUngThuoc, TienSuBenh, NgayDangKy, GhiChu "
                        + "FROM HoSo x WHERE x.BenhNhan_ID = bn.BenhNhan_ID ORDER BY x.HoSo_ID DESC) hs " : "")
                + "OUTER APPLY (SELECT TOP 1 lh.TrangThai AS LastTrangThai FROM LichHen lh "
                + "WHERE lh.BenhNhan_ID = bn.BenhNhan_ID ORDER BY lh.NgayKham DESC, lh.GioKham DESC) lastLh "
                + "WHERE bn.TaiKhoan_ID IS NOT NULL "
                + "AND (tk.VaiTro IS NULL OR tk.VaiTro = N'Bệnh nhân') "
                + "ORDER BY bn.BenhNhan_ID ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReceptionPatientRow r = mapReceptionPatientRow(rs, hasHoSo);
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static ReceptionPatientRow mapReceptionPatientRow(ResultSet rs, boolean hasHoSoVals) throws SQLException {
        ReceptionPatientRow r = new ReceptionPatientRow();
        r.id = rs.getInt("BenhNhan_ID");
        r.taiKhoanId = rs.getInt("TaiKhoan_ID");
        r.fullName = nStr(rs.getNString("HoTen"));
        r.phone = rs.getString("SoDienThoai") != null ? rs.getString("SoDienThoai").trim() : "";
        Date ns = rs.getDate("NgaySinh");
        r.birthDate = ns != null ? ns.toLocalDate().toString() : "";
        boolean gt = rs.getBoolean("GioiTinh");
        if (!rs.wasNull()) {
            r.gender = gt ? "Nam" : "Nữ";
        } else {
            r.gender = "Khác";
        }
        if (hasHoSoVals) {
            String dc = rs.getNString("DiaChi");
            r.address = nStr(dc);
            String du = rs.getNString("DiUngThuoc");
            String ts = rs.getNString("TienSuBenh");
            r.allergy = blankToKhong(nStr(du));
            r.medicalHistory = blankToKhong(nStr(ts));
            Date nd = rs.getDate("NgayDangKy");
            r.registerDate = nd != null ? nd.toLocalDate().toString() : "";
            r.notes = nStr(rs.getNString("GhiChu"));
        } else {
            r.address = "";
            r.allergy = "Không";
            r.medicalHistory = "Không";
            r.registerDate = "";
            r.notes = "";
        }

        String last = rs.getNString("LastLhTrangThai");
        r.status = deriveUiStatus(last, r.notes);
        return r;
    }

    private static String deriveUiStatus(String lastTrangThai, String ghiChu) {
        if (lastTrangThai != null) {
            String t = lastTrangThai.toLowerCase();
            if (t.contains("thanh toán") || t.contains("hoàn thành")) {
                return "completed";
            }
        }
        if (ghiChu != null) {
            String g = ghiChu.toLowerCase();
            if (g.contains("tái khám") || g.contains("tai kham")) {
                return "followup";
            }
        }
        return "active";
    }

    private static String blankToKhong(String s) {
        if (s == null || s.trim().isEmpty()) {
            return "Không";
        }
        return s;
    }

    private static String nStr(String v) {
        return v != null ? v : "";
    }

    private static boolean tableExists(String name) {
        String sql = "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private static boolean columnExists(Connection conn, String table, String column) throws SQLException {
        String sql = "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table);
            ps.setString(2, column);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** INSERT BenhNhan — hỗ trợ schema cũ (còn HoTen) và schema mới (chỉ TaiKhoan_ID). */
    private static int insertBenhNhanRow(Connection conn, int tkId, ReceptionPatientInput in) throws SQLException {
        boolean hasHoTen = columnExists(conn, "BenhNhan", "HoTen");
        boolean gioiTinh = genderToBit(in.gender);
        String sql;
        if (hasHoTen) {
            sql = "INSERT INTO BenhNhan (TaiKhoan_ID, HoTen, NgaySinh, GioiTinh) VALUES (?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO BenhNhan (TaiKhoan_ID) VALUES (?)";
        }
        try (PreparedStatement psBn = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            psBn.setInt(1, tkId);
            if (hasHoTen) {
                psBn.setNString(2, in.fullName != null ? in.fullName.trim() : "");
                if (in.ngaySinh != null && !in.ngaySinh.trim().isEmpty()) {
                    psBn.setDate(3, Date.valueOf(in.ngaySinh.trim()));
                } else {
                    psBn.setNull(3, Types.DATE);
                }
                psBn.setBoolean(4, gioiTinh);
            }
            psBn.executeUpdate();
            try (ResultSet keysBn = psBn.getGeneratedKeys()) {
                if (!keysBn.next()) {
                    throw new SQLException("Không tạo được BenhNhan");
                }
                return keysBn.getInt(1);
            }
        }
    }

    public Integer findTaiKhoanIdForBenhNhan(int benhNhanId) {
        String sql = "SELECT TaiKhoan_ID FROM BenhNhan WHERE BenhNhan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, benhNhanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("TaiKhoan_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean phoneExists(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        String sql = "SELECT COUNT(1) FROM TaiKhoan WHERE SoDienThoai = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean phoneExistsOtherThanTaiKhoan(String phone, int excludeTaiKhoanId) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        if (excludeTaiKhoanId < 1) {
            return phoneExists(phone);
        }
        String sql = "SELECT COUNT(1) FROM TaiKhoan WHERE SoDienThoai = ? AND TaiKhoan_ID <> ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone.trim());
            ps.setInt(2, excludeTaiKhoanId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ReceptionPatientErrors createReceptionPatient(ReceptionPatientInput in) throws SQLException {
        if (in.phone == null || in.phone.trim().isEmpty()) {
            return ReceptionPatientErrors.of("Thiếu số điện thoại");
        }
        if (phoneExists(in.phone)) {
            return ReceptionPatientErrors.of("Số điện thoại đã tồn tại");
        }
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int tkId;
                boolean gioiTinh = genderToBit(in.gender);
                String insertTk =
                        "INSERT INTO TaiKhoan (SoDienThoai, MatKhau, VaiTro, TrangThai, HoTen, NgaySinh, GioiTinh) "
                                + "VALUES (?, ?, N'Bệnh nhân', N'Hoạt động', ?, ?, ?)";
                try (PreparedStatement psTk = conn.prepareStatement(insertTk, Statement.RETURN_GENERATED_KEYS)) {
                    psTk.setString(1, in.phone.trim());
                    psTk.setString(2, defaultPatientPassword(in.password));
                    psTk.setNString(3, in.fullName.trim());
                    if (in.ngaySinh != null && !in.ngaySinh.trim().isEmpty()) {
                        psTk.setDate(4, Date.valueOf(in.ngaySinh.trim()));
                    } else {
                        psTk.setNull(4, Types.DATE);
                    }
                    psTk.setBoolean(5, gioiTinh);
                    psTk.executeUpdate();
                    try (ResultSet keys = psTk.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Không tạo được TaiKhoan");
                        }
                        tkId = keys.getInt(1);
                    }
                }
                int bnId = insertBenhNhanRow(conn, tkId, in);
                upsertHoSo(conn, bnId, in);
                conn.commit();
                return ReceptionPatientErrors.ok(bnId);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public ReceptionPatientErrors updateReceptionPatient(int benhNhanId, ReceptionPatientInput in) throws SQLException {
        Integer tkOpt = findTaiKhoanIdForBenhNhan(benhNhanId);
        if (tkOpt == null) {
            return ReceptionPatientErrors.of("Không tìm thấy bệnh nhân");
        }
        int tkId = tkOpt;
        if (phoneExistsOtherThanTaiKhoan(in.phone, tkId)) {
            return ReceptionPatientErrors.of("Số điện thoại đã được dùng cho tài khác khác");
        }
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean gioiTinh = genderToBit(in.gender);
                String sqlTk = "UPDATE TaiKhoan SET HoTen = ?, SoDienThoai = ?, NgaySinh = ?, GioiTinh = ? WHERE TaiKhoan_ID = ?";
                try (PreparedStatement psTk = conn.prepareStatement(sqlTk)) {
                    psTk.setNString(1, in.fullName.trim());
                    psTk.setString(2, in.phone.trim());
                    if (in.ngaySinh != null && !in.ngaySinh.trim().isEmpty()) {
                        psTk.setDate(3, Date.valueOf(in.ngaySinh.trim()));
                    } else {
                        psTk.setNull(3, Types.DATE);
                    }
                    psTk.setBoolean(4, gioiTinh);
                    psTk.setInt(5, tkId);
                    if (psTk.executeUpdate() <= 0) {
                        throw new SQLException("Không cập nhật được TaiKhoan");
                    }
                }
                upsertHoSo(conn, benhNhanId, in);
                conn.commit();
                return ReceptionPatientErrors.ok(benhNhanId);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public boolean deactivatePatientAccount(int benhNhanId) {
        Integer tkOpt = findTaiKhoanIdForBenhNhan(benhNhanId);
        if (tkOpt == null) {
            return false;
        }
        String sql = "UPDATE TaiKhoan SET TrangThai = N'Bị khóa' WHERE TaiKhoan_ID = ? AND VaiTro = N'Bệnh nhân'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tkOpt);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void upsertHoSo(Connection conn, int benhNhanId, ReceptionPatientInput in) throws SQLException {
        if (!tableExists("HoSo")) {
            return;
        }
        String allergy = in.allergyNormalized();
        String med = in.medNormalized();
        String check = "SELECT TOP 1 HoSo_ID FROM HoSo WHERE BenhNhan_ID = ? ORDER BY HoSo_ID DESC";
        try (PreparedStatement psC = conn.prepareStatement(check)) {
            psC.setInt(1, benhNhanId);
            try (ResultSet rs = psC.executeQuery()) {
                if (rs.next()) {
                    int hsId = rs.getInt("HoSo_ID");
                    String up = "UPDATE HoSo SET DiaChi = ?, DiUngThuoc = ?, TienSuBenh = ?, GhiChu = ? WHERE HoSo_ID = ?";
                    try (PreparedStatement psU = conn.prepareStatement(up)) {
                        psU.setNString(1, n(in.address));
                        psU.setNString(2, n(allergy));
                        psU.setNString(3, n(med));
                        psU.setNString(4, n(in.mergedNotesInternal()));
                        psU.setInt(5, hsId);
                        psU.executeUpdate();
                    }
                    return;
                }
            }
        }
        String ins = "INSERT INTO HoSo (BenhNhan_ID, DiaChi, DiUngThuoc, TienSuBenh, NgayDangKy, GhiChu) "
                + "VALUES (?, ?, ?, ?, CAST(GETDATE() AS DATE), ?)";
        try (PreparedStatement ps = conn.prepareStatement(ins)) {
            ps.setInt(1, benhNhanId);
            ps.setNString(2, n(in.address));
            ps.setNString(3, n(allergy));
            ps.setNString(4, n(med));
            ps.setNString(5, n(in.mergedNotesInternal()));
            ps.executeUpdate();
        }
    }

    private static String defaultPatientPassword(String p) {
        return (p == null || p.isBlank()) ? "123456" : p;
    }

    private static boolean genderToBit(String g) {
        if (g == null) return false;
        String t = g.trim();
        if (t.equalsIgnoreCase("Nam")) return true;
        if (t.equalsIgnoreCase("Nữ") || t.equalsIgnoreCase("Nu")) return false;
        return false;
    }

    private static String n(String x) {
        return x == null ? "" : x;
    }

    /** Input tạo/cập nhật từ JSON */
    public static class ReceptionPatientInput {
        public String fullName;
        public String gender;
        public String phone;
        @SerializedName("birthDate")
        public String ngaySinh;
        public String address;
        public String allergy;
        public String medicalHistory;
        /** UI: active/completed/followup — JSON frontend dùng "status". */
        @SerializedName(value = "status", alternate = {"uiStatus"})
        public String uiStatus = "active";
        public String notes = "";
        public String password = "";

        public String allergyNormalized() {
            String a = allergy == null ? "" : allergy.trim();
            return "Không".equalsIgnoreCase(a) || a.isEmpty() ? "Không" : a;
        }

        public String medNormalized() {
            String a = medicalHistory == null ? "" : medicalHistory.trim();
            return "Không".equalsIgnoreCase(a) || a.isEmpty() ? "Không" : a;
        }

        /** Gộp cờ tái khám / hoàn thành vào ghi chú nội bộ */
        String mergedNotesInternal() {
            String base = notes == null ? "" : notes.trim();
            String statusTag = "";
            if ("followup".equalsIgnoreCase(uiStatus)) {
                statusTag = "(Hẹn tái khám) ";
            }
            return (statusTag + base).trim();
        }
    }

    /** Kết quả thao tác */
    public static class ReceptionPatientErrors {
        private final boolean success;
        private final String message;
        private final Integer id;

        private ReceptionPatientErrors(boolean success, String message, Integer id) {
            this.success = success;
            this.message = message;
            this.id = id;
        }

        static ReceptionPatientErrors ok(int id) {
            return new ReceptionPatientErrors(true, null, id);
        }

        static ReceptionPatientErrors of(String msg) {
            return new ReceptionPatientErrors(false, msg, null);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Integer getId() {
            return id;
        }
    }
}