package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.DanhSachLichHenDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/doctor/appointments")
public class AppointmentListServlet extends HttpServlet {
    private final LichHenDAO lichHenDAO = new LichHenDAO();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            List<DanhSachLichHenDTO> danhsach = lichHenDAO.layDanhSachChoKhamNgayHienTai();
            String jsonData = gson.toJson(danhsach);
            out.print("{\"success\": true, \"data\": " + jsonData + "}");
        }
        catch(Exception e){
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Lỗi tải danh sách\"}");
        }
        finally{
            out.flush();
        }
    }
    
    // Cập nhật trạng thái thành "Đang khám"
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
            if (jsonObject == null || !jsonObject.has("id")) {
                out.print("{\"success\": false, \"message\": \"Thiếu mã lịch hẹn!\"}");
                return;
            }
            int lichHenID = jsonObject.get("id").getAsInt();
            boolean isSuccess = lichHenDAO.capNhatTrangThaiDangKham(lichHenID);
            if (isSuccess) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"...\"}");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Lỗi Server...\"}");
        } finally {
            out.flush();
}
    }
}
