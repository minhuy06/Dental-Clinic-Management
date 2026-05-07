package com.dentalclinic.service;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.BenhNhan;
import java.util.List;

public class LichHenService {
    private LichHenDAO lhDAO = new LichHenDAO();
    private BacSiDAO bsDAO = new BacSiDAO();

    public String createBooking(LichHen lh, List<Integer> dichVuIds) {
        // 1. Tìm bác sĩ rảnh
        List<Integer> doctorsAvailable = bsDAO.getAvailableDoctorIds(
            lh.getNgayKham().toString(), 
            lh.getGioKham().toString()
        );

        if (doctorsAvailable == null || doctorsAvailable.isEmpty()) {
            return "Hiện tại không có bác sĩ nào rảnh vào khung giờ này.";
        }

        // 2. Gán bác sĩ đầu tiên tìm được
        lh.setBacSiID(doctorsAvailable.get(0));

        // 3. Thiết lập mặc định
        lh.setTrangThai("Chờ duyệt");
        if (lh.getPhongID() <= 0) lh.setPhongID(1); 

        // 4. Lưu vào Database
        int result = lhDAO.insertBooking(lh, dichVuIds);

        return (result != -1) ? "SUCCESS" : "Lỗi khi lưu lịch hẹn vào hệ thống.";
    }
}