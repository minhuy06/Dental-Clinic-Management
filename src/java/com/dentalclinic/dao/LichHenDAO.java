package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.dentalclinic.model.LichHen;
import java.sql.*;
import java.util.List;

public class LichHenDAO {

    public int insertBooking(LichHen lh, List<Integer> dsDichVuID) {
        String sqlLichHen = "INSERT INTO LichHen (BenhNhan_ID, BacSi_ID, NgayKham, GioKham, GhiChu, TrangThai, Phong_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        // Đảm bảo tên bảng này có trong Database
        String sqlChiTiet = "INSERT INTO ChiTietLichHen (LichHen_ID, DichVu_ID) VALUES (?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            PreparedStatement ps = conn.prepareStatement(sqlLichHen, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, lh.getBenhNhanID());
            ps.setInt(2, lh.getBacSiID());
            ps.setDate(3, new java.sql.Date(lh.getNgayKham().getTime())); 
            ps.setTime(4, new java.sql.Time(lh.getGioKham().getTime()));
            ps.setNString(5, lh.getGhiChu());
            ps.setNString(6, lh.getTrangThai());
            ps.setInt(7, lh.getPhongID() == 0 ? 1 : lh.getPhongID()); 
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int generatedID = -1;
            if (rs.next()) {
                generatedID = rs.getInt(1);
            }

            if (generatedID != -1 && dsDichVuID != null && !dsDichVuID.isEmpty()) {
                PreparedStatement psDetail = conn.prepareStatement(sqlChiTiet);
                for (Integer dvID : dsDichVuID) {
                    psDetail.setInt(1, generatedID);
                    psDetail.setInt(2, dvID);
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            conn.commit();
            return generatedID;
            
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return -1;
        }
    }
}