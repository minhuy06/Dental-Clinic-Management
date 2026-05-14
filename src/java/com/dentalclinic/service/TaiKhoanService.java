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
    public TaiKhoan checkLogin(String phone,String password){
        TaiKhoan tk =null;
        TaiKhoanDAO tkdao=new TaiKhoanDAO();
        tk = tkdao.getTaiKhoanByPhone(phone);
        
        if(tk != null){
            if(checkPAP(tk,phone,password)){
                return tk;
            }
        }
        return null;
    }
    private boolean checkPAP(TaiKhoan tk,String phone,String password){
        return (phone.equals(tk.getSoDienThoai())) && (password.equals(tk.getMatKhau()));
    }
}
