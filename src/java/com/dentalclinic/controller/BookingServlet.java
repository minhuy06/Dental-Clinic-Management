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
    
    // Sử dụng Service thay vì gọi trực tiếp nhiều DAO
    private LichHenService lhService = new LichHenService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Thiết lập Tiếng Việt cho dữ liệu từ Form gửi lên
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 2. Kiểm tra đăng nhập (Lấy ID bệnh nhân từ session)
        HttpSession session = request.getSession();
        Integer benhNhanID = (Integer) session.getAttribute("userId"); 
        
        if (benhNhanID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 3. Đọc dữ liệu từ Form JSP
        String ngayStr = request.getParameter("ngayKham"); // Định dạng yyyy-MM-dd
        String gioStr = request.getParameter("gioKham");   // Định dạng HH:mm
        String ghiChu = request.getParameter("ghiChu");
        String[] dichVuIds = request.getParameterValues("dichVu"); 

        try {
            // 4. Tạo và đóng gói dữ liệu vào đối tượng Model
            LichHen lh = new LichHen();
            lh.setBenhNhanID(benhNhanID);
            lh.setNgayKham(java.sql.Date.valueOf(ngayStr));
            // Thêm ":00" để đúng định dạng HH:mm:ss của java.sql.Time
            lh.setGioKham(java.sql.Time.valueOf(gioStr + ":00"));
            lh.setGhiChu(ghiChu);
            lh.setTrangThai("Chờ duyệt"); // Trạng thái mặc định

            // Chuyển mảng String ID sang List<Integer>
            List<Integer> listDV = new ArrayList<>();
            if (dichVuIds != null) {
                for (String id : dichVuIds) {
                    listDV.add(Integer.parseInt(id));
                }
            }

            // 5. Gọi Service xử lý (Service sẽ tự tìm bác sĩ rảnh và lưu vào DB)
            String result = lhService.createBooking(lh, listDV);

            if ("SUCCESS".equals(result)) {
                // Đặt lịch thành công, chuyển hướng đến trang thông báo
                response.sendRedirect("booking-success.jsp");
            } else {
                // Thất bại (hết bác sĩ, lỗi DB...), quay lại trang đặt lịch kèm thông báo lỗi
                request.setAttribute("error", result);
                request.getRequestDispatcher("booking.jsp").forward(request, response);
            }

        } catch (IllegalArgumentException e) {
            // Lỗi định dạng ngày giờ không khớp
            request.setAttribute("error", "Định dạng ngày hoặc giờ không hợp lệ!");
            request.getRequestDispatcher("booking.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra trong quá trình xử lý!");
            request.getRequestDispatcher("booking.jsp").forward(request, response);
        }
    }
}