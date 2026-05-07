package com.dentalclinic.controller;

import com.dentalclinic.dao.LichLamViecDAO;
import com.dentalclinic.model.LichLamViec;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/dashboard")
public class AdminServlet extends HttpServlet {
    private LichLamViecDAO llvDAO = new LichLamViecDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Lấy danh sách trực tiếp từ DAO
        List<LichLamViec> list = llvDAO.getAllForAdmin();

        // 2. Đẩy danh sách dưới dạng List vào request
        request.setAttribute("listLich", list);

        // 3. Chuyển hướng sang trang admin.jsp
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }
}