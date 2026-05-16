package com.dentalclinic.dao;

import com.dentalclinic.model.TaiKhoanBsLtDTO;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {
    // Hiển thị danh sách tài khoản
    public List<TaiKhoanBsLtDTO> layThongTinTaiKhoan(){
        List<TaiKhoanBsLtDTO> list = new ArrayList<>();
        String sql = "Select tk.*, bs.ChuyenKhoa_ID, bs.TrinhDo, bs.AnhDaiDien, ck.TenChuyenKhoa " +
                "From TaiKhoan as tk "+
                "left join BacSi bs on tk.TaiKhoan_ID = bs.TaiKhoan_ID " +
                "LEFT JOIN ChuyenKhoa ck ON bs.ChuyenKhoa_ID = ck.ChuyenKhoa_ID " +
                "Where tk.TrangThai != N'Đã xóa'";
        
        try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                TaiKhoanBsLtDTO tk = new TaiKhoanBsLtDTO();
                tk.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                tk.setHoTen(rs.getNString("HoTen"));
                tk.setSoDienThoai((rs.getString("SoDienThoai")));
                tk.setVaiTro(rs.getNString("VaiTro"));
                tk.setNgaySinh(rs.getString("NgaySinh"));
                tk.setGioiTinh(rs.getBoolean("GioiTinh"));
                tk.setTrangThai(rs.getNString("TrangThai"));
                
                if("Bác sĩ".equals(tk.getVaiTro())){
                    tk.setChuyenKhoaID(rs.getInt("ChuyenKhoa_ID"));
                    tk.setTenChuyenKhoa(rs.getNString("TenChuyenKhoa"));
                    tk.setTrinhDo(rs.getNString("TrinhDo"));
                    tk.setAnhDaiDien(rs.getString("AnhDaiDien"));
                }
                list.add(tk);
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return list;
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
            
            if("doctor".equals(tk.getVaiTro())){
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
    public boolean doiTrangThaiTaiKhoan(int taiKhoanID, String trangThai){
        String sql = "UPDATE TaiKhoan SET TrangThai = ? WHERE TaiKhoan_ID = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql))
        {
           ps.setString(1, trangThai);
           ps.setInt(2, taiKhoanID);
           return ps.executeUpdate() > 0;
            
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa tài khoản
    public boolean xoaTaiKhoan(int taiKhoanID){
        String sql = "{call SP_XoaTaiKhoan(?)}";
        try (Connection conn = DBConnection.getConnection();
            CallableStatement cs = conn.prepareCall(sql))
        {
            cs.setInt(1, taiKhoanID);
            return cs.executeUpdate() > 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
