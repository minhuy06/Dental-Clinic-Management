/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.service.TaiKhoanService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kinhm
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet{
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");

    Gson gson = new Gson();
    JsonObject jsonResponse = new JsonObject();

    try {
        // --- MÁY QUÉT DỮ LIỆU GỐC ---
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String rawData = sb.toString();
        
        // Test
        System.out.println("=========================================");
        System.out.println(">>> DỮ LIỆU JS GỬI LÊN: [" + rawData + "]");
        System.out.println("=========================================");

        // Tiến hành Parse JSON
        JsonObject jsObj = gson.fromJson(rawData, JsonObject.class);

        if (jsObj == null || !jsObj.has("account") || !jsObj.has("password")) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Dữ liệu JSON bị rỗng hoặc sai key!");
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }

        String acc = jsObj.get("account").getAsString();
        String pass = jsObj.get("password").getAsString();

        // Gọi Database
        com.dentalclinic.service.TaiKhoanService tkser = new com.dentalclinic.service.TaiKhoanService();
        TaiKhoan tk = tkser.checkLogin(acc, pass);

        if (tk != null) {
            request.getSession().setAttribute("loggedInUser", tk);
            String redirectUrl = request.getContextPath() + "/index.jsp"; 
            String role = tk.getVaiTro();

            if ("Quản trị viên".equalsIgnoreCase(role)) {
                redirectUrl = request.getContextPath() + "/admin";
            } else if ("Bác sĩ".equalsIgnoreCase(role)) {
                redirectUrl = request.getContextPath() + "/doctor/index.jsp";
            } else if ("Lễ tân".equalsIgnoreCase(role)) {
                redirectUrl = request.getContextPath() + "/reception/index.jsp";
            } 

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
