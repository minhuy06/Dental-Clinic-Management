
package com.dentalclinic.dao;

import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BacSiDAO {
    public List<BacSi> getAllBacSi() {

        List<BacSi> list =new ArrayList<>();
        String sql ="SELECT"
                        +"bs.BacSi_ID, "
                        +"bs.TaiKhoan_ID, "
                        +"bs.AnhDaiDien, "
                        +"bs.TrinhDo, "
                        +"tk.HoTen, "
                        +"ck.TenChuyenKhoa "
                    +"FROM BacSi bs "
                    +"JOIN TaiKhoan tk ON bs.TaiKhoan_ID = tk.TaiKhoan_ID "
                    +"JOIN BacSi_ChuyenKhoa bsck ON bs.BacSi_ID = bsck.BacSi_ID "
                    +"JOIN ChuyenKhoa ck ON bsck.ChuyenKhoa_ID = ck.ChuyenKhoa_ID "
                    +"ORDER BY bs.BacSi_ID";

            try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BacSi bs = new BacSi();
                bs.setBacSiID(rs.getInt("BacSi_ID"));
                bs.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                bs.setAnhDaiDien(rs.getString("AnhDaiDien"));
                bs.setTrinhDo(rs.getString("TrinhDo"));
                bs.setTenChuyenKhoa(rs.getString("TenChuyenKhoa"));
                TaiKhoan tk = new TaiKhoan();
                tk.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                tk.setHoTen(rs.getString("HoTen"));
                bs.setTaiKhoan(tk);
                list.add(bs);
                }
            } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách bác sĩ: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
        