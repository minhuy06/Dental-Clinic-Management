package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ho-so")
public class HosoServlet extends HttpServlet {
    private LichHenDAO lhDAO = new LichHenDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        // Lấy ID người dùng từ session sau khi đăng nhập
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy danh sách Object trực tiếp
        List<LichHen> list = lhDAO.getLichHenByBenhNhan(userId);
        
        // Đẩy List vào attribute
        request.setAttribute("listLichHen", list);
        
        // Forward sang trang hoso.jsp
        request.getRequestDispatcher("hoso.jsp").forward(request, response);
    }
}