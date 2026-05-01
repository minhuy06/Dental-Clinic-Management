package com.dentalclinic.dao;

import com.dentalclinic.model.ChiTietDichVu;
import com.dentalclinic.model.PhieuKham;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

public class PhieuKhamDAO {
    
    public boolean luuPhieuKhamLamSang(PhieuKham pk){
        Connection conn = null;
        PreparedStatement psPhieuKham = null;
        PreparedStatement psChiTiet = null;
        ResultSet rs = null;
        boolean isSuccess = false;
        
        try{
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            String sqlPhieuKham = "Insert into PhieuKham (LichHen_ID, ChanDoan, NgayTao, LyDoKham, GhiChu, BacSi_ID, Phong_ID)" + " values (?, ?, getdate(), ?, ?, ?, ?)"; 
            psPhieuKham = conn.prepareStatement(sqlPhieuKham, Statement.RETURN_GENERATED_KEYS);
            psPhieuKham.setInt(1, pk.getLichHenID());
            psPhieuKham.setString(2, pk.getChanDoan());
            psPhieuKham.setString(4, pk.getLyDoKham());
            psPhieuKham.setString(5, pk.getGhiChu());
            psPhieuKham.setInt(6, pk.getBacSiID());
            psPhieuKham.setInt(7, pk.getPhongID());
            
            int affectedRows = psPhieuKham.executeUpdate();
            if(affectedRows > 0){
                rs = psPhieuKham.getGeneratedKeys();
                if(rs.next()){
                    int newPhieuKhamID = rs.getInt(1);
                    
                    // Thêm vào ChiTietDichVu
                    if(pk.getDanhSachDichVu() != null && !pk.getDanhSachDichVu().isEmpty()){
                        String sqlChiTiet = "Insert into ChiTietDichVu (PhieuKham_ID, DichVu_ID, ViTriRang, SoLuong, DonGia)" + "values (?, ?, ?, ?, ?)";
                        psChiTiet = conn.prepareStatement(sqlChiTiet);
                        for (ChiTietDichVu ctdv : pk.getDanhSachDichVu()){
                            psChiTiet.setInt(1, newPhieuKhamID);
                            psChiTiet.setInt(2, ctdv.getDichVuID());
                            
                            // Xử lý NULL cho dịch vụ toàn hàm
                            if(ctdv.getViTriRang() == 0){
                               psChiTiet.setInt(3, Types.INTEGER);
                            }
                            else
                                 psChiTiet.setInt(3, ctdv.getViTriRang());
                            
                            psChiTiet.setInt(4, ctdv.getSoLuong());
                            psChiTiet.setDouble(5, ctdv.getDonGia());
                            
                            psChiTiet.addBatch(); // Đưa vào hàng đợi
                        }
                        psChiTiet.executeBatch();
                    }
                    conn.commit();
                    isSuccess = true;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            try{
                // Có lỗi -> hủy bỏ toàn bộ
                if(conn != null) conn.rollback();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        } finally{
            try{
                if (rs != null) rs.close();
                if(psChiTiet != null) psChiTiet.close();
                if(psPhieuKham != null) psPhieuKham.close();
                if(conn != null){
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return isSuccess;
    }
}
