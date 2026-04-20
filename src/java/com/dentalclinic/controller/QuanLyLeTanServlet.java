/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.LeTan;
import com.dentalclinic.model.TaiKhoan;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/hospitalmanager/receptionM")
public class QuanLyLeTanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Kiểm tra Session và Quyền (Bảo mật kép ngoài Filter)
        HttpSession session = request.getSession(false);
        TaiKhoan account = (session != null) ? (TaiKhoan) session.getAttribute("accountLogan") : null;

        // Nếu chưa đăng nhập hoặc KHÔNG PHẢI Quản trị viên -> Chặn ngay!
        if (account == null || !"Quản trị viên".equalsIgnoreCase(account.getVaiTro())) {
            // Trả về một dòng HTML báo lỗi để cái Fetch API của JS hiển thị lên màn hình
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<h3 style='color:red;'>Lỗi 403: Bro không có quyền xem danh sách này!</h3>");
            return; // Dừng lập tức, không cho chọc xuống Database
        }

        
        TaiKhoanDAO dao = new TaiKhoanDAO();
        ArrayList<LeTan> danhSachLeTan = dao.layDanhSachLeTan();
        
        request.setAttribute("listLT", danhSachLeTan);

        request.getRequestDispatcher("/hospitalmanager/components/letan.jsp").forward(request, response);
    }
}
