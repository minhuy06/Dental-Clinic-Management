package com.dentalclinic.controller;

import com.dentalclinic.service.EmailService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpSession;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // Đảm bảo đọc được tiếng Việt có dấu
        request.setCharacterEncoding("UTF-8");
        
        // Thiết lập kiểu trả về là chữ thuần túy cho JavaScript đọc
        response.setContentType("text/plain;charset=UTF-8");
        
        // Lấy dữ liệu từ Form gửi lên
        String hoTen = request.getParameter("txtHoTen");
        String sdt = request.getParameter("txtSDT");
        String email = request.getParameter("txtEmail");
        String matKhau = request.getParameter("txtMatKhau");
        
        // Gọi Service gửi OTP qua Gmail
        EmailService emailService = new EmailService();
        String otpCode = emailService.generateOTP();
        
        // Thực hiện gửi OTP vào email của khách hàng
        boolean isSent = emailService.sendOTP(email, otpCode);
        
        if(isSent){
            // Lưu thông tin đăng ký vào Session để chờ xác thực
            HttpSession session = request.getSession();
            session.setAttribute("TEMP_HoTen", hoTen);
            session.setAttribute("TEMP_SDT", sdt);
            session.setAttribute("TEMP_Email", email);
            session.setAttribute("TEMP_MatKhau", matKhau);
            
            // Lưu mã OTP hệ thống sinh ra để đối chiếu với mã khách hàng nhập
            session.setAttribute("VERIFY_OTP", otpCode);
            
            // Phản hồi về cho Javascript mở Popup
            response.getWriter().write("SUCCESS");
        }
        else{
            // Báo lỗi nếu gửi Email thất bại
            response.getWriter().write("ERROR");
        }
    }
}