<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lịch, Dịch vụ & Bác sĩ - Nha Khoa Kvone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/datlich.css">
</head>
<body>
    <jsp:include page="components/header.jsp" />
    <main>
        <section class="page-hero">
            <div class="page-hero-bg"><img src="https://images.unsplash.com/photo-1629909613654-28e377c37b09?w=1400&h=400&fit=crop" alt="Nha khoa"></div>
            <div class="page-hero-overlay"></div>
            <div class="page-hero-content">
                <div class="hero-breadcrumb"><a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a><span class="separator">›</span><span>Đặt lịch, Dịch vụ & Bác sĩ</span></div>
                <h1>Chăm sóc răng miệng toàn diện</h1>
                <p>Đặt lịch khám nhanh chóng, dịch vụ đa dạng, bác sĩ chuyên khoa hàng đầu</p>
            </div>
        </section>

        <!-- DAT LICH -->
        <section class="booking-section" id="datlich">
            <div class="container">
                <div class="section-title"><h2>Đặt lịch khám</h2></div>
                <p class="section-intro">Chọn dịch vụ, ngày giờ phù hợp. Phòng khám sẽ phân bác sĩ và liên hệ xác nhận trong 30 phút.</p>
                <div class="booking-wrapper">
                    <div class="booking-info">
                        <h3>📍 Thông tin phòng khám</h3>
                        <ul><li>🏥 48 Cao Thắng, Hải Châu, Đà Nẵng</li><li>📞 1900 1533</li><li>🕐 T2 - T7: 8:00 - 17:00</li><li>🕐 Chủ nhật: 8:00 - 12:00</li></ul>
                        <div class="booking-note"><strong>Chú ý:</strong><br>- Sau khi bạn đặt lịch xong, hãy check lịch ở của bạn sau 30 phút để bên mình xác nhận lại lịch của bạn đã được duyệt chưa nhé.<br>- Để mọi việc diễn ra thuận lợi, bạn hãy đến đúng giớ. <br>- Cảm ơn bạn đã tin tưởng ở chúng tôi!</div>
                    </div>
                    <form class="booking-form" id="bookingForm" onsubmit="return handleBooking(event)">
                        <div class="form-group" id="serviceGroup">
                            <label>Chọn dịch vụ <span style="color:#e74c3c">*</span></label>
                            <select class="form-control" id="serviceAdd" onchange="previewService()">
                                <option value="">-- Chọn dịch vụ để thêm --</option>
                            </select>
                            <!-- So luong (chi hien khi dich vu tinh theo don vi) -->
                            <div id="qtyRow" style="display:none;margin-top:8px;display:none;align-items:center;gap:10px;">
                                <label style="margin:0;font-size:0.85rem;font-weight:600;">Số lượng răng:</label>
                                <input type="number" id="qtyInput" class="form-control" value="1" min="1" max="20" style="width:80px;padding:8px 10px;">
                            </div>
                            <button type="button" onclick="addService()" style="margin-top:10px;padding:9px 22px;font-size:0.85rem;font-weight:600;background:var(--primary);color:white;border:none;border-radius:6px;cursor:pointer;font-family:var(--font-body);">+ Thêm dịch vụ</button>
                            <div class="form-error">Vui lòng chọn ít nhất 1 dịch vụ</div>
                            <div id="selectedServices" style="margin-top:14px;"></div>
                            <div id="priceDisplay" style="display:none;margin-top:10px;padding:12px 16px;background:#fff4e6;border-radius:6px;font-size:0.9rem;color:#d35400;">
                                Tổng chi phí dự kiến: <strong id="priceValue" style="color:#e67e22;font-size:1.1rem;"></strong> VNĐ
                            </div>
                        </div>
                        <div class="form-group" id="dateGroup">
                            <label>Chọn ngày khám <span style="color:#e74c3c">*</span></label>
                            <input type="date" class="form-control" id="bookingDate">
                            <div class="form-error">Vui lòng chọn ngày khám</div>
                        </div>
                        <div class="form-group" id="timeGroup">
                            <label>Chọn giờ khám <span style="color:#e74c3c">*</span></label>
                            <div class="time-grid" id="timeGrid"></div>
                            <input type="hidden" id="bookingTime">
                            <div class="form-error">Vui lòng chọn giờ khám</div>
                        </div>
                        <div class="form-group">
                            <label>Ghi chú</label>
                            <textarea class="form-control" id="bookingNote" placeholder="Mô tả triệu chứng hoặc yêu cầu..." rows="3"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-lg" style="width:100%;">Đặt lịch hẹn</button>
                    </form>
                </div>
            </div>
        </section>

        <div style="text-align:center;padding:30px 0;"><div style="width:80px;height:1px;background:linear-gradient(to right,transparent,#dee2e6,transparent);margin:0 auto;"></div><div style="margin-top:-10px;display:inline-block;background:white;padding:0 16px;font-size:0.85rem;color:#0056b3;font-weight:600;letter-spacing:2px;">● ● ●</div></div>

        <!-- DICH VU -->
        <section class="services-price-section" id="dichvu">
            <div class="container">
                <div class="section-title"><h2>Dịch vụ</h2></div>
                <p class="section-intro">Giá niêm yết công khai, minh bạch. Không phát sinh chi phí ẩn.</p>

                <!-- Tim kiem + Loc -->
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

        <div style="text-align:center;padding:30px 0;"><div style="width:80px;height:1px;background:linear-gradient(to right,transparent,#dee2e6,transparent);margin:0 auto;"></div><div style="margin-top:-10px;display:inline-block;background:#f0f4f8;padding:0 16px;font-size:0.85rem;color:#0056b3;font-weight:600;letter-spacing:2px;">● ● ●</div></div>

        <!-- BAC SI -->
        <section class="doctors-section" id="bacsi">
            <div class="container">
                <div class="section-title"><h2>Đội ngũ bác sĩ</h2></div>
                <p class="section-intro">Các bác sĩ được đào tạo chuyên sâu, nhiều năm kinh nghiệm.</p>
                <div class="doctors-grid">
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Nguyễn Hải</h3><span class="doctor-specialty">Tổng quát</span><span class="doctor-degree">CKI</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Trần Tâm</h3><span class="doctor-specialty">Chỉnh nha</span><span class="doctor-degree">Thạc sĩ</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Lê Quang</h3><span class="doctor-specialty">Phục hình răng</span><span class="doctor-degree">Tiến sĩ</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1614608682850-e0d6ed316d47?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Phạm Hương</h3><span class="doctor-specialty">Thẩm mỹ</span><span class="doctor-degree">CKI</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1537368910025-700350fe46c7?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Hoàng Quân</h3><span class="doctor-specialty">Phẫu thuật miệng</span><span class="doctor-degree">CKII</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1582750433449-648ed127bb54?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Đặng Ngân</h3><span class="doctor-specialty">Nhổ răng</span><span class="doctor-degree">Cử nhân</span></div></div>
                </div>
            </div>
        </section>
    </main>
    <jsp:include page="components/footer.jsp" />

    <script>
    // === DU LIEU DICH VU ===
    var allServices = [
        {id:'1',name:'Khám tổng quát',desc:'Kiểm tra răng miệng toàn diện',time:'20 phút',price:100000,cat:'kham',perUnit:false},
        {id:'2',name:'Cạo vôi răng',desc:'Loại bỏ mảng bám, vôi răng',time:'30 phút',price:200000,cat:'kham',perUnit:false},
        {id:'3',name:'Trám răng Composite',desc:'Trám thẩm mỹ bằng vật liệu Composite',time:'30 phút',price:300000,cat:'kham',perUnit:true,unit:'răng'},
        {id:'4',name:'Nhổ răng sữa',desc:'Nhổ răng sữa cho trẻ em',time:'15 phút',price:100000,cat:'tre-em',perUnit:true,unit:'răng'},
        {id:'5',name:'Nhổ răng khôn mọc thẳng',desc:'Nhổ răng khôn không phẫu thuật',time:'30 phút',price:1000000,cat:'phau-thuat',perUnit:true,unit:'răng'},
        {id:'6',name:'Nhổ răng khôn mọc ngầm',desc:'Tiểu phẫu nhổ răng khôn phức tạp',time:'60 phút',price:3000000,cat:'phau-thuat',perUnit:true,unit:'răng'},
        {id:'7',name:'Tẩy trắng răng Laser',desc:'Tẩy trắng bằng công nghệ Laser',time:'45 phút',price:2500000,cat:'tham-my',perUnit:false},
        {id:'8',name:'Tẩy trắng răng tại nhà',desc:'Máng tẩy trắng sử dụng tại nhà',time:'20 phút',price:1500000,cat:'tham-my',perUnit:false},
        {id:'9',name:'Lấy tủy răng cửa',desc:'Điều trị tủy răng cửa bị viêm',time:'45 phút',price:800000,cat:'kham',perUnit:true,unit:'răng'},
        {id:'10',name:'Lấy tủy răng hàm',desc:'Điều trị tủy răng hàm nhiều chân',time:'60 phút',price:1500000,cat:'kham',perUnit:true,unit:'răng'},
        {id:'11',name:'Bọc răng sứ Titan',desc:'Răng sứ kim loại Titan tiêu chuẩn',time:'45 phút/răng',price:2000000,cat:'tham-my',perUnit:true,unit:'răng'},
        {id:'12',name:'Bọc răng sứ Cercon',desc:'Răng sứ Cercon cao cấp Đức',time:'45 phút/răng',price:5000000,cat:'tham-my',perUnit:true,unit:'răng'},
        {id:'13',name:'Bọc răng sứ Zirconia',desc:'Răng sứ Zirconia nguyên khối',time:'45 phút/răng',price:6000000,cat:'tham-my',perUnit:true,unit:'răng'},
        {id:'14',name:'Mặt dán sứ Veneer',desc:'Dán sứ siêu mỏng thẩm mỹ',time:'60 phút/răng',price:7000000,cat:'tham-my',perUnit:true,unit:'răng'},
        {id:'15',name:'Cấy ghép Implant tiêu chuẩn',desc:'Trụ Implant Titanium tiêu chuẩn',time:'60-90 phút',price:15000000,cat:'phau-thuat',perUnit:true,unit:'trụ'},
        {id:'16',name:'Cấy ghép Implant cao cấp',desc:'Trụ Implant cao cấp Châu Âu',time:'60-90 phút',price:30000000,cat:'phau-thuat',perUnit:true,unit:'trụ'},
        {id:'17',name:'Niềng răng mắc cài kim loại',desc:'Chỉnh nha mắc cài kim loại truyền thống',time:'60 phút/lần',price:25000000,cat:'chinh-nha',perUnit:false},
        {id:'18',name:'Niềng răng mắc cài sứ',desc:'Chỉnh nha mắc cài sứ thẩm mỹ',time:'60 phút/lần',price:35000000,cat:'chinh-nha',perUnit:false},
        {id:'19',name:'Niềng răng Invisalign',desc:'Khay trong suốt chỉnh nha hiện đại',time:'45 phút/lần',price:80000000,cat:'chinh-nha',perUnit:false},
        {id:'20',name:'Điều trị viêm nha chu',desc:'Điều trị viêm nướu, viêm nha chu',time:'30 phút',price:500000,cat:'kham',perUnit:false},
    ];

    // Render bang dich vu
    function renderServiceTable(list, showAll) {
        var tbody = document.getElementById('serviceTableBody');
        var html = '';
        list.forEach(function(s, i) {
            var cls = (!showAll && i >= 10) ? ' class="hidden-row"' : '';
            html += '<tr' + cls + ' data-cat="' + s.cat + '"><td>' + (i+1) + '</td><td>' + s.name + '<br><small style="color:var(--text-muted)">' + s.desc + '</small></td><td class="time-cell">🕐 ' + s.time + '</td><td class="price">' + s.price.toLocaleString('vi-VN') + '</td></tr>';
        });
        tbody.innerHTML = html;
    }

    // Render dropdown dich vu (hien gia + thoi gian)
    function renderServiceDropdown() {
        var sel = document.getElementById('serviceAdd');
        sel.innerHTML = '<option value="">-- Chọn dịch vụ để thêm --</option>';
        allServices.forEach(function(s) {
            var opt = document.createElement('option');
            opt.value = s.id;
            opt.textContent = s.name + ' | ' + s.time + ' | ' + s.price.toLocaleString('vi-VN') + 'đ';
            opt.setAttribute('data-price', s.price);
            opt.setAttribute('data-time', s.time);
            opt.setAttribute('data-perunit', s.perUnit ? '1' : '0');
            opt.setAttribute('data-unit', s.unit || '');
            sel.appendChild(opt);
        });
    }

    renderServiceTable(allServices, false);
    renderServiceDropdown();

    // Preview: hien/an so luong
    function previewService() {
        var sel = document.getElementById('serviceAdd');
        var opt = sel.options[sel.selectedIndex];
        var qtyRow = document.getElementById('qtyRow');
        if (opt.value && opt.getAttribute('data-perunit') === '1') {
            qtyRow.style.display = 'flex';
            document.getElementById('qtyInput').value = 1;
        } else {
            qtyRow.style.display = 'none';
        }
    }

    // Filter + search dich vu
    var servicesExpanded = false;
    function filterServices() {
        var search = document.getElementById('serviceSearch').value.toLowerCase();
        var cat = document.getElementById('serviceFilter').value;
        var filtered = allServices.filter(function(s) {
            var matchCat = cat === 'all' || s.cat === cat;
            var matchSearch = !search || s.name.toLowerCase().indexOf(search) > -1 || s.desc.toLowerCase().indexOf(search) > -1;
            return matchCat && matchSearch;
        });
        renderServiceTable(filtered, true);
        document.getElementById('showMoreBtn').style.display = 'none';
    }

    function toggleServices() {
        servicesExpanded = !servicesExpanded;
        document.querySelectorAll('.hidden-row').forEach(function(r) { r.style.display = servicesExpanded ? 'table-row' : 'none'; });
        document.getElementById('showMoreBtn').textContent = servicesExpanded ? 'Thu gọn ↑' : 'Xem thêm dịch vụ ↓';
    }

    // === CHON NHIEU DICH VU (co so luong) ===
    var selectedList = [];

    function addService() {
        var sel = document.getElementById('serviceAdd');
        var opt = sel.options[sel.selectedIndex];
        if (!opt.value) return;
        var svc = allServices.find(function(s) { return s.id === opt.value; });
        if (!svc) return;

        var qty = 1;
        if (svc.perUnit) {
            qty = parseInt(document.getElementById('qtyInput').value) || 1;
            if (qty < 1) qty = 1;
        }

        // Kiem tra trung
        var existing = selectedList.find(function(s) { return s.id === svc.id; });
        if (existing) {
            if (svc.perUnit) { existing.qty += qty; }
            else { alert('Dịch vụ này đã được chọn!'); return; }
        } else {
            selectedList.push({ id: svc.id, name: svc.name, price: svc.price, time: svc.time, qty: qty, perUnit: svc.perUnit, unit: svc.unit || '' });
        }

        sel.selectedIndex = 0;
        document.getElementById('qtyRow').style.display = 'none';
        renderSelectedServices();
        document.getElementById('serviceGroup').classList.remove('error');
    }

    function removeService(id) { selectedList = selectedList.filter(function(s) { return s.id !== id; }); renderSelectedServices(); }

    function renderSelectedServices() {
        var c = document.getElementById('selectedServices');
        var d = document.getElementById('priceDisplay');
        if (selectedList.length === 0) { c.innerHTML = ''; d.style.display = 'none'; return; }
        var html = '<table style="width:100%;border-collapse:collapse;font-size:0.88rem;"><thead><tr style="background:#f0f4f8;"><th style="text-align:left;padding:10px 12px;font-size:0.78rem;color:#4a4a5a;font-weight:600;">Dịch vụ</th><th style="text-align:center;padding:10px 12px;font-size:0.78rem;color:#4a4a5a;font-weight:600;">SL</th><th style="text-align:left;padding:10px 12px;font-size:0.78rem;color:#4a4a5a;font-weight:600;">Thời gian</th><th style="text-align:right;padding:10px 12px;font-size:0.78rem;color:#4a4a5a;font-weight:600;">Thành tiền</th><th style="width:40px;"></th></tr></thead><tbody>';
        var total = 0;
        selectedList.forEach(function(s) {
            var subtotal = s.price * s.qty;
            total += subtotal;
            html += '<tr style="border-bottom:1px solid #dee2e6;">';
            html += '<td style="padding:10px 12px;font-weight:600;color:#1a1a2e;">' + s.name + (s.perUnit ? ' <small style="color:#8c8c9a;">(/' + s.unit + ')</small>' : '') + '</td>';
            html += '<td style="padding:10px 12px;text-align:center;font-weight:600;">' + s.qty + '</td>';
            html += '<td style="padding:10px 12px;color:#e67e22;font-weight:500;font-size:0.82rem;">⏱ ' + s.time + '</td>';
            html += '<td style="padding:10px 12px;text-align:right;font-weight:700;color:#0056b3;white-space:nowrap;">' + subtotal.toLocaleString('vi-VN') + ' đ</td>';
            html += '<td style="padding:10px 6px;text-align:center;"><button type="button" onclick="removeService(\'' + s.id + '\')" style="width:26px;height:26px;border-radius:50%;border:1.5px solid #f8d7da;background:white;color:#e74c3c;cursor:pointer;font-size:0.8rem;display:inline-flex;align-items:center;justify-content:center;" onmouseover="this.style.background=\'#e74c3c\';this.style.color=\'white\'" onmouseout="this.style.background=\'white\';this.style.color=\'#e74c3c\'">✕</button></td>';
            html += '</tr>';
        });
        html += '</tbody></table>';
        c.innerHTML = html;
        document.getElementById('priceValue').textContent = total.toLocaleString('vi-VN');
        d.style.display = 'block';
    }

    // TIME GRID
    (function() { var t = new Date().toISOString().split('T')[0]; document.getElementById('bookingDate').setAttribute('min', t); })();
    var weekdayTimes = ['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','13:00','13:30','14:00','14:30','15:00','15:30','16:00','16:30'];
    var sundayTimes = ['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30'];
    function buildTimeGrid(times) {
        var grid = document.getElementById('timeGrid'); grid.innerHTML = ''; document.getElementById('bookingTime').value = '';
        times.forEach(function(t) { var btn = document.createElement('div'); btn.className = 'time-slot'; btn.textContent = t;
            btn.onclick = function() { document.querySelectorAll('.time-slot').forEach(function(s) { s.classList.remove('selected'); }); this.classList.add('selected'); document.getElementById('bookingTime').value = t; document.getElementById('timeGroup').classList.remove('error'); };
            grid.appendChild(btn); });
    }
    buildTimeGrid(weekdayTimes);
    document.getElementById('bookingDate').addEventListener('change', function() {
        var d = new Date(this.value).getDay(); buildTimeGrid(d === 0 ? sundayTimes : weekdayTimes); document.getElementById('dateGroup').classList.remove('error');
    });

    // BOOKING SUBMIT
    function handleBooking(e) {
        e.preventDefault(); var ok = true;
        if (selectedList.length === 0) { document.getElementById('serviceGroup').classList.add('error'); ok = false; } else document.getElementById('serviceGroup').classList.remove('error');
        if (!document.getElementById('bookingDate').value) { document.getElementById('dateGroup').classList.add('error'); ok = false; } else document.getElementById('dateGroup').classList.remove('error');
        if (!document.getElementById('bookingTime').value) { document.getElementById('timeGroup').classList.add('error'); ok = false; } else document.getElementById('timeGroup').classList.remove('error');
        if (ok) { alert('✅ Đặt lịch thành công! Phòng khám sẽ liên hệ xác nhận.'); document.getElementById('bookingForm').reset(); document.getElementById('priceDisplay').style.display = 'none'; document.querySelectorAll('.time-slot').forEach(function(s) { s.classList.remove('selected'); }); selectedList = []; renderSelectedServices(); }
        return false;
    }
    </script>
</body>
</html>
