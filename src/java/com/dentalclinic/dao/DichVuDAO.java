package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.DichVu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DichVuDAO {

    private static boolean hasColumn(ResultSetMetaData md, String col) throws SQLException {
        for (int i = 1; i <= md.getColumnCount(); i++) {
            String label = md.getColumnLabel(i);
            if (label != null && label.equalsIgnoreCase(col)) return true;
        }
        return false;
    }

    public List<DichVu> getAll() {
        List<DichVu> list = new ArrayList<>();
        String sql = "SELECT DichVu_ID, TenDichVu, GiaTien, ThoiLuongDuKien, ChuyenKhoa_ID, TinhTheoRang FROM DichVu";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setDichVuID(rs.getInt("DichVu_ID"));
                dv.setTenDichVu(rs.getNString("TenDichVu"));
                dv.setGiaTien(rs.getDouble("GiaTien"));
                dv.setThoiLuongDuKien(hasColumn(md, "ThoiLuongDuKien") ? rs.getInt("ThoiLuongDuKien") : 30);
                dv.setChuyenKhoaID(hasColumn(md, "ChuyenKhoa_ID") ? rs.getInt("ChuyenKhoa_ID") : 0);
                dv.setTinhTheoRang(hasColumn(md, "TinhTheoRang") && rs.getBoolean("TinhTheoRang"));
                list.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalDurationMinutesByIds(List<Integer> dichVuIds) {
        if (dichVuIds == null || dichVuIds.isEmpty()) return 0;
        String placeholders = dichVuIds.stream().map(x -> "?").collect(Collectors.joining(","));
        String sql = "SELECT SUM(ISNULL(ThoiLuongDuKien, 30)) AS TotalMinutes FROM DichVu WHERE DichVu_ID IN (" + placeholders + ")";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            for (Integer id : dichVuIds) {
                ps.setInt(idx++, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("TotalMinutes");
                    return total > 0 ? total : 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalDurationMinutes(java.util.Map<Integer, Integer> qtyByDvId) {
        if (qtyByDvId == null || qtyByDvId.isEmpty()) return 0;
        int total = 0;
        String sql = "SELECT ISNULL(ThoiLuongDuKien, 30) FROM DichVu WHERE DichVu_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (java.util.Map.Entry<Integer, Integer> e : qtyByDvId.entrySet()) {
                if (e.getKey() == null || e.getKey() < 1) continue;
                int qty = e.getValue() == null || e.getValue() < 1 ? 1 : e.getValue();
                ps.setInt(1, e.getKey());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        total += rs.getInt(1) * qty;
                    } else {
                        total += 30 * qty;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public boolean createFromAdmin(String tenDichVu, double giaTien, int thoiLuongDuKien, int chuyenKhoaId, boolean tinhTheoRang) {
        String sql = "INSERT INTO DichVu (TenDichVu, GiaTien, ThoiLuongDuKien, ChuyenKhoa_ID, TinhTheoRang) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, tenDichVu);
            ps.setDouble(2, giaTien);
            ps.setInt(3, thoiLuongDuKien);
            ps.setInt(4, chuyenKhoaId);
            ps.setBoolean(5, tinhTheoRang);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(int dichVuId) {
        String sql = "DELETE FROM DichVu WHERE DichVu_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dichVuId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFromAdmin(int dichVuId, String tenDichVu, double giaTien, int thoiLuongDuKien, int chuyenKhoaId, boolean tinhTheoRang) {
        String sql = "UPDATE DichVu SET TenDichVu = ?, GiaTien = ?, ThoiLuongDuKien = ?, ChuyenKhoa_ID = ?, TinhTheoRang = ? WHERE DichVu_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, tenDichVu);
            ps.setDouble(2, giaTien);
            ps.setInt(3, thoiLuongDuKien);
            ps.setInt(4, chuyenKhoaId);
            ps.setBoolean(5, tinhTheoRang);
            ps.setInt(6, dichVuId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}