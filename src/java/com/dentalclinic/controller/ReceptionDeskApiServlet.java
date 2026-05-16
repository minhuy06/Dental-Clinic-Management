package com.dentalclinic.controller;

import com.dentalclinic.dao.BenhNhanDAO;
import com.dentalclinic.dao.LeTanDAO;
import com.dentalclinic.dao.ReceptionReportDAO;
import com.dentalclinic.model.TaiKhoan;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(
        name = "ReceptionDeskApiServlet",
        urlPatterns = {
            "/api/patients",
            "/api/patients/add",
            "/api/patients/update",
            "/api/patients/delete",
            "/api/reports/revenue-summary"
        })
public class ReceptionDeskApiServlet extends HttpServlet {

    private final BenhNhanDAO benhNhanDAO = new BenhNhanDAO();
    private final LeTanDAO leTanDAO = new LeTanDAO();
    private final ReceptionReportDAO reportDAO = new ReceptionReportDAO();
    private final Gson gson = new Gson();

    private static String readBody(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = req.getReader()) {
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private static TaiKhoan requireReceptionUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        TaiKhoan u = (TaiKhoan) session.getAttribute("loggedInUser");
        if (u == null) return null;
        if (!"Lễ tân".equalsIgnoreCase(u.getVaiTro())) return null;
        return u;
    }

    private void replyJson(HttpServletResponse res, int status, Object body) throws IOException {
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(status);
        res.getWriter().write(gson.toJson(body));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requireReceptionUser(request) == null) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Vui lòng đăng nhập với vai trò lễ tân.");
            replyJson(response, HttpServletResponse.SC_UNAUTHORIZED, jo);
            return;
        }

        String path = request.getServletPath();
        if ("/api/patients".equals(path)) {
            List<BenhNhanDAO.ReceptionPatientRow> rows = benhNhanDAO.listReceptionPatients();
            replyJson(response, HttpServletResponse.SC_OK, rows);
            return;
        }

        if ("/api/reports/revenue-summary".equals(path)) {
            TaiKhoan user = requireReceptionUser(request);
            Integer leTanId = leTanDAO.findLeTanIdByTaiKhoanId(user.getTaiKhoanID());
            List<ReceptionReportDAO.RevenueTransaction> tx = reportDAO.fetchPaidInvoices(leTanId);
            Map<String, Object> payload = reportDAO.buildDashboardPayload(tx);
            replyJson(response, HttpServletResponse.SC_OK, payload);
            return;
        }

        JsonObject jo = new JsonObject();
        jo.addProperty("success", false);
        jo.addProperty("message", "API không hợp lệ.");
        replyJson(response, HttpServletResponse.SC_NOT_FOUND, jo);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requireReceptionUser(request) == null) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Vui lòng đăng nhập với vai trò lễ tân.");
            replyJson(response, HttpServletResponse.SC_UNAUTHORIZED, jo);
            return;
        }

        if (!"/api/patients/add".equals(request.getServletPath())) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Phương thức không hợp lệ.");
            replyJson(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, jo);
            return;
        }

        try {
            String raw = readBody(request);
            BenhNhanDAO.ReceptionPatientInput in =
                    gson.fromJson(raw.isEmpty() ? "{}" : raw, BenhNhanDAO.ReceptionPatientInput.class);
            if (in == null || in.fullName == null || in.fullName.isBlank()) {
                JsonObject jo = new JsonObject();
                jo.addProperty("success", false);
                jo.addProperty("message", "Thiếu họ tên.");
                replyJson(response, HttpServletResponse.SC_BAD_REQUEST, jo);
                return;
            }

            BenhNhanDAO.ReceptionPatientErrors err = benhNhanDAO.createReceptionPatient(in);
            JsonObject jo = new JsonObject();
            if (!err.isSuccess()) {
                jo.addProperty("success", false);
                jo.addProperty("message", err.getMessage() != null ? err.getMessage() : "Không lưu được.");
                replyJson(response, HttpServletResponse.SC_BAD_REQUEST, jo);
                return;
            }
            jo.addProperty("success", true);
            jo.addProperty("id", err.getId() != null ? err.getId() : 0);
            replyJson(response, HttpServletResponse.SC_OK, jo);
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Lỗi hệ thống: " + e.getMessage());
            replyJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, jo);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requireReceptionUser(request) == null) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Vui lòng đăng nhập với vai trò lễ tân.");
            replyJson(response, HttpServletResponse.SC_UNAUTHORIZED, jo);
            return;
        }

        if (!"/api/patients/update".equals(request.getServletPath())) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Phương thức không hợp lệ.");
            replyJson(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, jo);
            return;
        }

        try {
            String raw = readBody(request);
            JsonObject root =
                    gson.fromJson(raw.isEmpty() ? "{}" : raw, JsonObject.class);

            JsonElement idEl = root.get("id");
            int benhNhanId;
            if (idEl == null || !idEl.isJsonPrimitive()) {
                JsonObject jo = new JsonObject();
                jo.addProperty("success", false);
                jo.addProperty("message", "Thiếu mã bệnh nhân.");
                replyJson(response, HttpServletResponse.SC_BAD_REQUEST, jo);
                return;
            }
            benhNhanId = idEl.getAsInt();

            BenhNhanDAO.ReceptionPatientInput in = gson.fromJson(root, BenhNhanDAO.ReceptionPatientInput.class);
            if (in == null || in.fullName == null || in.fullName.isBlank()) {
                JsonObject jo = new JsonObject();
                jo.addProperty("success", false);
                jo.addProperty("message", "Thiếu họ tên.");
                replyJson(response, HttpServletResponse.SC_BAD_REQUEST, jo);
                return;
            }

            BenhNhanDAO.ReceptionPatientErrors err = benhNhanDAO.updateReceptionPatient(benhNhanId, in);
            JsonObject jo = new JsonObject();
            if (!err.isSuccess()) {
                jo.addProperty("success", false);
                jo.addProperty("message", err.getMessage() != null ? err.getMessage() : "Không cập nhật được.");
                replyJson(response, HttpServletResponse.SC_BAD_REQUEST, jo);
                return;
            }
            jo.addProperty("success", true);
            replyJson(response, HttpServletResponse.SC_OK, jo);
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Lỗi hệ thống: " + e.getMessage());
            replyJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, jo);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requireReceptionUser(request) == null) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Vui lòng đăng nhập với vai trò lễ tân.");
            replyJson(response, HttpServletResponse.SC_UNAUTHORIZED, jo);
            return;
        }

        if (!"/api/patients/delete".equals(request.getServletPath())) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Phương thức không hợp lệ.");
            replyJson(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, jo);
            return;
        }

        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.isBlank()) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Thiếu mã bệnh nhân.");
            replyJson(response, HttpServletResponse.SC_BAD_REQUEST, jo);
            return;
        }
        try {
            int id = Integer.parseInt(idRaw.trim());
            boolean ok = benhNhanDAO.deactivatePatientAccount(id);
            JsonObject jo = new JsonObject();
            jo.addProperty("success", ok);
            if (!ok) {
                jo.addProperty("message", "Không khóa được tài khoản hoặc không tìm thấy bệnh nhân.");
            }
            replyJson(response, ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST, jo);
        } catch (NumberFormatException ex) {
            JsonObject jo = new JsonObject();
            jo.addProperty("success", false);
            jo.addProperty("message", "Mã bệnh nhân không hợp lệ.");
            replyJson(response, HttpServletResponse.SC_BAD_REQUEST, jo);
        }
    }
}
