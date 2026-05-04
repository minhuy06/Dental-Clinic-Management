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
    //Kiểm tra số điện thoại, mật khẩu
    public TaiKhoan getAccount(String phone,String password){
        TaiKhoanDAO tkd = new TaiKhoanDAO();
        TaiKhoan tk = tkd.getAcountInfo(phone);
        
        if(tk != null){
            if(checkAccount(tk,phone,password)){
                return tk;
            }
        }
        return null;
    }
    private static boolean checkAccount(TaiKhoan tk,String phone,String password){
        return tk.getSoDienThoai().equals(phone) && tk.getMatKhau().equals(password);
    }
}
