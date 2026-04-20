/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;

/**
 *
 * @author kinhm
 */
//Get thông tin tài khoản
public class TaiKhoanDAO {
    //Truy xuất database TTTK
    public TaiKhoan layThongTinTaiKhoanSDT(String sdt){
        TaiKhoan tk = null;
        String sql = "SELECT * FROM Taikhoan WHERE SoDienThoai = ?";
        
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    tk = new TaiKhoan();
                    tk.setSoDienThoai(sdt);
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setHoTen(rs.getString("HoTen"));
                    tk.setVaiTro(rs.getString("VaiTro"));
                    tk.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                    tk.setTrangThai(rs.getString("Trang Thai"));
                    tk.setNgaySinh(rs.getDate("NgaySinh"));
                    tk.setGioiTinh(rs.getBoolean("GioiTinh"));
                }
            }
        } catch (SQLException se){
           se.getErrorCode();
        }
        return tk;
    }
}
