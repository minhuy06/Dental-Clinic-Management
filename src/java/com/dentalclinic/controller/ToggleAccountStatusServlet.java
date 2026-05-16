package com.dentalclinic.controller;

import com.dentalclinic.service.TaiKhoanService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ToggleAccountStatusServlet", urlPatterns = {"/api/accounts/toggle-status"})
public class ToggleAccountStatusServlet extends HttpServlet {
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            // Đọc dữ liệu JSON từ Request
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            // Sử dụng Gson để phân tích cú pháp sang JsonObject
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
            
            if (jsonObject.has("id") && jsonObject.has("status")){
                int id = jsonObject.get("id").getAsInt();
                String status = jsonObject.get("status").getAsString();
                
                // Chuyển giao tham số xuống lớp Nghiệp vụ xử lý
                TaiKhoanService service = new TaiKhoanService();
                boolean isSuccess = service.doiTrangThaiTaiKhoan(id, status);
                
                // Trả kết quả JSON
                out.print("{\"success\": " + isSuccess + "}");
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Dữ liệu yêu cầu không hợp lệ!\"}");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Lỗi máy chủ: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }  
    }
}
