<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String scrollSection = (String) request.getAttribute("scrollSection");
    if (scrollSection == null || scrollSection.isEmpty()) {
        scrollSection = "datlich";
    }
    String pageTitleAttr = (String) request.getAttribute("pageTitle");
    String pageTitle = pageTitleAttr != null ? pageTitleAttr : "Đặt lịch, Dịch vụ & Bác sĩ - Nha Khoa 5AE";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/datlich.css">
</head>
<body data-scroll-section="<%= scrollSection %>">
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

        <section class="booking-section" id="datlich">
            <div class="container">
                <div class="section-title"><h2>Đặt lịch khám</h2></div>
                <p class="section-intro">Chọn dịch vụ, ngày giờ phù hợp. Phòng khám sẽ phân bác sĩ và liên hệ xác nhận trong 30 phút.</p>

                <div class="new-booking-card">
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

                    <div class="form-group" id="serviceGroup">
                        <label>🦷 Chọn dịch vụ (có thể chọn nhiều) <span class="req">*</span></label>
                        <div class="svc-checkbox-grid" id="svcCheckboxGrid"></div>
                        <div class="form-error">Vui lòng chọn ít nhất 1 dịch vụ</div>
                    </div>

                    <div id="selectedServicesBox" class="nb-selected-box" style="display:none;">
                        <div class="nb-selected-header">🛒 Dịch vụ đã chọn</div>
                        <div class="nb-selected-list" id="nbSelectedList"></div>
                        <div class="nb-total">
                            <span class="nb-total-label">💰 Tổng tiền:</span>
                            <span id="nbTotalValue">0</span> đ
                        </div>
                    </div>

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

        <section class="doctors-section" id="bacsi">
            <div class="container">
                <div class="section-title"><h2>Đội ngũ bác sĩ</h2></div>
                <p class="section-intro">Các bác sĩ được đào tạo chuyên sâu, nhiều năm kinh nghiệm.</p>
                <div class="doctors-grid" id="doctorsGrid"></div>
            </div>
        </section>
    </main>
    <jsp:include page="components/footer.jsp" />

    <script>
    (function() {
        window.allServices = <%= request.getAttribute("serviceListJson") == null ? "[]" : request.getAttribute("serviceListJson") %>;

        var doctors = <%= request.getAttribute("doctorListJson") == null ? "[]" : request.getAttribute("doctorListJson") %>;
        var grid = document.getElementById('doctorsGrid');
        if (grid && doctors && doctors.length) {
            var fallbackImg = 'https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=400&h=400&fit=crop';
            grid.innerHTML = doctors.map(function(d) {
                var img = d.imgUrl || fallbackImg;
                return '<div class="doctor-card">'
                    + '<div class="doctor-img"><img src="' + img + '" alt="' + d.name + '" loading="lazy" onerror="this.src=\'' + fallbackImg + '\'"></div>'
                    + '<div class="doctor-info">'
                    + '<h3>' + d.name + '</h3>'
                    + '<span class="doctor-specialty">' + (d.specialty || '') + '</span>'
                    + '<span class="doctor-degree">' + (d.degree || '') + '</span>'
                    + '</div></div>';
            }).join('');
        }

        var hash = window.location.hash ? window.location.hash.replace('#', '') : '<%= scrollSection %>';
        if (hash) {
            setTimeout(function() {
                var el = document.getElementById(hash);
                if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }, 200);
        }
    })();
    </script>

    <script src="${pageContext.request.contextPath}/assets/js/dat-lich.js"></script>
</body>
</html>
