package com.dentalclinic.utils;

import com.dentalclinic.dao.BacSiDAO;
import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.model.BacSi;
import com.dentalclinic.model.DichVu;
import java.util.List;

/** Dữ liệu JSON cho trang Infor (đặt lịch, dịch vụ, bác sĩ). */
public final class InforPageHelper {

    private InforPageHelper() {
    }

    public static String jsonEscape(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

    public static String buildServiceListJson() {
        return buildServiceListJson(new DichVuDAO().getAll());
    }

    public static String buildServiceListJson(List<DichVu> services) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (services != null) {
            for (int i = 0; i < services.size(); i++) {
                DichVu dv = services.get(i);
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("{")
                        .append("\"id\":").append(dv.getDichVuID()).append(",")
                        .append("\"name\":\"").append(jsonEscape(dv.getTenDichVu())).append("\",")
                        .append("\"desc\":\"").append(jsonEscape(dv.getTenDichVu())).append("\",")
                        .append("\"time\":\"").append(dv.getThoiLuongDuKien()).append(" phút\",")
                        .append("\"price\":").append((long) dv.getGiaTien()).append(",")
                        .append("\"cat\":\"all\",")
                        .append("\"perUnit\":").append(dv.isTinhTheoRang());
                if (dv.isTinhTheoRang()) {
                    sb.append(",\"unit\":\"răng\"");
                }
                sb.append("}");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String buildDoctorListJson() {
        List<BacSi> doctors = new BacSiDAO().getAllForDisplay();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < doctors.size(); i++) {
            BacSi bs = doctors.get(i);
            if (i > 0) {
                sb.append(",");
            }
            String name = bs.getTaiKhoan() != null ? bs.getTaiKhoan().getHoTen() : "";
            String specialty = bs.getChuyenKhoa() != null ? bs.getChuyenKhoa().getTenChuyenKhoa() : "";
            String degree = bs.getTrinhDo() != null ? bs.getTrinhDo() : "";
            String img = bs.getAnhDaiDien() != null ? bs.getAnhDaiDien() : "";
            sb.append("{")
                    .append("\"name\":\"").append(jsonEscape(name)).append("\",")
                    .append("\"specialty\":\"").append(jsonEscape(specialty)).append("\",")
                    .append("\"degree\":\"").append(jsonEscape(degree)).append("\",")
                    .append("\"imgUrl\":\"").append(jsonEscape(img)).append("\"")
                    .append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    /** datlich | dichvu | bacsi */
    public static String resolveSection(String pathInfo) {
        if (pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)) {
            return "datlich";
        }
        if (pathInfo.equalsIgnoreCase("/Schedule")) {
            return "datlich";
        }
        if (pathInfo.equalsIgnoreCase("/service")) {
            return "dichvu";
        }
        if (pathInfo.equalsIgnoreCase("/Doctor")) {
            return "bacsi";
        }
        return null;
    }
}
