package com.dentalclinic.controller;

import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.TaiKhoan;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
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
                String phone = jsObj.has("phone") ? jsObj.get("phone").getAsString() : "";
                String dob = jsObj.has("dob") ? jsObj.get("dob").getAsString() : "";
                String genderStr = jsObj.has("gender") ? jsObj.get("gender").getAsString() : "";

                loggedInUser.setSoDienThoai(phone);

                if (!dob.isEmpty()) {
                    loggedInUser.setNgaySinh(Date.valueOf(dob));
                }

                boolean gioiTinhBool = "Nam".equalsIgnoreCase(genderStr);
                loggedInUser.setGioiTinh(gioiTinhBool);

                TaiKhoanDAO tkDAO = new TaiKhoanDAO();
                boolean isUpdated = tkDAO.updateInfo(loggedInUser);

                if (isUpdated) {
                    session.setAttribute("loggedInUser", loggedInUser);
                    jsonResponse.addProperty("success", true);
                } else {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Số điện thoại đã tồn tại hoặc có lỗi xảy ra.");
                }
            } else if (action.equals("/change-password")) {
                String oldPassword = jsObj.has("oldPassword") ? jsObj.get("oldPassword").getAsString() : "";
                String newPassword = jsObj.has("newPassword") ? jsObj.get("newPassword").getAsString() : "";

                if (!oldPassword.equals(loggedInUser.getMatKhau())) {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("code", "WRONG_PASSWORD");
                    jsonResponse.addProperty("message", "Mật khẩu cũ không chính xác!");
                } else {
                    TaiKhoanDAO tkDAO = new TaiKhoanDAO();
                    boolean isUpdated = tkDAO.updatePassword(loggedInUser.getTaiKhoanID(), newPassword);

                    if (isUpdated) {
                        loggedInUser.setMatKhau(newPassword);
                        session.setAttribute("loggedInUser", loggedInUser);
                        jsonResponse.addProperty("success", true);
                    } else {
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Lỗi cơ sở dữ liệu khi đổi mật khẩu.");
                    }
                }
            } else if (action.equals("/cancel-appointment") || action.equals("/request-cancel") || action.equals("/update-appointment")) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Tính năng quản lý lịch hẹn đang được hoàn thiện!");
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
}
