package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/reception-dashboard") // Đây là đường dẫn (Link) để vào trang
public class ReceptionServlet extends HttpServlet {
    private LichHenDAO lhDAO = new LichHenDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Lấy danh sách lịch hẹn
        List<LichHen> list = lhDAO.getAllLichHen();
        
        // 2. Gửi dữ liệu sang JSP
        request.setAttribute("allLichHen", list);
        
        // 3. Mở file index.jsp (Giao diện lễ tân chuyên nghiệp)
request.getRequestDispatcher("/reception/index.jsp").forward(request, response);
    }
}