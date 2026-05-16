package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
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
    private final LichHenDAO lichHenDAO = new LichHenDAO();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            String idParam = request.getParameter("id");
            if(idParam == null || idParam.trim().isEmpty()){
                out.print("{\"success\": false, \"message\": \"Thiếu mã lịch hẹn!\"}");
                return;
            }
            
            int lichHenID = Integer.parseInt(idParam);
            // Lấy thông tin Lịch Hẹn lồng nhau (LichHen -> BenhNhan -> HoSo)
            LichHen lh = lichHenDAO.layChiTietLichHen(lichHenID);
            
            if(lh != null){
                String jsonData = gson.toJson(lh);
                out.print("{\"success\": true, \"data\": " + jsonData + "}");
            }
            else{
                out.print("{\"success\": false, \"message\": \"Không tìm thấy thông tin lịch hẹn!\"}");
            }
        } catch(Exception e){
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Lỗi Server: " + e.getMessage() + "\"}");
        } finally{
            out.flush();
        }
    }
}
