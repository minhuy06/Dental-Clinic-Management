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

@WebServlet(name = "AddAccountInternalServlet", urlPatterns = {"/api/accounts/add"})
public class AddAccountInternalServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); // Trả về JSON cho trình duyệt
        PrintWriter out = response.getWriter();
        try{
            // Đọc file JSON từ giao diện gửi xuống
            BufferedReader reader = request.getReader();  // Hứng json 
            StringBuilder sb = new StringBuilder();
            String line;
            
            // Chép thông tin vào StringBuilder
            while((line = reader.readLine()) != null)
                sb.append(line);
            
            // Gắn chuỗi vào đối tượng dto
            Gson gson = new Gson();
            TaiKhoanBsLtDTO dto = gson.fromJson(sb.toString(), TaiKhoanBsLtDTO.class);
            
            // Cấu hình đường dẫn hệ thống
            String serverPath = request.getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img" + File.separator + "doctors";
            String sourcePath = System.getenv("CLINIC_IMG_PATH");
            
            // Chuyển cho Service xử lý thêm
            TaiKhoanService service = new TaiKhoanService();
            boolean isSuccess = service.themTaiKhoanNhanSu(dto, serverPath, sourcePath);
            
            out.print("{\"success\": " + isSuccess + "}");
        }
        catch (Exception e){
            e.printStackTrace();
            out.print("{\"success\": false, \"Tin nhắn\": \"Lỗi Server: " + e.getMessage() + "\"}");
        }
        finally{
            out.close();
        }
    }
}
