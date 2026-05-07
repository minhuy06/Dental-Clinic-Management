package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/reception/dashboard")
public class ReceptionServlet extends HttpServlet {
    private LichHenDAO lhDAO = new LichHenDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Lấy toàn bộ lịch hẹn trong hệ thống
        List<LichHen> allLichHen = lhDAO.getAllLichHen();
        
        // Gửi List sang index.jsp (Dùng JSTL <c:forEach> để hiển thị)
        request.setAttribute("listLichHen", allLichHen);
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}