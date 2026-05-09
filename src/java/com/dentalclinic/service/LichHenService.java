package com.dentalclinic.service;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.BenhNhanDAO;
import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.util.List;
import java.time.LocalTime;

public class LichHenService {
    private LichHenDAO lhDAO = new LichHenDAO();
    private BacSiDAO bsDAO = new BacSiDAO();
    private BenhNhanDAO benhNhanDAO = new BenhNhanDAO();
    private DichVuDAO dichVuDAO = new DichVuDAO();

    public String createBooking(LichHen lh, List<Integer> dichVuIds) {
        return createBooking(lh, dichVuIds, null);
    }

    public String createBooking(LichHen lh, List<Integer> dichVuIds, Integer requestedDoctorId) {
        if (lh.getPhongID() <= 0) lh.setPhongID(1);

        String ngay = lh.getNgayKham().toString();
        String gio = lh.getGioKham().toString();

        if (requestedDoctorId != null && requestedDoctorId > 0) {
            List<BacSiDAO.TimeRange> shifts = bsDAO.getDoctorShiftsOnDate(requestedDoctorId, ngay);
            if (shifts.isEmpty()) {
                return "Bác sĩ đã chọn không có ca làm việc trong ngày này.";
            }
            int durationMinutes = dichVuDAO.getTotalDurationMinutesByIds(dichVuIds);
            if (durationMinutes <= 0) durationMinutes = 30;
            int required = durationMinutes + 15;

            LocalTime start = lh.getGioKham().toLocalTime();
            LocalTime end = start.plusMinutes(required);
            boolean inShift = false;
            for (BacSiDAO.TimeRange sh : shifts) {
                if ((!start.isBefore(sh.getStart())) && (!end.isAfter(sh.getEnd()))) {
                    inShift = true;
                    break;
                }
            }
            if (!inShift) {
                return "Khung giờ này không nằm trong ca làm của bác sĩ.";
            }

            List<BacSiDAO.TimeRange> busySlots = bsDAO.getDoctorBusySlotsOnDate(requestedDoctorId, ngay);
            for (BacSiDAO.TimeRange busy : busySlots) {
                boolean overlap = start.isBefore(busy.getEnd()) && end.isAfter(busy.getStart());
                if (overlap) {
                    return "Bác sĩ đã chọn không rảnh ở khung giờ này.";
                }
            }
            lh.setBacSiID(requestedDoctorId);
        } else {
            List<Integer> doctorsAvailable = bsDAO.getAvailableDoctorIds(ngay, gio, lh.getPhongID());
            if (doctorsAvailable == null || doctorsAvailable.isEmpty()) {
                return "Hiện tại không có bác sĩ nào rảnh vào khung giờ này.";
            }
            lh.setBacSiID(doctorsAvailable.get(0));
        }

        // 1. Tìm bác sĩ rảnh
        // 2. Thiết lập mặc định
        lh.setTrangThai("Chờ xác nhận");

        // 3. Lưu vào Database
        int result = lhDAO.insertBooking(lh, dichVuIds);

        return (result != -1) ? "SUCCESS" : "Lỗi khi lưu lịch hẹn vào hệ thống.";
    }

    public int resolveOrCreateDeskPatient(String hoTen, String soDienThoai) {
        return benhNhanDAO.resolveOrCreateDeskPatient(hoTen, soDienThoai);
    }
}