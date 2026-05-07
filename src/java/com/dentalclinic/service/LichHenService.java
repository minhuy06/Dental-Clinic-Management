package com.dentalclinic.service;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.util.List;

public class LichHenService {
    private LichHenDAO lhDAO = new LichHenDAO();

    public boolean createBooking(LichHen lh, List<Integer> dichVuIds) {
        // Có thể thêm logic kiểm tra trùng lịch ở đây
        return lhDAO.insertBooking(lh, dichVuIds) > 0;
    }
}