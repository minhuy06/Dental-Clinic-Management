/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.service;

import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.TaiKhoan;
/**
 *
 * @author kinhm
 */

public class TaiKhoanService {
    public TaiKhoan kiemTraTaiKhoan(String sdt,String matKhau){
        
        TaiKhoanDAO tkd = new TaiKhoanDAO();
        TaiKhoan tk = tkd.layThongTinTaiKhoanSDT(sdt);
        if(tk != null){
            if(sdt.equals(tk.getSoDienThoai()) && matKhau.equals(tk.getMatKhau())){
                return tk;
                }
        }
        return null;
    }
}
