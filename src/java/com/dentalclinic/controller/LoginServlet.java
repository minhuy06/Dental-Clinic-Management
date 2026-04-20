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
import com.dentalclinic.service.TaiKhoanService;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kinhm
 */
//Điều khiển yêu cầu đăng nhập từ user
@WebServlet("/account/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException {
        //
        String phone = req.getParameter("userPhone");
        String password = req.getParameter("userPassword");
        TaiKhoanService tks =new TaiKhoanService();
        TaiKhoan accountLogin = tks.kiemTraTaiKhoan(phone, password);
        if(accountLogin != null){
            HttpSession session = req.getSession();
            session.setAttribute("accountLogan",accountLogin);
            //Check vai trò
            System.out.println("DEBUG: Da tao session voi key 'accountLogan' cho sdt: " + accountLogin.getSoDienThoai());
            if("Bác sĩ".equalsIgnoreCase(accountLogin.getVaiTro()))
            {
                res.sendRedirect(req.getContextPath() +"/doctor/doctor.jsp");
            }
            else if("Quản trị viên".equalsIgnoreCase(accountLogin.getVaiTro()))
            {
                res.sendRedirect(req.getContextPath() + "/admin/create-staff.jsp");
            } 
            else if("Lễ tân".equalsIgnoreCase(accountLogin.getVaiTro())) {
                res.sendRedirect(req.getContextPath() + "/reception/index.jsp");
            }
            else {
                res.sendRedirect(req.getContextPath() +"/index.jsp");
            }
        }
        else {
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().print("ERROR");
        }
    }
}
