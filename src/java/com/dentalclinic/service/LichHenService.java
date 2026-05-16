package com.dentalclinic.service;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.BenhNhanDAO;
import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LichHenService {
    private final LichHenDAO lhDAO = new LichHenDAO();
    private final BacSiDAO bsDAO = new BacSiDAO();
    private final BenhNhanDAO benhNhanDAO = new BenhNhanDAO();
    private final DichVuDAO dichVuDAO = new DichVuDAO();

    public String createBooking(LichHen lh, List<Integer> dichVuIds) {
        Map<Integer, Integer> qtyMap = new LinkedHashMap<>();
        if (dichVuIds != null) {
            for (Integer id : dichVuIds) {
                if (id == null) continue;
                qtyMap.put(id, qtyMap.getOrDefault(id, 0) + 1);
            }
        }
        return createBooking(lh, qtyMap, null);
    }

    public String createBooking(LichHen lh, List<Integer> dichVuIds, Integer requestedDoctorId) {
        Map<Integer, Integer> qtyMap = new LinkedHashMap<>();
        if (dichVuIds != null) {
            for (Integer id : dichVuIds) {
                if (id == null) continue;
                qtyMap.put(id, qtyMap.getOrDefault(id, 0) + 1);
            }
        }
        return createBooking(lh, qtyMap, requestedDoctorId);
    }

    public String createBooking(LichHen lh, Map<Integer, Integer> qtyByDvId, Integer requestedDoctorId) {
        if (lh == null) return "Thiếu thông tin lịch hẹn.";
        if (lh.getBenhNhanID() <= 0) return "Không xác định được tài khoản bệnh nhân.";
        if (lh.getNgayKham() == null || lh.getGioKham() == null) {
            return "Vui lòng chọn ngày và giờ khám.";
        }
        if (qtyByDvId == null || qtyByDvId.isEmpty()) {
            return "Vui lòng chọn ít nhất một dịch vụ.";
        }

        LocalDate ngay = lh.getNgayKham().toLocalDate();
        LocalTime gio = lh.getGioKham().toLocalTime();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (ngay.isBefore(today)) {
            return "Ngày khám không hợp lệ (không được chọn ngày trong quá khứ).";
        }
        if (ngay.equals(today) && !gio.isAfter(now)) {
            return "Không thể đặt lịch vào thời gian đã qua. Vui lòng chọn giờ khám sau thời điểm hiện tại.";
        }

        if (lh.getPhongID() <= 0) lh.setPhongID(1);

        String ngayStr = ngay.toString();

        int durationMinutes = dichVuDAO.getTotalDurationMinutes(qtyByDvId);
        if (durationMinutes <= 0) durationMinutes = 30;

        if (requestedDoctorId != null && requestedDoctorId > 0) {
            if (!bsDAO.isDoctorFreeForBooking(requestedDoctorId, ngayStr, gio, durationMinutes)) {
                return "Bác sĩ đã chọn không rảnh hoặc không có ca làm trong khung giờ này.";
            }
            lh.setBacSiID(requestedDoctorId);
            lh.setPhongID(bsDAO.findPhongIdForDoctorOnDate(requestedDoctorId, ngayStr));
        } else {
            List<Integer> freeDoctors = bsDAO.findAvailableDoctorIdsForBooking(ngayStr, gio, durationMinutes);
            if (freeDoctors.isEmpty()) {
                return "Hiện không có bác sĩ rảnh trong khung giờ này. Vui lòng chọn ngày/giờ khác hoặc liên hệ lễ tân.";
            }
            Collections.shuffle(freeDoctors);
            int assigned = freeDoctors.get(0);
            lh.setBacSiID(assigned);
            lh.setPhongID(bsDAO.findPhongIdForDoctorOnDate(assigned, ngayStr));
        }

        lh.setTrangThai("Chờ xác nhận");

        int result = lhDAO.insertBooking(lh, qtyByDvId);
        return (result != -1) ? "SUCCESS" : "Lỗi khi lưu lịch hẹn vào hệ thống.";
    }

    public int resolveOrCreateDeskPatient(String hoTen, String soDienThoai) {
        return benhNhanDAO.resolveOrCreateDeskPatient(hoTen, soDienThoai);
    }

    public int resolveBenhNhanForPatientAccount(int taiKhoanId) {
        return benhNhanDAO.ensureBenhNhanForTaiKhoan(taiKhoanId);
    }
}
