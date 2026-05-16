package com.dentalclinic.controller;

import com.dentalclinic.model.LichHen;
import com.dentalclinic.service.LichHenService;
import com.dentalclinic.utils.InforPageHelper;
import com.dentalclinic.utils.RoleNavHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dat-lich")
public class BookingServlet extends HttpServlet {
    
    private final LichHenService lhService = new LichHenService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Thiết lập Tiếng Việt cho dữ liệu gửi lên từ Form
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // 3. Đọc dữ liệu từ Form dat-lich.jsp
            String ngayStr = request.getParameter("ngayKham"); // yyyy-MM-dd
            String gioStr = request.getParameter("gioKham");   // HH:mm
            String ghiChu = request.getParameter("ghiChu");
            String[] dichVuIds = request.getParameterValues("dichVu"); 

            // 4. Đóng gói vào đối tượng Model LichHen
            LichHen lh = new LichHen();
            lh.setBenhNhanID(1);
            lh.setNgayKham(java.sql.Date.valueOf(ngayStr));
            lh.setGioKham(java.sql.Time.valueOf(gioStr + ":00"));
            lh.setGhiChu(ghiChu);
            
            // Trạng thái mặc định ban đầu
            lh.setTrangThai("Chờ xác nhận");
            // Mặc định phòng chờ số 1 (Hoặc logic tự động của bạn)
            lh.setPhongID(1);

            // Chuyển mảng String ID dịch vụ sang List<Integer>
            List<Integer> listDV = new ArrayList<>();
            if (dichVuIds != null) {
                for (String id : dichVuIds) {
                    listDV.add(Integer.parseInt(id));
                }
            }

            // 5. Gọi Service xử lý logic (Tìm bác sĩ rảnh và lưu vào DB)
            String result = lhService.createBooking(lh, listDV);

            if ("SUCCESS".equals(result)) {
                response.sendRedirect(RoleNavHelper.getScheduleUrl(request.getContextPath()) + "?booked=1");
            } else {
                forwardScheduleError(request, response, result);
            }

        } catch (IllegalArgumentException e) {
            forwardScheduleError(request, response, "Ngày hoặc giờ khám không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            forwardScheduleError(request, response, "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private void forwardScheduleError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("scrollSection", "datlich");
        request.setAttribute("serviceListJson", InforPageHelper.buildServiceListJson());
        request.setAttribute("doctorListJson", InforPageHelper.buildSampleDoctorListJson());
        request.setAttribute("pageTitle", "Đặt lịch khám - Nha Khoa 5AE");
        request.getRequestDispatcher("/dat-lich.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(RoleNavHelper.getScheduleUrl(request.getContextPath()));
    }
}