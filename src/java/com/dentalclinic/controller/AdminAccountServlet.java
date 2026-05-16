package com.dentalclinic.controller;

import com.dentalclinic.dao.AdminAccountDAO;
import com.dentalclinic.dao.AdminAccountDAO.OpResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/accounts")
public class AdminAccountServlet extends HttpServlet {
    private final AdminAccountDAO accountDAO = new AdminAccountDAO();

    private static String jsonEscape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.getWriter().write("{\"success\":false,\"message\":\"" + jsonEscape(message) + "\"}");
    }

    private void writeSuccess(HttpServletResponse response, OpResult result) throws IOException {
        response.getWriter().write("{\"success\":true,\"id\":" + result.taiKhoanId + "}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        try {
            String action = request.getParameter("action");
            if (action == null || action.isBlank()) {
                action = "create";
            }

            switch (action.toLowerCase()) {
                case "create":
                    handleCreate(request, response);
                    break;
                case "update":
                    handleUpdate(request, response);
                    break;
                case "toggle-status":
                    handleToggleStatus(request, response);
                    break;
                case "delete":
                    handleDelete(request, response);
                    break;
                default:
                    writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Hành động không được hỗ trợ.");
            }
        } catch (Exception e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OpResult result = accountDAO.createStaffAccount(
                request.getParameter("name"),
                request.getParameter("phone"),
                request.getParameter("password"),
                request.getParameter("role"),
                request.getParameter("dob"),
                request.getParameter("gender"),
                request.getParameter("specialty"),
                request.getParameter("degree"),
                request.getParameter("avatar"));
        if (!result.success) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, result.message);
            return;
        }
        writeSuccess(response, result);
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.isBlank()) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID tài khoản.");
            return;
        }
        int id = Integer.parseInt(idRaw.trim());
        OpResult result = accountDAO.updateAccount(
                id,
                request.getParameter("name"),
                request.getParameter("phone"),
                request.getParameter("password"),
                request.getParameter("role"),
                request.getParameter("status"),
                request.getParameter("dob"),
                request.getParameter("gender"),
                request.getParameter("specialty"),
                request.getParameter("degree"),
                request.getParameter("avatar"));
        if (!result.success) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, result.message);
            return;
        }
        writeSuccess(response, result);
    }

    private void handleToggleStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.isBlank()) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID tài khoản.");
            return;
        }
        int id = Integer.parseInt(idRaw.trim());
        OpResult result = accountDAO.toggleStatus(id, request.getParameter("status"));
        if (!result.success) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, result.message);
            return;
        }
        writeSuccess(response, result);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.isBlank()) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID tài khoản.");
            return;
        }
        int id = Integer.parseInt(idRaw.trim());
        OpResult result = accountDAO.deleteAccount(id);
        if (!result.success) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, result.message);
            return;
        }
        writeSuccess(response, result);
    }
}
