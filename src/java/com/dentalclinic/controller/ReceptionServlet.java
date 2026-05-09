package com.dentalclinic.controller;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.dao.LichHenDAO.ReceptionTotals;
import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.LichHen;
import java.io.IOException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/reception-dashboard")
public class ReceptionServlet extends HttpServlet {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("d/M/yyyy");

    private final LichHenDAO lhDAO = new LichHenDAO();
    private final BacSiDAO bacSiDAO = new BacSiDAO();

    /** Chuẩn hoá trạng thái khớp bộ lọc / thống kê */
    static String normalizedStatusFilterKey(String trangThai) {
        if (trangThai == null) return "pending";
        switch (trangThai.trim()) {
            case "Đã hủy":
                return "cancelled";
            case "Đã thanh toán":
            case "Hoàn thành":
                return "completed";
            case "Đã duyệt":
            case "Đã xác nhận":
            case "Đã đến":
                return "confirmed";
            case "Chờ duyệt":
            case "Chờ xác nhận":
                return "pending";
            default:
                return "pending";
        }
    }

    private static String trimParam(String param) {
        return param == null ? "" : param.trim();
    }

    /** loc: all | pending | confirmed | completed */
    private static String normalizeLoc(String param) {
        if (param == null || param.isBlank()) return "all";
        String p = param.trim().toLowerCase();
        if ("pending".equals(p) || "cho".equals(p)) return "pending";
        if ("confirmed".equals(p) || "duyet".equals(p)) return "confirmed";
        if ("completed".equals(p) || "hoanthanh".equals(p)) return "completed";
        if ("all".equals(p) || "tatca".equals(p)) return "all";
        return "all";
    }

    /** view: day | week | month | year */
    private static String normalizeView(String param) {
        if (param == null || param.isBlank()) return "day";
        String v = param.trim().toLowerCase();
        switch (v) {
            case "week":
            case "tuan":
                return "week";
            case "month":
            case "thang":
                return "month";
            case "year":
            case "nam":
                return "year";
            default:
                return "day";
        }
    }

    /** Tuần: thứ 2 → chủ nhật (VN) chứa ngày neo */
    private static LocalDate startOfWeekMon(LocalDate d) {
        return d.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private static LocalDate endOfWeekSun(LocalDate d) {
        return startOfWeekMon(d).plusDays(6);
    }

    /** Nhãn thanh toolbar */
    private static String buildToolbarLabel(String viewMode, LocalDate anchor) {
        String[] vn = {"", "Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy", "Chủ nhật"};
        switch (viewMode) {
            case "week":
                LocalDate mon = startOfWeekMon(anchor);
                LocalDate sun = endOfWeekSun(anchor);
                return "Tuần: " + mon.format(DMY) + " – " + sun.format(DMY);
            case "month":
                return "Tháng " + anchor.getMonthValue() + "/" + anchor.getYear();
            case "year":
                return "Năm " + anchor.getYear();
            default:
                int dv = anchor.getDayOfWeek().getValue();
                return vn[dv] + ", " + anchor.format(DMY);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LocalDate anchor = LocalDate.now();
        String ngayParam = request.getParameter("ngay");
        if (ngayParam != null && !ngayParam.isBlank()) {
            try {
                anchor = LocalDate.parse(ngayParam.trim(), ISO);
            } catch (DateTimeParseException e) {
                anchor = LocalDate.now();
            }
        }

        String loc = normalizeLoc(request.getParameter("loc"));
        String viewMode = normalizeView(request.getParameter("view"));
        ReceptionTotals totals = lhDAO.countReceptionDashboardTotals();
        /** hist=1: thẻ Tổng — danh sách = mọi lịch trong CSDL; không dùng view tuần-tháng. */
        boolean histTotal = "1".equalsIgnoreCase(trimParam(request.getParameter("hist")));
        boolean listFullTotalChip = "all".equals(loc) && histTotal;
        boolean listOneStatusHistory =
                ("pending".equals(loc) || "confirmed".equals(loc) || "completed".equals(loc));
        if (listFullTotalChip || listOneStatusHistory) {
            viewMode = "day";
        }

        List<LichHen> list;
        LocalDate rangeStart = anchor;
        LocalDate rangeEnd = anchor;

        if (listFullTotalChip) {
            list = lhDAO.getLichHenReceptionByLocAllTime("all", 15000);
        } else if (listOneStatusHistory) {
            list = lhDAO.getLichHenReceptionByLocAllTime(loc, 800);
        } else {
            switch (viewMode) {
                case "week":
                    rangeStart = startOfWeekMon(anchor);
                    rangeEnd = endOfWeekSun(anchor);
                    list = lhDAO.getLichHenReceptionBetweenDates(Date.valueOf(rangeStart), Date.valueOf(rangeEnd), 2500);
                    break;
                case "month":
                    rangeStart = anchor.withDayOfMonth(1);
                    rangeEnd = anchor.withDayOfMonth(anchor.lengthOfMonth());
                    list = lhDAO.getLichHenReceptionBetweenDates(Date.valueOf(rangeStart), Date.valueOf(rangeEnd), 5000);
                    break;
                case "year":
                    rangeStart = LocalDate.of(anchor.getYear(), 1, 1);
                    rangeEnd = LocalDate.of(anchor.getYear(), 12, 31);
                    list = lhDAO.getLichHenReceptionBetweenDates(Date.valueOf(rangeStart), Date.valueOf(rangeEnd), 10000);
                    break;
                default:
                    rangeStart = anchor;
                    rangeEnd = anchor;
                    list = lhDAO.getLichHenByNgayKham(Date.valueOf(anchor));
                    break;
            }
        }

        List<BacSi> doctors = bacSiDAO.getAllForDisplay();

        request.setAttribute("allLichHen", list);
        request.setAttribute("danhSachBacSi", doctors);
        request.setAttribute("selectedDateNgay", anchor.format(ISO));

        request.setAttribute("statTotal", totals.total);
        request.setAttribute("statPending", totals.pending);
        request.setAttribute("statConfirmed", totals.confirmed);
        request.setAttribute("statCompleted", totals.completed);

        request.setAttribute("receptionViewMode", viewMode);
        request.setAttribute("receptionLoc", loc);
        request.setAttribute("receptionHistBanner", listFullTotalChip);
        request.setAttribute(
                "receptionToolbarLabel",
                listOneStatusHistory
                        ? buildToolbarLabel("day", anchor)
                        : (listFullTotalChip
                                ? "Toàn hệ thống đến ngày hiện tại"
                                : buildToolbarLabel(viewMode, anchor)));

        request.getRequestDispatcher("/reception/index.jsp").forward(request, response);
    }
}
