package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoanBsLtDTO;
import com.dentalclinic.service.TaiKhoanService;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UpdateAccountServlet", urlPatterns = {"/api/accounts/update"})
public class UpdateAccountInternalServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            
            // Chuyển đổi dữ liệu JSON thành đối tượng DTO
            Gson gson = new Gson();
            TaiKhoanBsLtDTO dto = gson.fromJson(sb.toString(), TaiKhoanBsLtDTO.class);
            
            // Cấu hình đường dẫn hệ thống
            String serverPath = request.getServletContext().getRealPath("") + File.separator + "img";
            String sourcePath = System.getenv("CLINIC_IMG_PATH");
            
            // Chuyển cho Service xử lý cập nhật
            TaiKhoanService service = new TaiKhoanService();
            boolean isSuccess = service.capNhatTaiKhoan(dto, serverPath, sourcePath);
            
            out.print("{\"success\": " + isSuccess + "}");
        }
        catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Lỗi cập nhật Server: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }
}
