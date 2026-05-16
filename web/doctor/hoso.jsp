<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dentalclinic.model.TaiKhoan" %>
<%
    TaiKhoan loggedDoctor = (TaiKhoan) session.getAttribute("loggedInUser");
    String sessionDoctorName = "Bác sĩ";
    if (loggedDoctor != null && loggedDoctor.getHoTen() != null && !loggedDoctor.getHoTen().trim().isEmpty()) {
        sessionDoctorName = loggedDoctor.getHoTen().trim();
        if (!sessionDoctorName.toLowerCase().startsWith("bs")) {
            sessionDoctorName = "BS. " + sessionDoctorName;
        }
    }
    String patientName = request.getParameter("name");
    if (patientName == null || patientName.trim().isEmpty()) patientName = "Nguyễn Văn Hiển";
    String patientId = request.getParameter("patientId");
    if (patientId == null || patientId.trim().isEmpty()) patientId = "BN001234";
    String patientPhone = request.getParameter("phone");
    if (patientPhone == null || patientPhone.trim().isEmpty()) patientPhone = "0987 654 321";
    String doctorName = request.getParameter("doctor");
    if (doctorName == null || doctorName.trim().isEmpty()) doctorName = sessionDoctorName;
    String appointmentDateTime = request.getParameter("apptTime");
    if (appointmentDateTime == null || appointmentDateTime.trim().isEmpty()) appointmentDateTime = "22/05/2024 | 09:30 AM";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phòng Khám Nha Khoa 5AE - Hồ Sơ Khám Bệnh</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/doctor/css/hoso.css">
</head>
<body>

<header>
    <a class="btn-back" id="backBtn"  href="index.jsp"><i class="fa-solid fa-arrow-left"></i> Quay lại danh sách lịch hẹn</a>
    <div class="doctor-info" id="doctorRoomUI"><i class="fa-solid fa-user-doctor"></i> PHÒNG KHÁM SỐ 01 - <%= doctorName %></div>
    <div class="appointment-time"><i class="fa-regular fa-calendar"></i> <%= appointmentDateTime %></div>
</header>

