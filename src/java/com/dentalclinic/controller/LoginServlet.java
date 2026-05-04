/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.service.TaiKhoanService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kinhm
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override 
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException {
       
        String phone = req.getParameter("userPhone");
        String password = req.getParameter("userPassword");
        TaiKhoanService tks = new TaiKhoanService();
        TaiKhoan tk = tks.getAccount(phone, password);
        
        if(tk != null){
            HttpSession session = req.getSession();
            session.setAttribute("loggedInUser",tk);
            
            if("Bác sĩ".equalsIgnoreCase(tk.getVaiTro()))
            {
                res.sendRedirect(req.getContextPath() +"/doctor/index.jsp");
            }
            else if("Quản trị viên".equalsIgnoreCase(tk.getVaiTro()))
            {
                res.sendRedirect(req.getContextPath() + "/admin/index.jsp");
            } 
            else if("Lễ tân".equalsIgnoreCase(tk.getVaiTro())){
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
