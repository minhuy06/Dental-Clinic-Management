package com.dentalclinic.controller;

import com.dentalclinic.utils.InforPageHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Trang thông tin công khai: Đặt lịch, Dịch vụ, Bác sĩ.
 * /Infor/Schedule | /Infor/service | /Infor/Doctor
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

        request.setAttribute("inforSection", section);
        request.setAttribute("serviceListJson", InforPageHelper.buildServiceListJson());
        request.setAttribute("doctorListJson", InforPageHelper.buildDoctorListJson());

        if ("datlich".equals(section)) {
            request.setAttribute("pageTitle", "Đặt lịch khám - Nha Khoa 5AE");
        } else if ("dichvu".equals(section)) {
            request.setAttribute("pageTitle", "Dịch vụ & Bảng giá - Nha Khoa 5AE");
        } else {
            request.setAttribute("pageTitle", "Đội ngũ bác sĩ - Nha Khoa 5AE");
        }

        request.getRequestDispatcher("/dat-lich.jsp").forward(request, response);
    }
}
