package com.dentalclinic.controller;

import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.dao.LichHenDAO.PatientApptService;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.TaiKhoan;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/hoso")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 1. Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        TaiKhoan loggedInUser = (TaiKhoan) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/account/login.jsp");
            return;
        }

        Gson gson = new Gson();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

        JsonObject userJson = new JsonObject();
        userJson.addProperty("name", loggedInUser.getHoTen());
        userJson.addProperty("phone", loggedInUser.getSoDienThoai());
        userJson.addProperty("dob", loggedInUser.getNgaySinh() != null ? loggedInUser.getNgaySinh().toString() : "");
        boolean gioiTinhDB = loggedInUser.isGioiTinh();
        String gioiTinhStr = (gioiTinhDB) ? "Nam" : "Nữ";
        userJson.addProperty("gender", gioiTinhStr);
        userJson.addProperty("avatar", "");

        JsonArray servicesJsonArray = new JsonArray();
        DichVuDAO dvDAO = new DichVuDAO();
        List<DichVu> listTatCaDV = dvDAO.getAll();

        if (listTatCaDV != null) {
            for (DichVu dv : listTatCaDV) {
                JsonObject sObj = new JsonObject();
                sObj.addProperty("id", String.valueOf(dv.getDichVuID()));
                sObj.addProperty("name", dv.getTenDichVu());
                sObj.addProperty("price", dv.getGiaTien());
                sObj.addProperty("time", dv.getThoiLuongDuKien() + " phút");
                boolean perUnit = dv.isTinhTheoRang();
                sObj.addProperty("perUnit", perUnit);
                sObj.addProperty("unit", perUnit ? "răng" : "");
                servicesJsonArray.add(sObj);
            }
        }

        // ==========================================
        // 4. BUILD JSON: DANH SÁCH LỊCH HẸN CỦA BỆNH NHÂN
        // ==========================================
        JsonArray appointmentsJsonArray = new JsonArray();
        LichHenDAO lhDAO = new LichHenDAO();
        // Lấy danh sách lịch hẹn dựa vào ID tài khoản
        List<LichHen> listLichHen = lhDAO.getLichHenByBenhNhan(loggedInUser.getTaiKhoanID());

        if (listLichHen != null) {
            for (LichHen lh : listLichHen) {
                JsonObject aObj = new JsonObject();
                aObj.addProperty("id", lh.getLichHenID());

                String dbStatus = lh.getTrangThai() != null ? lh.getTrangThai().trim() : "";
                String jsStatus = mapPatientUiStatus(dbStatus);
                aObj.addProperty("status", jsStatus);
                aObj.addProperty("statusText", dbStatus);

                aObj.addProperty("date", lh.getNgayKham() != null ? sdfDate.format(lh.getNgayKham()) : "");
                aObj.addProperty("time", lh.getGioKham() != null ? sdfTime.format(lh.getGioKham()) : "");
                aObj.addProperty("customerNote", lh.getGhiChu() != null ? lh.getGhiChu() : "");

                if (lh.getBacSi() != null && lh.getBacSi().getTaiKhoan() != null) {
                    aObj.addProperty("doctorName", lh.getBacSi().getTaiKhoan().getHoTen());
                    aObj.addProperty("doctorSpec", "Nha Khoa");
                } else if ("Chờ phân ca".equalsIgnoreCase(dbStatus)) {
                    aObj.addProperty("doctorName", "Chờ phân bác sĩ");
                    aObj.addProperty("doctorSpec", "Phòng khám sẽ sắp xếp ca");
                }

                JsonArray apptServices = new JsonArray();
                double totalAmount = 0;
                List<PatientApptService> svcRows = lhDAO.loadPatientServicesForLichHen(lh.getLichHenID());
                for (PatientApptService s : svcRows) {
                    JsonObject sObj = new JsonObject();
                    sObj.addProperty("id", String.valueOf(s.id));
                    sObj.addProperty("name", s.name);
                    sObj.addProperty("price", s.price);
                    sObj.addProperty("qty", s.qty);
                    sObj.addProperty("perUnit", s.perUnit);
                    sObj.addProperty("unit", s.unit != null ? s.unit : "");
                    sObj.addProperty("time", s.time);
                    apptServices.add(sObj);
                    totalAmount += s.price * s.qty;
                }

                aObj.add("services", apptServices);
                aObj.addProperty("total", totalAmount);

                appointmentsJsonArray.add(aObj);
            }
        }

        // ==========================================
        // 5. GẮN VÀO REQUEST VÀ ĐẨY SANG GIAO DIỆN
        // ==========================================
        request.setAttribute("hosoUserJson", gson.toJson(userJson));
        request.setAttribute("hosoServicesJson", gson.toJson(servicesJsonArray));
        request.setAttribute("hosoAppointmentsJson", gson.toJson(appointmentsJsonArray));

        String role = loggedInUser.getVaiTro();
        String targetJSP = "/account/hoso.jsp"; // Mặc định nếu không thuộc các role dưới

        if ("Lễ tân".equalsIgnoreCase(role)) {
            targetJSP = "/reception/hoso.jsp";
        } else if ("Bác sĩ".equalsIgnoreCase(role)) {
            targetJSP = "/doctor/hoso.jsp";
        } else if ("Quản trị viên".equalsIgnoreCase(role)) {
            targetJSP = "/admin/hoso.jsp";
        } else if ("Bệnh nhân".equalsIgnoreCase(role)) {
            targetJSP = "/patient/hoso.jsp";
        }

        request.getRequestDispatcher(targetJSP).forward(request, response);
    }

    private static String mapPatientUiStatus(String dbStatus) {
        if (dbStatus == null || dbStatus.isEmpty()) {
            return "pending";
        }
        if ("Chờ xác nhận".equalsIgnoreCase(dbStatus) || "Chờ duyệt".equalsIgnoreCase(dbStatus)
                || "Chờ phân ca".equalsIgnoreCase(dbStatus)) {
            return "pending";
        }
        if ("Đã xác nhận".equalsIgnoreCase(dbStatus) || "Đã duyệt".equalsIgnoreCase(dbStatus)
                || "Đã đến".equalsIgnoreCase(dbStatus) || "Đang khám".equalsIgnoreCase(dbStatus)) {
            return "confirmed";
        }
        if ("Đã khám".equalsIgnoreCase(dbStatus) || "Đã hoàn thành".equalsIgnoreCase(dbStatus)
                || "Hoàn thành".equalsIgnoreCase(dbStatus)) {
            return "paid";
        }
        if ("Đã hủy".equalsIgnoreCase(dbStatus)) {
            return "cancelled";
        }
        return "pending";
    }
}
