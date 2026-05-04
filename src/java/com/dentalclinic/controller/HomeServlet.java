/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.DichVu;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kinhm
 */
@WebServlet("/datlich")
public class HomeServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException{
        DichVuDAO dvd = new DichVuDAO();
        BacSiDAO bsd = new BacSiDAO();
        
        List<DichVu> dsDichVu= dvd.getAllDichVu();
        List<BacSi> dsBacSi = bsd.layDanhSachBacSi();
        
        req.setAttribute("listDV", dsDichVu);
        req.setAttribute("listBS", dsBacSi);
        
        req.getRequestDispatcher("dat-lich.jsp").forward(req, res);
    }
}
