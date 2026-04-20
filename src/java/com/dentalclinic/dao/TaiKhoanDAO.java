/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.LeTan;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
    
    //Lấy danh sách lễ tân
    public ArrayList<LeTan> layDanhSachLeTan(){
        ArrayList<LeTan> danhSach = new ArrayList();
        String sql = "SELECT * FROM Taikhoan tk join Letan lt on tk.TaiKhoan_ID = lt.TaiKhoan_ID";
        try(Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    TaiKhoan tk = new TaiKhoan();
                    tk.setSoDienThoai(rs.getString("SoDienThoai"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setHoTen(rs.getString("HoTen"));
                    tk.setVaiTro(rs.getString("VaiTro"));
                    tk.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                    tk.setTrangThai(rs.getString("TrangThai"));
                    tk.setNgaySinh(rs.getDate("NgaySinh"));
                    tk.setGioiTinh(rs.getBoolean("GioiTinh"));
                    LeTan letan = new LeTan();
                    letan.setTaiKhoan(tk);
                    letan.setLeTanID(rs.getInt("LeTan_ID")); 
                    danhSach.add(letan);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return danhSach;
    }
    //Lấy danh sách tk bác sĩ
    public ArrayList<BacSi> layDanhSachBacSi() {
        ArrayList<BacSi> danhSach = new ArrayList();
        String sql = "SELECT * FROM TaiKhoan tk join Bacsi bs on tk.TaiKhoan_ID = bs.TaiKhoan_ID";
        
        try (Connection conn = DBConnection.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    TaiKhoan tk = new TaiKhoan();
                    tk.setSoDienThoai(rs.getString("SoDienThoai"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setHoTen(rs.getString("HoTen"));
                    tk.setVaiTro(rs.getString("VaiTro"));
                    tk.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                    tk.setTrangThai(rs.getString("TrangThai"));
                    tk.setNgaySinh(rs.getDate("NgaySinh"));
                    tk.setGioiTinh(rs.getBoolean("GioiTinh"));
                    BacSi bs = new BacSi();
                    bs.setTaiKhoan(tk);
                    bs.setBacSiID(rs.getInt("BacSi_ID"));
                    bs.setTrinhDo(rs.getString("TrinhDo"));
                    danhSach.add(bs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }
    
}
