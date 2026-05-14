package com.dentalclinic.controller;

import com.dentalclinic.dao.AdminDashboardDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/admin", "/admin/dashboard"})
public class AdminServlet extends HttpServlet {
    private final AdminDashboardDAO adminDAO = new AdminDashboardDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        request.setAttribute("adminServicesJson", adminDAO.getServicesJson());
        request.setAttribute("adminAccountsJson", adminDAO.getAccountsJson());
        request.setAttribute("adminShiftsJson", adminDAO.getShiftsJson());
        request.setAttribute("adminRevenueJson", adminDAO.getRevenueJson());

        request.getRequestDispatcher("/admin/admin.jsp").forward(request, response);
    }
}