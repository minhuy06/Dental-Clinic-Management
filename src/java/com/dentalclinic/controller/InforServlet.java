package com.dentalclinic.controller;

import com.dentalclinic.utils.InforPageHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Trang gộp: Đặt lịch + Dịch vụ + Bác sĩ (một trang, cuộn tới mục theo URL).
 */
@WebServlet("/Infor/*")
public class InforServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String section = InforPageHelper.resolveSection(request.getPathInfo());
        if (section == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("scrollSection", section);
        request.setAttribute("serviceListJson", InforPageHelper.buildServiceListJson());
        request.setAttribute("doctorListJson", InforPageHelper.buildSampleDoctorListJson());
        request.setAttribute("pageTitle", "Đặt lịch, Dịch vụ & Bác sĩ - Nha Khoa 5AE");

        request.getRequestDispatcher("/dat-lich.jsp").forward(request, response);
    }
}
