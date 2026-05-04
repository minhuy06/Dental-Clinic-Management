/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author kinhm
 */
public class TaiKhoanDAO {
    public TaiKhoan getAcountInfo(String phone){
        TaiKhoan tk = null;
        String sql = "SELECT * FROM Taikhoan WHERE Sodienthoai=?";
        try (Connection cn = DBConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setString(1, phone);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                tk = new TaiKhoan();
                tk.setSoDienThoai(phone);
                tk.setMatKhau(rs.getString("Matkhau"));
                tk.setVaiTro(rs.getString("Vaitro"));
                tk.setGioiTinh(rs.getBoolean("Gioitinh"));
                tk.setHoTen(rs.getString("Hoten"));
                tk.setTrangThai(rs.getString("Trangthai"));
                tk.setNgaySinh(rs.getDate("Ngaysinh"));
                tk.setTaiKhoanID(rs.getInt("Taikhoan_ID"));
            }
        } catch (Exception e){
            System.out.println("Loi truy van:" + e.getMessage());
        }
        return tk;
    }
}
