<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <div class="doctor-info"><i class="fa-solid fa-user-doctor"></i> PHÒNG KHÁM SỐ 01 - BS. NGUYỄN HOÀNG</div>
    <div class="appointment-time"><i class="fa-regular fa-calendar"></i> 22/05/2024 | 09:30 AM</div>
</header>

<div class="main-layout">
    <aside class="patient-sidebar">
        <div class="card">
            <div class="patient-info-header">
                <div class="patient-avatar"><i class="fa-solid fa-user-large"></i></div>
                <h3 class="patient-name">Nguyễn Văn Hiển</h3>
                <p class="patient-detail"><i class="fa-regular fa-venus-mars"></i> Nam | 28 Tuổi</p>
                <p class="patient-detail"><i class="fa-solid fa-phone"></i> 0987 654 321</p>
                <p class="patient-detail"><i class="fa-regular fa-address-card"></i> Mã BN: #BN001234</p>
            </div>
            <div class="medical-history">
                <div class="section-title"><i class="fa-solid fa-circle-exclamation"></i> CẢNH BÁO Y TẾ</div>
                <span class="allergy-tag"><i class="fa-solid fa-syringe"></i> Dị ứng Penicillin</span>
                <div class="section-title"><i class="fa-solid fa-clock-rotate-left"></i> TIỀN SỬ BỆNH LÝ</div>
                <div class="history-item"><strong>15/01/2024:</strong> Nhổ răng số 38 (răng khôn). Lành thương tốt.</div>
                <div class="history-item"><strong>10/10/2023:</strong> Hàn răng sâu số 46.</div>
            </div>
        </div>
        <div class="card">
            <div class="section-title"><i class="fa-regular fa-note-sticky"></i> GHI CHÚ CỦA LỄ TÂN</div>
            <p class="note-from-reception">"Bệnh nhân đau buốt vùng răng hàm dưới bên trái khi uống nước lạnh 2 ngày nay."</p>
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
                    <textarea id="symptoms" rows="2">Đau buốt khi ăn đồ lạnh, ê buốt răng số 46</textarea>
                </div>
                <div class="form-group">
                    <label><i class="fa-regular fa-clipboard"></i> CHẨN ĐOÁN</label>
                    <textarea id="diagnosis" rows="2">Sâu răng độ 3 (gần tủy) - Răng 46</textarea>
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
            <button id="addServiceBtn" class="btn-add-service"><i class="fa-solid fa-plus"></i> Thêm dịch vụ</button>
        </div>

        <div class="card">
            <label><i class="fa-solid fa-prescription-bottle"></i> ĐƠN THUỐC / DẶN DÒ</label>
            <textarea id="prescription" rows="3">- Amoxicillin 500mg: 2 lần/ngày x 5 ngày
- Paracetamol 500mg: khi đau
- Dặn dò: Tránh nhai bên điều trị, vệ sinh nhẹ nhàng</textarea>
        </div>

        <div class="action-bar">
            <button class="btn btn-outline" id="printBtn"><i class="fa-solid fa-print"></i> In đơn</button>
            <button class="btn btn-outline" id="xrayBtn"><i class="fa-solid fa-camera"></i> X-Quang</button>
            <button class="btn btn-danger" id="cancelBtn"><i class="fa-solid fa-xmark"></i> Hủy</button>
            <button class="btn btn-success" id="saveBtn"><i class="fa-solid fa-check-double"></i> HOÀN TẤT & LƯU</button>
        </div>
    </main>
</div>

<div id="toastMessage" class="toast-message"></div>

<script src="${pageContext.request.contextPath}/js/hoso.js"></script>
</body>
</html>