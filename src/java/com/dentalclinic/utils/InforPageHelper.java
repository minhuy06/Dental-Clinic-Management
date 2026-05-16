package com.dentalclinic.utils;

import com.dentalclinic.dao.DichVuDAO;
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

    /** Dữ liệu mẫu bác sĩ (ảnh Unsplash) cho trang công khai. */
    public static String buildSampleDoctorListJson() {
        return "["
                + "{\"name\":\"BS. Trần Văn An\",\"specialty\":\"Tổng quát\",\"degree\":\"CKI\",\"imgUrl\":\"https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=400&h=400&fit=crop\"},"
                + "{\"name\":\"BS. Nguyễn Thị Mai\",\"specialty\":\"Thẩm mỹ\",\"degree\":\"Thạc sĩ\",\"imgUrl\":\"https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=400&h=400&fit=crop\"},"
                + "{\"name\":\"BS. Lê Hoàng Nam\",\"specialty\":\"Chỉnh nha\",\"degree\":\"CKII\",\"imgUrl\":\"https://images.unsplash.com/photo-1622253692010-333ef3b4b55a?w=400&h=400&fit=crop\"},"
                + "{\"name\":\"BS. Phạm Thu Hà\",\"specialty\":\"Nha chu\",\"degree\":\"CKI\",\"imgUrl\":\"https://images.unsplash.com/photo-1594824476967-48c8b964273f?w=400&h=400&fit=crop\"},"
                + "{\"name\":\"BS. Đỗ Minh Tuấn\",\"specialty\":\"Implant\",\"degree\":\"Tiến sĩ\",\"imgUrl\":\"https://images.unsplash.com/photo-1537368910025-700dbbe09483?w=400&h=400&fit=crop\"},"
                + "{\"name\":\"BS. Võ Thị Lan\",\"specialty\":\"Nha khoa trẻ em\",\"degree\":\"CKI\",\"imgUrl\":\"https://images.unsplash.com/photo-1582750433449-648ed127bb54?w=400&h=400&fit=crop\"}"
                + "]";
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
