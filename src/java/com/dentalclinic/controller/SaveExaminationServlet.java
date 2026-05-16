package com.dentalclinic.controller;

import com.dentalclinic.dao.PhieuKhamDAO;
import com.dentalclinic.model.ChiTietDichVu;
import com.dentalclinic.model.PhieuKham;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SaveExaminationServlet", urlPatterns = {"/api/doctor/save-examination"})
public class SaveExaminationServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            String body = readBody(request);
            if (body == null || body.trim().isEmpty()) {
                sendJson(response, false, "Dữ liệu không hợp lệ!");
                return;
            }

            JsonObject json = gson.fromJson(body, JsonObject.class);
            if (json == null) {
                sendJson(response, false, "Dữ liệu không hợp lệ!");
                return;
            }

            boolean draft = json.has("draft") && !json.get("draft").isJsonNull() && json.get("draft").getAsBoolean();
            PhieuKham phieuKham = parsePhieuKham(json);

            if (phieuKham.getLichHenID() <= 0) {
                sendJson(response, false, "Lỗi: Không xác định được Lịch Hẹn!");
                return;
            }
            if (phieuKham.getDanhSachDichVu() == null || phieuKham.getDanhSachDichVu().isEmpty()) {
                sendJson(response, false, "Vui lòng chỉ định ít nhất một dịch vụ lâm sàng!");
                return;
            }

            PhieuKhamDAO dao = new PhieuKhamDAO();
            if (draft) {
                dao.luuPhieuKhamNhap(phieuKham);
                sendJson(response, true, "Đã lưu phiếu khám. Bạn có thể tiếp tục khám sau.");
            } else {
                dao.luuPhieuKhamLamSang(phieuKham);
                sendJson(response, true, "Lưu phiếu khám và hoàn tất thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if (msg == null || msg.trim().isEmpty()) {
                msg = e.getClass().getSimpleName();
            }
            sendJson(response, false, "Lỗi: " + msg);
        }
    }

    private PhieuKham parsePhieuKham(JsonObject json) {
        PhieuKham pk = new PhieuKham();
        if (json.has("lichHenID") && !json.get("lichHenID").isJsonNull()) {
            pk.setLichHenID(json.get("lichHenID").getAsInt());
        }
        if (json.has("chanDoan") && !json.get("chanDoan").isJsonNull()) {
            pk.setChanDoan(json.get("chanDoan").getAsString());
        }
        if (json.has("lyDoKham") && !json.get("lyDoKham").isJsonNull()) {
            pk.setLyDoKham(json.get("lyDoKham").getAsString());
        }
        if (json.has("ghiChu") && !json.get("ghiChu").isJsonNull()) {
            pk.setGhiChu(json.get("ghiChu").getAsString());
        }
        pk.setDanhSachDichVu(parseChiTietDichVu(json));
        return pk;
    }

    private List<ChiTietDichVu> parseChiTietDichVu(JsonObject json) {
        List<ChiTietDichVu> list = new ArrayList<>();
        if (!json.has("danhSachDichVu") || !json.get("danhSachDichVu").isJsonArray()) {
            return list;
        }
        JsonArray arr = json.getAsJsonArray("danhSachDichVu");
        for (JsonElement el : arr) {
            if (!el.isJsonObject()) {
                continue;
            }
            JsonObject o = el.getAsJsonObject();
            ChiTietDichVu ctd = new ChiTietDichVu();
            if (o.has("dichVuID") && !o.get("dichVuID").isJsonNull()) {
                ctd.setDichVuID(o.get("dichVuID").getAsInt());
            } else if (o.has("dichVuId") && !o.get("dichVuId").isJsonNull()) {
                ctd.setDichVuID(o.get("dichVuId").getAsInt());
            }
            if (o.has("donGia") && !o.get("donGia").isJsonNull()) {
                ctd.setDonGia(o.get("donGia").getAsDouble());
            }
            if (o.has("viTriRang") && !o.get("viTriRang").isJsonNull()) {
                ctd.setViTriRang(o.get("viTriRang").getAsInt());
            }
            if (o.has("soLuong") && !o.get("soLuong").isJsonNull()) {
                ctd.setSoLuong(o.get("soLuong").getAsInt());
            } else {
                ctd.setSoLuong(1);
            }
            if (ctd.getDichVuID() > 0) {
                list.add(ctd);
            }
        }
        return list;
    }

    private String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private void sendJson(HttpServletResponse response, boolean success, String message) throws IOException {
        JsonObject out = new JsonObject();
        out.addProperty("success", success);
        out.addProperty("message", message == null ? "" : message);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(gson.toJson(out));
        }
    }
}
