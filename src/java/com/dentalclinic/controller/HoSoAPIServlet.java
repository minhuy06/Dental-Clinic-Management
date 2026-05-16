package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.TaiKhoan;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/api/hoso/*")
public class HoSoAPIServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        HttpSession session = request.getSession(false);
        TaiKhoan loggedInUser = (session != null) ? (TaiKhoan) session.getAttribute("loggedInUser") : null;

        if (loggedInUser == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Vui lòng đăng nhập lại!");
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }

        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JsonObject jsObj = gson.fromJson(sb.toString(), JsonObject.class);

            String action = request.getPathInfo();

            if (action == null) {
                throw new Exception("Hành động không hợp lệ!");
            }

            if (action.equals("/update-info")) {
                handleUpdateInfo(jsObj, loggedInUser, session, jsonResponse);
            } else if (action.equals("/change-password")) {
                handleChangePassword(jsObj, loggedInUser, session, jsonResponse);
            } else if (action.equals("/cancel-appointment")) {
                handleCancelAppointment(jsObj, loggedInUser, jsonResponse);
            } else if (action.equals("/request-cancel")) {
                handleRequestCancel(jsObj, loggedInUser, jsonResponse);
            } else if (action.equals("/update-appointment")) {
                handleUpdateAppointment(jsObj, loggedInUser, jsonResponse);
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "API Không tồn tại.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi Server: " + e.getMessage());
        }
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    private void handleUpdateInfo(JsonObject jsObj, TaiKhoan loggedInUser, HttpSession session,
            JsonObject jsonResponse) {
        String phone = jsObj.has("phone") ? jsObj.get("phone").getAsString().trim() : "";
        String dob = jsObj.has("dob") ? jsObj.get("dob").getAsString() : "";
        String genderStr = jsObj.has("gender") ? jsObj.get("gender").getAsString() : "";

        if (phone.isEmpty()) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Vui lòng nhập số điện thoại.");
            return;
        }

        loggedInUser.setSoDienThoai(phone);
        if (!dob.isEmpty()) {
            loggedInUser.setNgaySinh(Date.valueOf(dob));
        }
        loggedInUser.setGioiTinh("Nam".equalsIgnoreCase(genderStr));

        TaiKhoanDAO tkDAO = new TaiKhoanDAO();
        if (tkDAO.updateInfo(loggedInUser)) {
            session.setAttribute("loggedInUser", loggedInUser);
            jsonResponse.addProperty("success", true);
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Cập nhật thất bại, vui lòng thử lại.");
        }
    }

    private void handleChangePassword(JsonObject jsObj, TaiKhoan loggedInUser, HttpSession session,
            JsonObject jsonResponse) {
        String oldPassword = jsObj.has("oldPassword") ? jsObj.get("oldPassword").getAsString() : "";
        String newPassword = jsObj.has("newPassword") ? jsObj.get("newPassword").getAsString() : "";

        if (!oldPassword.equals(loggedInUser.getMatKhau())) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("code", "WRONG_PASSWORD");
            jsonResponse.addProperty("message", "Mật khẩu cũ không chính xác!");
            return;
        }

        TaiKhoanDAO tkDAO = new TaiKhoanDAO();
        if (tkDAO.updatePassword(loggedInUser.getTaiKhoanID(), newPassword)) {
            loggedInUser.setMatKhau(newPassword);
            session.setAttribute("loggedInUser", loggedInUser);
            jsonResponse.addProperty("success", true);
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi cơ sở dữ liệu khi đổi mật khẩu.");
        }
    }

    private void handleCancelAppointment(JsonObject jsObj, TaiKhoan loggedInUser, JsonObject jsonResponse) {
        int lichHenId = parseAppointmentId(jsObj);
        if (lichHenId <= 0) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Mã lịch hẹn không hợp lệ.");
            return;
        }
        LichHenDAO lhDAO = new LichHenDAO();
        if (lhDAO.benhNhanHuyLichHen(lichHenId, loggedInUser.getTaiKhoanID())) {
            jsonResponse.addProperty("success", true);
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Không thể hủy lịch hẹn này.");
        }
    }

    private void handleRequestCancel(JsonObject jsObj, TaiKhoan loggedInUser, JsonObject jsonResponse) {
        int lichHenId = parseAppointmentId(jsObj);
        if (lichHenId <= 0) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Mã lịch hẹn không hợp lệ.");
            return;
        }
        LichHenDAO lhDAO = new LichHenDAO();
        if (lhDAO.benhNhanYeuCauHuyLichHen(lichHenId, loggedInUser.getTaiKhoanID())) {
            jsonResponse.addProperty("success", true);
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Không thể gửi yêu cầu hủy.");
        }
    }

    private void handleUpdateAppointment(JsonObject jsObj, TaiKhoan loggedInUser, JsonObject jsonResponse) {
        int lichHenId = parseAppointmentId(jsObj);
        String dateStr = jsObj.has("date") ? jsObj.get("date").getAsString() : "";
        String timeStr = jsObj.has("time") ? jsObj.get("time").getAsString() : "";
        String note = jsObj.has("note") ? jsObj.get("note").getAsString() : "";

        if (lichHenId <= 0 || dateStr.isEmpty() || timeStr.isEmpty()) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Thiếu thông tin lịch hẹn.");
            return;
        }

        List<int[]> dichVuLines = parseServiceLines(jsObj);
        if (dichVuLines.isEmpty()) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Vui lòng chọn ít nhất một dịch vụ.");
            return;
        }

        Date ngayKham = Date.valueOf(dateStr);
        Time gioKham = Time.valueOf(timeStr.length() == 5 ? timeStr + ":00" : timeStr);

        LichHenDAO lhDAO = new LichHenDAO();
        if (lhDAO.benhNhanCapNhatLichHen(lichHenId, loggedInUser.getTaiKhoanID(), ngayKham, gioKham, note, dichVuLines)) {
            jsonResponse.addProperty("success", true);
        } else {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Chỉ được sửa lịch đang chờ xác nhận.");
        }
    }

    private int parseAppointmentId(JsonObject jsObj) {
        if (!jsObj.has("appointmentId")) {
            return 0;
        }
        try {
            JsonElement el = jsObj.get("appointmentId");
            if (el.isJsonPrimitive() && el.getAsJsonPrimitive().isNumber()) {
                return el.getAsInt();
            }
            return Integer.parseInt(el.getAsString().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private List<int[]> parseServiceLines(JsonObject jsObj) {
        List<int[]> lines = new ArrayList<>();
        if (!jsObj.has("services") || !jsObj.get("services").isJsonArray()) {
            return lines;
        }
        JsonArray arr = jsObj.getAsJsonArray("services");
        for (JsonElement el : arr) {
            if (!el.isJsonObject()) {
                continue;
            }
            JsonObject s = el.getAsJsonObject();
            try {
                int id = s.get("id").getAsJsonPrimitive().isNumber()
                        ? s.get("id").getAsInt()
                        : Integer.parseInt(s.get("id").getAsString().trim());
                int qty = s.has("qty") ? s.get("qty").getAsInt() : 1;
                if (id > 0) {
                    lines.add(new int[]{id, qty > 0 ? qty : 1});
                }
            } catch (Exception ignored) {
            }
        }
        return lines;
    }
}
