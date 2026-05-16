package com.dentalclinic.controller;

import com.dentalclinic.dao.BenhNhanDAO;
import com.dentalclinic.dao.LeTanDAO;
import com.dentalclinic.dao.ReceptionReportDAO;
import com.dentalclinic.model.TaiKhoan;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Trang lễ tân: /reception-patient, /reception-report (thay cho JSP trực tiếp).
 */
@WebServlet(urlPatterns = {"/reception-patient", "/reception-report"})
public class ReceptionPagesServlet extends HttpServlet {

    private final BenhNhanDAO benhNhanDAO = new BenhNhanDAO();
    private final LeTanDAO leTanDAO = new LeTanDAO();
    private final ReceptionReportDAO reportDAO = new ReceptionReportDAO();
    private final Gson gson = new Gson();

    private static TaiKhoan requireReception(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        TaiKhoan u = (TaiKhoan) session.getAttribute("loggedInUser");
        if (u == null || !"Lễ tân".equalsIgnoreCase(u.getVaiTro())) {
            return null;
        }
        return u;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requireReception(request) == null) {
            response.sendRedirect(request.getContextPath() + "/account/login.jsp");
            return;
        }

        String path = request.getServletPath();
        if ("/reception-patient".equals(path)) {
            List<BenhNhanDAO.ReceptionPatientRow> rows;
            try {
                rows = benhNhanDAO.listReceptionPatients();
            } catch (Exception ex) {
                ex.printStackTrace();
                rows = Collections.emptyList();
            }
            request.setAttribute("patientListJson", gson.toJson(rows));
            request.setAttribute("patientDbCount", rows.size());
            request.getRequestDispatcher("/reception/benhnhan.jsp").forward(request, response);
        } else if ("/reception-report".equals(path)) {
            TaiKhoan user = requireReception(request);
            Integer leTanId = user != null ? leTanDAO.findLeTanIdByTaiKhoanId(user.getTaiKhoanID()) : null;
            Map<String, Object> reportPayload;
            try {
                reportPayload = reportDAO.buildDashboardPayload(reportDAO.fetchPaidInvoices(leTanId));
            } catch (Exception ex) {
                ex.printStackTrace();
                reportPayload = reportDAO.buildDashboardPayload(Collections.emptyList());
            }
            request.setAttribute("reportDataJson", gson.toJson(reportPayload));
            request.getRequestDispatcher("/reception/baocao.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
