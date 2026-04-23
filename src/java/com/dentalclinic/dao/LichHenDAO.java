package com.dentalclinic.dao;

import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.BenhNhan;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LichHenDAO {
    public List<LichHen> getAllLichHen() {
        List<LichHen> list = new ArrayList<>();
        String sql = "SELECT "
                + "    lh.LichHen_ID, "
                + "    lh.BenhNhan_ID, "
                + "    lh.BacSi_ID, "
                + "    lh.DichVu_ID, "
                + "    bn.HoTen AS TenBN, "
                + "    tkBS.HoTen AS TenBS, "
                + "    dv.TenDichVu, "
                + "    lh.NgayKham, "
                + "    lh.GioKham, "
                + "    lh.TrangThai, "
                + "    lh.GhiChu "
                + "FROM LichHen lh "
                + "JOIN BenhNhan bn ON lh.BenhNhan_ID = bn.BenhNhan_ID "
                + "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID "
                + "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID "
                + "JOIN DichVu dv ON lh.DichVu_ID = dv.DichVu_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Tạo đối tượng chính
                LichHen lh = new LichHen();
                lh.setLichHenID(rs.getInt("LichHen_ID"));
                lh.setBenhNhanID(rs.getInt("BenhNhan_ID"));
                lh.setBacSiID(rs.getInt("BacSi_ID"));
                lh.setDichVuID(rs.getInt("DichVu_ID"));
                lh.setNgayKham(rs.getDate("NgayKham"));
                lh.setGioKham(rs.getTime("GioKham"));
                lh.setTrangThai(rs.getString("TrangThai"));
                lh.setGhiChu(rs.getString("GhiChu"));

                BenhNhan bn = new BenhNhan();
                bn.setBenhNhanID(rs.getInt("BenhNhan_ID"));
                TaiKhoan tkBN = new TaiKhoan();
                tkBN.setHoTen(rs.getString("TenBN"));
                bn.setTaiKhoan(tkBN);
                lh.setBenhNhan(bn);
                BacSi bs = new BacSi();
                bs.setBacSiID(rs.getInt("BacSi_ID"));
                TaiKhoan tkBS = new TaiKhoan();
                tkBS.setHoTen(rs.getString("TenBS"));
                bs.setTaiKhoan(tkBS);
                lh.setBacSi(bs);
                DichVu dv = new DichVu();
                dv.setDichVuID(rs.getInt("DichVu_ID"));
                dv.setTenDichVu(rs.getString("TenDichVu"));
                lh.setDichVu(dv);

                list.add(lh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<LichHen> getLichHenByBenhNhan(int benhNhanID) {
        List<LichHen> list = new ArrayList<>();
        String sql = "SELECT lh.*, tkBS.HoTen AS TenBS, dv.TenDichVu " +
                 "FROM LichHen lh " +
                 "JOIN BacSi bs ON lh.BacSi_ID = bs.BacSi_ID " +
                 "JOIN TaiKhoan tkBS ON bs.TaiKhoan_ID = tkBS.TaiKhoan_ID " +
                 "JOIN DichVu dv ON lh.DichVu_ID = dv.DichVu_ID " +
                 "WHERE lh.BenhNhan_ID = ? ORDER BY lh.NgayKham DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, benhNhanID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichHen lh = new LichHen();
                    lh.setLichHenID(rs.getInt("LichHen_ID"));
                    lh.setNgayKham(rs.getDate("NgayKham"));
                    lh.setGioKham(rs.getTime("GioKham"));
                    lh.setTrangThai(rs.getString("TrangThai"));

                    BacSi bs = new BacSi();
                    TaiKhoan tk = new TaiKhoan();
                    tk.setHoTen(rs.getString("TenBS"));
                    bs.setTaiKhoan(tk);
                    lh.setBacSi(bs);

                    DichVu dv = new DichVu();
                    dv.setTenDichVu(rs.getString("TenDichVu"));
                    lh.setDichVu(dv);

                    list.add(lh);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    public boolean insertLichHen(LichHen model) {
        String sql = "INSERT INTO LichHen (BenhNhan_ID, BacSi_ID, DichVu_ID, NgayKham, GioKham, GhiChu, TrangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, model.getBenhNhanID());
            ps.setInt(2, model.getBacSiID());
            ps.setInt(3, model.getDichVuID());
            ps.setDate(4, model.getNgayKham());
            ps.setTime(5, model.getGioKham());
            ps.setString(6, model.getGhiChu());
            ps.setString(7, "Chờ xác nhận"); // Mặc định theo quy tắc DB

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateTrangThai(int lichHenID, String trangThaiMoi) {
        String sql = "UPDATE LichHen SET TrangThai = ? WHERE LichHen_ID = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setString(1, trangThaiMoi); // Truyền vào "Đã xác nhận" hoặc "Đã hủy"
            ps.setInt(2, lichHenID);
        
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}