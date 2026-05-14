package com.dentalclinic.controller;

import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.dao.LichHenDAO;
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

@WebServlet("/hoso") // Đường dẫn để truy cập trang hồ sơ
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Cấu hình chống lỗi font Tiếng Việt
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

        // ==========================================
        // 2. BUILD JSON: THÔNG TIN BỆNH NHÂN (USER)
        // ==========================================
        JsonObject userJson = new JsonObject();
        userJson.addProperty("name", loggedInUser.getHoTen());
        userJson.addProperty("phone", loggedInUser.getSoDienThoai());
        // Chống lỗi NullPointerException khi ngày sinh bị null
        userJson.addProperty("dob", loggedInUser.getNgaySinh() != null ? loggedInUser.getNgaySinh().toString() : ""); 
        boolean gioiTinhDB = loggedInUser.isGioiTinh(); 

        // Chuyển đổi: Nếu null thì để rỗng, nếu 1 thì "Nam", ngược lại (0) thì "Nữ"
        String gioiTinhStr = "";
        gioiTinhStr = (gioiTinhDB == true) ? "Nam" : "Nữ";
        userJson.addProperty("gender",gioiTinhStr);
        userJson.addProperty("avatar", ""); // Thêm link avatar nếu DB có lưu


        // ==========================================
        // 3. BUILD JSON: DANH SÁCH TẤT CẢ DỊCH VỤ (Để đổ vào Modal Edit)
        // ==========================================
        JsonArray servicesJsonArray = new JsonArray();
        DichVuDAO dvDAO = new DichVuDAO();
        List<DichVu> listTatCaDV = dvDAO.getAll(); // Sửa lại tên hàm lấy tất cả dịch vụ của bạn

        if (listTatCaDV != null) {
            for (DichVu dv : listTatCaDV) {
                JsonObject sObj = new JsonObject();
                sObj.addProperty("id", String.valueOf(dv.getDichVuID())); // ID dịch vụ
                sObj.addProperty("name", dv.getTenDichVu());
                sObj.addProperty("price", dv.getGiaTien());
                sObj.addProperty("time", dv.getThoiLuongDuKien()); // DB nếu không có thời gian thì Fix cứng
                sObj.addProperty("perUnit", false); // Tạm fix cứng
                sObj.addProperty("unit", "");
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
                aObj.addProperty("id", lh.getLichHenID()); // Sửa lại getter ID của bạn
                
                // Quy đổi trạng thái từ DB sang Key của Javascript
                String dbStatus = lh.getTrangThai(); // Ví dụ: "Chờ xác nhận"
                String jsStatus = "pending";
                if ("Đã xác nhận".equalsIgnoreCase(dbStatus)) jsStatus = "confirmed";
                else if ("Đã khám".equalsIgnoreCase(dbStatus) || "Hoàn thành".equalsIgnoreCase(dbStatus)) jsStatus = "paid";
                
                aObj.addProperty("status", jsStatus);
                aObj.addProperty("statusText", dbStatus);
                
                // Format Ngày và Giờ
                aObj.addProperty("date", lh.getNgayKham() != null ? sdfDate.format(lh.getNgayKham()) : "");
                aObj.addProperty("time", lh.getGioKham() != null ? sdfTime.format(lh.getGioKham()) : "");
                aObj.addProperty("customerNote", lh.getGhiChu() != null ? lh.getGhiChu() : "");
                

                if (lh.getBacSi() != null) {
                    aObj.addProperty("doctorName", lh.getBacSi().getTaiKhoan().getHoTen());
                    aObj.addProperty("doctorSpec", "Nha Khoa");
                }

                // Lấy danh sách Chi Tiết Dịch Vụ CỦA RIÊNG LỊCH HẸN NÀY
                JsonArray apptServices = new JsonArray();
                double totalAmount = 0;
                
                /*
                   ChiTietLichHenDAO ctlhDAO = new ChiTietLichHenDAO();
                   List<ChiTietLichHen> listChiTiet = ctlhDAO.getByLichHen(lh.getLichHenID());
                   for(ChiTietLichHen ct : listChiTiet) {
                       JsonObject sObj = new JsonObject();
                       sObj.addProperty("id", String.valueOf(ct.getDichVu().getDichVuID()));
                       sObj.addProperty("name", ct.getDichVu().getTenDichVu());
                       sObj.addProperty("price", ct.getGiaTien());
                       sObj.addProperty("qty", ct.getSoLuong());
                       sObj.addProperty("time", "30 phút");
                       apptServices.add(sObj);
                       totalAmount += (ct.getGiaTien() * ct.getSoLuong());
                   }
                */
                
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

        // Forward dữ liệu kèm JSON sang đúng thư mục phân quyền
        request.getRequestDispatcher(targetJSP).forward(request, response);
    }
}