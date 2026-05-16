package com.dentalclinic.controller;

import com.dentalclinic.dao.LichLamViecDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichLamViec;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/shifts")
public class AdminShiftServlet extends HttpServlet {
    private final LichLamViecDAO dao = new LichLamViecDAO();
    private final LichHenDAO lichHenDAO = new LichHenDAO();

    private static int parseRoomId(String roomRaw) {
        if (roomRaw == null || roomRaw.isBlank()) return 1;
        StringBuilder digits = new StringBuilder();
        for (char c : roomRaw.toCharArray()) {
            if (Character.isDigit(c)) digits.append(c);
        }
        if (digits.length() == 0) return 1;
        try {
            return Integer.parseInt(digits.toString());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        String action = request.getParameter("action");
        try {
            if ("create".equalsIgnoreCase(action)) {
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                String shiftType = request.getParameter("shiftType");
                String dateRaw = request.getParameter("date");
                int roomId = parseRoomId(request.getParameter("room"));
                int caId = "afternoon".equalsIgnoreCase(shiftType) ? 2 : 1;
                Date ngay = Date.valueOf(dateRaw);

                if (dao.isDuplicate(staffId, ngay, caId)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Nhân viên đã có ca trong ngày này.\"}");
                    return;
                }

                LichLamViec lv = new LichLamViec();
                lv.setTaiKhoanID(staffId);
                lv.setCaID(caId);
                lv.setNgayLam(ngay);
                lv.setPhongID(roomId);
                boolean ok = dao.insert(lv);
                if (!ok) throw new IllegalStateException("Không thể lưu ca làm việc.");
                int assigned = lichHenDAO.assignPendingBookingsForShift(staffId, ngay, caId, roomId);
                response.getWriter().write("{\"success\":true,\"assignedBookings\":" + assigned + "}");
                return;
            }

            if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                String shiftType = request.getParameter("shiftType");
                String dateRaw = request.getParameter("date");
                int roomId = parseRoomId(request.getParameter("room"));
                int caId = "afternoon".equalsIgnoreCase(shiftType) ? 2 : 1;
                Date ngay = Date.valueOf(dateRaw);

                if (dao.isDuplicateExcludingId(staffId, ngay, caId, id)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Nhân viên đã có ca trong ngày này.\"}");
                    return;
                }

                LichLamViec lv = new LichLamViec();
                lv.setLichID(id);
                lv.setTaiKhoanID(staffId);
                lv.setCaID(caId);
                lv.setNgayLam(ngay);
                lv.setPhongID(roomId);
                boolean ok = dao.update(lv);
                if (!ok) throw new IllegalStateException("Không thể cập nhật ca làm việc.");
                response.getWriter().write("{\"success\":true}");
                return;
            }

            if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = dao.delete(id);
                if (!ok) throw new IllegalStateException("Không thể xóa ca làm việc.");
                response.getWriter().write("{\"success\":true}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Action không hợp lệ.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}
