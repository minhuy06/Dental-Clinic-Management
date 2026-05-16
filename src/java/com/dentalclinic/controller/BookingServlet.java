package com.dentalclinic.controller;

import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.service.LichHenService;
import com.dentalclinic.utils.InforPageHelper;
import com.dentalclinic.utils.RoleNavHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/dat-lich")
public class BookingServlet extends HttpServlet {

    private final LichHenService lhService = new LichHenService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        boolean wantsJson = isJsonRequest(request);

        TaiKhoan user = getLoggedInPatient(request);
        if (user == null) {
            if (wantsJson) {
                writeJson(response, false, "Vui lòng đăng nhập tài khoản bệnh nhân để đặt lịch.", 401);
            } else {
                response.sendRedirect(request.getContextPath() + "/account/login.jsp?redirect=datlich");
            }
            return;
        }

        try {
            BookingInput input = parseInput(request);
            if (input.ngayKham == null || input.ngayKham.isBlank()) {
                fail(request, response, wantsJson, "Vui lòng chọn ngày khám.");
                return;
            }
            if (input.gioKham == null || input.gioKham.isBlank()) {
                fail(request, response, wantsJson, "Vui lòng chọn giờ khám.");
                return;
            }
            if (input.qtyByDvId.isEmpty()) {
                fail(request, response, wantsJson, "Vui lòng chọn ít nhất một dịch vụ.");
                return;
            }

            int benhNhanId = lhService.resolveBenhNhanForPatientAccount(user.getTaiKhoanID());
            if (benhNhanId <= 0) {
                fail(request, response, wantsJson, "Không tìm thấy hồ sơ bệnh nhân. Vui lòng đăng ký hoặc liên hệ lễ tân.");
                return;
            }

            LichHen lh = new LichHen();
            lh.setBenhNhanID(benhNhanId);
            lh.setNgayKham(java.sql.Date.valueOf(input.ngayKham.trim()));
            String gio = input.gioKham.trim();
            if (gio.length() == 5) gio = gio + ":00";
            lh.setGioKham(java.sql.Time.valueOf(gio));
            lh.setGhiChu(input.ghiChu);
            lh.setPhongID(1);

            String result = lhService.createBooking(lh, input.qtyByDvId, null, true);
            if (!LichHenService.RESULT_SUCCESS.equals(result)
                    && !LichHenService.RESULT_SUCCESS_PENDING_SHIFT.equals(result)) {
                fail(request, response, wantsJson, result);
                return;
            }

            if (wantsJson) {
                String okMsg = LichHenService.RESULT_SUCCESS_PENDING_SHIFT.equals(result)
                        ? "Đã ghi nhận lịch hẹn! Phòng khám sẽ sắp xếp ca bác sĩ và lễ tân xác nhận sớm nhất."
                        : "Đặt lịch thành công! Lễ tân sẽ xác nhận trong thời gian sớm nhất.";
                writeJson(response, true, okMsg, 200);
            } else {
                response.sendRedirect(RoleNavHelper.getScheduleUrl(request.getContextPath()) + "?booked=1");
            }
        } catch (IllegalArgumentException e) {
            fail(request, response, wantsJson, "Ngày hoặc giờ khám không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            fail(request, response, wantsJson, "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private static class BookingInput {
        String ngayKham;
        String gioKham;
        String ghiChu;
        Map<Integer, Integer> qtyByDvId = new LinkedHashMap<>();
    }

    private BookingInput parseInput(HttpServletRequest request) throws IOException {
        BookingInput input = new BookingInput();
        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().contains("application/json")) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
            }
            JsonObject root = gson.fromJson(sb.length() == 0 ? "{}" : sb.toString(), JsonObject.class);
            if (root == null) root = new JsonObject();
            input.ngayKham = getJsonString(root, "date", "ngayKham");
            input.gioKham = getJsonString(root, "time", "gioKham");
            input.ghiChu = getJsonString(root, "note", "ghiChu");
            if (root.has("services") && root.get("services").isJsonArray()) {
                JsonArray arr = root.getAsJsonArray("services");
                for (JsonElement el : arr) {
                    if (!el.isJsonObject()) continue;
                    JsonObject svc = el.getAsJsonObject();
                    int id = svc.has("id") ? parseIntSafe(svc.get("id").getAsString()) : 0;
                    if (id < 1 && svc.has("id")) id = svc.get("id").getAsInt();
                    int qty = svc.has("quantity") ? svc.get("quantity").getAsInt() : 1;
                    if (id > 0) input.qtyByDvId.put(id, input.qtyByDvId.getOrDefault(id, 0) + Math.max(1, qty));
                }
            }
            return input;
        }

        input.ngayKham = request.getParameter("ngayKham");
        input.gioKham = request.getParameter("gioKham");
        input.ghiChu = request.getParameter("ghiChu");
        String[] dichVuIds = request.getParameterValues("dichVu");
        if (dichVuIds != null) {
            for (String idRaw : dichVuIds) {
                int id = parseIntSafe(idRaw);
                if (id > 0) input.qtyByDvId.put(id, input.qtyByDvId.getOrDefault(id, 0) + 1);
            }
        }
        return input;
    }

    private TaiKhoan getLoggedInPatient(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        Object obj = session.getAttribute("loggedInUser");
        if (!(obj instanceof TaiKhoan)) return null;
        TaiKhoan tk = (TaiKhoan) obj;
        String role = tk.getVaiTro();
        if (role == null) return null;
        if ("Bệnh nhân".equalsIgnoreCase(role.trim())) return tk;
        return null;
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String xhr = request.getHeader("X-Requested-With");
        String ct = request.getContentType();
        if (ct != null && ct.toLowerCase().contains("application/json")) return true;
        if ("XMLHttpRequest".equalsIgnoreCase(xhr)) return true;
        return accept != null && accept.contains("application/json");
    }

    private void fail(HttpServletRequest request, HttpServletResponse response, boolean wantsJson, String message)
            throws ServletException, IOException {
        if (wantsJson) {
            writeJson(response, false, message, 400);
        } else {
            forwardScheduleError(request, response, message);
        }
    }

    private void writeJson(HttpServletResponse response, boolean success, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        json.addProperty("message", message);
        response.getWriter().write(gson.toJson(json));
    }

    private static String getJsonString(JsonObject obj, String... keys) {
        for (String k : keys) {
            if (obj.has(k) && !obj.get(k).isJsonNull()) {
                return obj.get(k).getAsString();
            }
        }
        return "";
    }

    private static int parseIntSafe(String raw) {
        if (raw == null || raw.isBlank()) return 0;
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void forwardScheduleError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("scrollSection", "datlich");
        request.setAttribute("serviceListJson", InforPageHelper.buildServiceListJson());
        request.setAttribute("doctorListJson", InforPageHelper.buildSampleDoctorListJson());
        request.setAttribute("pageTitle", "Đặt lịch khám - Nha Khoa 5AE");
        request.getRequestDispatcher("/dat-lich.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(RoleNavHelper.getScheduleUrl(request.getContextPath()));
    }
}
