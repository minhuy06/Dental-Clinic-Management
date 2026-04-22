package com.dentalclinic.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "VerifyOTPServlet", urlPatterns = {"/VerifyOTPServlet"})
public class VerifyOTPServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        // Lấy mã từ người dùng và mã từ hệ thống
        String userOTP = request.getParameter("txtOTP");
        HttpSession session = request.getSession();
        String systemOTP = (String) session.getAttribute("VERIFY_OTP");
        
        // So sánh
        if(systemOTP != null && systemOTP.equals(userOTP)){
            
            // Lấy thông tin lưu Database
            String hoTen = (String) session.getAttribute("TEMP_HoTen");
            String sdt = (String) session.getAttribute("TEMP_SDT");
            String matKhau = (String) session.getAttribute("TEMP_MatKhau");
            
            // Gọi dao lưu vào database
            
            
            // Xóa Session
            session.removeAttribute("TEMP_HoTen");
            session.removeAttribute("TEMP_SDT");
            session.removeAttribute("TEMP_MatKhau");
            session.removeAttribute("VERIFY_OTP");
            
            // Thành công -> Bắn về trang Đăng nhập
            response.sendRedirect("account/login.jsp?msg=success"); // "?msg=success" dùng để in thông báo đăng kí thành công
        }
        else{
            request.setAttribute("errorMsg", "Mã OTP không chính xác!");
            request.getRequestDispatcher("account/xac-thuc-otp.jsp").forward(request, response);
        }
    }
}
