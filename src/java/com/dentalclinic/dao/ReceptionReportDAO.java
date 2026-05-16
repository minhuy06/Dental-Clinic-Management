package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Báo cáo doanh thu lễ tân từ Hóa đơn + phiếu khám (schema script).
 */
public class ReceptionReportDAO {

    public static class RevenueTransaction {
        public String id;
        public String customer;
        public String service;
        public String date;
        public String method;
        public double amount;
    }

    /** Hóa đơn đã thanh toán; chỉ lịch sử do chính lễ tân thu (LeTan_ID). */
    public List<RevenueTransaction> fetchPaidInvoices(Integer leTanId) {
        List<RevenueTransaction> list = new ArrayList<>();
        if (!invoiceJoinChainAvailable() || leTanId == null || leTanId <= 0) {
            return list;
        }
        String sql =
                "SELECT hd.HoaDon_ID, tk.HoTen AS CustomerName, "
                + "ISNULL(xs.TenDichVu, N'Dịch vụ') AS TenDichVu, "
                + "CONVERT(date, hd.NgayThanhToan) AS PayDate, "
                + "CAST(hd.TongTien AS FLOAT) AS Amount, hd.PhuongThucThanhToan "
                + "FROM HoaDon hd "
                + "INNER JOIN PhieuKham pk ON pk.PhieuKham_ID = hd.PhieuKham_ID "
                + "INNER JOIN LichHen lh ON lh.LichHen_ID = pk.LichHen_ID "
                + "INNER JOIN BenhNhan bn ON bn.BenhNhan_ID = lh.BenhNhan_ID "
                + "INNER JOIN TaiKhoan tk ON tk.TaiKhoan_ID = bn.TaiKhoan_ID "
                + "OUTER APPLY ("
                + "  SELECT TOP 1 dv.TenDichVu "
                + "  FROM ChiTietDichVu ctd "
                + "  INNER JOIN DichVu dv ON dv.DichVu_ID = ctd.DichVu_ID "
                + "  WHERE ctd.PhieuKham_ID = pk.PhieuKham_ID ORDER BY ctd.ChiTietDichVu_ID) xs "
                + "WHERE hd.NgayThanhToan IS NOT NULL AND hd.LeTan_ID = ? "
                + "ORDER BY hd.NgayThanhToan DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leTanId);
            try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RevenueTransaction t = new RevenueTransaction();
                t.id = "HD-" + rs.getInt("HoaDon_ID");
                t.customer = rs.getNString("CustomerName") != null ? rs.getNString("CustomerName") : "";
                t.service = rs.getNString("TenDichVu") != null ? rs.getNString("TenDichVu") : "";
                Date d = rs.getDate("PayDate");
                t.date = d != null ? d.toLocalDate().toString() : "";
                String m = rs.getNString("PhuongThucThanhToan");
                if (m == null || m.trim().isEmpty()) {
                    m = "Tiền mặt";
                }
                if (m.length() > 80) {
                    m = m.substring(0, 80);
                }
                t.method = m;
                t.amount = rs.getDouble("Amount");
                list.add(t);
            }
            }
        } catch (SQLException e) {
            System.err.println("[ReceptionReportDAO] fetchPaidInvoices: " + e.getMessage());
        }
        return list;
    }

    private static boolean invoiceJoinChainAvailable() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'HoaDon'")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
            }
            try (PreparedStatement p2 = conn.prepareStatement(
                    "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'PhieuKham'");
                 ResultSet r2 = p2.executeQuery()) {
                return r2.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public Map<String, Object> buildDashboardPayload(List<RevenueTransaction> tx) {
        Map<String, Double> svcTotals = new LinkedHashMap<>();
        Map<String, Double> payTotals = new LinkedHashMap<>();
        double grand = 0;
        for (RevenueTransaction t : tx) {
            grand += t.amount;
            String sn = (t.service == null || t.service.trim().isEmpty()) ? "Khác" : t.service.trim();
            svcTotals.merge(sn, t.amount, Double::sum);
            String pm = normalizePaymentBucket(t.method);
            payTotals.merge(pm, t.amount, Double::sum);
        }

        List<Map.Entry<String, Double>> svcSorted = new ArrayList<>(svcTotals.entrySet());
        svcSorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<Map<String, Object>> serviceStats = new ArrayList<>();
        if (grand > 0 && !svcSorted.isEmpty()) {
            int i = 0;
            for (Map.Entry<String, Double> e : svcSorted) {
                double pct = Math.round(e.getValue() / grand * 1000.0) / 10.0;
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("name", e.getKey());
                row.put("percent", pct);
                row.put("amount", Math.round(e.getValue()));
                row.put("color", pickColor(i));
                serviceStats.add(row);
                i++;
            }
        }

        String[] icons = new String[]{"fas fa-university", "fas fa-money-bill-wave", "fas fa-credit-card", "fas fa-wallet"};
        List<Map<String, Object>> paymentStats = new ArrayList<>();
        if (grand > 0 && !payTotals.isEmpty()) {
            int idx = 0;
            for (Map.Entry<String, Double> e : payTotals.entrySet()) {
                double pct = Math.round(e.getValue() / grand * 1000.0) / 10.0;
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("name", e.getKey());
                row.put("icon", icons[idx % icons.length]);
                row.put("amount", Math.round(e.getValue()));
                row.put("percent", pct);
                row.put("color", pickColor(idx + 3));
                paymentStats.add(row);
                idx++;
            }
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("transactions", tx);
        out.put("serviceStats", serviceStats);
        out.put("paymentStats", paymentStats);
        out.put("totalRevenue", Math.round(grand));
        return out;
    }

    private static String normalizePaymentBucket(String method) {
        if (method == null) {
            return "Tiền mặt";
        }
        String u = method.toUpperCase(Locale.ROOT);
        if (u.contains("CHUY") || u.contains("CK") || u.contains("TRANSFER")) {
            return "Chuyển khoản";
        }
        if (u.contains("VISA") || u.contains("MASTER") || u.contains("THẺ")) {
            return "Thẻ Visa/Master";
        }
        return method.trim().isEmpty() ? "Tiền mặt" : method.trim();
    }

    private static String pickColor(int i) {
        String[] cols = {"#2563eb", "#10b981", "#f59e0b", "#8b5cf6", "#ec4899", "#06b6d4"};
        return cols[i % cols.length];
    }
}
