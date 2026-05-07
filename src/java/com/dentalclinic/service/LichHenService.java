package com.dentalclinic.service;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHen;
import java.util.List;

public class LichHenService {
    private LichHenDAO lichHenDAO = new LichHenDAO();

    public List<LichHen> getAllLichHen(){
        return lichHenDAO.getAllLichHen();
    }

    public List<LichHen> getLichHenByBenhNhan(int benhNhanID){
        return lichHenDAO.getLichHenByBenhNhan(benhNhanID);
    }

    
}