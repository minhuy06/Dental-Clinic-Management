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
    public static final String STATUS_CHO_XAC_NHAN = "Chờ xác nhận";
    public static final String STATUS_DA_XAC_NHAN = "Đã xác nhận";
    public static final String RESULT_SUCCESS = "SUCCESS";
    /** BN đặt lịch không có BS rảnh — chờ lễ tân xác nhận trước, admin phân BS sau. */
    public static final String RESULT_SUCCESS_NO_DOCTOR = "SUCCESS_NO_DOCTOR";
    public static final String MSG_PATIENT_TIME_CONFLICT = "Giờ này bạn đã có lịch khác";
    public static final String MSG_PHONE_DUPLICATE = "Đã trùng số điện thoại";
    public static final String MSG_DOCTOR_NOT_FREE = "Bác sĩ đã chọn không rảnh hoặc không có ca làm trong khung giờ này.";
    public static final String MSG_ROOM_NOT_FREE = "Phòng khám đã có lịch trong khung giờ này.";
    public static final String MSG_DOCTOR_ROOM_MISMATCH = "Bác sĩ không có ca làm tại phòng đã chọn trong ngày này.";

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
        return createBooking(lh, qtyMap, requestedDoctorId, false);
    }

    public String createBooking(LichHen lh, Map<Integer, Integer> qtyByDvId, Integer requestedDoctorId) {
        return createBooking(lh, qtyByDvId, requestedDoctorId, false);
    }

    /** patientSelfBooking=true: không có BS rảnh → Chờ xác nhận (lễ tân duyệt trước, admin gán BS sau). */
    public String createBooking(LichHen lh, Map<Integer, Integer> qtyByDvId, Integer requestedDoctorId, boolean patientSelfBooking) {
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

        String ngayStr = ngay.toString();

        int durationMinutes = dichVuDAO.getTotalDurationMinutes(qtyByDvId);
        if (durationMinutes <= 0) durationMinutes = 30;

        if (hasPatientTimeConflict(lh.getBenhNhanID(), ngay, gio, qtyByDvId, null)) {
            return MSG_PATIENT_TIME_CONFLICT;
        }

        if (requestedDoctorId != null && requestedDoctorId > 0) {
            String slotErr = validateDoctorAndRoomSlot(requestedDoctorId, lh.getPhongID(), ngay, gio, durationMinutes, null);
            if (slotErr != null) {
                return slotErr;
            }
            lh.setBacSiID(requestedDoctorId);
            int phong = lh.getPhongID() > 0 ? lh.getPhongID() : bsDAO.findPhongIdForDoctorOnDate(requestedDoctorId, ngayStr);
            lh.setPhongID(phong > 0 ? phong : 1);
        } else {
            List<Integer> freeDoctors = bsDAO.findAvailableDoctorIdsForBooking(ngayStr, gio, durationMinutes);
            if (freeDoctors.isEmpty()) {
                if (patientSelfBooking) {
                    lh.setBacSiID(0);
                    lh.setPhongID(0);
                    lh.setTrangThai(STATUS_CHO_XAC_NHAN);
                    int pendingId = lhDAO.insertBooking(lh, qtyByDvId);
                    return pendingId != -1 ? RESULT_SUCCESS_NO_DOCTOR : "Lỗi khi lưu lịch hẹn vào hệ thống.";
                }
                return "Hiện không có bác sĩ rảnh trong khung giờ này. Vui lòng chọn ngày/giờ khác hoặc liên hệ lễ tân.";
            }
            Collections.shuffle(freeDoctors);
            int assigned = freeDoctors.get(0);
            lh.setBacSiID(assigned);
            lh.setPhongID(bsDAO.findPhongIdForDoctorOnDate(assigned, ngayStr));
        }

        if (lh.getPhongID() <= 0 && lh.getBacSiID() > 0) {
            lh.setPhongID(bsDAO.findPhongIdForDoctorOnDate(lh.getBacSiID(), ngayStr));
        }
        if (lh.getPhongID() <= 0 && lh.getBacSiID() <= 0) {
            lh.setPhongID(0);
        } else if (lh.getPhongID() <= 0) {
            lh.setPhongID(1);
        }

        if (lh.getTrangThai() == null || lh.getTrangThai().isBlank()) {
            lh.setTrangThai(STATUS_CHO_XAC_NHAN);
        }

        int result = lhDAO.insertBooking(lh, qtyByDvId);
        if (result == -1) {
            return "Lỗi khi lưu lịch hẹn vào hệ thống.";
        }
        return RESULT_SUCCESS;
    }

    public int resolveDeskPatientForReceptionBooking(String hoTen, String soDienThoai, Integer knownBenhNhanId) {
        return benhNhanDAO.resolveDeskPatientForReceptionBooking(hoTen, soDienThoai, knownBenhNhanId);
    }

    public int resolveBenhNhanForPatientAccount(int taiKhoanId) {
        return benhNhanDAO.ensureBenhNhanForTaiKhoan(taiKhoanId);
    }

    public boolean hasPatientTimeConflict(int benhNhanId, LocalDate ngay, LocalTime gio,
            Map<Integer, Integer> qtyByDvId, Integer excludeLichHenId) {
        if (benhNhanId < 1 || ngay == null || gio == null) {
            return false;
        }
        int durationMinutes = dichVuDAO.getTotalDurationMinutes(qtyByDvId);
        if (durationMinutes <= 0) durationMinutes = 30;
        return lhDAO.hasPatientBookingConflict(benhNhanId, ngay.toString(), gio, durationMinutes, excludeLichHenId);
    }

    /** null = hợp lệ; ngược lại là thông báo lỗi. */
    public String validateDoctorAndRoomSlot(int bacSiId, int phongId, LocalDate ngay, LocalTime gio,
            int durationMinutes, Integer excludeLichHenId) {
        if (bacSiId < 1) {
            return null;
        }
        String ngayStr = ngay.toString();
        if (!bsDAO.isDoctorFreeForBooking(bacSiId, ngayStr, gio, durationMinutes, excludeLichHenId)) {
            return MSG_DOCTOR_NOT_FREE;
        }
        if (phongId > 0) {
            if (!lhDAO.isRoomFreeForBooking(phongId, ngayStr, gio, durationMinutes, excludeLichHenId)) {
                return MSG_ROOM_NOT_FREE;
            }
            if (!bsDAO.isDoctorScheduledInRoomOnDate(bacSiId, phongId, ngayStr)) {
                return MSG_DOCTOR_ROOM_MISMATCH;
            }
        }
        return null;
    }

    /** Sau khi BN đổi ngày/giờ: tự gán BS rảnh hoặc bỏ gán BS (giữ trạng thái lễ tân đã duyệt nếu có). */
    public void refreshAssignmentAfterScheduleChange(int lichHenId, Map<Integer, Integer> qtyByDvId,
            LocalDate ngay, LocalTime gio) {
        if (lichHenId < 1 || ngay == null || gio == null) {
            return;
        }
        LichHen current = lhDAO.getById(lichHenId);
        String statusWhenNoDoctor = STATUS_CHO_XAC_NHAN;
        if (current != null && current.getTrangThai() != null) {
            String st = current.getTrangThai().trim();
            if (STATUS_DA_XAC_NHAN.equalsIgnoreCase(st) || "Đã duyệt".equalsIgnoreCase(st)) {
                statusWhenNoDoctor = st;
            }
        }
        int durationMinutes = dichVuDAO.getTotalDurationMinutes(qtyByDvId);
        if (durationMinutes <= 0) durationMinutes = 30;
        String ngayStr = ngay.toString();
        List<Integer> freeDoctors = new ArrayList<>();
        for (Integer candidate : bsDAO.findAvailableDoctorIdsForBooking(ngayStr, gio, durationMinutes)) {
            if (bsDAO.isDoctorFreeForBooking(candidate, ngayStr, gio, durationMinutes, lichHenId)) {
                freeDoctors.add(candidate);
            }
        }
        if (freeDoctors.isEmpty()) {
            lhDAO.updateBookingAssignment(lichHenId, null, null, statusWhenNoDoctor);
            return;
        }
        int assigned = freeDoctors.get(0);
        int room = bsDAO.findPhongIdForDoctorOnDate(assigned, ngayStr);
        String statusWithDoctor = (current != null && STATUS_DA_XAC_NHAN.equalsIgnoreCase(
                current.getTrangThai() != null ? current.getTrangThai().trim() : ""))
                ? STATUS_DA_XAC_NHAN : STATUS_CHO_XAC_NHAN;
        lhDAO.updateBookingAssignment(lichHenId, assigned, room > 0 ? room : null, statusWithDoctor);
    }
}
