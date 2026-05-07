package com.dentalclinic.service;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.util.List;

public class LichHenService {
    private LichHenDAO lhDAO = new LichHenDAO();
    private BacSiDAO bsDAO = new BacSiDAO();

    /**
     * Xử lý logic đặt lịch hẹn
     * @param lh Đối tượng lịch hẹn chứa thông tin bệnh nhân, ngày, giờ, ghi chú
     * @param dichVuIds Danh sách ID các dịch vụ khách chọn
     * @return Thông báo kết quả (SUCCESS hoặc nội dung lỗi)
     */
    public String createBooking(LichHen lh, List<Integer> dichVuIds) {
        // 1. Kiểm tra bác sĩ rảnh dựa trên ngày và giờ khám
        // Sử dụng hàm getAvailableDoctorIds bạn vừa viết trong BacSiDAO
        List<Integer> doctorsAvailable = bsDAO.getAvailableDoctorIds(
            lh.getNgayKham().toString(), 
            lh.getGioKham().toString()
        );

        if (doctorsAvailable == null || doctorsAvailable.isEmpty()) {
            return "Rất tiếc, khung giờ này hiện tại không có bác sĩ nào rảnh hoặc chưa có lịch trực. Vui lòng chọn khung giờ khác!";
        }

        // 2. Tự động chọn bác sĩ đầu tiên trong danh sách rảnh
        int selectedDoctorId = doctorsAvailable.get(0);
        lh.setBacSiID(selectedDoctorId);

        // 3. Thiết lập các thông tin mặc định khác
        if (lh.getTrangThai() == null || lh.getTrangThai().isEmpty()) {
            lh.setTrangThai("Chờ duyệt"); // Lưu ý: Không dùng chữ N ở đây
        }
        
        // Mặc định phòng chờ là phòng số 1 nếu chưa có logic xếp phòng phức tạp
        if (lh.getPhongID() == 0) {
            lh.setPhongID(1); 
        }

        // 4. Gọi DAO để thực hiện lưu vào Database (Transaction)
        int resultID = lhDAO.insertBooking(lh, dichVuIds);

        if (resultID != -1) {
            return "SUCCESS";
        } else {
            return "Đã xảy ra lỗi khi lưu lịch hẹn. Vui lòng thử lại sau!";
        }
    }
}