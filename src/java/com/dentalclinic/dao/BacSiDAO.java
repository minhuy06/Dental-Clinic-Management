package com.dentalclinic.dao;

import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BacSiDAO {

    private static final String GET_ALL_BAC_SI = 
        "SELECT bs.BacSi_ID, bs.TaiKhoan_ID, bs.AnhDaiDien, bs.TrinhDo, tk.HoTen, " +
        "(SELECT TOP 1 ck.TenChuyenKhoa FROM BacSi_ChuyenKhoa bsck " +
        " JOIN ChuyenKhoa ck ON bsck.ChuyenKhoa_ID = ck.ChuyenKhoa_ID " +
        " WHERE bsck.BacSi_ID = bs.BacSi_ID) AS TenChuyenKhoa " +
        "FROM BacSi bs " +
        "JOIN TaiKhoan tk ON bs.TaiKhoan_ID = tk.TaiKhoan_ID " +
        "ORDER BY bs.BacSi_ID";

    public List<BacSi> getAllBacSi() {
        List<BacSi> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL_BAC_SI);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                tk.setHoTen(rs.getString("HoTen"));

                BacSi bs = new BacSi();
                bs.setBacSiID(rs.getInt("BacSi_ID"));
                bs.setTaiKhoanID(rs.getInt("TaiKhoan_ID"));
                bs.setAnhDaiDien(rs.getString("AnhDaiDien"));
                bs.setTrinhDo(rs.getString("TrinhDo"));
                bs.setTenChuyenKhoa(rs.getString("TenChuyenKhoa"));

                bs.setTaiKhoan(tk);

                list.add(bs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}