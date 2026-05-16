package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoan;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Đánh dấu những khu vực cần bảo vệ. Bất cứ ai truy cập vào các đường dẫn này đều bị chặn lại kiểm tra.
@WebFilter(urlPatterns = {"/admin/*", "/doctor/*", "/reception/*", "/patient/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        // Khởi tạo filter 
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        // 1. Kiểm tra đăng nhập
        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        
        if (!isLoggedIn) {
            // Chưa đăng nhập
            res.sendRedirect(req.getContextPath() + "/account/login.jsp");
            return;
        }
        
        // 2. Nếu đã đăng nhập, lấy thông tin tài khoản và quyền ra kiểm tra
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        String role = user.getVaiTro();
        String requestURI = req.getRequestURI();
        
        // 3. Phân quyền chi tiết dựa trên đường dẫn (URL)
        boolean isAuthorized = true;

        if (requestURI.contains("/admin/") && !"Quản trị viên".equalsIgnoreCase(role)) {
            isAuthorized = false;
        } else if (requestURI.contains("/doctor/") && !"Bác sĩ".equalsIgnoreCase(role)) {
            isAuthorized = false;
        } else if (requestURI.contains("/reception/") && !"Lễ tân".equalsIgnoreCase(role)) {
            isAuthorized = false;
        } else if (requestURI.contains("/patient/") && !"Bệnh nhân".equalsIgnoreCase(role)) {
            isAuthorized = false;
        }

        if (!isAuthorized) {
            res.sendRedirect(req.getContextPath() + "/403.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Hủy filter
    }
}
