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
        
        // Đảm bảo được được tiếng Việt có dấu
        request.setCharacterEncoding("UTF-8");
        
        // Thiết lập kiểu trả về là chữ thuần túy (Text) cho JavaScript đọc
        response.setContentType("text/plain;charset=UTF-8");
        
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
            
            // Thành công -> In chữ SUCCESS để JavaScript biết và chuyển sang trang Đăng nhập
            response.getWriter().write("SUCCESS");
        }
        else{
            // Sai mã OTP thì in chữ ERROR
            response.getWriter().write("ERROR_OTP");
        }
    }
}
