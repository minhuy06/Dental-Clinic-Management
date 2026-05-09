package com.dentalclinic.controller;

import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.service.LichHenService;
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
    private final DichVuDAO dichVuDAO = new DichVuDAO();

    private static String jsonEscape(String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\r", "\\r")
                  .replace("\n", "\\n")
                  .replace("\t", "\\t");
    }

    private static String buildServiceListJson(List<DichVu> services) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < services.size(); i++) {
            DichVu dv = services.get(i);
            if (i > 0) sb.append(",");
            sb.append("{")
              .append("\"id\":").append(dv.getDichVuID()).append(",")
              .append("\"name\":\"").append(jsonEscape(dv.getTenDichVu())).append("\",")
              .append("\"desc\":\"").append(jsonEscape(dv.getTenDichVu())).append("\",")
              .append("\"time\":\"").append(dv.getThoiLuongDuKien()).append(" phút\",")
              .append("\"price\":").append((long) dv.getGiaTien()).append(",")
              .append("\"cat\":\"all\",")
              .append("\"perUnit\":").append(dv.isTinhTheoRang());
            if (dv.isTinhTheoRang()) {
                sb.append(",\"unit\":\"răng\"");
            }
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }

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
                response.sendRedirect(request.getContextPath() + "/dat-lich?booked=1");
            } else {
                request.setAttribute("error", result);
                request.setAttribute("serviceListJson", buildServiceListJson(dichVuDAO.getAll()));
                request.getRequestDispatcher("/dat-lich.jsp").forward(request, response);
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Ngày hoặc giờ khám không hợp lệ!");
            request.setAttribute("serviceListJson", buildServiceListJson(dichVuDAO.getAll()));
            request.getRequestDispatcher("/dat-lich.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("serviceListJson", buildServiceListJson(dichVuDAO.getAll()));
            request.getRequestDispatcher("/dat-lich.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("serviceListJson", buildServiceListJson(dichVuDAO.getAll()));
        request.getRequestDispatcher("/dat-lich.jsp").forward(request, response);
    }
}