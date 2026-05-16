package com.dentalclinic.service;

import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.TaiKhoanBsLtDTO;
import com.dentalclinic.utils.FileUtils;
import java.util.Base64;

public class TaiKhoanService {
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    
    // HÀM DÙNG CHUNG: Xử lý logic ảnh đại diện cho Bác sĩ
    private void xuLyAnhDaiDien(TaiKhoanBsLtDTO dto, String serverPath, String sourcePath) {
        if ("doctor".equals(dto.getVaiTro())) {
            String avatarData = dto.getAnhDaiDien();
            
            // Có ảnh Base64 gửi lên
            if (avatarData != null && avatarData.startsWith("data:image")) {
                String[] parts = avatarData.split(",");
                if (parts.length > 1) {
                    byte[] imageBytes = Base64.getDecoder().decode(parts[1]);
                    String fileName = "avatar_" + dto.getSoDienThoai() + "_" + System.currentTimeMillis() + ".jpg";
                    
                    // Gọi FileUtils lưu file
                    FileUtils.saveToFile(serverPath, fileName, imageBytes);
                    if (sourcePath != null && !sourcePath.trim().isEmpty()) {
                        FileUtils.saveToFile(sourcePath, fileName, imageBytes);
                    }
                    dto.setAnhDaiDien("assets/img/doctors/" + fileName);
                }
            } 
            // Không có ảnh gửi lên (Gán ảnh mặc định)
            else if (avatarData == null || avatarData.isEmpty()) {
                dto.setAnhDaiDien("assets/img/doctors/default-avatar.jpg");
            }
        } else {
            // Khách hàng, Lễ tân, Admin không được có ảnh
            dto.setAnhDaiDien(null);
        }
    }
     
    // Nghiệp vụ: Thêm tài khoản mới cho Admin
    public boolean themTaiKhoanNhanSu(TaiKhoanBsLtDTO dto, String serverPath, String sourcePath) {
        xuLyAnhDaiDien(dto, serverPath, sourcePath);
        return taiKhoanDAO.themTaiKhoanNhanSu(dto);
    }
    
    // Nghiệp vụ: Cập nhật tài khoản cho Admin
    public boolean capNhatTaiKhoan(TaiKhoanBsLtDTO dto, String serverPath, String sourcePath) {
        xuLyAnhDaiDien(dto, serverPath, sourcePath);
        return taiKhoanDAO.capNhatTaiKhoanNhanSu(dto);
    }
    
    // Nghiệp vụ: Khóa/mở tài khoản cho Admin
    public boolean doiTrangThaiTaiKhoan(int taiKhoanID, String status) {
        String trangThaiTiengViet = "active".equals(status) ? "Hoạt động" : "Bị khóa";

        // Gọi sang tầng DAO để thực thi lệnh update
        return taiKhoanDAO.doiTrangThaiTaiKhoan(taiKhoanID, trangThaiTiengViet);
}
}
