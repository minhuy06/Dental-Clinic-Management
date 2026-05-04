/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.ChuyenKhoa;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.PhongKham;
import com.dentalclinic.model.PhongKhamDichVu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.dentalclinic.utils.DBConnection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author kinhm
 */
public class DichVuDAO {
    //Chỉ lấy thông tin cơ bản của dịch vụ
    public DichVu getDVByID(int id){
        DichVu dv = null;
        String sql = "SELECT * FROM Dichvu WHERE Dichvu_ID = ?";
        try(Connection cn = DBConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                dv.setDichVuID(id);
                dv.setChuyenKhoaID(rs.getInt("Chuyenkhoa_ID"));
                dv.setGiaTien(rs.getDouble("Giatien"));
                dv.setTenDichVu(rs.getString("Tendichvu"));
                dv.setThoiLuongDuKien(rs.getInt("Thoiluongdukien"));
            }
        } catch (Exception e){
            System.out.println("Loi truy xuat:" + e.getMessage());
        }
        return dv;
    }
    
    public List<PhongKhamDichVu> getDSPhongKhamByDichVuID(int id){
        List<PhongKhamDichVu> ds = new ArrayList();
        String sql = "SELECT dv.*, pk.* FROM PhongKhamDichVu pkdv join PhongKham pk on pkdv.PhongKham_ID = pk.PhongKham_ID join DichVu dv on dv.DichVu_ID = pkdv.DichVu_ID WHERE dv.DichVu_ID = ?";
        
        try (Connection cn=DBConnection.getConnection();PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PhongKhamDichVu pkdv = new PhongKhamDichVu();
                PhongKham pk = new PhongKham();
                pkdv.setDichVuID(id);
                pk.setPhongID(rs.getInt("pk.PhongKham_ID"));
                pk.setTenPhong(rs.getString("pk.Tenphong"));
                pk.setTrangThai(rs.getString("pk.Trangthai"));
                pkdv.setPhongKham(pk);
                ds.add(pkdv);
            }
        } catch (Exception e){
            
        }
        return ds;
    }
    public List<DichVu> getAllDichVu(){
        List<DichVu> ds = new ArrayList();
        
        String sql = "SELECT * From Dichvu dv join ChuyenKhoa ck on dv.ChuyenKhoa_ID = ck.ChuyenKhoa_ID";
        
        try(Connection cn = DBConnection.getConnection();PreparedStatement ps = cn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                DichVu dv = new DichVu();
                ChuyenKhoa ck = new ChuyenKhoa();
                ck.setTenChuyenKhoa(rs.getString("TenChuyenKhoa"));
                dv.setDichVuID(rs.getInt("DichVu_ID"));
                dv.setGiaTien(rs.getDouble("GiaTien"));
                dv.setTenDichVu(rs.getString("TenDichVu"));
                dv.setThoiLuongDuKien(rs.getInt("ThoiLuongDuKien"));
                dv.setChuyenKhoaID(rs.getInt("ChuyenKhoa_ID"));
                dv.setChuyenKhoa(ck);
                ds.add(dv);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return ds;
    }
}
