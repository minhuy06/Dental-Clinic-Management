/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.TaiKhoan;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kinhm
 */
@WebServlet("/AdminData")
public class AdminDataServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        
        String action = req.getParameter("action");
        PrintWriter out = res.getWriter();
        Gson gson = new Gson();

        try {
            if ("services".equals(action)) {
                DichVuDAO dv = new DichVuDAO();
                List<DichVu> list = dv.getAllDichVu(); // Gọi xuống DAO lấy data
                out.print(gson.toJson(list));
            } 
            else if ("accounts".equals(action)) {
                TaiKhoanDAO tkd = new TaiKhoanDAO();
                List<TaiKhoan> list = tkd.getAllAccounts();
                out.print(gson.toJson(list));
            }
            // Tương tự cho schedule và revenue...
        } catch (Exception e) {
            res.setStatus(500);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
        out.flush();
    }
}
