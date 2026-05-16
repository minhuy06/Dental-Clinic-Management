package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.service.TaiKhoanService;
import com.dentalclinic.utils.RoleNavHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String rawData = sb.toString();

            JsonObject jsObj = gson.fromJson(rawData, JsonObject.class);

            if (jsObj == null || !jsObj.has("account") || !jsObj.has("password")) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Dữ liệu JSON bị rỗng hoặc sai key!");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            String acc = jsObj.get("account").getAsString();
            String pass = jsObj.get("password").getAsString();

            TaiKhoanService tkser = new TaiKhoanService();
            TaiKhoan tk = tkser.checkLogin(acc, pass);

            if (tk != null) {
                request.getSession().setAttribute("loggedInUser", tk);
                String redirectUrl = RoleNavHelper.getWorkspaceUrl(request.getContextPath(), tk.getVaiTro());

                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("redirectUrl", redirectUrl);
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Sai tên đăng nhập hoặc mật khẩu");
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi Parse JSON: " + e.getMessage());
        }

        response.getWriter().write(gson.toJson(jsonResponse));
    }
}
