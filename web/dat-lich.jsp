<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                        <ul>
                            <li>🏥 48 Cao Thắng, Hải Châu, Đà Nẵng</li>
                            <li>📞 1900 1533</li>
                            <li>🕐 T2 - T7: 8:00 - 17:00</li>
                            <li>🕐 Chủ nhật: 8:00 - 12:00</li>
                        </ul>
                        <div class="booking-note"><strong>Quy trình:</strong><br>1. Bạn chọn dịch vụ & ngày giờ<br>2. Phòng khám xác nhận & phân bác sĩ<br>3. Bạn đến khám theo lịch hẹn</div>
                    </div>
                    <form class="booking-form" id="bookingForm" onsubmit="return handleBooking(event)">
                        <!-- Chon dich vu (nhieu) -->
                        <div class="form-group" id="serviceGroup">
                            <label>Chọn dịch vụ <span style="color:#e74c3c">*</span></label>
                            <select class="form-control" id="serviceAdd">
                                <option value="">-- Chọn dịch vụ để thêm --</option>
                                <c:if test="${not empty listDV}"> 
                                    <c:forEach items="${listDV}" var="dv" varStatus="loop">
                                        <option value="${loop.count}" data-price="${dv.giaTien}" data-time="${dv.thoiLuongDuKien} phút">${dv.tenDichVu}</option>
                                    </c:forEach>
                                </c:if>
                            </select>
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
                            <textarea class="form-control" id="bookingNote" placeholder="Mô tả triệu chứng hoặc yêu cầu của bạn..." rows="3"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-lg" style="width:100%;">Đặt lịch hẹn</button>
                    </form>
                </div>
            </div>
        </section>

        <div class="section-divider"><span class="divider-icon">💎</span></div>

        <!-- DICH VU -->
        <section class="services-price-section" id="dichvu">
            <div class="container">
                <div class="section-title"><h2>Dịch vụ</h2></div>
                <p class="section-intro">Giá niêm yết công khai, minh bạch. Không phát sinh chi phí ẩn.</p>
                <div class="price-table-wrapper">
                    <table class="price-table" id="priceTable">
                        <thead><tr><th>STT</th><th>Dịch vụ</th><th>⏱ Thời gian</th><th>Giá (VNĐ)</th></tr></thead>
                        <tbody>
                        <c:if test="${not empty listDV}">
                            <c:forEach items="${listDV}" var="dv" varStatus="loop">
                                <tr class="${loop.index >= 10 ? 'hidden-row' :''}"><td>${loop.count}</td><td>${dv.chuyenKhoa.tenChuyenKhoa}<br><small style="color:var(--text-muted)">${dv.tenDichVu}</small></td><td class="time-cell">🕐 ${dv.thoiLuongDuKien} phút</td><td class="price">${dv.giaTien}</td></tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="text-center mt-3">
                    <button class="btn btn-outline" id="showMoreBtn" onclick="toggleServices()">Xem thêm dịch vụ ↓</button>
                </div>
            </div>
        </section>

        <div class="section-divider on-gray"><span class="divider-icon">✨</span></div>

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
    (function(){var t=new Date().toISOString().split('T')[0];document.getElementById('bookingDate').setAttribute('min',t);})();

    var weekdayTimes=['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','13:00','13:30','14:00','14:30','15:00','15:30','16:00','16:30'];
    var sundayTimes=['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30'];

    function buildTimeGrid(times){
        var grid=document.getElementById('timeGrid');grid.innerHTML='';document.getElementById('bookingTime').value='';
        times.forEach(function(t){var btn=document.createElement('div');btn.className='time-slot';btn.textContent=t;
        btn.onclick=function(){document.querySelectorAll('.time-slot').forEach(function(s){s.classList.remove('selected');});this.classList.add('selected');document.getElementById('bookingTime').value=t;document.getElementById('timeGroup').classList.remove('error');};grid.appendChild(btn);});
    }
    buildTimeGrid(weekdayTimes);

    document.getElementById('bookingDate').addEventListener('change',function(){
        var d=new Date(this.value).getDay();buildTimeGrid(d===0?sundayTimes:weekdayTimes);document.getElementById('dateGroup').classList.remove('error');
    });

    // === CHON NHIEU DICH VU ===
    var selectedList=[];

    function addService(){
        var sel=document.getElementById('serviceAdd');var opt=sel.options[sel.selectedIndex];if(!opt.value)return;
        if(selectedList.find(function(s){return s.id===opt.value;})){alert('Dịch vụ này đã được chọn!');return;}
        selectedList.push({id:opt.value,name:opt.text,price:parseInt(opt.getAttribute('data-price')),time:opt.getAttribute('data-time')});
        sel.selectedIndex=0;renderServices();document.getElementById('serviceGroup').classList.remove('error');
    }

    function removeService(id){selectedList=selectedList.filter(function(s){return s.id!==id;});renderServices();}

    function renderServices(){
        var c=document.getElementById('selectedServices');
        var d=document.getElementById('priceDisplay');
        if(selectedList.length===0){c.innerHTML='';d.style.display='none';return;}

        var html='<table style="width:100%;border-collapse:collapse;font-size:0.88rem;">';
        html+='<thead><tr style="background:#f0f4f8;"><th style="text-align:left;padding:10px 12px;font-size:0.78rem;color:#4a4a5a;font-weight:600;">Dịch vụ</th><th style="text-align:left;padding:10px 12px;font-size:0.78rem;color:#4a4a5a;font-weight:600;">Thời gian</th><th style="text-align:right;padding:10px 12px;font-size:0.78rem;color:#4a4a5a;font-weight:600;">Giá</th><th style="width:40px;"></th></tr></thead>';
        html+='<tbody>';
        var total=0;
        selectedList.forEach(function(s){
            total+=s.price;
            html+='<tr style="border-bottom:1px solid #dee2e6;">';
            html+='<td style="padding:10px 12px;font-weight:600;color:#1a1a2e;">'+s.name+'</td>';
            html+='<td style="padding:10px 12px;color:#e67e22;font-weight:500;font-size:0.82rem;">⏱ '+s.time+'</td>';
            html+='<td style="padding:10px 12px;text-align:right;font-weight:700;color:#0056b3;white-space:nowrap;">'+s.price.toLocaleString('vi-VN')+' đ</td>';
            html+='<td style="padding:10px 6px;text-align:center;"><button type="button" onclick="removeService(\''+s.id+'\')" style="width:26px;height:26px;border-radius:50%;border:1.5px solid #f8d7da;background:white;color:#e74c3c;cursor:pointer;font-size:0.8rem;display:flex;align-items:center;justify-content:center;transition:all 0.2s;" onmouseover="this.style.background=\'#e74c3c\';this.style.color=\'white\';this.style.borderColor=\'#e74c3c\'" onmouseout="this.style.background=\'white\';this.style.color=\'#e74c3c\';this.style.borderColor=\'#f8d7da\'">✕</button></td>';
            html+='</tr>';
        });
        html+='</tbody></table>';

        c.innerHTML=html;
        document.getElementById('priceValue').textContent=total.toLocaleString('vi-VN');
        d.style.display='block';
    }

    var servicesExpanded=false;
    function toggleServices(){servicesExpanded=!servicesExpanded;document.querySelectorAll('.hidden-row').forEach(function(r){r.style.display=servicesExpanded?'table-row':'none';});document.getElementById('showMoreBtn').textContent=servicesExpanded?'Thu gọn ↑':'Xem thêm dịch vụ ↓';}

    function handleBooking(e){
        e.preventDefault();var ok=true;
        if(selectedList.length===0){document.getElementById('serviceGroup').classList.add('error');ok=false;}else{document.getElementById('serviceGroup').classList.remove('error');}
        if(!document.getElementById('bookingDate').value){document.getElementById('dateGroup').classList.add('error');ok=false;}else{document.getElementById('dateGroup').classList.remove('error');}
        if(!document.getElementById('bookingTime').value){document.getElementById('timeGroup').classList.add('error');ok=false;}else{document.getElementById('timeGroup').classList.remove('error');}
        if(ok){alert('Đặt lịch thành công! Phòng khám sẽ liên hệ xác nhận.');document.getElementById('bookingForm').reset();document.getElementById('priceDisplay').style.display='none';document.querySelectorAll('.time-slot').forEach(function(s){s.classList.remove('selected');});selectedList=[];renderServices();}
        return false;
    }
    </script>
</body>
</html>
