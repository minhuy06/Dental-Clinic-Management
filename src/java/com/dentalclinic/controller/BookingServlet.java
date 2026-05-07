package com.dentalclinic.controller;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/dat-lich")
public class BookingServlet extends HttpServlet {
    private BacSiDAO bsDAO = new BacSiDAO();
    private LichHenDAO lhDAO = new LichHenDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Giả sử bạn đã có BenhNhan trong Session sau khi đăng nhập
        HttpSession session = request.getSession();
        Integer benhNhanID = (Integer) session.getAttribute("userId"); 
        
        if (benhNhanID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin từ form
        String ngayStr = request.getParameter("ngayKham");
        String gioStr = request.getParameter("gioKham");
        String ghiChu = request.getParameter("ghiChu");
        String[] dichVuIds = request.getParameterValues("dichVu"); // Array các checkbox ID

        // 1. Tìm bác sĩ rảnh
        List<Integer> ranhIds = bsDAO.getAvailableDoctorIds(ngayStr, gioStr);
        
        if (ranhIds.isEmpty()) {
            request.setAttribute("error", "Không còn bác sĩ rảnh ca này!");
            request.getRequestDispatcher("booking.jsp").forward(request, response);
            return;
        }

        // 2. Tạo đối tượng LichHen
        LichHen lh = new LichHen();
        lh.setBenhNhanID(benhNhanID);
        lh.setBacSiID(ranhIds.get(0)); // Tự động gán bác sĩ đầu tiên rảnh
        lh.setNgayKham(java.sql.Date.valueOf(ngayStr));
        lh.setGioKham(java.sql.Time.valueOf(gioStr + ":00"));
        lh.setGhiChu(ghiChu);
        lh.setTrangThai("Chờ duyệt");
        lh.setPhongID(1);

        // Chuyển mảng ID dịch vụ sang List<Integer>
        List<Integer> listDV = new ArrayList<>();
        if (dichVuIds != null) {
            for (String id : dichVuIds) listDV.add(Integer.parseInt(id));
        }

        // 3. Lưu vào DB
        int result = lhDAO.insertBooking(lh, listDV);

        if (result != -1) {
            response.sendRedirect("success.jsp");
        } else {
            request.setAttribute("error", "Lỗi hệ thống!");
            request.getRequestDispatcher("booking.jsp").forward(request, response);
        }
    }
}