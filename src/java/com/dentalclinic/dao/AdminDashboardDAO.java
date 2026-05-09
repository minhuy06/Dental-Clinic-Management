package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminDashboardDAO {

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n");
    }

    private static String roleToUi(String role) {
        if (role == null) return "staff";
        switch (role.trim()) {
            case "Quản trị viên": return "admin";
            case "Bệnh nhân": return "customer";
            case "Bác sĩ": return "doctor";
            default: return "staff";
        }
    }

    private static String statusToUi(String st) {
        return "Hoạt động".equalsIgnoreCase(st) ? "active" : "inactive";
    }

    private static String genderToUi(Boolean gender) {
        if (gender == null) return "";
        return gender ? "male" : "female";
    }

    private static String serviceCatByChuyenKhoa(int ck) {
        switch (ck) {
            case 1: return "kham";
            case 2: return "chinh-nha";
            case 3: return "tham-my";
            case 4: return "phau-thuat";
            case 5: return "tre-em";
            default: return "kham";
        }
    }

    public String getServicesJson() {
        String sql = "SELECT DichVu_ID, TenDichVu, GiaTien, ThoiLuongDuKien, ChuyenKhoa_ID, TinhTheoRang FROM DichVu ORDER BY DichVu_ID";
        StringBuilder sb = new StringBuilder("[");
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            boolean first = true;
            while (rs.next()) {
                if (!first) sb.append(",");
                first = false;
                int ck = rs.getInt("ChuyenKhoa_ID");
                boolean per = rs.getBoolean("TinhTheoRang");
                sb.append("{")
                  .append("\"id\":").append(rs.getInt("DichVu_ID")).append(",")
                  .append("\"name\":\"").append(esc(rs.getNString("TenDichVu"))).append("\",")
                  .append("\"desc\":\"").append(esc(rs.getNString("TenDichVu"))).append("\",")
                  .append("\"time\":\"").append(rs.getInt("ThoiLuongDuKien")).append(" phút\",")
                  .append("\"price\":").append((long) rs.getDouble("GiaTien")).append(",")
                  .append("\"cat\":\"").append(serviceCatByChuyenKhoa(ck)).append("\",")
                  .append("\"perUnit\":").append(per).append(",")
                  .append("\"unit\":\"").append(per ? "răng" : "").append("\",")
                  .append("\"status\":\"active\"")
                  .append("}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sb.append("]");
        return sb.toString();
    }

    public String getAccountsJson() {
        String sql = "SELECT tk.TaiKhoan_ID, tk.HoTen, tk.SoDienThoai, tk.VaiTro, tk.TrangThai, tk.NgaySinh, tk.GioiTinh, " +
                     "bs.TrinhDo, ck.TenChuyenKhoa " +
                     "FROM TaiKhoan tk " +
                     "LEFT JOIN BacSi bs ON bs.TaiKhoan_ID = tk.TaiKhoan_ID " +
                     "LEFT JOIN ChuyenKhoa ck ON ck.ChuyenKhoa_ID = bs.ChuyenKhoa_ID " +
                     "ORDER BY tk.TaiKhoan_ID";
        StringBuilder sb = new StringBuilder("[");
        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            boolean first = true;
            while (rs.next()) {
                if (!first) sb.append(",");
                first = false;
                Date dob = rs.getDate("NgaySinh");
                boolean hasGender = rs.getObject("GioiTinh") != null;
                String gender = hasGender ? genderToUi(rs.getBoolean("GioiTinh")) : "";
                sb.append("{")
                  .append("\"id\":").append(rs.getInt("TaiKhoan_ID")).append(",")
                  .append("\"name\":\"").append(esc(rs.getNString("HoTen"))).append("\",")
                  .append("\"username\":\"").append(esc(rs.getString("SoDienThoai"))).append("\",")
                  .append("\"role\":\"").append(roleToUi(rs.getNString("VaiTro"))).append("\",")
                  .append("\"phone\":\"").append(esc(rs.getString("SoDienThoai"))).append("\",")
                  .append("\"dob\":\"").append(dob == null ? "" : iso.format(dob)).append("\",")
                  .append("\"gender\":\"").append(gender).append("\",")
                  .append("\"specialty\":\"").append(esc(rs.getNString("TenChuyenKhoa"))).append("\",")
                  .append("\"degree\":\"").append(esc(rs.getNString("TrinhDo"))).append("\",")
                  .append("\"status\":\"").append(statusToUi(rs.getNString("TrangThai"))).append("\"")
                  .append("}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sb.append("]");
        return sb.toString();
    }

    public String getShiftsJson() {
        String sql = "SELECT lv.Lich_ID, lv.TaiKhoan_ID, tk.HoTen, lv.NgayLam, cl.TenCa, pk.TenPhong " +
                     "FROM LichLamViec lv " +
                     "JOIN TaiKhoan tk ON tk.TaiKhoan_ID = lv.TaiKhoan_ID " +
                     "JOIN CaLam cl ON cl.Ca_ID = lv.Ca_ID " +
                     "JOIN PhongKham pk ON pk.Phong_ID = lv.Phong_ID " +
                     "ORDER BY lv.NgayLam DESC, lv.Lich_ID DESC";
        StringBuilder sb = new StringBuilder("[");
        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            boolean first = true;
            while (rs.next()) {
                if (!first) sb.append(",");
                first = false;
                String tenCa = rs.getNString("TenCa");
                String shiftType = (tenCa != null && tenCa.toLowerCase().contains("chi")) ? "afternoon" : "morning";
                sb.append("{")
                  .append("\"id\":").append(rs.getInt("Lich_ID")).append(",")
                  .append("\"staffId\":").append(rs.getInt("TaiKhoan_ID")).append(",")
                  .append("\"staffName\":\"").append(esc(rs.getNString("HoTen"))).append("\",")
                  .append("\"shiftType\":\"").append(shiftType).append("\",")
                  .append("\"date\":\"").append(iso.format(rs.getDate("NgayLam"))).append("\",")
                  .append("\"room\":\"").append(esc(rs.getNString("TenPhong"))).append("\",")
                  .append("\"note\":\"\"")
                  .append("}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sb.append("]");
        return sb.toString();
    }

    public String getRevenueJson() {
        String txSql = "SELECT TOP 300 hd.HoaDon_ID, hd.NgayThanhToan, hd.TongTien, hd.PhuongThucThanhToan, " +
                       "COALESCE(tkbn.HoTen, N'Bệnh nhân') AS TenBenhNhan, " +
                       "COALESCE(tkbs.HoTen, N'Bác sĩ') AS TenBacSi, " +
                       "COALESCE(sv.TenDichVu, N'Dịch vụ') AS TenDichVu " +
                       "FROM HoaDon hd " +
                       "JOIN PhieuKham pk ON pk.PhieuKham_ID = hd.PhieuKham_ID " +
                       "LEFT JOIN LichHen lh ON lh.LichHen_ID = pk.LichHen_ID " +
                       "LEFT JOIN BenhNhan bn ON bn.BenhNhan_ID = lh.BenhNhan_ID " +
                       "LEFT JOIN TaiKhoan tkbn ON tkbn.TaiKhoan_ID = bn.TaiKhoan_ID " +
                       "LEFT JOIN BacSi bs ON bs.BacSi_ID = lh.BacSi_ID " +
                       "LEFT JOIN TaiKhoan tkbs ON tkbs.TaiKhoan_ID = bs.TaiKhoan_ID " +
                       "OUTER APPLY (SELECT TOP 1 dv.TenDichVu FROM ChiTietDichVu ctdv JOIN DichVu dv ON dv.DichVu_ID=ctdv.DichVu_ID WHERE ctdv.PhieuKham_ID = pk.PhieuKham_ID) sv " +
                       "WHERE hd.NgayThanhToan IS NOT NULL " +
                       "ORDER BY hd.NgayThanhToan DESC";

        List<Map<String, Object>> txs = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        long[] monthRevenue = new long[12];
        long[] monthAppt = new long[12];
        Map<String, Long> paymentMap = new HashMap<>();
        List<Map<String, Object>> byCat = new ArrayList<>();
        List<Map<String, Object>> topServices = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(txSql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("NgayThanhToan");
                long amount = (long) rs.getDouble("TongTien");
                if (ts != null) {
                    cal.setTime(ts);
                    if (cal.get(Calendar.YEAR) == currentYear) {
                        int m = cal.get(Calendar.MONTH);
                        monthRevenue[m] += amount;
                        monthAppt[m] += 1;
                    }
                }
                String pay = rs.getNString("PhuongThucThanhToan");
                if (pay == null || pay.isBlank()) pay = "Tiền mặt";
                paymentMap.put(pay, paymentMap.getOrDefault(pay, 0L) + amount);

                Map<String, Object> tx = new HashMap<>();
                tx.put("id", "GD" + rs.getInt("HoaDon_ID"));
                tx.put("date", ts == null ? "" : new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(ts));
                tx.put("patient", rs.getNString("TenBenhNhan"));
                tx.put("service", rs.getNString("TenDichVu"));
                tx.put("doctor", rs.getNString("TenBacSi"));
                tx.put("method", pay);
                tx.put("amount", amount);
                tx.put("appointments", 1);
                txs.add(tx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String catSql = "SELECT TOP 6 COALESCE(ck.TenChuyenKhoa, N'Khác') AS CatName, SUM(hd.TongTien) AS TotalRev " +
                        "FROM HoaDon hd " +
                        "JOIN PhieuKham pk ON pk.PhieuKham_ID = hd.PhieuKham_ID " +
                        "LEFT JOIN ChiTietDichVu ctdv ON ctdv.PhieuKham_ID = pk.PhieuKham_ID " +
                        "LEFT JOIN DichVu dv ON dv.DichVu_ID = ctdv.DichVu_ID " +
                        "LEFT JOIN ChuyenKhoa ck ON ck.ChuyenKhoa_ID = dv.ChuyenKhoa_ID " +
                        "WHERE hd.NgayThanhToan IS NOT NULL " +
                        "GROUP BY ck.TenChuyenKhoa ORDER BY TotalRev DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(catSql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", rs.getNString("CatName"));
                item.put("revenue", (long) rs.getDouble("TotalRev"));
                byCat.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String topSql = "SELECT TOP 5 dv.TenDichVu AS ServiceName, SUM(ctdv.SoLuong * ctdv.DonGia) AS TotalRev, " +
                        "COALESCE(ck.TenChuyenKhoa, N'Khác') AS CatName " +
                        "FROM ChiTietDichVu ctdv " +
                        "JOIN DichVu dv ON dv.DichVu_ID = ctdv.DichVu_ID " +
                        "LEFT JOIN ChuyenKhoa ck ON ck.ChuyenKhoa_ID = dv.ChuyenKhoa_ID " +
                        "JOIN PhieuKham pk ON pk.PhieuKham_ID = ctdv.PhieuKham_ID " +
                        "JOIN HoaDon hd ON hd.PhieuKham_ID = pk.PhieuKham_ID " +
                        "WHERE hd.NgayThanhToan IS NOT NULL " +
                        "GROUP BY dv.TenDichVu, ck.TenChuyenKhoa ORDER BY TotalRev DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(topSql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", rs.getNString("ServiceName"));
                item.put("cat", rs.getNString("CatName"));
                item.put("amount", (long) rs.getDouble("TotalRev"));
                topServices.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder("{");
        sb.append("\"months\":[");
        for (int i = 0; i < 12; i++) {
            if (i > 0) sb.append(",");
            sb.append("{\"month\":\"T").append(i + 1).append("\",\"revenue\":").append(monthRevenue[i]).append(",\"appointments\":").append(monthAppt[i]).append("}");
        }
        sb.append("],");
        sb.append("\"byCat\":[");
        String[] colors = new String[]{"#2563eb", "#8b5cf6", "#ef4444", "#f59e0b", "#10b981", "#14b8a6"};
        for (int i = 0; i < byCat.size(); i++) {
            if (i > 0) sb.append(",");
            Map<String, Object> c = byCat.get(i);
            sb.append("{\"name\":\"").append(esc((String) c.get("name"))).append("\",")
              .append("\"color\":\"").append(colors[i % colors.length]).append("\",")
              .append("\"revenue\":").append((Long) c.get("revenue")).append("}");
        }
        sb.append("],");
        sb.append("\"topServices\":[");
        for (int i = 0; i < topServices.size(); i++) {
            if (i > 0) sb.append(",");
            Map<String, Object> t = topServices.get(i);
            sb.append("{\"name\":\"").append(esc((String) t.get("name"))).append("\",")
              .append("\"cat\":\"").append(esc((String) t.get("cat"))).append("\",")
              .append("\"amount\":").append((Long) t.get("amount")).append("}");
        }
        sb.append("],");

        long totalPayment = 0L;
        for (Long v : paymentMap.values()) totalPayment += v;
        sb.append("\"payments\":[");
        boolean firstPay = true;
        for (Map.Entry<String, Long> en : paymentMap.entrySet()) {
            if (!firstPay) sb.append(",");
            firstPay = false;
            String method = en.getKey();
            String icon = "fa-money-bills";
            String color = "#10b981";
            String bg = "#d1fae5";
            String m = method.toLowerCase();
            if (m.contains("chuy")) { icon = "fa-building-columns"; color = "#2563eb"; bg = "#dbeafe"; }
            else if (m.contains("th")) { icon = "fa-credit-card"; color = "#8b5cf6"; bg = "#ede9fe"; }
            else if (m.contains("g")) { icon = "fa-calendar-days"; color = "#f59e0b"; bg = "#fef3c7"; }
            int pct = totalPayment <= 0 ? 0 : (int) Math.round((en.getValue() * 100.0) / totalPayment);
            sb.append("{\"method\":\"").append(esc(method)).append("\",")
              .append("\"icon\":\"").append(icon).append("\",")
              .append("\"color\":\"").append(color).append("\",")
              .append("\"bg\":\"").append(bg).append("\",")
              .append("\"pct\":").append(pct).append(",")
              .append("\"amount\":").append(en.getValue()).append("}");
        }
        sb.append("],");

        sb.append("\"txns\":[");
        int top = Math.min(10, txs.size());
        for (int i = 0; i < top; i++) {
            if (i > 0) sb.append(",");
            Map<String, Object> t = txs.get(i);
            sb.append("{\"id\":\"").append(esc((String) t.get("id"))).append("\",")
              .append("\"date\":\"").append(esc((String) t.get("date"))).append("\",")
              .append("\"patient\":\"").append(esc((String) t.get("patient"))).append("\",")
              .append("\"service\":\"").append(esc((String) t.get("service"))).append("\",")
              .append("\"doctor\":\"").append(esc((String) t.get("doctor"))).append("\",")
              .append("\"method\":\"").append(esc((String) t.get("method"))).append("\",")
              .append("\"amount\":").append((Long) t.get("amount")).append("}");
        }
        sb.append("],");

        sb.append("\"allTxns\":[");
        for (int i = 0; i < txs.size(); i++) {
            if (i > 0) sb.append(",");
            Map<String, Object> t = txs.get(i);
            sb.append("{\"id\":\"").append(esc((String) t.get("id"))).append("\",")
              .append("\"date\":\"").append(esc((String) t.get("date"))).append("\",")
              .append("\"patient\":\"").append(esc((String) t.get("patient"))).append("\",")
              .append("\"service\":\"").append(esc((String) t.get("service"))).append("\",")
              .append("\"doctor\":\"").append(esc((String) t.get("doctor"))).append("\",")
              .append("\"method\":\"").append(esc((String) t.get("method"))).append("\",")
              .append("\"amount\":").append((Long) t.get("amount")).append(",")
              .append("\"appointments\":1")
              .append("}");
        }
        sb.append("]");

        sb.append("}");
        return sb.toString();
    }
}
