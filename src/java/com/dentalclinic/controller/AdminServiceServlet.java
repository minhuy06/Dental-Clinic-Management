package com.dentalclinic.controller;

import com.dentalclinic.dao.DichVuDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/services")
public class AdminServiceServlet extends HttpServlet {
    private final DichVuDAO dichVuDAO = new DichVuDAO();

    private static int mapCategory(String cat) {
        if (cat == null) return 1;
        switch (cat.trim()) {
            case "kham": return 1;
            case "chinh-nha": return 2;
            case "tham-my": return 3;
            case "phau-thuat": return 4;
            case "tre-em": return 5;
            default: return 1;
        }
    }

    private static int parseDurationMinutes(String raw) {
        if (raw == null || raw.isBlank()) return 30;
        StringBuilder digits = new StringBuilder();
        for (char c : raw.toCharArray()) {
            if (Character.isDigit(c)) digits.append(c);
        }
        if (digits.length() == 0) return 30;
        try {
            return Math.max(5, Integer.parseInt(digits.toString()));
        } catch (NumberFormatException e) {
            return 30;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        try {
            String action = request.getParameter("action");
            if ("delete".equalsIgnoreCase(action)) {
                String idRaw = request.getParameter("id");
                if (idRaw == null || idRaw.isBlank()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Thiếu id dịch vụ.\"}");
                    return;
                }
                int id = Integer.parseInt(idRaw.trim());
                boolean ok = dichVuDAO.deleteById(id);
                if (!ok) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Không xóa được dịch vụ trong DB (có thể đang được sử dụng).\"}");
                    return;
                }
                response.getWriter().write("{\"success\":true}");
                return;
            }

            String name = request.getParameter("name");
            String priceRaw = request.getParameter("price");
            String cat = request.getParameter("cat");
            String time = request.getParameter("time");
            String perUnitRaw = request.getParameter("perUnit");

            if (name == null || name.isBlank() || priceRaw == null || priceRaw.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Thiếu tên hoặc giá dịch vụ.\"}");
                return;
            }

            double price = Double.parseDouble(priceRaw.trim());
            int minutes = parseDurationMinutes(time);
            int chuyenKhoaId = mapCategory(cat);
            boolean perUnit = "true".equalsIgnoreCase(perUnitRaw);

            boolean ok;
            if ("update".equalsIgnoreCase(action)) {
                String idRaw = request.getParameter("id");
                if (idRaw == null || idRaw.isBlank()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Thiếu id dịch vụ để cập nhật.\"}");
                    return;
                }
                int id = Integer.parseInt(idRaw.trim());
                ok = dichVuDAO.updateFromAdmin(id, name.trim(), price, minutes, chuyenKhoaId, perUnit);
                if (!ok) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\":false,\"message\":\"Không thể cập nhật dịch vụ trong DB.\"}");
                    return;
                }
                response.getWriter().write("{\"success\":true}");
                return;
            }

            ok = dichVuDAO.createFromAdmin(name.trim(), price, minutes, chuyenKhoaId, perUnit);
            if (!ok) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Không thể thêm dịch vụ vào DB.\"}");
                return;
            }
            response.getWriter().write("{\"success\":true}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}
