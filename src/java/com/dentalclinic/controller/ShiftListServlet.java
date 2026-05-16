package com.dentalclinic.controller;

import com.dentalclinic.dao.LichLamViecDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ShiftListServlet", urlPatterns = {"/api/shifts"})
public class ShiftListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            LichLamViecDAO dao = new LichLamViecDAO();
            // Lấy chuỗi JSON đã được dựng sẵn từ tầng DAO
            String jsonResult = dao.layDanhSachCaLam();
            // Xuất chuỗi này về Front-end
            out.print(jsonResult);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("[]");
        } finally {
            out.close();
        }
    }
}
