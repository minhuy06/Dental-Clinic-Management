package com.dentalclinic.controller;

import com.dentalclinic.service.TwilioSMSService;
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
        // Đảm bảo được được tiếng Việt có dấu
        request.setCharacterEncoding("UTF-8");
        
        // Lấy dữ liệu từ Form
        String hoTen = request.getParameter("txtHoTen");
        String sdt = request.getParameter("txtSDT");
        String matKhau = request.getParameter("txtMatKhau");
        
        // Gọi Service gửi OTP
        TwilioSMSService smsService = new TwilioSMSService();
        String otpCode = smsService.generateOTP();
        boolean isSent = smsService.sendOTP(sdt, otpCode);
        
        if(isSent){
            
            // Lưu thông tin đăng kí vào Session để chờ xác thực OTP
            HttpSession session = request.getSession();
            session.setAttribute("TEMP_HoTen", hoTen);
            session.setAttribute("TEMP_SDT", sdt);
            session.setAttribute("TEMP_MatKhau", matKhau);
            session.setAttribute("VERIFY_OTP", otpCode);
            
            // Chuyển sang trang nhập OTP
            response.sendRedirect("account/xac-thuc-otp.jsp");
        }
        else{
            // Nếu lỗi gửi tin nhắn
            request.setAttribute("errorMsg", "Không thể gửi SMS, vui lòng kiểm tra lại số điện thoại");
            request.getRequestDispatcher("account/dang-ky.jsp").forward(request, response);
        }
    }
}
