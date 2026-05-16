package com.dentalclinic.dao;



import com.dentalclinic.utils.DBConnection;

import com.dentalclinic.model.LichHen;

import com.dentalclinic.model.TaiKhoan;

import com.dentalclinic.model.BenhNhan;

import com.dentalclinic.model.BacSi;

import com.dentalclinic.model.DanhSachLichHenDTO;

import com.dentalclinic.model.HoSo;

import com.dentalclinic.model.PhongKham;
import com.dentalclinic.model.ChiTietLichHen;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.PhieuKham;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

                    ganDanhSachDichVuDatChoLichHen(lh);
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
        Map<Integer, Integer> qtyMap = new HashMap<>();
        if (dichVuIds != null) {
            for (Integer dvID : dichVuIds) {
                if (dvID == null) continue;
                qtyMap.put(dvID, qtyMap.getOrDefault(dvID, 0) + 1);
            }
        }
        return insertBooking(lh, qtyMap);
    }

    public int insertBooking(LichHen lh, Map<Integer, Integer> qtyByDvId) {
        if (qtyByDvId == null || qtyByDvId.isEmpty()) return -1;

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

                    String sqlDV = "INSERT INTO ChiTietLichHen (LichHen_ID, DichVu_ID, SoLuong) VALUES (?, ?, ?)";

                    try (PreparedStatement psDV = conn.prepareStatement(sqlDV)) {

                        for (Map.Entry<Integer, Integer> e : qtyByDvId.entrySet()) {
                            if (e.getKey() == null || e.getKey() < 1) continue;
                            int qty = e.getValue() == null || e.getValue() < 1 ? 1 : e.getValue();
                            psDV.setInt(1, lhID);
                            psDV.setInt(2, e.getKey());
                            psDV.setInt(3, qty);
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
                + " SUM(CASE WHEN lh.TrangThai IN (N'Đã duyệt', N'Đã xác nhận', N'Đã đến', N'Đang khám') THEN 1 ELSE 0 END) AS CConf,"
                + " SUM(CASE WHEN lh.TrangThai IN (N'Hoàn thành', N'Đã hoàn thành', N'Đã thanh toán') THEN 1 ELSE 0 END) AS CComp"
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
                where = " AND lh.TrangThai IN (N'Đã duyệt', N'Đã xác nhận', N'Đã đến', N'Đang khám') ";
                break;
            case "completed":
                where = " AND lh.TrangThai IN (N'Hoàn thành', N'Đã hoàn thành', N'Đã thanh toán') ";
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
        public List<DichVuQty> dichVuItems = new ArrayList<>();
    }

    public static class DichVuQty {
        public int id;
        public int quantity;
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
                loadMergedDichVuForDetail(conn, lichHenId, d);
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
        String itemPhieuSql = "SELECT dv.TenDichVu, ctd.SoLuong, ctd.DonGia AS GiaTien "
                + "FROM PhieuKham pk "
                + "JOIN ChiTietDichVu ctd ON ctd.PhieuKham_ID = pk.PhieuKham_ID "
                + "JOIN DichVu dv ON dv.DichVu_ID = ctd.DichVu_ID "
                + "WHERE pk.LichHen_ID = ? ORDER BY ctd.ChiTietDichVu_ID";
        String itemDatHenSql = "SELECT dv.TenDichVu, ISNULL(ctlh.SoLuong, 1) AS SoLuong, dv.GiaTien "
                + "FROM ChiTietLichHen ctlh "
                + "JOIN DichVu dv ON dv.DichVu_ID = ctlh.DichVu_ID "
                + "WHERE ctlh.LichHen_ID = ?";
        ReceptionPaymentDetail d = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psHead = conn.prepareStatement(headSql)) {
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

            try (PreparedStatement psPk = conn.prepareStatement(itemPhieuSql)) {
                psPk.setInt(1, lichHenId);
                try (ResultSet rsItem = psPk.executeQuery()) {
                    while (rsItem.next()) {
                        appendPaymentItem(d, rsItem.getNString("TenDichVu"),
                                rsItem.getInt("SoLuong"), rsItem.getDouble("GiaTien"));
                    }
                }
            }
            if (d.items.isEmpty()) {
                try (PreparedStatement psLh = conn.prepareStatement(itemDatHenSql)) {
                    psLh.setInt(1, lichHenId);
                    try (ResultSet rsItem = psLh.executeQuery()) {
                        while (rsItem.next()) {
                            appendPaymentItem(d, rsItem.getNString("TenDichVu"),
                                    rsItem.getInt("SoLuong"), rsItem.getDouble("GiaTien"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return d;
    }

    private static void appendPaymentItem(ReceptionPaymentDetail d, String name, int qty, double unitPrice) {
        if (d == null || name == null || name.isBlank()) {
            return;
        }
        PaymentItem item = new PaymentItem();
        item.serviceName = name.trim();
        item.quantity = qty > 0 ? qty : 1;
        item.unitPrice = unitPrice;
        item.lineTotal = item.unitPrice * item.quantity;
        d.subtotal += item.lineTotal;
        d.items.add(item);
    }

    /**
     * Lễ tân xác nhận thanh toán. Trả về null nếu thành công, ngược lại trả thông báo lỗi.
     */
    public String xacNhanThanhToan(int lichHenId, String phuongThucThanhToan, double tongTien, Integer leTanId) {
        if (lichHenId <= 0 || tongTien <= 0) {
            return "Thiếu mã lịch hẹn hoặc tổng tiền không hợp lệ.";
        }
        if (leTanId == null || leTanId <= 0) {
            return "Không xác định được lễ tân thu tiền. Vui lòng đăng nhập lại.";
        }
        try {
            PhieuKhamDAO pkDao = new PhieuKhamDAO();
            int phieuKhamId = timPhieuKhamId(lichHenId);
            if (phieuKhamId <= 0) {
                phieuKhamId = pkDao.taoPhieuKhamTuDatHen(lichHenId);
            }
            if (phieuKhamId <= 0) {
                return "Chưa có phiếu khám hoặc dịch vụ để lập hóa đơn.";
            }
            if (!pkDao.damBaoHoaDonChoLichHen(lichHenId)) {
                System.err.println("[LichHenDAO] damBaoHoaDonChoLichHen thất bại, thử upsert trực tiếp.");
            }
            if (!pkDao.upsertHoaDonThanhToan(phieuKhamId, tongTien, phuongThucThanhToan, leTanId)) {
                return "Không ghi được hóa đơn (HoaDon). Kiểm tra bảng HoaDon và ràng buộc LeTan_ID.";
            }
            if (!capNhatTrangThaiSauThanhToan(lichHenId)) {
                return "Đã ghi hóa đơn nhưng không cập nhật được trạng thái lịch hẹn sang Đã hoàn thành.";
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            String friendly = mapPaymentSqlError(e);
            if (friendly != null) {
                return friendly;
            }
            String msg = e.getMessage();
            return (msg != null && !msg.isBlank()) ? msg : "Lỗi cơ sở dữ liệu khi lưu hóa đơn.";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage() != null ? e.getMessage() : "Lỗi không xác định khi thanh toán.";
        }
    }

    /** Trigger cũ tg_CapNhatTrangThai_ThanhToan ghi PhieuKham.TrangThai (cột không tồn tại) — chạy patch SQL. */
    private static String mapPaymentSqlError(SQLException e) {
        Throwable t = e;
        while (t != null) {
            String msg = t.getMessage();
            if (msg != null && msg.contains("TrangThai")) {
                return "CSDL còn trigger tg_CapNhatTrangThai_ThanhToan trên HoaDon. "
                        + "Chạy database/patch_drop_hoadon_trangthai_trigger.sql trên DbQuanLyNhaKhoa, rồi thử lại.";
            }
            t = t.getCause();
        }
        return null;
    }

    private int timPhieuKhamId(int lichHenId) throws SQLException {
        String sqlPk = "SELECT PhieuKham_ID FROM PhieuKham WHERE LichHen_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlPk)) {
            ps.setInt(1, lichHenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PhieuKham_ID");
                }
            }
        }
        return -1;
    }

    /** Gắn trạng thái HoaDon (Chưa/Đã thanh toán) cho danh sách lịch hẹn lễ tân. */
    public void attachTrangThaiHoaDonForAppointments(List<LichHen> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            return;
        }
        List<Integer> ids = new ArrayList<>();
        for (LichHen lh : appointments) {
            if (lh != null && lh.getLichHenID() > 0) {
                ids.add(lh.getLichHenID());
            }
        }
        if (ids.isEmpty()) {
            return;
        }
        Map<Integer, String> byLh = loadTrangThaiHoaDonByLichHenIds(ids);
        for (LichHen lh : appointments) {
            if (lh != null) {
                lh.setTrangThaiHoaDon(byLh.get(lh.getLichHenID()));
            }
        }
    }

    private Map<Integer, String> loadTrangThaiHoaDonByLichHenIds(List<Integer> lichHenIds) {
        Map<Integer, String> map = new HashMap<>();
        if (lichHenIds == null || lichHenIds.isEmpty()) {
            return map;
        }
        StringBuilder in = new StringBuilder();
        for (int i = 0; i < lichHenIds.size(); i++) {
            if (i > 0) in.append(",");
            in.append("?");
        }
        String sql = "SELECT pk.LichHen_ID, hd.HoaDon_ID, hd.NgayThanhToan FROM PhieuKham pk "
                + "INNER JOIN HoaDon hd ON hd.PhieuKham_ID = pk.PhieuKham_ID "
                + "WHERE pk.LichHen_ID IN (" + in + ")";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < lichHenIds.size(); i++) {
                ps.setInt(i + 1, lichHenIds.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int lhId = rs.getInt("LichHen_ID");
                    if (rs.getTimestamp("NgayThanhToan") != null) {
                        map.put(lhId, "Đã thanh toán");
                    } else if (rs.getInt("HoaDon_ID") > 0) {
                        map.put(lhId, "Chưa thanh toán");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[LichHenDAO] loadTrangThaiHoaDonByLichHenIds: " + e.getMessage());
        }
        return map;
    }

    /** Gắn danh sách tên dịch vụ đặt (ChiTietLichHen) cho danh sách lịch hẹn lễ tân. */
    public void attachTenDichVuForAppointments(List<LichHen> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            return;
        }
        Map<Integer, List<String>> byId = loadTenDichVuByLichHenIds(appointments);
        for (LichHen lh : appointments) {
            List<String> names = byId.get(lh.getLichHenID());
            lh.setTenDichVuList(names != null ? names : new ArrayList<>());
        }
    }

    private Map<Integer, List<String>> loadTenDichVuByLichHenIds(List<LichHen> appointments) {
        Map<Integer, List<String>> result = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        for (LichHen lh : appointments) {
            if (lh != null && lh.getLichHenID() > 0) {
                ids.add(lh.getLichHenID());
            }
        }
        if (ids.isEmpty()) {
            return result;
        }
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) inClause.append(',');
            inClause.append('?');
        }
        String sqlPk = "SELECT pk.LichHen_ID, dv.TenDichVu FROM PhieuKham pk "
                + "JOIN ChiTietDichVu ctd ON ctd.PhieuKham_ID = pk.PhieuKham_ID "
                + "JOIN DichVu dv ON dv.DichVu_ID = ctd.DichVu_ID "
                + "WHERE pk.LichHen_ID IN (" + inClause + ") "
                + "ORDER BY pk.LichHen_ID, dv.TenDichVu";
        String sqlLh = "SELECT ctlh.LichHen_ID, dv.TenDichVu FROM ChiTietLichHen ctlh "
                + "INNER JOIN DichVu dv ON dv.DichVu_ID = ctlh.DichVu_ID "
                + "WHERE ctlh.LichHen_ID IN (" + inClause + ") "
                + "ORDER BY ctlh.LichHen_ID, dv.TenDichVu";
        try (Connection conn = DBConnection.getConnection()) {
            napTenDichVuTuQuery(conn, sqlPk, ids, result);
            napTenDichVuTuQuery(conn, sqlLh, ids, result);
        } catch (SQLException e) {
            System.err.println("[LichHenDAO] loadTenDichVuByLichHenIds: " + e.getMessage());
        }
        return result;
    }

    /** Dịch vụ phiếu khám (bác sĩ) + đặt lịch — ưu tiên số lượng từ phiếu khám. */
    private void loadMergedDichVuForDetail(Connection conn, int lichHenId, ReceptionBookingDetail d) throws SQLException {
        java.util.LinkedHashMap<Integer, Integer> merged = new java.util.LinkedHashMap<>();
        String sqlPk = "SELECT ctd.DichVu_ID, ISNULL(ctd.SoLuong, 1) AS SoLuong FROM PhieuKham pk "
                + "INNER JOIN ChiTietDichVu ctd ON ctd.PhieuKham_ID = pk.PhieuKham_ID "
                + "WHERE pk.LichHen_ID = ? ORDER BY ctd.ChiTietDichVu_ID";
        try (PreparedStatement ps = conn.prepareStatement(sqlPk)) {
            ps.setInt(1, lichHenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int dvId = rs.getInt("DichVu_ID");
                    int qty = rs.getInt("SoLuong");
                    merged.put(dvId, qty > 0 ? qty : 1);
                }
            }
        }
        String sqlLh = "SELECT ctlh.DichVu_ID, ISNULL(ctlh.SoLuong, 1) AS SoLuong FROM ChiTietLichHen ctlh "
                + "WHERE ctlh.LichHen_ID = ? ORDER BY ctlh.ChiTietLichHen_ID";
        try (PreparedStatement ps = conn.prepareStatement(sqlLh)) {
            ps.setInt(1, lichHenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int dvId = rs.getInt("DichVu_ID");
                    if (!merged.containsKey(dvId)) {
                        int qty = rs.getInt("SoLuong");
                        merged.put(dvId, qty > 0 ? qty : 1);
                    }
                }
            }
        }
        for (java.util.Map.Entry<Integer, Integer> e : merged.entrySet()) {
            d.dichVuIds.add(e.getKey());
            DichVuQty item = new DichVuQty();
            item.id = e.getKey();
            item.quantity = e.getValue();
            d.dichVuItems.add(item);
        }
    }

    private void napTenDichVuTuQuery(Connection conn, String sql, List<Integer> ids, Map<Integer, List<String>> result)
            throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int lichHenId = rs.getInt("LichHen_ID");
                    String ten = rs.getNString("TenDichVu");
                    if (ten == null || ten.isBlank()) {
                        continue;
                    }
                    List<String> names = result.computeIfAbsent(lichHenId, k -> new ArrayList<>());
                    String trimmed = ten.trim();
                    if (!names.contains(trimmed)) {
                        names.add(trimmed);
                    }
                }
            }
        }
    }

    public List<DanhSachLichHenDTO> layDanhSachChoKhamNgayHienTai() {
        return layDanhSachChoKhamNgayHienTai(null);
    }

    /** Hàng chờ khám hôm nay; lọc theo bác sĩ nếu bacSiId > 0. */
    public List<DanhSachLichHenDTO> layDanhSachChoKhamNgayHienTai(Integer bacSiId) {
        List<DanhSachLichHenDTO> list = new ArrayList<>();
        boolean filterDoctor = bacSiId != null && bacSiId > 0;
        String sql = "Select lh.LichHen_ID, tk_bn.HoTen as TenBenhNhan, tk_bn.SoDienThoai, CONVERT(VARCHAR(5), lh.GioKham, 108) as GioHen, tk_bs.HoTen as TenBacSi, lh.GhiChu as LyDoKham, lh.TrangThai "
                + "from LichHen as lh "
                + "join BenhNhan bn on lh.BenhNhan_ID = bn.BenhNhan_ID "
                + "join TaiKhoan tk_bn on bn.TaiKhoan_ID = tk_bn.TaiKhoan_ID "
                + "JOIN BacSi bs on lh.BacSi_ID = bs.BacSi_ID "
                + "JOIN TaiKhoan tk_bs ON bs.TaiKhoan_ID = tk_bs.TaiKhoan_ID "
                + "WHERE CONVERT(DATE, lh.NgayKham) = CONVERT(DATE, GETDATE()) and lh.TrangThai in (N'Đã đến', N'Đang khám') "
                + (filterDoctor ? " AND lh.BacSi_ID = ? " : "")
                + "ORDER BY lh.GioKham ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (filterDoctor) {
                ps.setInt(1, bacSiId);
            }
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    DanhSachLichHenDTO ds = new DanhSachLichHenDTO();
                    ds.setLichHenID(rs.getInt("LichHen_ID"));
                    ds.setTenBenhNhan(rs.getNString("TenBenhNhan"));
                    ds.setSoDienThoai(rs.getString("SoDienThoai"));
                    ds.setGioHen(rs.getString("GioHen"));
                    ds.setTenBacSi(rs.getNString("TenBacSi"));
                    ds.setLyDoKham(rs.getNString("LyDoKham"));

                    String trangThaiDB = rs.getNString("TrangThai");
                    if ("Đã đến".equalsIgnoreCase(trangThaiDB)) ds.setTrangThai("waiting");
                    else if ("Đang khám".equalsIgnoreCase(trangThaiDB)) ds.setTrangThai("examining");
                    else if ("Đã khám".equalsIgnoreCase(trangThaiDB)
                            || "Đã hoàn thành".equalsIgnoreCase(trangThaiDB)) ds.setTrangThai("completed");
                    else ds.setTrangThai("cancelled");

                    list.add(ds);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean capNhatTrangThaiDangKham(int lichHenID) {
        String sql = "UPDATE LichHen SET TrangThai = N'Đang khám' WHERE LichHen_ID = ? AND TrangThai = N'Đã đến'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichHenID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Lễ tân thu tiền xong → Đã hoàn thành. */
    public boolean capNhatTrangThaiSauThanhToan(int lichHenID) {
        String sql = "UPDATE LichHen SET TrangThai = N'Đã hoàn thành' "
                + "WHERE LichHen_ID = ? AND TrangThai <> N'Đã hủy' AND TrangThai <> N'Đã hoàn thành'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichHenID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Bác sĩ xác nhận hoàn tất khám → Đã khám (lễ tân thu tiền sau). */
    public boolean capNhatTrangThaiDaHoanThanh(int lichHenID, Integer bacSiId) {
        String sql = "UPDATE LichHen SET TrangThai = N'Đã khám' "
                + "WHERE LichHen_ID = ? AND TrangThai IN (N'Đang khám', N'Đã đến')";
        if (bacSiId != null && bacSiId > 0) {
            sql += " AND BacSi_ID = ?";
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichHenID);
            if (bacSiId != null && bacSiId > 0) {
                ps.setInt(2, bacSiId);
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public LichHen layChiTietLichHen(int lichHenID) {
        String sql = "select tk_bn.HoTen, tk_bn.GioiTinh, tk_bn.SoDienThoai, tk_bn.NgaySinh, "
                + "hs.DiUngThuoc, hs.TienSuBenh, lh.BenhNhan_ID, lh.GhiChu AS GhiChuLichHen, "
                + "tk_bs.HoTen as TenBacSi, pk.TenPhong "
                + "from LichHen as lh "
                + "join BenhNhan bn on lh.BenhNhan_ID = bn.BenhNhan_ID "
                + "join TaiKhoan tk_bn on bn.TaiKhoan_ID = tk_bn.TaiKhoan_ID "
                + "left join HoSo hs on bn.BenhNhan_ID = hs.BenhNhan_ID "
                + "left join BacSi bs on lh.BacSi_ID = bs.BacSi_ID "
                + "left join TaiKhoan tk_bs on bs.TaiKhoan_ID = tk_bs.TaiKhoan_ID "
                + "left join PhongKham pk on lh.Phong_ID = pk.Phong_ID "
                + "where lh.LichHen_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lichHenID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tkBN = new TaiKhoan();
                    tkBN.setHoTen(rs.getNString("HoTen"));
                    tkBN.setGioiTinh(rs.getBoolean("GioiTinh"));
                    tkBN.setNgaySinh(rs.getDate("NgaySinh"));
                    tkBN.setSoDienThoai(rs.getString("SoDienThoai"));

                    HoSo hs = new HoSo();
                    hs.setDiUngThuoc(rs.getNString("DiUngThuoc") != null ? rs.getNString("DiUngThuoc") : "Không có cảnh báo");
                    hs.setTienSuBenh(rs.getNString("TienSuBenh") != null ? rs.getNString("TienSuBenh") : "Chưa ghi nhận");

                    BenhNhan bn = new BenhNhan();
                    bn.setBenhNhanID(rs.getInt("BenhNhan_ID"));
                    bn.setTaiKhoan(tkBN);
                    bn.setHoSo(hs);

                    TaiKhoan tkBS = new TaiKhoan();
                    tkBS.setHoTen(rs.getNString("TenBacSi"));
                    BacSi bs = new BacSi();
                    bs.setTaiKhoan(tkBS);

                    PhongKham phongKham = new PhongKham();
                    phongKham.setTenPhong(rs.getNString("TenPhong") != null ? rs.getNString("TenPhong") : "Chưa xếp phòng");

                    LichHen lh = new LichHen();
                    lh.setLichHenID(lichHenID);
                    lh.setGhiChu(rs.getNString("GhiChuLichHen"));
                    lh.setBenhNhan(bn);
                    lh.setBacSi(bs);
                    lh.setPhongKham(phongKham);

                    ganDanhSachDichVuDatChoLichHen(lh);
                    PhieuKhamDAO phieuKhamDAO = new PhieuKhamDAO();
                    lh.setPhieuKham(phieuKhamDAO.layPhieuKhamTheoLichHen(lichHenID));

                    return lh;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void ganDanhSachDichVuDatChoLichHen(LichHen lh) {
        if (lh == null || lh.getLichHenID() <= 0) {
            return;
        }
        String sql = "SELECT ctlh.DichVu_ID, ISNULL(ctlh.SoLuong, 1) AS SoLuong, "
                + "dv.TenDichVu, dv.GiaTien, dv.TinhTheoRang, ISNULL(dv.ThoiLuongDuKien, 30) AS ThoiLuongDuKien FROM ChiTietLichHen ctlh "
                + "JOIN DichVu dv ON dv.DichVu_ID = ctlh.DichVu_ID WHERE ctlh.LichHen_ID = ? "
                + "ORDER BY dv.TenDichVu";
        List<ChiTietLichHen> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lh.getLichHenID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietLichHen ct = new ChiTietLichHen();
                    ct.setLichHenId(lh.getLichHenID());
                    ct.setDichVuId(rs.getInt("DichVu_ID"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    DichVu dv = new DichVu();
                    dv.setDichVuID(rs.getInt("DichVu_ID"));
                    dv.setTenDichVu(rs.getNString("TenDichVu"));
                    dv.setGiaTien(rs.getDouble("GiaTien"));
                    dv.setTinhTheoRang(rs.getBoolean("TinhTheoRang"));
                    dv.setThoiLuongDuKien(rs.getInt("ThoiLuongDuKien"));
                    ct.setDichVu(dv);
                    list.add(ct);
                }
            }
        } catch (SQLException e) {
            System.err.println("[LichHenDAO] ganDanhSachDichVuDatChoLichHen: " + e.getMessage());
        }
        lh.setDanhSachDichVuDat(list);
    }

    /** Dòng dịch vụ hiển thị trên hồ sơ bệnh nhân (phiếu khám + đặt lịch). */
    public static class PatientApptService {
        public int id;
        public String name;
        public double price;
        public int qty;
        public boolean perUnit;
        public String unit;
        public String time;
    }

    public List<PatientApptService> loadPatientServicesForLichHen(int lichHenId) {
        List<PatientApptService> result = new ArrayList<>();
        if (lichHenId <= 0) {
            return result;
        }
        java.util.LinkedHashMap<Integer, Integer> merged = new java.util.LinkedHashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            loadMergedDichVuQtyMap(conn, lichHenId, merged);
        } catch (SQLException e) {
            System.err.println("[LichHenDAO] loadPatientServicesForLichHen: " + e.getMessage());
            return result;
        }
        if (merged.isEmpty()) {
            return result;
        }
        String placeholders = merged.keySet().stream().map(x -> "?").collect(java.util.stream.Collectors.joining(","));
        String sql = "SELECT DichVu_ID, TenDichVu, GiaTien, TinhTheoRang, ISNULL(ThoiLuongDuKien, 30) AS ThoiLuongDuKien "
                + "FROM DichVu WHERE DichVu_ID IN (" + placeholders + ")";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            for (Integer id : merged.keySet()) {
                ps.setInt(idx++, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int dvId = rs.getInt("DichVu_ID");
                    Integer qty = merged.get(dvId);
                    if (qty == null) {
                        continue;
                    }
                    PatientApptService row = new PatientApptService();
                    row.id = dvId;
                    row.name = rs.getNString("TenDichVu");
                    row.price = rs.getDouble("GiaTien");
                    row.qty = qty > 0 ? qty : 1;
                    row.perUnit = rs.getBoolean("TinhTheoRang");
                    row.unit = row.perUnit ? "răng" : "";
                    int minutes = rs.getInt("ThoiLuongDuKien");
                    row.time = minutes + " phút";
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("[LichHenDAO] loadPatientServicesForLichHen dv: " + e.getMessage());
        }
        return result;
    }

    private void loadMergedDichVuQtyMap(Connection conn, int lichHenId, java.util.LinkedHashMap<Integer, Integer> merged)
            throws SQLException {
        String sqlPk = "SELECT ctd.DichVu_ID, ISNULL(ctd.SoLuong, 1) AS SoLuong FROM PhieuKham pk "
                + "INNER JOIN ChiTietDichVu ctd ON ctd.PhieuKham_ID = pk.PhieuKham_ID "
                + "WHERE pk.LichHen_ID = ? ORDER BY ctd.ChiTietDichVu_ID";
        try (PreparedStatement ps = conn.prepareStatement(sqlPk)) {
            ps.setInt(1, lichHenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int dvId = rs.getInt("DichVu_ID");
                    int qty = rs.getInt("SoLuong");
                    merged.put(dvId, qty > 0 ? qty : 1);
                }
            }
        }
        String sqlLh = "SELECT ctlh.DichVu_ID, ISNULL(ctlh.SoLuong, 1) AS SoLuong FROM ChiTietLichHen ctlh "
                + "WHERE ctlh.LichHen_ID = ? ORDER BY ctlh.ChiTietLichHen_ID";
        try (PreparedStatement ps = conn.prepareStatement(sqlLh)) {
            ps.setInt(1, lichHenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int dvId = rs.getInt("DichVu_ID");
                    if (!merged.containsKey(dvId)) {
                        int qty = rs.getInt("SoLuong");
                        merged.put(dvId, qty > 0 ? qty : 1);
                    }
                }
            }
        }
    }

    /** Bệnh nhân hủy lịch chờ xác nhận. */
    public boolean benhNhanHuyLichHen(int lichHenId, int taiKhoanId) {
        String sql = "UPDATE lh SET lh.TrangThai = N'Đã hủy' FROM LichHen lh "
                + "INNER JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID "
                + "WHERE lh.LichHen_ID = ? AND bn.TaiKhoan_ID = ? "
                + "AND lh.TrangThai IN (N'Chờ xác nhận', N'Chờ duyệt')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichHenId);
            ps.setInt(2, taiKhoanId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Bệnh nhân gửi yêu cầu hủy lịch đã xác nhận. */
    public boolean benhNhanYeuCauHuyLichHen(int lichHenId, int taiKhoanId) {
        String sql = "UPDATE lh SET lh.GhiChu = CASE "
                + "WHEN lh.GhiChu IS NULL OR lh.GhiChu NOT LIKE N'%[Yêu cầu hủy lịch]%' "
                + "THEN ISNULL(lh.GhiChu, N'') + N' [Yêu cầu hủy lịch]' ELSE lh.GhiChu END "
                + "FROM LichHen lh INNER JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID "
                + "WHERE lh.LichHen_ID = ? AND bn.TaiKhoan_ID = ? "
                + "AND lh.TrangThai IN (N'Đã xác nhận', N'Đã duyệt', N'Đã đến', N'Đang khám')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichHenId);
            ps.setInt(2, taiKhoanId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Bệnh nhân cập nhật lịch chờ xác nhận (ngày, giờ, ghi chú, dịch vụ). */
    public boolean benhNhanCapNhatLichHen(int lichHenId, int taiKhoanId, java.sql.Date ngayKham,
            java.sql.Time gioKham, String ghiChu, java.util.List<int[]> dichVuIdQty) {
        if (dichVuIdQty == null || dichVuIdQty.isEmpty()) {
            return false;
        }
        String sqlCheck = "SELECT lh.LichHen_ID FROM LichHen lh "
                + "INNER JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID "
                + "WHERE lh.LichHen_ID = ? AND bn.TaiKhoan_ID = ? "
                + "AND lh.TrangThai IN (N'Chờ xác nhận', N'Chờ duyệt')";
        String sqlUpdate = "UPDATE LichHen SET NgayKham = ?, GioKham = ?, GhiChu = ? WHERE LichHen_ID = ?";
        String sqlDeleteDv = "DELETE FROM ChiTietLichHen WHERE LichHen_ID = ?";
        String sqlInsertDv = "INSERT INTO ChiTietLichHen (LichHen_ID, DichVu_ID, SoLuong) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, lichHenId);
                psCheck.setInt(2, taiKhoanId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                }
            }
            conn.setAutoCommit(false);
            try (PreparedStatement psUp = conn.prepareStatement(sqlUpdate)) {
                psUp.setDate(1, ngayKham);
                psUp.setTime(2, gioKham);
                psUp.setNString(3, ghiChu);
                psUp.setInt(4, lichHenId);
                if (psUp.executeUpdate() <= 0) {
                    conn.rollback();
                    return false;
                }
            }
            try (PreparedStatement psDel = conn.prepareStatement(sqlDeleteDv)) {
                psDel.setInt(1, lichHenId);
                psDel.executeUpdate();
            }
            try (PreparedStatement psIns = conn.prepareStatement(sqlInsertDv)) {
                for (int[] line : dichVuIdQty) {
                    if (line == null || line.length < 2 || line[0] <= 0) {
                        continue;
                    }
                    int qty = line[1] > 0 ? line[1] : 1;
                    psIns.setInt(1, lichHenId);
                    psIns.setInt(2, line[0]);
                    psIns.setInt(3, qty);
                    psIns.addBatch();
                }
                psIns.executeBatch();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

}


