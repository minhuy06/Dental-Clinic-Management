package com.dentalclinic.controller;

import com.dentalclinic.dao.TaiKhoanDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeleteAccountServlet", urlPatterns = {"/api/accounts/delete"})
public class SoftDeleteAccountServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Thiếu ID tài khoản!\"}");
                return;
            }
            
            int id = Integer.parseInt(idParam);
            TaiKhoanDAO dao = new TaiKhoanDAO();
            boolean isSuccess = dao.xoaTaiKhoan(id);
            
            if (isSuccess) {
                out.print("{\"success\": true, \"message\": \"Tài khoản đã được xóa thành công!\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"Không tìm thấy tài khoản để xóa!\"}");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Lỗi Server: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }
}
