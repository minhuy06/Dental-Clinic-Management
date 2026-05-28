package com.dentalclinic.controller;

import com.dentalclinic.dao.AdminAccountDAO;
import com.dentalclinic.dao.AdminAccountDAO.OpResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;

@WebServlet("/admin/accounts")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 10485760, maxRequestSize = 20971520)
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

    private String processAvatarUpload(HttpServletRequest request) {
        try {
            Part filePart = request.getPart("avatarFile");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                // Lọc bỏ dấu tiếng Việt và dấu cách để tránh lỗi không hiển thị được ảnh (404) trên Tomcat
                String ext = "";
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0) {
                    ext = fileName.substring(dotIndex);
                    fileName = fileName.substring(0, dotIndex);
                }
                String safeName = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("[^a-zA-Z0-9_-]", "");
                String uniqueFileName = System.currentTimeMillis() + "_" + safeName + ext;
                
                // 1. Lưu vào thư mục chạy thực tế của Tomcat (thường là build/web/...)
                String deployPath = request.getServletContext().getRealPath("/assets/img/doctors");
                if (deployPath != null) {
                    File deployDir = new File(deployPath);
                    if (!deployDir.exists()) deployDir.mkdirs();
                    filePart.write(deployPath + File.separator + uniqueFileName);
                    
                    // 2. Lưu một bản copy vào thư mục source code gốc (web/...) để không bị mất khi Clean & Build
                    String sourcePath = deployPath.replace(File.separator + "build" + File.separator + "web", File.separator + "web");
                    if (!sourcePath.equals(deployPath)) {
                        File sourceDir = new File(sourcePath);
                        if (sourceDir.exists()) {
                            java.nio.file.Files.copy(
                                Paths.get(deployPath + File.separator + uniqueFileName),
                                Paths.get(sourcePath + File.separator + uniqueFileName),
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING
                            );
                        }
                    }
                }
                return request.getContextPath() + "/assets/img/doctors/" + uniqueFileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request.getParameter("avatar");
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String avatarPath = processAvatarUpload(request);
        OpResult result = accountDAO.createStaffAccount(
                request.getParameter("name"),
                request.getParameter("phone"),
                request.getParameter("password"),
                request.getParameter("role"),
                request.getParameter("dob"),
                request.getParameter("gender"),
                request.getParameter("specialty"),
                request.getParameter("degree"),
                avatarPath);
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
        String avatarPath = processAvatarUpload(request);
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
                avatarPath);
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
