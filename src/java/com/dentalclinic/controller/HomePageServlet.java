/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoan;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Dùng đường dẫn này để làm link gốc mỗi khi bấm vào Logo
@WebServlet("/trangchu/trang-chu")
public class HomePageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Lấy phiên làm việc hiện tại
        HttpSession session = request.getSession(false);
        TaiKhoan account = (session != null) ? (TaiKhoan) session.getAttribute("accountLogan") : null;

        // 2. Nếu chưa đăng nhập -> Đuổi về trang Login
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 3. Phân luồng dựa theo Vai Trò (Role-based Routing)
        

        if ("Quản trị viên".equalsIgnoreCase(account.getVaiTro())) {
            // Quản lý thì dẫn vào thư mục hospitalmanager (hoặc gọi cái Servlet HomePage lúc nãy)
            response.sendRedirect(request.getContextPath() + "/hospitalmanager/index.jsp");
            
        } else if ("Bác sĩ".equalsIgnoreCase(account.getVaiTro())) {
            // Bác sĩ thì dẫn về thư mục của bác sĩ
            response.sendRedirect(request.getContextPath() + "/doctor/index.jsp");
            
        } else if ("Lễ tân".equalsIgnoreCase(account.getVaiTro())) {
            // Lễ tân thì về màn hình trực quầy
            response.sendRedirect(request.getContextPath() + "/reception/index.jsp");
            
        } else {
            // Bệnh nhân bình thường thì dẫn ra trang chủ đặt lịch bên ngoài
            response.sendRedirect(request.getContextPath() + "/index.jsp"); 
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
