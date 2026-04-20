/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoan;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kinhm
 */
@WebFilter("/*")
public class AuthorizationFilter implements Filter{
    @Override
    public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain) throws IOException,ServletException{
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();
        
        
        String path = request.getRequestURI().substring(request.getContextPath().length());
        
        //Các trang được truy cập dưới dạng chưa đăng nhập
        if (path.equals("/") || path.equals("/index.jsp") || 
            path.startsWith("/account/login.jsp") || 
            path.startsWith("/account/login") || 
            path.startsWith("/assets/") || // CSS, JS, Images
            path.startsWith("/css/") || 
            path.startsWith("/js/")) {
            
            chain.doFilter(request, response);
            return;
        }
        
        TaiKhoan account = (session != null) ? (TaiKhoan) session.getAttribute("accountLogan") : null;
        
        //Ngăn truy cập các dịch vụ bắt buộc đăng nhập
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/account/login.jsp");
            return;
        }
        
        String vaiTro = account.getVaiTro().toUpperCase();

        // Ví dụ: Chỉ ADMIN mới được vào các trang bắt đầu bằng /admin/
        if (path.startsWith("/admin/") || path.startsWith("/hospitalmanager/")) {
            if ("Quản trị viên".equalsIgnoreCase(vaiTro)) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/error/error.jsp");
            }
            return;
        }

        // Ví dụ: Chỉ BAC_SI mới được vào khu vực chuyên môn
        if (path.startsWith("/doctor/")) {
            if ("Bác sĩ".equalsIgnoreCase(vaiTro) || "Quản trị viên".equals(vaiTro)) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/error/error.jsp");
            }
            return;
        }
        
        if (path.startsWith("/reception/")){
            if ("Lễ tân".equalsIgnoreCase(vaiTro)){
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/error/error.jsp");
            }
            return;
        }
        //Phân quyền cho bệnh nhân
        if(path.startsWith("/patient/") || path.equals("/index.jsp") || path.equals("/dat-lich.jsp")){
            if("Bệnh nhân".equalsIgnoreCase(vaiTro)){
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/error/error.jsp");
            }
            return;
        } 
        chain.doFilter(request, response);
        
    }
 }

