package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Đường dẫn này dùng để gõ trên trình duyệt: localhost:8080/TenProject/test-list
@WebServlet("/test-list")
public class TestListServlet extends HttpServlet {
    
    // Đừng new ở đây nếu hay bị lỗi, hãy khai báo thôi
    private LichHenDAO lhDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Khởi tạo ở đây để nếu lỗi thì nó hiện ra log rõ ràng
            lhDAO = new LichHenDAO(); 
            List<LichHen> list = lhDAO.getAllLichHen();
            
            request.setAttribute("allLichHen", list);
            request.getRequestDispatcher("/reception/list-lich-hen.jsp").forward(request, response);
        } catch (Exception e) {
            // Nếu lỗi, nó sẽ in ra Console chính xác lỗi gì ở dòng nào
            e.printStackTrace(); 
            response.getWriter().println("Loi khoi tao: " + e.getMessage());
        }
    }
}