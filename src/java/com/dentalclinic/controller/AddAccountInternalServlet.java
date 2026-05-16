package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoanBsLtDTO;
import com.dentalclinic.dao.TaiKhoanDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

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
            
            // Xử lý ảnh đại diện
            String avatarData = dto.getAnhDaiDien();
            if (avatarData != null && avatarData.startsWith("data:image")) // JS gửi file ảnh dưới dạng chuỗi văn bản (data:image/jpeg;base64, /9j/4AAQSkZJ...)
            {
                String[] parts = avatarData.split(",");
                if (parts.length > 1){
                    String base64Image = parts[1];
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                    // Đặt tên cho file ảnh
                    String fileName = "avatar_" + dto.getSoDienThoai() + ".jpg";
                    
                    // Đường dẫn đến ảnh trên Server Tomcat
                    String serverPath = request.getServletContext().getRealPath("") + java.io.File.separator + "img";
                    saveToFile(serverPath,fileName, imageBytes);
                    
                    // Lưu File ảnh vào thư mục img trong dự án
                    String sourcePath = System.getenv("CLINIC_IMG_PATH");
                    if (sourcePath != null && !sourcePath.trim().isEmpty()){
                        saveToFile(sourcePath, fileName, imageBytes);
                        System.out.println("Đã sao lưu ảnh thành công về NetBeans: " + sourcePath);
                    }
                    else{
                        System.out.println("Máy này chưa cài đặt biến CLINIC_IMG_PATH. Ảnh chỉ lưu tạm trên Server!");
                    }
                    dto.setAnhDaiDien("assets/img/doctors/" +fileName);
                }
            }
            else
                dto.setAnhDaiDien("assets/img/doctors/default-avatar.jpg");
            
            TaiKhoanDAO dao = new TaiKhoanDAO();
            boolean isSuccess = dao.themTaiKhoanNhanSu(dto);
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
    
    private void saveToFile(String path, String fileName, byte[] data){
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // Tự động tạo thư mục img bên trong build/web nếu chưa có
        }
        
        File file = new File(path + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)){
            fos.write(data);
        }
        catch (Exception e){
            System.out.println("Lỗi lưu file tại: " + path + " - Chi tiết: " + e.getMessage());
        }
    }
}
