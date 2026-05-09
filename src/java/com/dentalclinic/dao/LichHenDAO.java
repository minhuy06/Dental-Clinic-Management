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

                ResultSetMetaData md = rs.getMetaData();

                boolean hasPhong = hasColumn(md, "Phong_ID");

                while (rs.next()) {

                    LichHen lh = mapResultSetToLichHen(rs, hasPhong);

                    TaiKhoan tkBs = new TaiKhoan();

                    tkBs.setHoTen(rs.getNString("TenBacSi"));

                    BacSi bs = new BacSi();

                    bs.setTaiKhoan(tkBs);

                    lh.setBacSi(bs);

                    list.add(lh);

                }

            }

        } catch (SQLException e) { e.printStackTrace(); }

        return list;

    }



    public List<LichHen> getAllLichHen() {

        List<LichHen> list = new ArrayList<>();

        String sql = "SELECT lh.*, tkBN.HoTen AS TenBenhNhan FROM LichHen lh " +

                     "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID " +

                     "JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID " +

                     "ORDER BY lh.NgayKham DESC";

        try (Connection conn = DBConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData md = rs.getMetaData();

            boolean hasPhong = hasColumn(md, "Phong_ID");

            while (rs.next()) {

                LichHen lh = mapResultSetToLichHen(rs, hasPhong);

                TaiKhoan tk = new TaiKhoan();

                tk.setHoTen(rs.getNString("TenBenhNhan"));

                BenhNhan bn = new BenhNhan();

                bn.setTaiKhoan(tk);

                lh.setBenhNhan(bn);

                list.add(lh);

            }

        } catch (SQLException e) { e.printStackTrace(); }

        return list;

    }



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

                ps.setNString(6, lh.getTrangThai());

                ps.setInt(7, lh.getPhongID());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {

                    int lhID = rs.getInt(1);

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



    private static boolean hasColumn(ResultSetMetaData md, String name) throws SQLException {

        int n = md.getColumnCount();

        for (int i = 1; i <= n; i++) {

            String label = md.getColumnLabel(i);

            if (label != null && label.equalsIgnoreCase(name)) {

                return true;

            }

        }

        return false;

    }



    private LichHen mapResultSetToLichHen(ResultSet rs, boolean hasPhongColumn) throws SQLException {

        LichHen lh = new LichHen();

        lh.setLichHenID(rs.getInt("LichHen_ID"));

        lh.setBenhNhanID(rs.getInt("BenhNhan_ID"));

        lh.setBacSiID(rs.getInt("BacSi_ID"));

        lh.setNgayKham(rs.getDate("NgayKham"));

        lh.setGioKham(rs.getTime("GioKham"));

        lh.setGhiChu(rs.getNString("GhiChu"));

        lh.setTrangThai(rs.getNString("TrangThai"));

        lh.setPhongID(hasPhongColumn ? rs.getInt("Phong_ID") : 0);

        return lh;

    }



    private void attachReceptionDisplay(LichHen lh, ResultSet rs) throws SQLException {

        TaiKhoan tkBn = new TaiKhoan();

        tkBn.setHoTen(rs.getNString("TenBenhNhan"));

        tkBn.setSoDienThoai(rs.getString("SdtBenhNhan"));

        BenhNhan bnObj = new BenhNhan();

        bnObj.setTaiKhoan(tkBn);

        lh.setBenhNhan(bnObj);

        TaiKhoan tkBs = new TaiKhoan();

        tkBs.setHoTen(rs.getNString("TenBacSi"));

        BacSi bsObj = new BacSi();

        bsObj.setTaiKhoan(tkBs);

        lh.setBacSi(bsObj);

        String tenPhong = rs.getNString("TenPhong");

        lh.setTenPhong(tenPhong != null && !tenPhong.isEmpty()

                ? tenPhong : (lh.getPhongID() > 0 ? ("Phòng " + lh.getPhongID()) : "—"));

    }



    /** Full schema: có lh.Phong_ID + TenPhong. */

    private List<LichHen> runReceptionQueryFull(String sql, Integer dateParamIndex, java.sql.Date ngay)

            throws SQLException {

        List<LichHen> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (dateParamIndex != null) {

                ps.setDate(dateParamIndex, ngay);

            }

            try (ResultSet rs = ps.executeQuery()) {

                ResultSetMetaData md = rs.getMetaData();

                boolean hasPhongCol = hasColumn(md, "Phong_ID");

                while (rs.next()) {

                    LichHen lh = mapResultSetToLichHen(rs, hasPhongCol);

                    attachReceptionDisplay(lh, rs);

                    list.add(lh);

                }

            }

        }

        return list;

    }



    /** Cũ hoặc thiếu Phong_ID: không tham chiếu Phong trong SQL. */

    private List<LichHen> runReceptionQueryLegacy(String sql, Integer dateParamIndex, java.sql.Date ngay) {

        List<LichHen> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (dateParamIndex != null) {

                ps.setDate(dateParamIndex, ngay);

            }

            try (ResultSet rs = ps.executeQuery()) {

                ResultSetMetaData md = rs.getMetaData();

                boolean hasPhongCol = hasColumn(md, "Phong_ID");

                while (rs.next()) {

                    LichHen lh = mapResultSetToLichHen(rs, hasPhongCol);

                    attachReceptionDisplay(lh, rs);

                    list.add(lh);

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return list;

    }



    public List<LichHen> getLichHenByNgayKham(java.sql.Date ngay) {

        try {

            String sql = "SELECT lh.*, tkBN.HoTen AS TenBenhNhan, tkBN.SoDienThoai AS SdtBenhNhan, " +

                         "tkBS.HoTen AS TenBacSi, pk.TenPhong AS TenPhong " +

                         "FROM LichHen lh " +

                         "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID " +

                         "JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID " +

                         "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +

                         "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID " +

                         "LEFT JOIN PhongKham pk ON lh.Phong_ID = pk.Phong_ID " +

                         "WHERE CAST(lh.NgayKham AS DATE) = CAST(? AS DATE) " +

                         "ORDER BY lh.GioKham ASC";

            return runReceptionQueryFull(sql, 1, ngay);

        } catch (SQLException e) {

            System.err.println("[LichHenDAO] getLichHenByNgayKham fallback (no Phong join): " + e.getMessage());

            String legacy = "SELECT lh.LichHen_ID, lh.BenhNhan_ID, lh.BacSi_ID, lh.NgayKham, lh.GioKham, lh.GhiChu, lh.TrangThai, " +

                         "COALESCE(tkBN.HoTen, N'') AS TenBenhNhan, tkBN.SoDienThoai AS SdtBenhNhan, " +

                         "COALESCE(tkBS.HoTen, N'') AS TenBacSi, CAST(NULL AS NVARCHAR(100)) AS TenPhong " +

                         "FROM LichHen lh " +

                         "LEFT JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID " +

                         "LEFT JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID " +

                         "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +

                         "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID " +

                         "WHERE CAST(lh.NgayKham AS DATE) = CAST(? AS DATE) " +

                         "ORDER BY lh.GioKham ASC";

            return runReceptionQueryLegacy(legacy, 1, ngay);

        }

    }



    public List<LichHen> getLichHenReceptionLatest(int limit) {

        if (limit <= 0) limit = 100;

        try {

            String sql = "SELECT TOP (" + limit + ") lh.*, tkBN.HoTen AS TenBenhNhan, tkBN.SoDienThoai AS SdtBenhNhan, " +

                         "tkBS.HoTen AS TenBacSi, pk.TenPhong AS TenPhong " +

                         "FROM LichHen lh " +

                         "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID " +

                         "JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID " +

                         "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +

                         "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID " +

                         "LEFT JOIN PhongKham pk ON lh.Phong_ID = pk.Phong_ID " +

                         "ORDER BY lh.NgayKham DESC, lh.GioKham DESC";

            return runReceptionQueryFull(sql, null, null);

        } catch (SQLException e) {

            System.err.println("[LichHenDAO] getLichHenReceptionLatest fallback (no Phong join): " + e.getMessage());

            String legacy = "SELECT TOP (" + limit + ") lh.LichHen_ID, lh.BenhNhan_ID, lh.BacSi_ID, lh.NgayKham, lh.GioKham, lh.GhiChu, lh.TrangThai, " +

                         "COALESCE(tkBN.HoTen, N'') AS TenBenhNhan, tkBN.SoDienThoai AS SdtBenhNhan, " +

                         "COALESCE(tkBS.HoTen, N'') AS TenBacSi, CAST(NULL AS NVARCHAR(100)) AS TenPhong " +

                         "FROM LichHen lh " +

                         "LEFT JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID " +

                         "LEFT JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID " +

                         "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +

                         "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID " +

                         "ORDER BY lh.NgayKham DESC, lh.GioKham DESC";

            return runReceptionQueryLegacy(legacy, null, null);

        }

    }

    /** Thống kê toàn cục trên bảng LichHen (không lọc theo tuần/tháng). */
    public static final class ReceptionTotals {
        public final int total;
        public final int pending;
        public final int confirmed;
        public final int completed;

        public ReceptionTotals(int total, int pending, int confirmed, int completed) {
            this.total = total;
            this.pending = pending;
            this.confirmed = confirmed;
            this.completed = completed;
        }
    }

    public ReceptionTotals countReceptionDashboardTotals() {
        String sql =
                "SELECT COUNT(1) AS CTotal,"
                + " SUM(CASE WHEN lh.TrangThai IN (N'Chờ duyệt', N'Chờ xác nhận') THEN 1 ELSE 0 END) AS CPendi,"
                + " SUM(CASE WHEN lh.TrangThai IN (N'Đã duyệt', N'Đã xác nhận', N'Đã đến') THEN 1 ELSE 0 END) AS CConf,"
                + " SUM(CASE WHEN lh.TrangThai IN (N'Hoàn thành', N'Đã thanh toán') THEN 1 ELSE 0 END) AS CComp"
                + " FROM LichHen lh";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ReceptionTotals(rs.getInt("CTotal"),
                        rs.getInt("CPendi"),
                        rs.getInt("CConf"),
                        rs.getInt("CComp"));
            }
        } catch (SQLException e) {
            System.err.println("[LichHenDAO] countReceptionDashboardTotals: " + e.getMessage());
            e.printStackTrace();
        }
        return new ReceptionTotals(0, 0, 0, 0);
    }

    /** loc: pending | confirmed | completed — toàn bộ lịch sử có trạng thái đó. */
    public List<LichHen> getLichHenReceptionByLocAllTime(String loc, int top) {
        if (top <= 0) top = 800;
        String where;
        switch (loc == null ? "" : loc) {
            case "pending":
                where = " AND lh.TrangThai IN (N'Chờ duyệt', N'Chờ xác nhận') ";
                break;
            case "confirmed":
                where = " AND lh.TrangThai IN (N'Đã duyệt', N'Đã xác nhận', N'Đã đến') ";
                break;
            case "completed":
                where = " AND lh.TrangThai IN (N'Hoàn thành', N'Đã thanh toán') ";
                break;
            default:
                where = "";
        }
        try {
            String sql = "SELECT TOP (" + top + ") lh.*, tkBN.HoTen AS TenBenhNhan, tkBN.SoDienThoai AS SdtBenhNhan, "
                    + "tkBS.HoTen AS TenBacSi, pk.TenPhong AS TenPhong FROM LichHen lh "
                    + "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID "
                    + "JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID "
                    + "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID "
                    + "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID "
                    + "LEFT JOIN PhongKham pk ON lh.Phong_ID = pk.Phong_ID "
                    + " WHERE 1=1 " + where
                    + " ORDER BY lh.NgayKham DESC, lh.GioKham DESC";
            return runReceptionQueryFullNoParam(sql);
        } catch (SQLException e) {
            System.err.println("[LichHenDAO] getLichHenReceptionByLocAllTime full: " + e.getMessage());
            String legacy = "SELECT TOP (" + top + ") lh.LichHen_ID, lh.BenhNhan_ID, lh.BacSi_ID, lh.NgayKham, lh.GioKham, lh.GhiChu, lh.TrangThai, "
                    + "COALESCE(tkBN.HoTen, N'') AS TenBenhNhan, tkBN.SoDienThoai AS SdtBenhNhan, "
                    + "COALESCE(tkBS.HoTen, N'') AS TenBacSi, CAST(NULL AS NVARCHAR(100)) AS TenPhong FROM LichHen lh "
                    + "LEFT JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID "
                    + "LEFT JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID "
                    + "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID "
                    + "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID "
                    + " WHERE 1=1 " + where
                    + " ORDER BY lh.NgayKham DESC, lh.GioKham DESC";
            return runReceptionLegacyNoParam(legacy);
        }
    }

    /** Lọc ngày khám trong khoảng [start, end] (cận dưới trên inclusives). */
    public List<LichHen> getLichHenReceptionBetweenDates(java.sql.Date start, java.sql.Date endInclusive, int top) {
        if (top <= 0) top = 2500;
        try {
            String sql = "SELECT TOP (" + top + ") lh.*, tkBN.HoTen AS TenBenhNhan, tkBN.SoDienThoai AS SdtBenhNhan, "
                    + "tkBS.HoTen AS TenBacSi, pk.TenPhong AS TenPhong FROM LichHen lh "
                    + "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID "
                    + "JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID "
                    + "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID "
                    + "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID "
                    + "LEFT JOIN PhongKham pk ON lh.Phong_ID = pk.Phong_ID "
                    + "WHERE CAST(lh.NgayKham AS DATE) >= CAST(? AS DATE) AND CAST(lh.NgayKham AS DATE) <= CAST(? AS DATE) "
                    + "ORDER BY lh.NgayKham ASC, lh.GioKham ASC";
            return runReceptionTwoDates(sql, start, endInclusive);
        } catch (SQLException e) {
            System.err.println("[LichHenDAO] between full: " + e.getMessage());
            String legacy = "SELECT TOP (" + top + ") lh.LichHen_ID, lh.BenhNhan_ID, lh.BacSi_ID, lh.NgayKham, lh.GioKham, lh.GhiChu, lh.TrangThai, "
                    + "COALESCE(tkBN.HoTen, N'') AS TenBenhNhan, tkBN.SoDienThoai AS SdtBenhNhan, "
                    + "COALESCE(tkBS.HoTen, N'') AS TenBacSi, CAST(NULL AS NVARCHAR(100)) AS TenPhong FROM LichHen lh "
                    + "LEFT JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID "
                    + "LEFT JOIN TaiKhoan tkBN ON bn.TaiKhoan_ID = tkBN.TaiKhoan_ID "
                    + "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID "
                    + "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID "
                    + "WHERE CAST(lh.NgayKham AS DATE) >= CAST(? AS DATE) AND CAST(lh.NgayKham AS DATE) <= CAST(? AS DATE) "
                    + "ORDER BY lh.NgayKham ASC, lh.GioKham ASC";
            return runReceptionTwoDatesLegacy(legacy, start, endInclusive);
        }
    }

    private List<LichHen> runReceptionQueryFullNoParam(String sql) throws SQLException {
        List<LichHen> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            boolean hasPhongCol = hasColumn(md, "Phong_ID");
            while (rs.next()) {
                LichHen lh = mapResultSetToLichHen(rs, hasPhongCol);
                attachReceptionDisplay(lh, rs);
                list.add(lh);
            }
        }
        return list;
    }

    private List<LichHen> runReceptionLegacyNoParam(String sql) {
        List<LichHen> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            boolean hasPhongCol = hasColumn(md, "Phong_ID");
            while (rs.next()) {
                LichHen lh = mapResultSetToLichHen(rs, hasPhongCol);
                attachReceptionDisplay(lh, rs);
                list.add(lh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<LichHen> runReceptionTwoDates(String sql, java.sql.Date a, java.sql.Date b) throws SQLException {
        List<LichHen> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, a);
            ps.setDate(2, b);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                boolean hasPhongCol = hasColumn(md, "Phong_ID");
                while (rs.next()) {
                    LichHen lh = mapResultSetToLichHen(rs, hasPhongCol);
                    attachReceptionDisplay(lh, rs);
                    list.add(lh);
                }
            }
        }
        return list;
    }

    private List<LichHen> runReceptionTwoDatesLegacy(String sql, java.sql.Date a, java.sql.Date b) {
        List<LichHen> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, a);
            ps.setDate(2, b);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                boolean hasPhongCol = hasColumn(md, "Phong_ID");
                while (rs.next()) {
                    LichHen lh = mapResultSetToLichHen(rs, hasPhongCol);
                    attachReceptionDisplay(lh, rs);
                    list.add(lh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateTrangThai(int lichHenId, String trangThai) {
        String sql = "UPDATE LichHen SET TrangThai = ? WHERE LichHen_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, trangThai);
            ps.setInt(2, lichHenId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(int lichHenId) {
        String sql = "DELETE FROM LichHen WHERE LichHen_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichHenId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class ReceptionBookingDetail {
        public int lichHenId;
        public int benhNhanId;
        public int bacSiId;
        public int phongId;
        public Date ngayKham;
        public Time gioKham;
        public String ghiChu;
        public String patientName;
        public String patientPhone;
        public List<Integer> dichVuIds = new ArrayList<>();
    }

    public ReceptionBookingDetail getReceptionDetailById(int lichHenId) {
        String sql = "SELECT lh.LichHen_ID, lh.BenhNhan_ID, lh.BacSi_ID, lh.Phong_ID, lh.NgayKham, lh.GioKham, lh.GhiChu, " +
                     "tk.HoTen AS TenBenhNhan, tk.SoDienThoai AS SdtBenhNhan " +
                     "FROM LichHen lh " +
                     "JOIN BenhNhan bn ON bn.BenhNhan_ID = lh.BenhNhan_ID " +
                     "JOIN TaiKhoan tk ON tk.TaiKhoan_ID = bn.TaiKhoan_ID " +
                     "WHERE lh.LichHen_ID = ?";
        ReceptionBookingDetail d = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichHenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = new ReceptionBookingDetail();
                    d.lichHenId = rs.getInt("LichHen_ID");
                    d.benhNhanId = rs.getInt("BenhNhan_ID");
                    d.bacSiId = rs.getInt("BacSi_ID");
                    d.phongId = rs.getInt("Phong_ID");
                    d.ngayKham = rs.getDate("NgayKham");
                    d.gioKham = rs.getTime("GioKham");
                    d.ghiChu = rs.getNString("GhiChu");
                    d.patientName = rs.getNString("TenBenhNhan");
                    d.patientPhone = rs.getString("SdtBenhNhan");
                }
            }
            if (d != null) {
                try (PreparedStatement psDv = conn.prepareStatement("SELECT DichVu_ID FROM ChiTietLichHen WHERE LichHen_ID = ?")) {
                    psDv.setInt(1, lichHenId);
                    try (ResultSet rsDv = psDv.executeQuery()) {
                        while (rsDv.next()) {
                            d.dichVuIds.add(rsDv.getInt("DichVu_ID"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }

    public boolean updateBookingWithServices(LichHen lh, List<Integer> dichVuIds) {
        String sqlUpdate = "UPDATE LichHen SET BenhNhan_ID = ?, BacSi_ID = ?, NgayKham = ?, GioKham = ?, GhiChu = ?, Phong_ID = ? WHERE LichHen_ID = ?";
        String sqlDeleteDv = "DELETE FROM ChiTietLichHen WHERE LichHen_ID = ?";
        String sqlInsertDv = "INSERT INTO ChiTietLichHen (LichHen_ID, DichVu_ID) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psUp = conn.prepareStatement(sqlUpdate)) {
                psUp.setInt(1, lh.getBenhNhanID());
                psUp.setInt(2, lh.getBacSiID());
                psUp.setDate(3, lh.getNgayKham());
                psUp.setTime(4, lh.getGioKham());
                psUp.setNString(5, lh.getGhiChu());
                psUp.setInt(6, lh.getPhongID());
                psUp.setInt(7, lh.getLichHenID());
                if (psUp.executeUpdate() <= 0) {
                    conn.rollback();
                    return false;
                }
            }
            try (PreparedStatement psDel = conn.prepareStatement(sqlDeleteDv)) {
                psDel.setInt(1, lh.getLichHenID());
                psDel.executeUpdate();
            }
            if (dichVuIds != null && !dichVuIds.isEmpty()) {
                try (PreparedStatement psIns = conn.prepareStatement(sqlInsertDv)) {
                    for (Integer dv : dichVuIds) {
                        psIns.setInt(1, lh.getLichHenID());
                        psIns.setInt(2, dv);
                        psIns.addBatch();
                    }
                    psIns.executeBatch();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class PaymentItem {
        public String serviceName;
        public int quantity;
        public double unitPrice;
        public double lineTotal;
    }

    public static class ReceptionPaymentDetail {
        public int lichHenId;
        public String patientName;
        public String patientPhone;
        public Date ngayKham;
        public Time gioKham;
        public String doctorName;
        public List<PaymentItem> items = new ArrayList<>();
        public double subtotal;
    }

    public ReceptionPaymentDetail getReceptionPaymentDetail(int lichHenId) {
        String headSql = "SELECT lh.LichHen_ID, lh.NgayKham, lh.GioKham, " +
                         "tkbn.HoTen AS TenBenhNhan, tkbn.SoDienThoai AS SdtBenhNhan, " +
                         "tkbs.HoTen AS TenBacSi " +
                         "FROM LichHen lh " +
                         "JOIN BenhNhan bn ON bn.BenhNhan_ID = lh.BenhNhan_ID " +
                         "JOIN TaiKhoan tkbn ON tkbn.TaiKhoan_ID = bn.TaiKhoan_ID " +
                         "JOIN BacSi bs ON bs.BacSi_ID = lh.BacSi_ID " +
                         "JOIN TaiKhoan tkbs ON tkbs.TaiKhoan_ID = bs.TaiKhoan_ID " +
                         "WHERE lh.LichHen_ID = ?";
        String itemSql = "SELECT dv.TenDichVu, ISNULL(ctlh.SoLuong, 1) AS SoLuong, dv.GiaTien " +
                         "FROM ChiTietLichHen ctlh " +
                         "JOIN DichVu dv ON dv.DichVu_ID = ctlh.DichVu_ID " +
                         "WHERE ctlh.LichHen_ID = ?";
        ReceptionPaymentDetail d = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psHead = conn.prepareStatement(headSql);
             PreparedStatement psItem = conn.prepareStatement(itemSql)) {
            psHead.setInt(1, lichHenId);
            try (ResultSet rs = psHead.executeQuery()) {
                if (rs.next()) {
                    d = new ReceptionPaymentDetail();
                    d.lichHenId = rs.getInt("LichHen_ID");
                    d.ngayKham = rs.getDate("NgayKham");
                    d.gioKham = rs.getTime("GioKham");
                    d.patientName = rs.getNString("TenBenhNhan");
                    d.patientPhone = rs.getString("SdtBenhNhan");
                    d.doctorName = rs.getNString("TenBacSi");
                }
            }
            if (d == null) return null;
            psItem.setInt(1, lichHenId);
            try (ResultSet rsItem = psItem.executeQuery()) {
                while (rsItem.next()) {
                    PaymentItem item = new PaymentItem();
                    item.serviceName = rsItem.getNString("TenDichVu");
                    item.quantity = rsItem.getInt("SoLuong");
                    item.unitPrice = rsItem.getDouble("GiaTien");
                    item.lineTotal = item.unitPrice * item.quantity;
                    d.subtotal += item.lineTotal;
                    d.items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return d;
    }

}


