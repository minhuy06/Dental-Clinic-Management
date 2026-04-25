package com.dentalclinic.dao;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.BenhNhan;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class TaiKhoanDAO {
    public boolean themTaiKhoanBenhNhan(TaiKhoan tk, BenhNhan bn){
        String sqlTaiKhoan = "Insert into TaiKhoan (SoDienThoai, MatKhau, VaiTro, TrangThai, Email, HoTen, NgaySinh, GioiTinh) values (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlBenhNhan = "Insert into BenhNhan (TaiKhoan_ID) values (?)";
        
        Connection conn = null; // Khởi tạo biến kết nối SQL server
        PreparedStatement psTaiKhoan = null;
        PreparedStatement psBenhNhan = null;
        ResultSet rs = null;
        boolean isSuccess = false;
        
        try{
            conn = DBConnection.getConnection(); // Kết nối Db
            conn.setAutoCommit(false); // Bắt đầu giao dịch
            
            // Thêm vào TaiKhoan
            psTaiKhoan = conn.prepareStatement(sqlTaiKhoan, Statement.RETURN_GENERATED_KEYS);
            psTaiKhoan.setString(1, tk.getSoDienThoai());
            psTaiKhoan.setString(2, tk.getMatKhau());
            psTaiKhoan.setString(3, tk.getVaiTro());
            psTaiKhoan.setString(4, tk.getTrangThai());
            psTaiKhoan.setString(5, tk.getEmail());
            psTaiKhoan.setString(6, tk.getHoTen());
            psTaiKhoan.setDate(7, tk.getNgaySinh());
            psTaiKhoan.setBoolean(8, tk.isGioiTinh());
            
            psTaiKhoan.executeUpdate();
            
            // Lấy TaiKhoan_ID vừa được tạo ở TaiKhoan để thêm vào BenhNhan
            rs = psTaiKhoan.getGeneratedKeys();
            int newTaiKhoanID = 0;
            if(rs.next())
                newTaiKhoanID = rs.getInt(1);
            
            // Thêm vào bảng BenhNhan với TaiKhoan_ID tương ứng
            if(newTaiKhoanID > 1){
                psBenhNhan = conn.prepareStatement(sqlBenhNhan);
                psBenhNhan.setInt(1, newTaiKhoanID);
                psBenhNhan.executeUpdate();
            }
            
            // Lưu thay đổi vào database
            conn.commit();
            isSuccess = true;
            
        } catch(Exception e){
            try{
                if(conn != null){
                    conn.rollback();
                    System.out.println("Da RollBack transaction do co loi khi Insert");
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally{
            // Đóng kết nối
            try{
                if (rs != null) rs.close();
                if (psTaiKhoan != null) psTaiKhoan.close();
                if (psBenhNhan != null) psBenhNhan.close();
                if (conn != null){
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        
        return isSuccess;
    }
}
