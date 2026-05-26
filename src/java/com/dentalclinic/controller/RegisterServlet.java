package com.dentalclinic.controller;

import com.dentalclinic.dao.BenhNhanDAO;
import com.dentalclinic.dao.BenhNhanDAO.ReceptionPatientErrors;
import com.dentalclinic.dao.BenhNhanDAO.ReceptionPatientInput;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Đăng ký bệnh nhân: bước 1 lưu form vào session (hiện popup OTP giả lập),
 * bước 2 nhập mã 123456 để tạo tài khoản.
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    /** Mã OTP giả lập — không gửi SMS. */
    public static final String MOCK_OTP = "123456";
    private static final String SESSION_PENDING = "pendingPatientRegistration";

    private final BenhNhanDAO benhNhanDAO = new BenhNhanDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String action = trim(request.getParameter("action"));
        if ("verify".equalsIgnoreCase(action)) {
            response.getWriter().write(verifyAndCreate(request));
        } else {
            response.getWriter().write(prepareRegistration(request));
        }
    }

    private String prepareRegistration(HttpServletRequest request) {
        String hoTen = trim(request.getParameter("txtHoTen"));
        String sdt = trim(request.getParameter("txtSDT"));
        String matKhau = request.getParameter("txtMatKhau");
        String ngaySinh = trim(request.getParameter("txtNgaySinh"));
        String gioiTinh = trim(request.getParameter("txtGioiTinh"));

        if (hoTen.isEmpty() || sdt.isEmpty() || matKhau == null || matKhau.isBlank()) {
            return "INVALID";
        }
        if (!sdt.matches("^0[0-9]{9}$")) {
            return "INVALID_PHONE";
        }
        if (benhNhanDAO.phoneExists(sdt)) {
            return "PHONE_EXISTS";
        }

        PendingRegistration pending = new PendingRegistration();
        pending.hoTen = hoTen;
        pending.soDienThoai = sdt;
        pending.matKhau = matKhau;
        pending.ngaySinh = ngaySinh;
        pending.gioiTinh = gioiTinh.isEmpty() ? "1" : gioiTinh;

        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_PENDING, pending);
        return "SUCCESS";
    }

    private String verifyAndCreate(HttpServletRequest request) {
        String otp = trim(request.getParameter("txtOTP"));
        if (!MOCK_OTP.equals(otp)) {
            return "FAILED";
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "EXPIRED";
        }
        PendingRegistration pending = (PendingRegistration) session.getAttribute(SESSION_PENDING);
        if (pending == null) {
            return "EXPIRED";
        }

        if (benhNhanDAO.phoneExists(pending.soDienThoai)) {
            session.removeAttribute(SESSION_PENDING);
            return "PHONE_EXISTS";
        }

        try {
            ReceptionPatientInput in = new ReceptionPatientInput();
            in.fullName = pending.hoTen;
            in.phone = pending.soDienThoai;
            in.password = pending.matKhau;
            in.ngaySinh = pending.ngaySinh;
            in.gender = "0".equals(pending.gioiTinh) || "false".equalsIgnoreCase(pending.gioiTinh) ? "Nữ" : "Nam";

            ReceptionPatientErrors result = benhNhanDAO.createReceptionPatient(in);
            session.removeAttribute(SESSION_PENDING);
            if (result != null && result.isSuccess()) {
                return "SUCCESS";
            }
            if (result != null && result.getMessage() != null && result.getMessage().contains("điện thoại")) {
                return "PHONE_EXISTS";
            }
            return "FAILED";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAILED";
        }
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    public static class PendingRegistration implements Serializable {
        private static final long serialVersionUID = 1L;
        public String hoTen;
        public String soDienThoai;
        public String matKhau;
        public String ngaySinh;
        public String gioiTinh;
    }
}
