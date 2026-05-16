package com.dentalclinic.controller;

import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.TaiKhoanBsLtDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AccountListServlet", urlPatterns = {"/api/accounts"})
public class AccountListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException{
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            TaiKhoanDAO dao = new TaiKhoanDAO();
            List<TaiKhoanBsLtDTO> list = dao.layThongTinTaiKhoan();
            
            // Map dữ liệu từ Java sang định dạng JSON của admin.js
            JsonArray jsonArray = new JsonArray();
            for(TaiKhoanBsLtDTO tk : list){
                JsonObject obj = new JsonObject();
                obj.addProperty("id", tk.getTaiKhoanID());
                obj.addProperty("name", tk.getHoTen());
                obj.addProperty("phone", tk.getSoDienThoai());
                obj.addProperty("dob", tk.getNgaySinh());
                obj.addProperty("gender", tk.isGioiTinh() ? "male" : "female");
                obj.addProperty("avatar", tk.getAnhDaiDien());
                obj.addProperty("degree", tk.getTrinhDo() != null ? tk.getTrinhDo() : "");
                obj.addProperty("specialty", tk.getTenChuyenKhoa());
                
                String roleJS = "customer";
                if ("Bác sĩ".equals(tk.getVaiTro())) roleJS = "doctor";
                else if("Lễ tân".equals(tk.getVaiTro())) roleJS = "staff";
                else if("Quản trị viên".equals(tk.getVaiTro())) roleJS = "admin";
                obj.addProperty("role", roleJS);
                
                String statusJS = "Hoạt động".equals(tk.getTrangThai()) ? "active" : "inactive";
                obj.addProperty("status", statusJS); 
                
                jsonArray.add(obj);
            }
            // Xuất chuỗi JSON về giao diện
            out.print(jsonArray.toString());
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
