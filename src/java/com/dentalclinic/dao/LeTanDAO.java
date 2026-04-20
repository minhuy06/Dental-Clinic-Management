/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.LeTan;
import com.dentalclinic.utils.DBConnection;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.dentalclinic.dao.TaiKhoanDAO;
/**
 *
 * @author kinhm
 */
public class LeTanDAO {
    public ArrayList<LeTan> layDanhSachLeTan(){
        ArrayList<LeTan> danhSach = new ArrayList();
        String sql = "SELECT * FROM Letan";
        try(Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    LeTan letan = new LeTan();
                    
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return danhSach;
    }
}
