package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LichLamViecDAO {
    public String layDanhSachCaLam(){
        JsonArray jsonArray = new JsonArray();
        String sql = "SELECT llv.Lich_ID, llv.NgayLam, tk.TaiKhoan_ID, tk.HoTen, " +
                     "cl.TenCa, pk.TenPhong " +
                     "FROM LichLamViec llv " +
                     "JOIN TaiKhoan tk ON llv.TaiKhoan_ID = tk.TaiKhoan_ID " +
                     "JOIN CaLam cl ON llv.Ca_ID = cl.Ca_Id " +
                     "LEFT JOIN PhongKham pk ON llv.Phong_ID = pk.Phong_ID " +
                     "WHERE tk.TrangThai = N'Hoạt động'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            
            while (rs.next()){
                // Khởi tạo một đối tượng JSON trống cho mỗi dòng dữ liệu
                JsonObject obj = new JsonObject();
                
                // Đổ dữ liệu từ ResultSet thẳng vào các thuộc tính JSON
                obj.addProperty("id", rs.getInt("Lich_ID"));
                obj.addProperty("staffId", rs.getInt("TaiKhoan_ID"));
                obj.addProperty("staffName", rs.getNString("HoTen"));
                
                String tenCaDB = rs.getNString("TenCa");
                obj.addProperty("shiftType", (tenCaDB != null && tenCaDB.toLowerCase().contains("chiều")) ? "afternoon" : "morning");
                
                obj.addProperty("date", rs.getString("NgayLam"));
                obj.addProperty("room", rs.getNString("TenPhong") != null ? rs.getNString("TenPhong") : "Chưa xếp phòng");
                obj.addProperty("note", "");
                
                // Đưa đối tượng vào mảng JSON
                jsonArray.add(obj);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
}
