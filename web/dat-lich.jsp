<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String loggedInUser = (String) session.getAttribute("loggedInUser");
    boolean isLoggedIn  = (loggedInUser != null);
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lịch, Dịch vụ & Bác sĩ - Nha Khoa 5AE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/datlich.css">
</head>
<body>
    <jsp:include page="components/header.jsp" />
    <main>
        <section class="page-hero">
            <div class="page-hero-bg"><img src="${pageContext.request.contextPath}/assets/img/cham-soc.jpg" alt="Nha khoa"></div>
            <div class="page-hero-overlay"></div>
            <div class="page-hero-content">
                <h1>Chăm sóc răng miệng toàn diện</h1>
                <p>Đặt lịch khám nhanh chóng, dịch vụ đa dạng, bác sĩ chuyên khoa hàng đầu</p>
            </div>
        </section>

        <!-- DAT LICH -->
        <section class="booking-section" id="datlich">
            <div class="container">
                <div class="section-title"><h2>Đặt lịch khám</h2></div>
                <p class="section-intro">Chọn dịch vụ, ngày giờ phù hợp. Phòng khám sẽ phân bác sĩ và liên hệ xác nhận trong 30 phút.</p>

                <div class="new-booking-card">
                    <!-- Ngay + Gio -->
                    <div class="nb-row-2">
                        <div class="form-group" id="dateGroup">
                            <label>Ngày khám <span class="req">*</span></label>
                            <input type="date" class="form-control" id="bookingDate">
                            <div class="form-error">Vui lòng chọn ngày</div>
                        </div>
                        <div class="form-group" id="timeGroup">
                            <label>Giờ khám <span class="req">*</span></label>
                            <select class="form-control" id="bookingTimeSelect">
                                <option value="">Chọn giờ</option>
                            </select>
                            <div class="form-error">Vui lòng chọn giờ</div>
                        </div>
                    </div>

                    <!-- Dich vu checkbox grid -->
                    <div class="form-group" id="serviceGroup">
                        <label>🦷 Chọn dịch vụ (có thể chọn nhiều) <span class="req">*</span></label>
                        <div class="svc-checkbox-grid" id="svcCheckboxGrid"></div>
                        <div class="form-error">Vui lòng chọn ít nhất 1 dịch vụ</div>
                    </div>

                    <!-- Dich vu da chon -->
                    <div id="selectedServicesBox" class="nb-selected-box" style="display:none;">
                        <div class="nb-selected-header">🛒 Dịch vụ đã chọn</div>
                        <div class="nb-selected-list" id="nbSelectedList"></div>
                        <div class="nb-total">
                            <span class="nb-total-label">💰 Tổng tiền:</span>
                            <span id="nbTotalValue">0</span> đ
                        </div>
                    </div>

                    <!-- Ghi chu -->
                    <div class="form-group" style="margin-top:16px;">
                        <label>Ghi chú</label>
                        <textarea class="form-control" id="bookingNote" placeholder="Ghi chú thêm..." rows="3"></textarea>
                    </div>

                    <div style="display:flex;gap:12px;justify-content:flex-end;margin-top:20px;">
                        <button type="button" onclick="resetBookingForm()" class="btn btn-outline">Hủy</button>
                        <button type="button" onclick="handleBooking()" class="btn btn-primary">Lưu lịch hẹn</button>
                    </div>
                </div>
            </div>
        </section>

        <div style="text-align:center;padding:30px 0;">
            <div style="width:80px;height:1px;background:linear-gradient(to right,transparent,#dee2e6,transparent);margin:0 auto;"></div>
            <div style="margin-top:-10px;display:inline-block;background:white;padding:0 16px;font-size:0.85rem;color:#0056b3;font-weight:600;letter-spacing:2px;">● ● ●</div>
        </div>

        <!-- DICH VU -->
        <section class="services-price-section" id="dichvu">
            <div class="container">
                <div class="section-title"><h2>Dịch vụ</h2></div>
                <p class="section-intro">Giá niêm yết công khai, minh bạch. Không phát sinh chi phí ẩn.</p>
                <div class="service-toolbar">
                    <input type="text" class="form-control service-search" id="serviceSearch" placeholder="🔍 Tìm kiếm dịch vụ..." oninput="filterServices()">
                    <select class="form-control service-filter" id="serviceFilter" onchange="filterServices()">
                        <option value="all">Tất cả danh mục</option>
                        <option value="kham">Khám & Chẩn đoán</option>
                        <option value="tham-my">Thẩm mỹ</option>
                        <option value="chinh-nha">Chỉnh nha</option>
                        <option value="phau-thuat">Phẫu thuật</option>
                        <option value="tre-em">Trẻ em</option>
                        <option value="khac">Khác</option>
                    </select>
                </div>
                <div class="price-table-wrapper">
                    <table class="price-table" id="priceTable">
                        <thead><tr><th>STT</th><th>Dịch vụ</th><th>⏱ Thời gian</th><th>Giá (VNĐ)</th></tr></thead>
                        <tbody id="serviceTableBody"></tbody>
                    </table>
                </div>
                <div class="text-center mt-3">
                    <button class="btn btn-outline" id="showMoreBtn" onclick="toggleServices()">Xem thêm dịch vụ ↓</button>
                </div>
            </div>
        </section>

        <div style="text-align:center;padding:30px 0;">
            <div style="width:80px;height:1px;background:linear-gradient(to right,transparent,#dee2e6,transparent);margin:0 auto;"></div>
            <div style="margin-top:-10px;display:inline-block;background:#f0f4f8;padding:0 16px;font-size:0.85rem;color:#0056b3;font-weight:600;letter-spacing:2px;">● ● ●</div>
        </div>

        <!-- BAC SI -->
        <section class="doctors-section" id="bacsi">
            <div class="container">
                <div class="section-title"><h2>Đội ngũ bác sĩ</h2></div>
                <p class="section-intro">Các bác sĩ được đào tạo chuyên sâu, nhiều năm kinh nghiệm.</p>
                <div class="doctors-grid" id="doctorsGrid">
                    <%-- Render động từ backend --%>
                </div>
            </div>
        </section>
    </main>
    <jsp:include page="components/footer.jsp" />

    <!-- POPUP YEU CAU DANG NHAP -->
    <div class="system-modal-overlay" id="loginRequiredModal" onclick="if(event.target===this)closeLoginModal()">
        <div class="system-modal">
            <span class="sys-icon">🔐</span>
            <div class="sys-title">Yêu cầu đăng nhập</div>
            <div class="sys-msg">Bạn cần đăng nhập để đặt lịch khám. Vui lòng đăng nhập hoặc đăng ký tài khoản để tiếp tục.</div>
            <div style="display:flex;gap:12px;justify-content:center;">
                <button class="sys-close" onclick="goToLogin()">🔑 Đăng nhập</button>
                <button class="sys-close" style="background:var(--text-muted);" onclick="closeLoginModal()">Hủy</button>
            </div>
        </div>
    </div>

    <script>window.IS_LOGGED_IN = <%= isLoggedIn %>;</script>

    <%-- ================================================================
         DATA INJECTION — Backend đặt các attribute vào request scope:
           request.setAttribute("serviceListJson", "[{...}, {...}]");
           request.setAttribute("doctorListJson",  "[{...}, {...}]");

         Mỗi service object cần: id, name, desc, time, price, cat, perUnit, unit
         Mỗi doctor object cần:  name, specialty, degree, imgUrl

         Nếu tên field trong DB khác, map lại trong Servlet/Controller trước khi
         set attribute — KHÔNG cần sửa file JS.
    ================================================================= --%>
    <script>
    (function() {
        // ── DỊCH VỤ ──────────────────────────────────────────────────
        // Backend inject: request.setAttribute("serviceListJson", jsonString);
        var injectedServices = '${not empty serviceListJson ? serviceListJson : ""}';

        if (injectedServices && injectedServices !== '') {
            try {
                window.allServices = JSON.parse(injectedServices);
            } catch(e) {
                console.warn('[dat-lich] serviceListJson parse error:', e);
                window.allServices = null;
            }
        }
        // Nếu backend chưa inject thì allServices sẽ dùng fallback trong dat-lich.js

        // ── BÁC SĨ ───────────────────────────────────────────────────
        // Backend inject: request.setAttribute("doctorListJson", jsonString);
        var injectedDoctors = '${not empty doctorListJson ? doctorListJson : ""}';
        var doctors = null;

        if (injectedDoctors && injectedDoctors !== '') {
            try {
                doctors = JSON.parse(injectedDoctors);
            } catch(e) {
                console.warn('[dat-lich] doctorListJson parse error:', e);
            }
        }

        // Fallback: dữ liệu mẫu nếu backend chưa inject
        if (!doctors || doctors.length === 0) {
            doctors = [
                {name:'BS. Nguyễn Hải',  specialty:'Tổng quát',        degree:'CKI',    imgUrl:'assets/img/doctors/bs-nguyen-hai.jpg'},
                {name:'BS. Trần Tâm',    specialty:'Chỉnh nha',         degree:'Thạc sĩ',imgUrl:'assets/img/doctors/bs-tran-tam.jpg'},
                {name:'BS. Lê Quang',    specialty:'Phục hình răng',    degree:'Tiến sĩ',imgUrl:'assets/img/doctors/bs-le-quang.jpg'},
                {name:'BS. Phạm Hương',  specialty:'Thẩm mỹ',           degree:'CKI',    imgUrl:'assets/img/doctors/bs-pham-huong.jpg'},
                {name:'BS. Hoàng Quân',  specialty:'Phẫu thuật miệng',  degree:'CKII',   imgUrl:'assets/img/doctors/bs-hoang-quan.jpg'},
                {name:'BS. Đặng Ngân',   specialty:'Nhổ răng',           degree:'Cử nhân',imgUrl:'assets/img/doctors/bs-dang-ngan.jpg'}
            ];
        }

        // Render doctor cards
        var grid = document.getElementById('doctorsGrid');
        if (grid) {
            var ctx = '${pageContext.request.contextPath}';
            grid.innerHTML = doctors.map(function(d) {
                var img = d.imgUrl
                    ? (d.imgUrl.indexOf('http') === 0 ? d.imgUrl : ctx + '/' + d.imgUrl)
                    : ctx + '/assets/img/doctors/default.jpg';
                return '<div class="doctor-card">'
                    + '<div class="doctor-img"><img src="' + img + '" alt="' + d.name + '" onerror="this.src=\'' + ctx + '/assets/img/doctors/default.jpg\'"></div>'
                    + '<div class="doctor-info">'
                    + '<h3>' + d.name + '</h3>'
                    + '<span class="doctor-specialty">' + (d.specialty || '') + '</span>'
                    + '<span class="doctor-degree">' + (d.degree || '') + '</span>'
                    + '</div></div>';
            }).join('');
        }
    })();
    </script>

    <script src="${pageContext.request.contextPath}/assets/js/dat-lich.js"></script>
</body>
</html>