package com.dentalclinic.service;

import com.dentalclinic.dao.TaiKhoanDAO;
import com.dentalclinic.model.TaiKhoan;

public class TaiKhoanService {
    public TaiKhoan checkLogin(String phone, String password) {
        TaiKhoanDAO tkdao = new TaiKhoanDAO();
        TaiKhoan tk = tkdao.getTaiKhoanByPhone(phone);
        if (tk != null && checkPAP(tk, phone, password)) {
            return tk;
        }
        return null;
    }

    private boolean checkPAP(TaiKhoan tk, String phone, String password) {
        return phone.equals(tk.getSoDienThoai()) && password.equals(tk.getMatKhau());
    }
}
