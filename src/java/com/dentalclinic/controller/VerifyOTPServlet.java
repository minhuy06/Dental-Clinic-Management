package com.dentalclinic.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.model.BenhNhan;
import com.dentalclinic.dao.TaiKhoanDAO;
import java.sql.Date;

@WebServlet(name = "VerifyOTPServlet", urlPatterns = {"/VerifyOTPServlet"})
public class VerifyOTPServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        // Đảm bảo đọc được tiếng Việt có dấu
        request.setCharacterEncoding("UTF-8");
        
        // Thiết lập kiểu trả về là chữ thuần túy cho JavaScript đọc
        response.setContentType("text/plain;charset=UTF-8");
        
        // Lấy mã từ người dùng và mã từ hệ thống
        String userOTP = request.getParameter("txtOTP");
        HttpSession session = request.getSession();
        String systemOTP = (String) session.getAttribute("VERIFY_OTP");
        
        // So sánh (Thêm trim() để tránh lỗi do người dùng copy paste dư khoảng trắng)
        if(systemOTP != null && userOTP != null && systemOTP.equals(userOTP.trim())){
            
            // Lấy toàn bộ thông tin đăng ký từ Session
            String hoTen = (String) session.getAttribute("TEMP_HoTen");
            String sdt = (String) session.getAttribute("TEMP_SDT");
            String email = (String) session.getAttribute("TEMP_Email");
            String matKhau = (String) session.getAttribute("TEMP_MatKhau");
            String ngaySinhStr = (String) session.getAttribute("TEMP_NgaySinh");
            String gioiTinhStr = (String) session.getAttribute("TEMP_GioiTinh");
            
            // Ép kiểu ngày sinh và giới tính
            boolean gioiTinh = "1".equalsIgnoreCase(gioiTinhStr);
            Date ngaySinh = Date.valueOf(ngaySinhStr);
            
            // Đóng gói vào model
            TaiKhoan tk = new TaiKhoan();
            tk.setHoTen(hoTen);
            tk.setSoDienThoai(sdt);
            tk.setEmail(email);
            tk.setMatKhau(matKhau);
            tk.setNgaySinh(ngaySinh);
            tk.setGioiTinh(gioiTinh);
            tk.setVaiTro("Bệnh nhân");
            tk.setTrangThai("Hoạt động");
            
            BenhNhan bn = new BenhNhan();
            // Gọi dao để lưu vào Database
            TaiKhoanDAO tkdao = new TaiKhoanDAO();
            boolean isSuccess = tkdao.themTaiKhoanBenhNhan(tk, bn);
            
            // Xóa Session dọn dẹp bộ nhớ
            if(isSuccess){
                session.removeAttribute("TEMP_HoTen");
                session.removeAttribute("TEMP_SDT");
                session.removeAttribute("TEMP_Email");
                session.removeAttribute("TEMP_MatKhau");
                session.removeAttribute("TEMP_NgaySinh");
                session.removeAttribute("TEMP_GioiTinh");
                session.removeAttribute("VERIFY_OTP");
            
                // Thành công -> In chữ SUCCESS để JavaScript chuyển sang trang Đăng nhập
                response.getWriter().write("SUCCESS");
            }
            else
                response.getWriter().write("ERROR_DB");
        }
        else{
            // Sai mã OTP thì in chữ ERROR
            response.getWriter().write("ERROR_OTP");
        }
    }
}