/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.dao;

import com.dentalclinic.model.TaiKhoan;
import com.dentalclinic.utils.CONNECTIONSQLSERVER;
import com.sun.jdi.connect.spi.Connection;

/**
 *
 * @author kinhm
 */
//Get thông tin tài khoản
public class TaiKhoanDAO {
    //Truy xuất database
    public TaiKhoan layThongTinTaiKhoanSDT(String sdt){
        TaiKhoan tk = null;
        String sql = "SELECT * FROM Taikhoan WHERE SoDienThoai = ?";
        
        try (Connection conn = CONNECTIONSQLSERVER.)
    }
}
