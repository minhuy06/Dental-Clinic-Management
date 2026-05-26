package com.dentalclinic.controller;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.dao.LeTanDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.dao.LichHenDAO.ReceptionTotals;
import com.dentalclinic.dao.PhongKhamDAO;
import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.PhongKham;
import com.dentalclinic.model.TaiKhoan;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.dentalclinic.service.LichHenService;

@WebServlet("/reception-dashboard")
public class ReceptionServlet extends HttpServlet {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("d/M/yyyy");

    private final LichHenDAO lhDAO = new LichHenDAO();
    private final BacSiDAO bacSiDAO = new BacSiDAO();
    private final LeTanDAO leTanDAO = new LeTanDAO();
    private final DichVuDAO dichVuDAO = new DichVuDAO();
    private final PhongKhamDAO phongKhamDAO = new PhongKhamDAO();
    private final LichHenService lichHenService = new LichHenService();

    private static String jsonEscape(String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\r", "\\r")
                  .replace("\n", "\\n")
                  .replace("\t", "\\t");
    }

    private static String buildServiceListJson(List<DichVu> services) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < services.size(); i++) {
            DichVu dv = services.get(i);
            if (i > 0) sb.append(",");
            sb.append("{")
              .append("\"id\":").append(dv.getDichVuID()).append(",")
              .append("\"name\":\"").append(jsonEscape(dv.getTenDichVu())).append("\",")
              .append("\"price\":").append((long) dv.getGiaTien()).append(",")
              .append("\"time\":\"").append(dv.getThoiLuongDuKien()).append(" phút\",")
              .append("\"perUnit\":").append(dv.isTinhTheoRang());
            if (dv.isTinhTheoRang()) {
                sb.append(",\"unit\":\"răng\"");
            }
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String buildRoomListJson(List<PhongKham> rooms) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < rooms.size(); i++) {
            PhongKham room = rooms.get(i);
            if (i > 0) sb.append(",");
            sb.append("{")
              .append("\"id\":").append(room.getPhongID()).append(",")
              .append("\"name\":\"").append(jsonEscape(room.getTenPhong())).append("\"")
              .append("}");
        }
        sb.append("]");
        return sb.toString();
    }

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
            case "Đang khám":
                return "confirmed";
            case "Đã khám":
                return "examined";
            case "Đã hoàn thành":
                return "completed";
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

    private Integer resolveLeTanId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null || user.getTaiKhoanID() <= 0) {
            return null;
        }
        return leTanDAO.findLeTanIdByTaiKhoanId(user.getTaiKhoanID());
    }

    private static String firstNonBlank(String... values) {
        if (values == null) return "";
        for (String v : values) {
            if (v != null && !v.isBlank()) return v.trim();
        }
        return "";
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

        if (list != null && !list.isEmpty()) {
            lhDAO.attachTenDichVuForAppointments(list);
            lhDAO.attachTrangThaiHoaDonForAppointments(list);
        }

        List<BacSi> doctors = bacSiDAO.getAllForDisplay();
        List<DichVu> services = dichVuDAO.getAll();
        List<PhongKham> rooms = phongKhamDAO.getAll();

        request.setAttribute("allLichHen", list);
        request.setAttribute("danhSachBacSi", doctors);
        request.setAttribute("danhSachPhongKham", rooms);
        request.setAttribute("serviceListJson", buildServiceListJson(services));
        request.setAttribute("roomListJson", buildRoomListJson(rooms));
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = trimParam(request.getParameter("action"));
        if (!action.isEmpty()) {
            response.setContentType("application/json; charset=UTF-8");
            try {
                if ("approve".equalsIgnoreCase(action)) {
                    int lichHenId = Integer.parseInt(trimParam(request.getParameter("lichHenId")));
                    // DB constraint nhận trạng thái "Đã xác nhận" (UI hiển thị "Đã duyệt")
                    boolean ok = lhDAO.updateTrangThai(lichHenId, "Đã xác nhận");
                    if (!ok) throw new IllegalStateException("Không cập nhật được trạng thái.");
                    response.getWriter().write("{\"success\":true}");
                    return;
                }
                if ("set-status".equalsIgnoreCase(action)) {
                    int lichHenId = Integer.parseInt(trimParam(request.getParameter("lichHenId")));
                    String statusUi = trimParam(request.getParameter("status"));
                    String dbStatus = "Đã xác nhận";
                    if ("pending".equalsIgnoreCase(statusUi)) {
                        dbStatus = "Chờ xác nhận";
                    } else if ("approved".equalsIgnoreCase(statusUi) || "confirmed".equalsIgnoreCase(statusUi)) {
                        dbStatus = "Đã xác nhận";
                    } else if ("arrived".equalsIgnoreCase(statusUi)) {
                        dbStatus = "Đã đến";
                    } else if ("cancelled".equalsIgnoreCase(statusUi)) {
                        dbStatus = "Đã hủy";
                    }
                    boolean ok = lhDAO.updateTrangThai(lichHenId, dbStatus);
                    if (!ok) throw new IllegalStateException("Không cập nhật được trạng thái.");
                    response.getWriter().write("{\"success\":true}");
                    return;
                }
                if ("pay".equalsIgnoreCase(action)) {
                    int lichHenId = Integer.parseInt(trimParam(request.getParameter("lichHenId")));
                    String paymentMethod = firstNonBlank(
                            request.getParameter("paymentMethod"),
                            request.getParameter("phuongThucThanhToan"));
                    if (paymentMethod.isBlank()) {
                        paymentMethod = "Tiền mặt";
                    }
                    double tongTien = 0;
                    String tongRaw = firstNonBlank(
                            request.getParameter("totalAmount"),
                            request.getParameter("tongTien"));
                    if (!tongRaw.isBlank()) {
                        tongTien = Double.parseDouble(tongRaw.replace(",", "").trim());
                    }
                    if (tongTien <= 0) {
                        LichHenDAO.ReceptionPaymentDetail detail = lhDAO.getReceptionPaymentDetail(lichHenId);
                        if (detail != null) {
                            tongTien = detail.subtotal * 1.1;
                        }
                    }
                    if (tongTien <= 0) {
                        throw new IllegalStateException("Không xác định được tổng tiền thanh toán.");
                    }
                    Integer leTanId = resolveLeTanId(request);
                    String payError = lhDAO.xacNhanThanhToan(lichHenId, paymentMethod, tongTien, leTanId);
                    if (payError != null) {
                        throw new IllegalStateException(payError);
                    }
                    response.getWriter().write("{\"success\":true}");
                    return;
                }
                if ("delete".equalsIgnoreCase(action)) {
                    int lichHenId = Integer.parseInt(trimParam(request.getParameter("lichHenId")));
                    boolean ok = lhDAO.deleteById(lichHenId);
                    if (!ok) throw new IllegalStateException("Không xóa được lịch hẹn.");
                    response.getWriter().write("{\"success\":true}");
                    return;
                }
                if ("get-detail".equalsIgnoreCase(action)) {
                    int lichHenId = Integer.parseInt(trimParam(request.getParameter("lichHenId")));
                    LichHenDAO.ReceptionBookingDetail d = lhDAO.getReceptionDetailById(lichHenId);
                    if (d == null) throw new IllegalStateException("Không tìm thấy lịch hẹn.");
                    StringBuilder sb = new StringBuilder();
                    sb.append("{")
                      .append("\"success\":true,")
                      .append("\"id\":").append(d.lichHenId).append(",")
                      .append("\"patientName\":\"").append(jsonEscape(d.patientName)).append("\",")
                      .append("\"patientPhone\":\"").append(jsonEscape(d.patientPhone)).append("\",")
                      .append("\"ngayKham\":\"").append(d.ngayKham == null ? "" : d.ngayKham.toString()).append("\",")
                      .append("\"gioKham\":\"").append(d.gioKham == null ? "" : d.gioKham.toString()).append("\",")
                      .append("\"bacSiId\":").append(d.bacSiId).append(",")
                      .append("\"phongId\":").append(d.phongId).append(",")
                      .append("\"ghiChu\":\"").append(jsonEscape(d.ghiChu)).append("\",")
                      .append("\"dichVuIds\":[");
                    for (int i = 0; i < d.dichVuIds.size(); i++) {
                        if (i > 0) sb.append(",");
                        sb.append(d.dichVuIds.get(i));
                    }
                    sb.append("],\"dichVuItems\":[");
                    for (int i = 0; i < d.dichVuItems.size(); i++) {
                        LichHenDAO.DichVuQty it = d.dichVuItems.get(i);
                        if (i > 0) sb.append(",");
                        sb.append("{\"id\":").append(it.id).append(",\"quantity\":").append(it.quantity).append("}");
                    }
                    sb.append("]}");
                    response.getWriter().write(sb.toString());
                    return;
                }
                if ("update-booking".equalsIgnoreCase(action)) {
                    int lichHenId = Integer.parseInt(trimParam(request.getParameter("lichHenId")));
                    String uNgay = firstNonBlank(request.getParameter("ngayKham"), request.getParameter("ngay_kham"));
                    String uGio = firstNonBlank(request.getParameter("gioKham"), request.getParameter("gio_kham"));
                    String uName = firstNonBlank(request.getParameter("patientName"), request.getParameter("tenBenhNhan"), request.getParameter("hoTen"));
                    String uPhone = firstNonBlank(request.getParameter("patientPhone"), request.getParameter("soDienThoai"), request.getParameter("phone"));
                    String uPhong = trimParam(request.getParameter("phongId"));
                    String uBs = trimParam(request.getParameter("bacSiId"));
                    String[] dvRaw = request.getParameterValues("dichVu");
                    if (dvRaw == null || dvRaw.length == 0) dvRaw = request.getParameterValues("dichVu[]");
                    if (uNgay.isBlank() || uGio.isBlank() || uName.isBlank() || uPhone.isBlank() || uPhong.isBlank() || uBs.isBlank()) {
                        throw new IllegalArgumentException("Thiếu thông tin cập nhật.");
                    }
                    List<Integer> dvIds = new ArrayList<>();
                    if (dvRaw != null) {
                        for (String x : dvRaw) dvIds.add(Integer.parseInt(x));
                    }
                    int benhNhanId = lichHenService.resolveOrCreateDeskPatient(uName, uPhone);
                    if (benhNhanId <= 0) throw new IllegalStateException("Không thể lưu thông tin bệnh nhân.");
                    String normalizedTime = uGio.length() == 5 ? (uGio + ":00") : uGio;
                    Map<Integer, Integer> qtyByDv = new LinkedHashMap<>();
                    for (Integer dvId : dvIds) {
                        if (dvId == null) continue;
                        qtyByDv.put(dvId, qtyByDv.getOrDefault(dvId, 0) + 1);
                    }
                    LocalDate ngayCapNhat = LocalDate.parse(uNgay);
                    LocalTime gioCapNhat = LocalTime.parse(normalizedTime);
                    if (lichHenService.hasPatientTimeConflict(benhNhanId, ngayCapNhat, gioCapNhat, qtyByDv, lichHenId)) {
                        throw new IllegalStateException(LichHenService.MSG_PATIENT_TIME_CONFLICT);
                    }
                    LichHen lh = new LichHen();
                    lh.setLichHenID(lichHenId);
                    lh.setBenhNhanID(benhNhanId);
                    lh.setBacSiID(Integer.parseInt(uBs));
                    lh.setNgayKham(Date.valueOf(uNgay));
                    lh.setGioKham(Time.valueOf(normalizedTime));
                    lh.setPhongID(Integer.parseInt(uPhong));
                    lh.setGhiChu(request.getParameter("ghiChu"));
                    boolean ok = lhDAO.updateBookingWithServices(lh, dvIds);
                    if (!ok) throw new IllegalStateException("Không cập nhật được lịch hẹn.");
                    response.getWriter().write("{\"success\":true}");
                    return;
                }
                if ("get-payment-detail".equalsIgnoreCase(action)) {
                    int lichHenId = Integer.parseInt(trimParam(request.getParameter("lichHenId")));
                    LichHenDAO.ReceptionPaymentDetail d = lhDAO.getReceptionPaymentDetail(lichHenId);
                    if (d == null) throw new IllegalStateException("Không tìm thấy dữ liệu thanh toán.");
                    StringBuilder sb = new StringBuilder();
                    sb.append("{")
                      .append("\"success\":true,")
                      .append("\"id\":").append(d.lichHenId).append(",")
                      .append("\"patientName\":\"").append(jsonEscape(d.patientName)).append("\",")
                      .append("\"patientPhone\":\"").append(jsonEscape(d.patientPhone)).append("\",")
                      .append("\"ngayKham\":\"").append(d.ngayKham == null ? "" : d.ngayKham.toString()).append("\",")
                      .append("\"gioKham\":\"").append(d.gioKham == null ? "" : d.gioKham.toString()).append("\",")
                      .append("\"doctorName\":\"").append(jsonEscape(d.doctorName)).append("\",")
                      .append("\"subtotal\":").append((long) d.subtotal).append(",")
                      .append("\"items\":[");
                    for (int i = 0; i < d.items.size(); i++) {
                        LichHenDAO.PaymentItem it = d.items.get(i);
                        if (i > 0) sb.append(",");
                        sb.append("{")
                          .append("\"name\":\"").append(jsonEscape(it.serviceName)).append("\",")
                          .append("\"quantity\":").append(it.quantity).append(",")
                          .append("\"unitPrice\":").append((long) it.unitPrice).append(",")
                          .append("\"lineTotal\":").append((long) it.lineTotal)
                          .append("}");
                    }
                    sb.append("]}");
                    response.getWriter().write(sb.toString());
                    return;
                }
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Action không hợp lệ.\"}");
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"" + jsonEscape(e.getMessage()) + "\"}");
                return;
            }
        }

        String ngayKham = firstNonBlank(request.getParameter("ngayKham"), request.getParameter("ngay_kham"));
        String gioKham = firstNonBlank(request.getParameter("gioKham"), request.getParameter("gio_kham"));
        String patientName = firstNonBlank(
                request.getParameter("patientName"),
                request.getParameter("tenBenhNhan"),
                request.getParameter("hoTen"));
        String patientPhone = firstNonBlank(
                request.getParameter("patientPhone"),
                request.getParameter("soDienThoai"),
                request.getParameter("phone"));
        String ghiChu = request.getParameter("ghiChu");
        String phongIdRaw = request.getParameter("phongId");
        String bacSiIdRaw = request.getParameter("bacSiId");
        String[] dichVuRaw = request.getParameterValues("dichVu");
        if (dichVuRaw == null || dichVuRaw.length == 0) {
            dichVuRaw = request.getParameterValues("dichVu[]");
        }
        boolean ajax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));

        try {
            List<String> missing = new ArrayList<>();
            if (ngayKham.isBlank()) missing.add("ngày khám");
            if (gioKham.isBlank()) missing.add("giờ khám");
            if (patientName.isBlank()) missing.add("họ tên bệnh nhân");
            if (patientPhone.isBlank()) missing.add("số điện thoại");
            if (dichVuRaw == null || dichVuRaw.length == 0) missing.add("dịch vụ");
            if (!missing.isEmpty()) {
                throw new IllegalArgumentException("Thiếu thông tin: " + String.join(", ", missing));
            }

            String normalizedTime = gioKham.trim();
            if (normalizedTime.length() == 5) {
                normalizedTime = normalizedTime + ":00";
            }

            int phongId = 1;
            if (phongIdRaw != null && !phongIdRaw.isBlank()) {
                phongId = Integer.parseInt(phongIdRaw.trim());
            }
            Integer bacSiId = null;
            if (bacSiIdRaw != null && !bacSiIdRaw.isBlank()) {
                bacSiId = Integer.parseInt(bacSiIdRaw.trim());
            }

            List<Integer> dichVuIds = new ArrayList<>();
            for (String dv : dichVuRaw) {
                dichVuIds.add(Integer.parseInt(dv));
            }

            int benhNhanId = lichHenService.resolveOrCreateDeskPatient(patientName, patientPhone);
            if (benhNhanId <= 0) {
                throw new IllegalStateException("Không thể lưu thông tin bệnh nhân.");
            }

            LichHen lh = new LichHen();
            lh.setBenhNhanID(benhNhanId);
            lh.setNgayKham(Date.valueOf(ngayKham));
            lh.setGioKham(Time.valueOf(normalizedTime));
            lh.setGhiChu(ghiChu);
            lh.setTrangThai("Chờ xác nhận");
            lh.setPhongID(phongId);

            String result = lichHenService.createBooking(lh, dichVuIds, bacSiId);
            if (!"SUCCESS".equals(result)) {
                throw new IllegalStateException(result);
            }

            if (ajax) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("SUCCESS");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/reception-dashboard?booked=1");
        } catch (Exception ex) {
            if (ajax) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(ex.getMessage());
                return;
            }
            response.sendRedirect(request.getContextPath() + "/reception-dashboard?error=1");
        }
    }
}
