/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.TaiKhoan;
import java.io.IOException;
import java.util.ArrayList;
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
@WebServlet("/doctorM")
public class QuanLyBacSiServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException{
        HttpSession session =req.getSession(false);
        TaiKhoan tk = (session != null) ? (TaiKhoan) session.getAttribute("accountLogan") : null;
        
        if(tk == null || !"Quản trị viên".equalsIgnoreCase(tk.getVaiTro())){
            res.setContentType("text/html;charset=UTF-8");
            res.getWriter().write("<h3 style='color:red;'>Lỗi 403: Bro không có quyền xem danh sách này!</h3>");
            return;
        }
        
        TaiKhoanDAO tkd = new TaiKhoanDAO();
        ArrayList<BacSi> danhSachbs = tkd.layDanhSachBacSi();
        
        req.setAttribute("listBS", danhSachbs);
        req.getRequestDispatcher("/hospitalmanager/components/bacsi.jsp").forward(req, res);
    }
}