<div class="main-layout">
    <aside class="patient-sidebar">
        <div class="card">
            <div class="patient-info-header">
                <div class="patient-avatar"><i class="fa-solid fa-user-large"></i></div>
                <h3 class="patient-name" id="patientNameUI"><%= patientName %></h3>
                <p class="patient-detail"><i class="fa-regular fa-venus-mars"></i> <span id="patientAgeGenderUI">Đang tải…</span></p>
                <p class="patient-detail"><i class="fa-solid fa-phone"></i> <span id="patientPhoneUI"><%= patientPhone %></span></p>
                <p class="patient-detail"><i class="fa-regular fa-address-card"></i> Mã BN: <span id="patientCodeUI">#<%= patientId %></span></p>
            </div>
            <div class="medical-history">
                <div class="section-title"><i class="fa-solid fa-circle-exclamation"></i> CẢNH BÁO Y TẾ</div>
                <span class="allergy-tag"><i class="fa-solid fa-syringe"></i> <span id="diUngThuocUI">Đang tải…</span></span>
                <div class="section-title"><i class="fa-solid fa-clock-rotate-left"></i> TIỀN SỬ BỆNH LÝ</div>
                <div class="history-item" id="tienSuBenhUI" style="white-space: pre-wrap;">Đang tải…</div>
            </div>
        </div>
        <div class="card">
            <div class="section-title"><i class="fa-regular fa-note-sticky"></i> GHI CHÚ CỦA LỄ TÂN</div>
            <p class="note-from-reception" id="receptionNoteUI">—</p>
        </div>
    </aside>

    <main class="exam-area">
        <div class="card">
            <div class="section-title" style="text-align: center;"><i class="fa-solid fa-tooth"></i> SƠ ĐỒ RĂNG (ODONTOGRAM)</div>
            <div class="teeth-map">
                <div class="teeth-row">
                    <div class="tooth" data-tooth="18">18</div><div class="tooth" data-tooth="17">17</div><div class="tooth" data-tooth="16">16</div>
                    <div class="tooth" data-tooth="15">15</div><div class="tooth" data-tooth="14">14</div><div class="tooth" data-tooth="13">13</div>
                    <div class="tooth" data-tooth="12">12</div><div class="tooth" data-tooth="11">11</div><div style="width:20px"></div>
                    <div class="tooth" data-tooth="21">21</div><div class="tooth" data-tooth="22">22</div><div class="tooth" data-tooth="23">23</div>
                    <div class="tooth" data-tooth="24">24</div><div class="tooth" data-tooth="25">25</div><div class="tooth" data-tooth="26">26</div>
                    <div class="tooth" data-tooth="27">27</div><div class="tooth" data-tooth="28">28</div>
                </div>
                <div class="teeth-row">
                    <div class="tooth issue" data-tooth="48">48</div><div class="tooth" data-tooth="47">47</div>
                    <div class="tooth selected" data-tooth="46">46</div><div class="tooth" data-tooth="45">45</div>
                    <div class="tooth" data-tooth="44">44</div><div class="tooth" data-tooth="43">43</div><div class="tooth" data-tooth="42">42</div>
                    <div class="tooth" data-tooth="41">41</div><div style="width:20px"></div>
                    <div class="tooth" data-tooth="31">31</div><div class="tooth" data-tooth="32">32</div><div class="tooth" data-tooth="33">33</div>
                    <div class="tooth" data-tooth="34">34</div><div class="tooth" data-tooth="35">35</div><div class="tooth" data-tooth="36">36</div>
                    <div class="tooth" data-tooth="37">37</div><div class="tooth" data-tooth="38">38</div>
                </div>
            </div>
            <p style="font-size:0.75rem; text-align:center;">⬤ Răng có vấn đề &nbsp;&nbsp; 🔵 Đang chọn &nbsp;&nbsp; ⚫ Click để chọn</p>
        </div>

        <div class="card">
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <div class="form-group">
                    <label><i class="fa-regular fa-message"></i> LÝ DO KHÁM</label>
                    <textarea id="symptoms" rows="2" placeholder="Lý do khám / triệu chứng…"></textarea>
                </div>
                <div class="form-group">
                    <label><i class="fa-regular fa-clipboard"></i> CHẨN ĐOÁN</label>
                    <textarea id="diagnosis" rows="2" placeholder="Chẩn đoán…"></textarea>
                </div>
            </div>

            <label><i class="fa-solid fa-list-check"></i> CHỈ ĐỊNH ĐIỀU TRỊ (Dịch vụ & Số lượng)</label>
            <div style="overflow-x: auto;">
                <table class="treatment-list">
                    <thead>
                        <tr>
                            <th style="min-width: 200px;">Dịch vụ</th>
                            <th style="width: 90px;">Răng số</th>
                            <th style="width: 90px;">Số lượng</th>
                            <th style="width: 120px;">Đơn giá</th>
                            <th style="width: 130px;">Thành tiền</th>
                            <th style="min-width: 150px;">Ghi chú</th>
                            <th style="width: 50px;"></th>
                        </tr>
                    </thead>
                    <tbody id="treatmentBody"></tbody>
                </table>
            </div>
            <div class="service-selection-wrapper" style="display: flex; gap: 10px; align-items: center; margin-top: 15px;">
                <select id="serviceSelect" style="max-width: 300px; padding: 8px; border-radius: 8px; border: 1px solid var(--border); outline: none;">
                    <option value="">-- Chọn dịch vụ để thêm --</option>
                </select>
                <button id="addServiceBtn" class="btn-add-service" style="margin-top: 0;"><i class="fa-solid fa-plus"></i> Thêm dịch vụ</button>
            </div>
        </div>

        <div class="card">
            <label><i class="fa-solid fa-prescription-bottle"></i> ĐƠN THUỐC / DẶN DÒ</label>
            <textarea id="prescription" rows="3" placeholder="Đơn thuốc / dặn dò…"></textarea>
        </div>

        <div class="action-bar">
            <button class="btn btn-outline" id="printBtn"><i class="fa-solid fa-print"></i> In đơn</button>
            <button class="btn btn-outline" id="xrayBtn"><i class="fa-solid fa-camera"></i> X-Quang</button>
            <button class="btn btn-danger" id="cancelBtn"><i class="fa-solid fa-xmark"></i> Hủy</button>
            <button type="button" class="btn btn-success" id="saveBtn"><i class="fa-solid fa-check-double"></i> Hoàn tất & lưu</button>
        </div>
    </main>
</div>

<div id="toastMessage" class="toast-message"></div>

<%@ page import="com.dentalclinic.dao.DichVuDAO" %>
<%@ page import="com.dentalclinic.dao.LichHenDAO" %>
<%@ page import="com.dentalclinic.model.DichVu" %>
<%@ page import="com.dentalclinic.model.LichHen" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%
    DichVuDAO dvDAO = new DichVuDAO();
    List<DichVu> listDV = dvDAO.getAll();
    String jsonDV = new Gson().toJson(listDV);

    String initialBookedJson = "[]";
    String lichHenIdParam = request.getParameter("id");
    if (lichHenIdParam != null && !lichHenIdParam.trim().isEmpty()) {
        try {
            int lhId = Integer.parseInt(lichHenIdParam.trim());
            LichHen lhDetail = new LichHenDAO().layChiTietLichHen(lhId);
            if (lhDetail != null && lhDetail.getDanhSachDichVuDat() != null && !lhDetail.getDanhSachDichVuDat().isEmpty()) {
                initialBookedJson = new Gson().toJson(lhDetail.getDanhSachDichVuDat());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
%>
<script>
    window.CONTEXT_PATH = '${pageContext.request.contextPath}';
    window.DOCTOR_HOSO_BOOTSTRAP = {
        patientName: '<%= patientName.replace("'", "\\'") %>',
        patientId: '<%= patientId.replace("'", "\\'") %>',
        patientPhone: '<%= patientPhone.replace("'", "\\'") %>',
        doctorName: '<%= doctorName.replace("'", "\\'") %>',
        appointmentDateTime: '<%= appointmentDateTime.replace("'", "\\'") %>'
    };
    var danhSachDichVuTuDB = <%= jsonDV %>;
    window.INITIAL_BOOKED_SERVICES = <%= initialBookedJson %>;
</script>
<script src="${pageContext.request.contextPath}/doctor/js/hoso.js?v=20260516l"></script>
</body>
</html>
