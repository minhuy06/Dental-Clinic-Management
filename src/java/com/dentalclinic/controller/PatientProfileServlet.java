package com.dentalclinic.controller;

import com.dentalclinic.dao.HoSoDAO;
import com.dentalclinic.model.HoSo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/doctor/patient-profile")
public class PatientProfileServlet extends HttpServlet {
    private final HoSoDAO hoSoDAO = new HoSoDAO();
    // Sử dụng GsonBuilder để ép định dạng ngày tháng chuẩn yyyy-MM-dd khi chuyển thành JSON
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Cấu hình UTF-8 để không bị lỗi font tiếng Việt khi trả dữ liệu về Frontend
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            // Lấy ID lịch hẹn từ URL
            String idParam = request.getParameter("id");
            if(idParam == null || idParam.trim().isEmpty()){
                out.print("{\"success\": false, \"message\": \"Thiếu mã lịch hẹn!\"}");
                return;
            }
            
            int lichHenID = Integer.parseInt(idParam);
            // Gọi DAO lấy dữ liệu lồng 3 lớp (HoSo -> BenhNhan -> TaiKhoan)
            HoSo hs = hoSoDAO.layThongTinHoSo(lichHenID);
            
            if(hs != null){
                // Gson quét và vẽ ra cấu trúc JSON để trả về cho giao diện
                String jsonData = gson.toJson(hs);
                out.print("{\"success\": true, \"data\": " + jsonData + "}");
            }
            else{
                out.print("{\"success\": false, \"message\": \"Không tìm thấy thông tin hồ sơ cho lịch hẹn này!\"}");
            }
        } catch(Exception e){
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Lỗi Server: " + e.getMessage() + "\"}");
        } finally{
            out.flush();
        }
    }
}
