package com.dentalclinic.dao;

import com.dentalclinic.model.ChiTietDichVu;
import com.dentalclinic.model.PhieuKham;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.Types;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;

public class PhieuKhamDAO {
    
    public boolean luuPhieuKhamLamSang(PhieuKham pk){  
        String sqlPhieuKham = "{call SP_LuuPhieuKham (?, ?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
            SQLServerCallableStatement cs = (SQLServerCallableStatement) conn.prepareCall(sqlPhieuKham)){
            
            cs.setInt(1, pk.getLichHenID());
            cs.setNString(2, pk.getChanDoan());
            cs.setNString(3, pk.getLyDoKham());
            cs.setNString(4, pk.getGhiChu());
            
            // Tạo Table-Valued Parameter cho ChiTietDichVu
            SQLServerDataTable tvpTable = new SQLServerDataTable();
            tvpTable.addColumnMetadata("DichVu_ID", Types.INTEGER);
            tvpTable.addColumnMetadata("DonGia", Types.DOUBLE);
            tvpTable.addColumnMetadata("ViTriRang", Types.NVARCHAR);
            tvpTable.addColumnMetadata("SoLuong", Types.INTEGER);
            
            // Đổ danh sách dịch vụ từ Model vào bảng TVP
            for(ChiTietDichVu ctdv : pk.getDanhSachDichVu()){
                tvpTable.addRow(
                    ctdv.getDichVuID(),
                    ctdv.getDonGia(),
                    ctdv.getViTriRang(),
                    ctdv.getSoLuong());
            }
            
            // Gắn TVP vào tham số thứ 5 của Stored Procedure
            cs.setStructured(5, "Type_ChiTietDichVu", tvpTable);
            int rowAffected = cs.executeUpdate();
            return rowAffected > 0;
            
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
