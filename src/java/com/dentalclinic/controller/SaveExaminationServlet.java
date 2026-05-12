package com.dentalclinic.controller;

import com.dentalclinic.dao.PhieuKhamDAO;
import com.dentalclinic.model.PhieuKham;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import java.io.PrintWriter;

@WebServlet(name = "SaveExaminationServlet", urlPatterns = {"/api/doctor/save-examination"})
public class SaveExaminationServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        // Cấu hình UTF-8 để không bị lỗi font tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            // Đọc dữ liệu JSON gửi từ Servlet
            BufferedReader reader = request.getReader();
            
            // Dùng GSON biến json thành đối tượng PhieuKham
            Gson gson = new Gson();
            PhieuKham phieukham = gson.fromJson(reader, PhieuKham.class);
            
            // Validate            
            if (phieukham.getLichHenID() <= 0){
                out.print("{\"success\": false, \"message\": \"Lỗi: Không xác định được Lịch Hẹn!\"}");
                return;
            }
            if (phieukham.getDanhSachDichVu() == null || phieukham.getDanhSachDichVu().isEmpty()){
                out.print("{\"success\": false, \"message\": \"Vui lòng chỉ định ít nhất một dịch vụ lâm sàng!\"}");
                return;
            }
            
            // Đưa đối tượng xuống Db
            PhieuKhamDAO dao = new PhieuKhamDAO();
            boolean isSuccess = dao.luuPhieuKhamLamSang(phieukham);
            
            // Trả về kết quả cho js
            if(isSuccess)
                System.out.print("{\"success\": true, \"message\": \"Lưu phiếu khám thành công!\"}");
            else
                System.out.print("{\"success\": false, \"message\": \"Lỗi: Không thể lưu vào CSDL.\"}");
            
        } catch(Exception e){
            e.printStackTrace();
            System.out.print("{\"success\": false, \"message\": \"Lỗi Server: " + e.getMessage() + "\"}");
            
        } finally{
            out.close();
        }
    }
}
