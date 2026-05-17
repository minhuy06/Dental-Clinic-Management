package com.dentalclinic.dao;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.model.TaiKhoanBsLtDTO;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class TaiKhoanDAO {

    public TaiKhoan getTaiKhoanByPhone(String phone) {
        TaiKhoan tk = null;
        String sql = "SELECT * FROM TAIKHOAN WHERE soDienThoai=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoan();
                    tk.setSoDienThoai(rs.getString("soDienThoai"));
                    tk.setTaiKhoanID(rs.getInt("taiKhoan_ID"));
                    tk.setHoTen(rs.getString("hoTen"));
                    tk.setGioiTinh(rs.getBoolean("gioiTinh"));
                    tk.setMatKhau(rs.getString("matKhau"));
                    tk.setVaiTro(rs.getString("vaiTro"));
                    tk.setTrangThai(rs.getString("trangThai"));
                    tk.setNgaySinh(rs.getDate("ngaySinh"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tk;
    }

    public boolean updatePassword(int taiKhoanId, String newPassword) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TaiKhoan_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, taiKhoanId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi đổi mật khẩu TaiKhoanDAO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInfo(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET SoDienThoai = ?, NgaySinh = ?, GioiTinh = ? WHERE TaiKhoan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getSoDienThoai());
            ps.setDate(2, tk.getNgaySinh() != null ? new java.sql.Date(tk.getNgaySinh().getTime()) : null);

            ps.setObject(3, tk.isGioiTinh()); // Dùng setObject để đẩy kiểu BIT (1/0) xuống
            ps.setInt(4, tk.getTaiKhoanID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm tài khoản nhân sự
    public boolean themTaiKhoanNhanSu(TaiKhoanBsLtDTO tk){
        String sql = "{call SP_ThemTaiKhoan_NhanSu (?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall(sql))
        {
            cs.setNString(1, tk.getHoTen());
            cs.setString(2, tk.getSoDienThoai());
            cs.setString(3, tk.getMatKhau());
            cs.setString(4, tk.getVaiTro());
            cs.setDate(5, java.sql.Date.valueOf(tk.getNgaySinh()));
            cs.setBoolean(6, tk.isGioiTinh());
            
            if("doctor".equals(tk.getVaiTro())){
                cs.setInt(7, tk.getChuyenKhoaID());
                cs.setString(8, tk.getTrinhDo());
                cs.setString(9, tk.getAnhDaiDien());
            }
            else{
                cs.setNull(7, java.sql.Types.INTEGER);
                cs.setNull(8, java.sql.Types.NVARCHAR);
                cs.setNull(9, java.sql.Types.VARCHAR);
            }
            
            cs.execute();
            return true;
            
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật tài khoản nhân sự
    public boolean capNhatTaiKhoanNhanSu(TaiKhoanBsLtDTO tk){
        String sql = "{call SP_CapNhatTaiKhoan(?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)}";
        try (Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall(sql))
        {
            cs.setInt(1, tk.getTaiKhoanID());
            cs.setString(2, tk.getHoTen());
            cs.setString(3, tk.getSoDienThoai());
            cs.setString(4, tk.getMatKhau());
            cs.setString(5, tk.getVaiTro());
            cs.setString(6, tk.getTrangThai());
            cs.setDate(7, java.sql.Date.valueOf(tk.getNgaySinh()));
            cs.setBoolean(8, tk.isGioiTinh());
            
            if("Bác sĩ".equals(tk.getVaiTro())){
                cs.setInt(9, tk.getChuyenKhoaID());
                cs.setString(10, tk.getTrinhDo());
                cs.setString(11, tk.getAnhDaiDien());
            }
            else{
                cs.setNull(9, java.sql.Types.INTEGER);
                cs.setNull(10, java.sql.Types.NVARCHAR);
                cs.setNull(11, java.sql.Types.VARCHAR);
            }
            return cs.executeUpdate() > 0;
            
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    // Đổi trạng thái tài khoản (Khóa/Mở khóa)
    public boolean doiTrangThaiTaiKhoanNhanSu(TaiKhoanBsLtDTO tk){
        String sql = "{update TaiKhoan set TrangThai = ? where TaiKhoan_ID = ?}";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql))
        {
           ps.setString(1, tk.getTrangThai());
           ps.setInt(2, tk.getTaiKhoanID());
           return ps.executeUpdate() > 0;
            
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa tài khoản
    public boolean xoaTaiKhoan(TaiKhoanBsLtDTO tk){
        String sql = "{call SP_XoaTaiKhoan(?)}";
        try (Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall(sql))
        {
            cs.setInt(1, tk.getTaiKhoanID());
            return cs.executeUpdate() > 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
