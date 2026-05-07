package com.dentalclinic.controller;

import com.dentalclinic.model.LichHen;
import com.dentalclinic.service.LichHenService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/dat-lich")
public class BookingServlet extends HttpServlet {
    
    private LichHenService lhService = new LichHenService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Thiết lập Tiếng Việt cho dữ liệu gửi lên từ Form
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 2. Lấy ID người dùng từ Session (ID của bảng BenhNhan)
        HttpSession session = request.getSession();
        Integer bnID = (Integer) session.getAttribute("benhNhanId"); 
        
        // Nếu chưa đăng nhập hoặc không tìm thấy ID bệnh nhân
        if (bnID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 3. Đọc dữ liệu từ Form dat-lich.jsp
            String ngayStr = request.getParameter("ngayKham"); // yyyy-MM-dd
            String gioStr = request.getParameter("gioKham");   // HH:mm
            String ghiChu = request.getParameter("ghiChu");
            String[] dichVuIds = request.getParameterValues("dichVu"); 

            // 4. Đóng gói vào đối tượng Model LichHen
            LichHen lh = new LichHen();
            lh.setBenhNhanID(bnID);
            lh.setNgayKham(java.sql.Date.valueOf(ngayStr));
            lh.setGioKham(java.sql.Time.valueOf(gioStr + ":00"));
            lh.setGhiChu(ghiChu);
            
            // Trạng thái mặc định ban đầu
            lh.setTrangThai("Chờ duyệt");
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
                // ĐẶT THÀNH CÔNG: Chuyển hướng sang trang hồ sơ cá nhân để xem kết quả
                // Dùng Redirect để trang Hồ Sơ load lại dữ liệu mới nhất từ DB
                response.sendRedirect(request.getContextPath() + "/ho-so");
            } else {
                // THẤT BẠI: Quay lại trang đặt lịch kèm thông báo lỗi (Hết bác sĩ, trùng lịch...)
                request.setAttribute("error", result);
                request.getRequestDispatcher("dat-lich.jsp").forward(request, response);
            }

        } catch (IllegalArgumentException e) {
            // Lỗi khi parse ngày tháng hoặc giờ
            request.setAttribute("error", "Ngày hoặc giờ khám không hợp lệ!");
            request.getRequestDispatcher("dat-lich.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("dat-lich.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Nếu người dùng truy cập trực tiếp bằng link, chuyển về trang đặt lịch
        response.sendRedirect("dat-lich.jsp");
    }
}