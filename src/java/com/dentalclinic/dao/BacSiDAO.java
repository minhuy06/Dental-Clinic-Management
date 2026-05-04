/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.dentalclinic.utils.DBConnection;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author kinhm
 */
public class BacSiDAO {
    public BacSi getDoctorInfo(TaiKhoan tk){
        BacSi bs = null;
        String sql = "SELECT * FROM Bacsi WHERE Sodienthoai=?";
        
        try (Connection cn = DBConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setString(1, tk.getSoDienThoai());
            
            ResultSet rs = ps.executeQuery();
            
            
            
            if(rs.next()){
                bs.setTaiKhoan(tk);
                bs.setTrinhDo(rs.getString("Trinhdo"));
                bs.setBacSiID(rs.getInt("BacSi_ID"));
                bs.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return bs;
    }
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
