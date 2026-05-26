package com.dentalclinic.dao;

import com.dentalclinic.model.ChiTietDichVu;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.PhieuKham;
import com.dentalclinic.utils.DBConnection;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PhieuKhamDAO {

    /** Hoàn tất khám: cập nhật phiếu đã có (kể cả khi đã có HoaDon), hoặc tạo mới qua SP. */
    public void luuPhieuKhamLamSang(PhieuKham pk) throws Exception {
        if (pk == null || pk.getLichHenID() <= 0) {
            throw new SQLException("Thiếu mã lịch hẹn");
        }
        if (pk.getDanhSachDichVu() == null || pk.getDanhSachDichVu().isEmpty()) {
            throw new SQLException("Vui lòng chọn ít nhất một dịch vụ");
        }

        int phieuKhamId = timPhieuKhamIdTheoLichHen(pk.getLichHenID(), null);
        if (phieuKhamId > 0) {
            Connection conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            try {
                hoanTatPhieuKhamDaCo(conn, phieuKhamId, pk);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } else {
            goiStoredProcedureLuuPhieuKham(pk);
        }
    }

    private void hoanTatPhieuKhamDaCo(Connection conn, int phieuKhamId, PhieuKham pk) throws SQLException {
        updatePhieuKhamRow(conn, phieuKhamId, pk);
        try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM ChiTietDichVu WHERE PhieuKham_ID = ?")) {
            psDel.setInt(1, phieuKhamId);
            psDel.executeUpdate();
        }
        insertChiTietDichVuRows(conn, phieuKhamId, pk.getDanhSachDichVu());
        new LichHenDAO().syncChiTietLichHenFromClinical(conn, pk.getLichHenID(), pk.getDanhSachDichVu());
        capNhatHoaDonTheoPhieu(conn, phieuKhamId);
        capNhatTrangThaiLichHen(conn, pk.getLichHenID());
    }

    private void goiStoredProcedureLuuPhieuKham(PhieuKham pk) throws SQLException {
        String sqlPhieuKham = "{call SP_LuuPhieuKham (?, ?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             SQLServerCallableStatement cs = (SQLServerCallableStatement) conn.prepareCall(sqlPhieuKham)) {

            cs.setInt(1, pk.getLichHenID());
            cs.setNString(2, nullToEmpty(pk.getChanDoan()));
            cs.setNString(3, nullToEmpty(pk.getLyDoKham()));
            cs.setNString(4, nullToEmpty(pk.getGhiChu()));

            SQLServerDataTable tvpTable = buildTvp(pk.getDanhSachDichVu());
            cs.setStructured(5, "Type_ChiTietDichVu", tvpTable);
            cs.execute();
        }
        new LichHenDAO().syncChiTietLichHenFromClinical(pk.getLichHenID(), pk.getDanhSachDichVu());
    }

    private void capNhatHoaDonTheoPhieu(Connection conn, int phieuKhamId) throws SQLException {
        double tong = tinhTongTienChiTiet(conn, phieuKhamId);
        if (tong <= 0) {
            throw new SQLException("Không tính được tổng tiền dịch vụ");
        }
        boolean coHoaDon = false;
        try (PreparedStatement psChk = conn.prepareStatement("SELECT 1 FROM HoaDon WHERE PhieuKham_ID = ?")) {
            psChk.setInt(1, phieuKhamId);
            try (ResultSet rs = psChk.executeQuery()) {
                coHoaDon = rs.next();
            }
        }
        if (coHoaDon) {
            try (PreparedStatement psUpd = conn.prepareStatement("UPDATE HoaDon SET TongTien = ? WHERE PhieuKham_ID = ?")) {
                psUpd.setDouble(1, tong);
                psUpd.setInt(2, phieuKhamId);
                psUpd.executeUpdate();
            }
        } else {
            try (PreparedStatement psIns = conn.prepareStatement("INSERT INTO HoaDon (PhieuKham_ID, TongTien) VALUES (?, ?)")) {
                psIns.setInt(1, phieuKhamId);
                psIns.setDouble(2, tong);
                psIns.executeUpdate();
            }
        }
    }

    private double tinhTongTienChiTiet(Connection conn, int phieuKhamId) throws SQLException {
        try (PreparedStatement psSum = conn.prepareStatement(
                "SELECT ISNULL(SUM(DonGia * ISNULL(SoLuong, 1)), 0) AS Tong FROM ChiTietDichVu WHERE PhieuKham_ID = ?")) {
            psSum.setInt(1, phieuKhamId);
            try (ResultSet rsSum = psSum.executeQuery()) {
                if (rsSum.next()) {
                    return rsSum.getDouble("Tong");
                }
            }
        } catch (SQLException e) {
            try (PreparedStatement psSum = conn.prepareStatement(
                    "SELECT ISNULL(SUM(DonGia), 0) AS Tong FROM ChiTietDichVu WHERE PhieuKham_ID = ?")) {
                psSum.setInt(1, phieuKhamId);
                try (ResultSet rsSum = psSum.executeQuery()) {
                    if (rsSum.next()) {
                        return rsSum.getDouble("Tong");
                    }
                }
            }
        }
        return 0;
    }

    private void capNhatTrangThaiLichHen(Connection conn, int lichHenId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE LichHen SET TrangThai = N'Đã khám' WHERE LichHen_ID = ? AND TrangThai IN (N'Đang khám', N'Đã đến')")) {
            ps.setInt(1, lichHenId);
            ps.executeUpdate();
        }
    }

    /** Lưu nháp phiếu khám — không đổi trạng thái lịch hẹn, không tạo hóa đơn. */
    public void luuPhieuKhamNhap(PhieuKham pk) throws SQLException {
        if (pk == null || pk.getLichHenID() <= 0) {
            throw new SQLException("Thiếu mã lịch hẹn");
        }
        if (pk.getDanhSachDichVu() == null || pk.getDanhSachDichVu().isEmpty()) {
            throw new SQLException("Vui lòng chọn ít nhất một dịch vụ");
        }

        Connection conn = DBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            int phieuKhamId = timPhieuKhamIdTheoLichHen(pk.getLichHenID(), conn);
            if (phieuKhamId <= 0) {
                phieuKhamId = insertPhieuKhamRow(conn, pk);
            } else {
                updatePhieuKhamRow(conn, phieuKhamId, pk);
                try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM ChiTietDichVu WHERE PhieuKham_ID = ?")) {
                    psDel.setInt(1, phieuKhamId);
                    psDel.executeUpdate();
                }
            }

            if (phieuKhamId <= 0) {
                throw new SQLException("Không tạo được phiếu khám");
            }

            insertChiTietDichVuRows(conn, phieuKhamId, pk.getDanhSachDichVu());
            new LichHenDAO().syncChiTietLichHenFromClinical(conn, pk.getLichHenID(), pk.getDanhSachDichVu());
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    private int insertPhieuKhamRow(Connection conn, PhieuKham pk) throws SQLException {
        String sqlFull = "INSERT INTO PhieuKham (LichHen_ID, ChanDoan, NgayTao, LyDoKham, GhiChu) "
                + "VALUES (?, ?, GETDATE(), ?, ?)";
        String sqlShort = "INSERT INTO PhieuKham (LichHen_ID, ChanDoan, NgayTao) VALUES (?, ?, GETDATE())";
        try {
            return executePhieuInsert(conn, sqlFull, pk, true);
        } catch (SQLException ex) {
            System.err.println("[PhieuKhamDAO] insert full fallback: " + ex.getMessage());
            return executePhieuInsert(conn, sqlShort, pk, false);
        }
    }

    private int executePhieuInsert(Connection conn, String sql, PhieuKham pk, boolean extended) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pk.getLichHenID());
            ps.setNString(2, nullToEmpty(pk.getChanDoan()));
            if (extended) {
                ps.setNString(3, nullToEmpty(pk.getLyDoKham()));
                ps.setNString(4, nullToEmpty(pk.getGhiChu()));
            }
            ps.executeUpdate();
            return readNewIdentity(conn, ps);
        }
    }

    private void updatePhieuKhamRow(Connection conn, int phieuKhamId, PhieuKham pk) throws SQLException {
        String sqlFull = "UPDATE PhieuKham SET ChanDoan = ?, LyDoKham = ?, GhiChu = ? WHERE PhieuKham_ID = ?";
        String sqlShort = "UPDATE PhieuKham SET ChanDoan = ? WHERE PhieuKham_ID = ?";
        try {
            try (PreparedStatement ps = conn.prepareStatement(sqlFull)) {
                ps.setNString(1, nullToEmpty(pk.getChanDoan()));
                ps.setNString(2, nullToEmpty(pk.getLyDoKham()));
                ps.setNString(3, nullToEmpty(pk.getGhiChu()));
                ps.setInt(4, phieuKhamId);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            try (PreparedStatement ps = conn.prepareStatement(sqlShort)) {
                ps.setNString(1, nullToEmpty(pk.getChanDoan()));
                ps.setInt(2, phieuKhamId);
                ps.executeUpdate();
            }
        }
    }

    private void insertChiTietDichVuRows(Connection conn, int phieuKhamId, List<ChiTietDichVu> lines) throws SQLException {
        String sqlWithQty = "INSERT INTO ChiTietDichVu (PhieuKham_ID, DichVu_ID, DonGia, ViTriRang, SoLuong) VALUES (?, ?, ?, ?, ?)";
        String sqlNoQty = "INSERT INTO ChiTietDichVu (PhieuKham_ID, DichVu_ID, DonGia, ViTriRang) VALUES (?, ?, ?, ?)";
        boolean useQty = true;
        try (PreparedStatement probe = conn.prepareStatement("SELECT TOP 1 SoLuong FROM ChiTietDichVu")) {
            probe.executeQuery();
        } catch (SQLException e) {
            useQty = false;
        }

        String sql = useQty ? sqlWithQty : sqlNoQty;
        try (PreparedStatement psCt = conn.prepareStatement(sql)) {
            for (ChiTietDichVu ctdv : lines) {
                psCt.setInt(1, phieuKhamId);
                psCt.setInt(2, ctdv.getDichVuID());
                psCt.setDouble(3, ctdv.getDonGia());
                if (ctdv.getViTriRang() > 0) {
                    psCt.setInt(4, ctdv.getViTriRang());
                } else {
                    psCt.setNull(4, Types.INTEGER);
                }
                if (useQty) {
                    psCt.setInt(5, ctdv.getSoLuong() > 0 ? ctdv.getSoLuong() : 1);
                }
                psCt.addBatch();
            }
            psCt.executeBatch();
        }
    }

    private int readNewIdentity(Connection conn, PreparedStatement ps) throws SQLException {
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException ignored) {
        }
        try (PreparedStatement psId = conn.prepareStatement("SELECT CAST(SCOPE_IDENTITY() AS INT)");
             ResultSet rs = psId.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public PhieuKham layPhieuKhamTheoLichHen(int lichHenId) {
        String sqlPk = "SELECT PhieuKham_ID, LichHen_ID, ChanDoan, LyDoKham, GhiChu, NgayTao FROM PhieuKham WHERE LichHen_ID = ?";
        String sqlCt = "SELECT ctd.ChiTietDichVu_ID, ctd.PhieuKham_ID, ctd.DichVu_ID, ctd.DonGia, ctd.ViTriRang, "
                + "ISNULL(ctd.SoLuong, 1) AS SoLuong, dv.TenDichVu, dv.GiaTien, dv.TinhTheoRang "
                + "FROM ChiTietDichVu ctd JOIN DichVu dv ON dv.DichVu_ID = ctd.DichVu_ID "
                + "WHERE ctd.PhieuKham_ID = ? ORDER BY ctd.ChiTietDichVu_ID";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psPk = conn.prepareStatement(sqlPk)) {
            psPk.setInt(1, lichHenId);
            try (ResultSet rs = psPk.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                PhieuKham pk = new PhieuKham();
                int phieuKhamId = rs.getInt("PhieuKham_ID");
                pk.setPhieuKhamID(phieuKhamId);
                pk.setLichHenID(rs.getInt("LichHen_ID"));
                pk.setChanDoan(rs.getNString("ChanDoan"));
                try {
                    pk.setLyDoKham(rs.getNString("LyDoKham"));
                    pk.setGhiChu(rs.getNString("GhiChu"));
                } catch (SQLException ignored) {
                    pk.setLyDoKham("");
                    pk.setGhiChu("");
                }
                pk.setNgayTao(rs.getTimestamp("NgayTao"));

                List<ChiTietDichVu> lines = new ArrayList<>();
                try (PreparedStatement psCt = conn.prepareStatement(sqlCt)) {
                    psCt.setInt(1, phieuKhamId);
                    try (ResultSet rsCt = psCt.executeQuery()) {
                        while (rsCt.next()) {
                            lines.add(mapChiTietDichVu(rsCt));
                        }
                    }
                } catch (SQLException ex) {
                    String sqlCtLegacy = "SELECT ctd.ChiTietDichVu_ID, ctd.PhieuKham_ID, ctd.DichVu_ID, ctd.DonGia, ctd.ViTriRang, "
                            + "dv.TenDichVu, dv.GiaTien, dv.TinhTheoRang FROM ChiTietDichVu ctd "
                            + "JOIN DichVu dv ON dv.DichVu_ID = ctd.DichVu_ID WHERE ctd.PhieuKham_ID = ? "
                            + "ORDER BY ctd.ChiTietDichVu_ID";
                    try (PreparedStatement psCt = conn.prepareStatement(sqlCtLegacy)) {
                        psCt.setInt(1, phieuKhamId);
                        try (ResultSet rsCt = psCt.executeQuery()) {
                            while (rsCt.next()) {
                                ChiTietDichVu ctd = mapChiTietDichVu(rsCt);
                                ctd.setSoLuong(1);
                                lines.add(ctd);
                            }
                        }
                    }
                }
                pk.setDanhSachDichVu(lines);
                return pk;
            }
        } catch (SQLException e) {
            System.err.println("[PhieuKhamDAO] layPhieuKhamTheoLichHen: " + e.getMessage());
        }
        return null;
    }

    private ChiTietDichVu mapChiTietDichVu(ResultSet rsCt) throws SQLException {
        ChiTietDichVu ctd = new ChiTietDichVu();
        ctd.setChiTietDichVuID(rsCt.getInt("ChiTietDichVu_ID"));
        ctd.setPhieuKhamID(rsCt.getInt("PhieuKham_ID"));
        ctd.setDichVuID(rsCt.getInt("DichVu_ID"));
        ctd.setDonGia(rsCt.getDouble("DonGia"));
        ctd.setViTriRang(rsCt.getInt("ViTriRang"));
        try {
            ctd.setSoLuong(rsCt.getInt("SoLuong"));
        } catch (SQLException e) {
            ctd.setSoLuong(1);
        }
        DichVu dv = new DichVu();
        dv.setDichVuID(rsCt.getInt("DichVu_ID"));
        dv.setTenDichVu(rsCt.getNString("TenDichVu"));
        dv.setGiaTien(rsCt.getDouble("GiaTien"));
        dv.setTinhTheoRang(rsCt.getBoolean("TinhTheoRang"));
        ctd.setDichVu(dv);
        return ctd;
    }

    public boolean damBaoHoaDonChoLichHen(int lichHenId) {
        try {
            int phieuKhamId = timPhieuKhamIdTheoLichHen(lichHenId, null);
            if (phieuKhamId <= 0) {
                phieuKhamId = taoPhieuKhamTuDatHen(lichHenId);
            }
            if (phieuKhamId <= 0) {
                return false;
            }
            return taoHoaDonNeuChuaCo(phieuKhamId);
        } catch (SQLException e) {
            System.err.println("[PhieuKhamDAO] damBaoHoaDonChoLichHen: " + e.getMessage());
            return false;
        }
    }

    /** Tạo phiếu khám + chi tiết từ dịch vụ đặt lịch nếu bác sĩ chưa lưu phiếu. */
    public int taoPhieuKhamTuDatHen(int lichHenId) throws SQLException {
        if (lichHenId <= 0) {
            return -1;
        }
        int existing = timPhieuKhamIdTheoLichHen(lichHenId, null);
        if (existing > 0) {
            return existing;
        }
        String sqlItems = "SELECT ctlh.DichVu_ID, ISNULL(ctlh.SoLuong, 1) AS SoLuong, dv.GiaTien "
                + "FROM ChiTietLichHen ctlh JOIN DichVu dv ON dv.DichVu_ID = ctlh.DichVu_ID "
                + "WHERE ctlh.LichHen_ID = ?";
        List<ChiTietDichVu> lines = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlItems)) {
            ps.setInt(1, lichHenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietDichVu ctd = new ChiTietDichVu();
                    ctd.setDichVuID(rs.getInt("DichVu_ID"));
                    ctd.setSoLuong(rs.getInt("SoLuong"));
                    ctd.setDonGia(rs.getDouble("GiaTien"));
                    ctd.setViTriRang(0);
                    lines.add(ctd);
                }
            }
        }
        if (lines.isEmpty()) {
            return -1;
        }
        PhieuKham pk = new PhieuKham();
        pk.setLichHenID(lichHenId);
        pk.setChanDoan("");
        pk.setLyDoKham("");
        pk.setGhiChu("");
        pk.setDanhSachDichVu(lines);
        luuPhieuKhamNhap(pk);
        return timPhieuKhamIdTheoLichHen(lichHenId, null);
    }

    private boolean taoHoaDonNeuChuaCo(int phieuKhamId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psChk = conn.prepareStatement("SELECT 1 FROM HoaDon WHERE PhieuKham_ID = ?")) {
            psChk.setInt(1, phieuKhamId);
            try (ResultSet rs = psChk.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
            double tong = 0;
            try (PreparedStatement psSum = conn.prepareStatement(
                    "SELECT ISNULL(SUM(DonGia * ISNULL(SoLuong, 1)), 0) AS Tong FROM ChiTietDichVu WHERE PhieuKham_ID = ?")) {
                psSum.setInt(1, phieuKhamId);
                try (ResultSet rsSum = psSum.executeQuery()) {
                    if (rsSum.next()) {
                        tong = rsSum.getDouble("Tong");
                    }
                }
            } catch (SQLException e) {
                try (PreparedStatement psSum = conn.prepareStatement(
                        "SELECT ISNULL(SUM(DonGia), 0) AS Tong FROM ChiTietDichVu WHERE PhieuKham_ID = ?")) {
                    psSum.setInt(1, phieuKhamId);
                    try (ResultSet rsSum = psSum.executeQuery()) {
                        if (rsSum.next()) {
                            tong = rsSum.getDouble("Tong");
                        }
                    }
                }
            }
            if (tong <= 0) {
                return false;
            }
            return insertHoaDonRow(conn, phieuKhamId, tong);
        }
    }

    /** INSERT HoaDon chờ thanh toán (NgayThanhToan = NULL). Không dùng cột TrangThai. */
    boolean insertHoaDonRow(Connection conn, int phieuKhamId, double tongTien) throws SQLException {
        String[] variants = {
            "INSERT INTO HoaDon (PhieuKham_ID, TongTien) VALUES (?, ?)",
            "INSERT INTO HoaDon (PhieuKham_ID, LeTan_ID, TongTien) VALUES (?, NULL, ?)",
            "INSERT INTO HoaDon (PhieuKham_ID, LeTan_ID, TongTien) VALUES (?, 1, ?)"
        };
        SQLException last = null;
        for (String sql : variants) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, phieuKhamId);
                ps.setDouble(2, tongTien);
                if (ps.executeUpdate() > 0) {
                    return true;
                }
            } catch (SQLException e) {
                last = e;
            }
        }
        if (last != null) {
            throw last;
        }
        return false;
    }

    /** Cập nhật hoặc tạo HoaDon khi lễ tân thanh toán (đánh dấu bằng NgayThanhToan). */
    boolean upsertHoaDonThanhToan(int phieuKhamId, double tongTien, String phuongThuc, Integer leTanId) throws SQLException {
        String pt = (phuongThuc == null || phuongThuc.isBlank()) ? "Tiền mặt" : phuongThuc.trim();
        try (Connection conn = DBConnection.getConnection()) {
            if (updateHoaDonThanhToan(conn, phieuKhamId, tongTien, pt, true, leTanId)) {
                return true;
            }
            if (updateHoaDonThanhToan(conn, phieuKhamId, tongTien, pt, false, leTanId)) {
                return true;
            }
            return insertHoaDonDaThanhToan(conn, phieuKhamId, tongTien, pt, leTanId);
        }
    }

    private boolean updateHoaDonThanhToan(Connection conn, int phieuKhamId, double tongTien, String pt,
            boolean withMethod, Integer leTanId) throws SQLException {
        boolean hasLeTan = leTanId != null && leTanId > 0;
        String sql;
        if (hasLeTan && withMethod) {
            sql = "UPDATE HoaDon SET TongTien = ?, PhuongThucThanhToan = ?, NgayThanhToan = GETDATE(), LeTan_ID = ? WHERE PhieuKham_ID = ?";
        } else if (hasLeTan) {
            sql = "UPDATE HoaDon SET TongTien = ?, NgayThanhToan = GETDATE(), LeTan_ID = ? WHERE PhieuKham_ID = ?";
        } else if (withMethod) {
            sql = "UPDATE HoaDon SET TongTien = ?, PhuongThucThanhToan = ?, NgayThanhToan = GETDATE() WHERE PhieuKham_ID = ?";
        } else {
            sql = "UPDATE HoaDon SET TongTien = ?, NgayThanhToan = GETDATE() WHERE PhieuKham_ID = ?";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;
            ps.setDouble(i++, tongTien);
            if (withMethod) {
                ps.setNString(i++, pt);
            }
            if (hasLeTan) {
                ps.setInt(i++, leTanId);
            }
            ps.setInt(i, phieuKhamId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (withMethod && hasLeTan) {
                return false;
            }
            if (withMethod || hasLeTan) {
                return false;
            }
            throw e;
        }
    }

    private boolean insertHoaDonDaThanhToan(Connection conn, int phieuKhamId, double tongTien, String pt, Integer leTanId)
            throws SQLException {
        boolean hasLeTan = leTanId != null && leTanId > 0;
        String[] variants;
        if (hasLeTan) {
            variants = new String[] {
                "INSERT INTO HoaDon (PhieuKham_ID, LeTan_ID, TongTien, PhuongThucThanhToan, NgayThanhToan) VALUES (?, ?, ?, ?, GETDATE())",
                "INSERT INTO HoaDon (PhieuKham_ID, LeTan_ID, TongTien, NgayThanhToan) VALUES (?, ?, ?, GETDATE())"
            };
        } else {
            variants = new String[] {
                "INSERT INTO HoaDon (PhieuKham_ID, TongTien, PhuongThucThanhToan, NgayThanhToan) VALUES (?, ?, ?, GETDATE())",
                "INSERT INTO HoaDon (PhieuKham_ID, TongTien, NgayThanhToan) VALUES (?, ?, GETDATE())"
            };
        }
        SQLException last = null;
        for (String sql : variants) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int i = 1;
                ps.setInt(i++, phieuKhamId);
                if (hasLeTan) {
                    ps.setInt(i++, leTanId);
                }
                ps.setDouble(i++, tongTien);
                if (sql.contains("PhuongThucThanhToan")) {
                    ps.setNString(i, pt);
                }
                if (ps.executeUpdate() > 0) {
                    return true;
                }
            } catch (SQLException e) {
                last = e;
            }
        }
        if (last != null) {
            throw last;
        }
        return false;
    }

    /** Chỉ dùng khi chắc chắn chưa có HoaDon — xóa HoaDon trước rồi mới xóa phiếu. */
    public void xoaPhieuKhamTheoLichHen(int lichHenId) throws SQLException {
        int id = timPhieuKhamIdTheoLichHen(lichHenId, null);
        if (id <= 0) {
            return;
        }
        Connection conn = DBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            try (PreparedStatement psHd = conn.prepareStatement("DELETE FROM HoaDon WHERE PhieuKham_ID = ?")) {
                psHd.setInt(1, id);
                psHd.executeUpdate();
            }
            try (PreparedStatement psDt = conn.prepareStatement("DELETE FROM ChiTietDonThuoc WHERE PhieuKham_ID = ?")) {
                psDt.setInt(1, id);
                psDt.executeUpdate();
            } catch (SQLException ignored) {
            }
            try (PreparedStatement psCt = conn.prepareStatement("DELETE FROM ChiTietDichVu WHERE PhieuKham_ID = ?")) {
                psCt.setInt(1, id);
                psCt.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PhieuKham WHERE PhieuKham_ID = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    private int timPhieuKhamIdTheoLichHen(int lichHenId, Connection extConn) throws SQLException {
        String sql = "SELECT PhieuKham_ID FROM PhieuKham WHERE LichHen_ID = ?";
        boolean external = extConn != null;
        Connection conn = external ? extConn : DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lichHenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PhieuKham_ID");
                }
            }
        } finally {
            if (!external && conn != null) {
                conn.close();
            }
        }
        return -1;
    }

    private SQLServerDataTable buildTvp(List<ChiTietDichVu> list) throws SQLException {
        SQLServerDataTable tvpTable = new SQLServerDataTable();
        tvpTable.addColumnMetadata("DichVu_ID", Types.INTEGER);
        tvpTable.addColumnMetadata("DonGia", Types.DOUBLE);
        tvpTable.addColumnMetadata("ViTriRang", Types.INTEGER);
        tvpTable.addColumnMetadata("SoLuong", Types.INTEGER);
        for (ChiTietDichVu ctdv : list) {
            tvpTable.addRow(
                    ctdv.getDichVuID(),
                    ctdv.getDonGia(),
                    ctdv.getViTriRang(),
                    ctdv.getSoLuong() > 0 ? ctdv.getSoLuong() : 1);
        }
        return tvpTable;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
