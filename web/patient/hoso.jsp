<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String activeTab = request.getParameter("tab");
    if (activeTab == null) activeTab = "info";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân - Nha Khoa Kvone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/hoso.css">
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    <main class="profile-page">
        <div class="container">
            <div class="profile-layout">
                <div class="profile-sidebar">
                    <div class="sidebar-header"><div class="sidebar-avatar">👤</div><h3>Nguyễn Văn An</h3><p>0901000001</p></div>
                    <div class="sidebar-menu">
                        <div class="sidebar-menu-item <%= "info".equals(activeTab)?"active":"" %>" onclick="switchTab('info',this)"><span class="menu-icon">📋</span> Thông tin cá nhân</div>
                        <div class="sidebar-menu-item <%= "history".equals(activeTab)?"active":"" %>" onclick="switchTab('history',this)"><span class="menu-icon">📅</span> Quản lý lịch hẹn</div>
                        <div class="sidebar-menu-item <%= "password".equals(activeTab)?"active":"" %>" onclick="switchTab('password',this)"><span class="menu-icon">🔒</span> Đổi mật khẩu</div>
                        <div class="sidebar-menu-item logout" onclick="if(confirm('Bạn có chắc muốn đăng xuất?'))window.location.href='${pageContext.request.contextPath}/index.jsp?logout=true'"><span class="menu-icon">🚪</span> Đăng xuất</div>
                    </div>
                </div>

                <div class="profile-content">
                    <!-- TAB INFO (chi xem, co nut chinh sua mo modal) -->
                    <div class="tab-content <%= "info".equals(activeTab)?"active":"" %>" id="tab-info">
                        <div class="profile-content-header"><h2>Thông tin cá nhân</h2><p>Thông tin tài khoản của bạn</p></div>
                        <div class="info-display">
                            <div class="info-row-display">
                                <div class="info-field"><span class="field-label">Họ và tên</span><span class="field-value" id="dispName">Nguyễn Văn An</span></div>
                                <div class="info-field"><span class="field-label">Số điện thoại</span><span class="field-value" id="dispPhone">0901000001</span></div>
                            </div>
                            <div class="info-row-display">
                                <div class="info-field"><span class="field-label">Email</span><span class="field-value" id="dispEmail">nguyenvanan@gmail.com</span></div>
                                <div class="info-field"><span class="field-label">Ngày sinh</span><span class="field-value" id="dispDob">15/05/1990</span></div>
                            </div>
                            <div class="info-row-display">
                                <div class="info-field"><span class="field-label">Giới tính</span><span class="field-value" id="dispGender">Nam</span></div>
                            </div>
                        </div>
                        <div style="text-align:right;margin-top:20px;">
                            <button class="btn btn-primary" onclick="openEditModal()">✏️ Chỉnh sửa</button>
                        </div>
                    </div>

                    <!-- TAB HISTORY -->
                    <div class="tab-content <%= "history".equals(activeTab)?"active":"" %>" id="tab-history">
                        <div class="profile-content-header"><h2>Quản lý lịch hẹn</h2><p>Xem và quản lý các lịch hẹn</p></div>
                        <div class="history-toolbar"><div class="history-filter">
                            <button class="filter-chip active" onclick="filterHistory('all',this)">Tất cả</button>
                            <button class="filter-chip" onclick="filterHistory('pending',this)">Chờ xác nhận</button>
                            <button class="filter-chip" onclick="filterHistory('confirmed',this)">Đã xác nhận</button>
                            <button class="filter-chip" onclick="filterHistory('paid',this)">Đã khám</button>
                        </div></div>
                        <div class="history-table-wrapper">
                            <table class="history-table" id="historyTable">
                                <thead><tr><th>STT</th><th>Ngày</th><th>Giờ</th><th>Dịch vụ</th><th>Trạng thái</th><th style="text-align:center">Thao tác</th></tr></thead>
                                <tbody>
                                    <tr data-status="paid" data-id="1"><td class="stt-cell">1</td><td>01/03/2024</td><td>08:00</td><td>Khám tổng quát, Cạo vôi</td><td><span class="status-badge paid">Đã khám</span></td><td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(1)">Xem</button></div></td></tr>
                                    <tr data-status="confirmed" data-id="2"><td class="stt-cell">2</td><td>15/04/2024</td><td>09:30</td><td>Tư vấn chỉnh nha</td><td><span class="status-badge confirmed">Đã xác nhận</span></td><td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(2)">Xem</button><button class="btn-action btn-cancel" onclick="confirmCancelConfirmed(2)">Hủy</button></div></td></tr>
                                    <tr data-status="pending" data-id="3"><td class="stt-cell">3</td><td>20/05/2024</td><td>14:00</td><td>Bọc sứ Zirconia x2, Tẩy trắng</td><td><span class="status-badge pending">Chờ xác nhận</span></td><td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(3)">Xem</button><button class="btn-action btn-edit" onclick="openEditAppointment(3)">Sửa</button><button class="btn-action btn-cancel" onclick="confirmCancel(3)">Hủy</button></div></td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- TAB PASSWORD -->
                    <div class="tab-content <%= "password".equals(activeTab)?"active":"" %>" id="tab-password">
                        <div class="profile-content-header"><h2>Đổi mật khẩu</h2><p>Cập nhật mật khẩu bảo mật</p></div>
                        <form class="password-form" id="passwordForm" onsubmit="return changePassword(event)">
                            <div class="form-group" id="oldPassGroup"><label>Mật khẩu cũ <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="oldPassword" placeholder="Nhập mật khẩu hiện tại"><div class="form-error">Vui lòng nhập</div></div>
                            <div class="form-group" id="newPassGroup"><label>Mật khẩu mới <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="newPassword" placeholder="VD: Abc@1234" oninput="checkStrength(this.value)"><div class="password-strength"><div class="password-strength-bar" id="strengthBar"></div></div><div class="password-hint">Tối thiểu 8 ký tự, gồm hoa/thường/số/đặc biệt</div><div class="form-error">Không đạt yêu cầu</div></div>
                            <div class="form-group" id="confirmPassGroup"><label>Nhập lại mật khẩu mới <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="confirmNewPassword" placeholder="Nhập lại"><div class="form-error">Không khớp</div></div>
                            <div class="form-actions"><button type="submit" class="btn btn-primary">🔐 Đổi mật khẩu</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- MODAL CHINH SUA THONG TIN -->
    <div class="modal-overlay" id="editInfoModal" onclick="if(event.target===this)closeEditModal()">
        <div class="modal-box" style="max-width:520px;">
            <div class="modal-header"><h3>✏️ Chỉnh sửa thông tin</h3><button class="modal-close" onclick="closeEditModal()">✕</button></div>
            <div class="modal-body" style="padding:28px;">
                <div class="form-group"><label>Họ và tên</label><input type="text" class="form-control" id="editName" value="Nguyễn Văn An" disabled style="background:#f0f4f8;color:#8c8c9a;"></div>
                <div class="form-row" style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                    <div class="form-group"><label>Số điện thoại</label><input type="tel" class="form-control" id="editPhone" value="0901000001"></div>
                    <div class="form-group"><label>Email</label><input type="email" class="form-control" id="editEmail" value="nguyenvanan@gmail.com"></div>
                </div>
                <div class="form-row" style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                    <div class="form-group"><label>Ngày sinh</label><input type="date" class="form-control" id="editDob" value="1990-05-15"></div>
                    <div class="form-group"><label>Giới tính</label><select class="form-control" id="editGender"><option value="Nam" selected>Nam</option><option value="Nữ">Nữ</option></select></div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeEditModal()">Hủy</button>
                <button class="btn btn-primary" onclick="saveEditInfo()">Xác nhận</button>
            </div>
        </div>
    </div>

    <!-- MODAL CHI TIET PHIEU KHAM -->
    <div class="modal-overlay" id="detailModal" onclick="if(event.target===this)closeModal()">
        <div class="modal-box receipt-modal"><div class="modal-body" id="modalBody"></div><div class="modal-footer"><button class="btn btn-outline" onclick="closeModal()">Đóng</button></div></div>
    </div>

    <!-- MODAL HUY PENDING -->
    <div class="modal-overlay" id="cancelModal" onclick="if(event.target===this)closeCancelModal()">
        <div class="modal-box confirm-box"><div class="modal-header" style="background:#e74c3c"><h3>⚠️ Xác nhận hủy</h3><button class="modal-close" onclick="closeCancelModal()">✕</button></div><div class="modal-body"><span class="confirm-icon">🗓️</span><div class="confirm-title">Hủy lịch hẹn này?</div><div class="confirm-message">Lịch hẹn sẽ bị xóa khỏi danh sách.</div></div><div class="modal-footer"><button class="btn btn-outline" onclick="closeCancelModal()">Không</button><button class="btn" onclick="doCancelAppointment()" style="background:#e74c3c;color:white">Có, hủy</button></div></div>
    </div>

    <!-- MODAL THONG BAO HUY DA XAC NHAN -->
    <div class="modal-overlay" id="cancelInfoModal" onclick="if(event.target===this)closeCancelInfoModal()">
        <div class="modal-box confirm-box"><div class="modal-header" style="background:var(--accent)"><h3>📞 Thông báo</h3><button class="modal-close" onclick="closeCancelInfoModal()">✕</button></div><div class="modal-body"><span class="confirm-icon">⏳</span><div class="confirm-title">Yêu cầu hủy đang chờ xử lý</div><div class="confirm-message">Lịch hẹn đã được xác nhận không thể hủy trực tiếp. Vui lòng liên hệ hotline <strong style="color:var(--primary)">1900 1533</strong> để được hỗ trợ hủy lịch.</div></div><div class="modal-footer"><button class="btn btn-accent" onclick="closeCancelInfoModal()">Đã hiểu</button></div></div>
    </div>
    
    
    
    <!-- MODAL CHINH SUA LICH HEN PENDING -->
    <div class="modal-overlay" id="editAppointmentModal" onclick="if(event.target===this)closeEditAppointment()">
        <div class="modal-box" style="max-width:600px;">
            <div class="modal-header"><h3>📝 Chỉnh sửa lịch hẹn</h3><button class="modal-close" onclick="closeEditAppointment()">✕</button></div>
            <div class="modal-body" style="padding:28px;">
                <div class="form-group">
                    <label>Dịch vụ đã chọn</label>
                    <div id="editSvcList"></div>
                    <select class="form-control" id="editSvcAdd" style="margin-top:10px;">
                        <option value="">-- Thêm dịch vụ --</option>
                    </select>
                    <button type="button" onclick="addEditSvc()" style="margin-top:8px;padding:8px 18px;font-size:0.82rem;font-weight:600;background:var(--primary);color:white;border:none;border-radius:6px;cursor:pointer;font-family:var(--font-body);">+ Thêm</button>
                </div>
                <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                    <div class="form-group"><label>Ngày khám</label><input type="date" class="form-control" id="editDate"></div>
                    <div class="form-group"><label>Giờ khám</label>
                        <select class="form-control" id="editTime">
                            <option value="08:00">08:00</option><option value="08:30">08:30</option><option value="09:00">09:00</option><option value="09:30">09:30</option>
                            <option value="10:00">10:00</option><option value="10:30">10:30</option><option value="11:00">11:00</option><option value="11:30">11:30</option>
                            <option value="13:00">13:00</option><option value="13:30">13:30</option><option value="14:00">14:00</option><option value="14:30">14:30</option>
                            <option value="15:00">15:00</option><option value="15:30">15:30</option><option value="16:00">16:00</option><option value="16:30">16:30</option>
                        </select>
                    </div>
                </div>
                <div class="form-group"><label>Ghi chú</label><textarea class="form-control" id="editNote" rows="2"></textarea></div>
                <div id="editTotal" style="padding:12px 16px;background:#fff4e6;border-radius:6px;font-size:0.9rem;color:#d35400;margin-top:4px;display:none;">
                    Tổng dự kiến: <strong id="editTotalValue" style="color:#e67e22;font-size:1.1rem;"></strong> VNĐ
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeEditAppointment()">Hủy</button>
                <button class="btn btn-primary" onclick="saveEditAppointment()">Lưu thay đổi</button>
            </div>
        </div>
    </div>

    <!-- MODAL XAC NHAN HUY (DA XAC NHAN) -->
    <div class="modal-overlay" id="cancelConfirmedModal" onclick="if(event.target===this)closeCancelConfirmedModal()">
        <div class="modal-box confirm-box">
            <div class="modal-header" style="background:#e74c3c"><h3>⚠️ Xác nhận hủy lịch</h3><button class="modal-close" onclick="closeCancelConfirmedModal()">✕</button></div>
            <div class="modal-body"><span class="confirm-icon">🗓️</span><div class="confirm-title">Bạn chắc chắn muốn hủy?</div><div class="confirm-message">Lịch hẹn đã xác nhận cần thông qua phòng khám để hủy.</div></div>
            <div class="modal-footer"><button class="btn btn-outline" onclick="closeCancelConfirmedModal()">Không</button><button class="btn" onclick="doCancelConfirmed()" style="background:#e74c3c;color:white">Xác nhận hủy</button></div>
        </div>
    </div>
    <jsp:include page="../components/footer.jsp" />

    <script>
    var appointmentDetails = {
        1: { status:'paid',statusText:'Đã khám',date:'01/03/2024',time:'08:00',customerNote:'Muốn cạo vôi định kỳ.',
            doctorName:'BS. Nguyễn Hải',doctorSpec:'Tổng quát',doctorDegree:'CKI',doctorAvatar:'https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=100&h=100&fit=crop',room:'Phòng 101',
            diagnosis:'Vôi răng nhiều, cần cạo vôi tổng quát.',doctorNote:'Tái khám sau 6 tháng.',
            services:[{name:'Khám tổng quát',price:100000,time:'20 phút',qty:1},{name:'Cạo vôi răng',price:200000,time:'30 phút',qty:1}],total:300000},
        2: { status:'confirmed',statusText:'Đã xác nhận',date:'15/04/2024',time:'09:30',customerNote:'Răng cửa mọc lệch.',
            doctorName:'BS. Trần Tâm',doctorSpec:'Chỉnh nha',doctorDegree:'Thạc sĩ',doctorAvatar:'https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=100&h=100&fit=crop',room:'Phòng 203',
            diagnosis:null,doctorNote:null,
            services:[{name:'Tư vấn chỉnh nha',price:500000,time:'45 phút',qty:1},{name:'Chụp X-quang',price:300000,time:'15 phút',qty:1}],total:800000},
        3: { status:'pending',statusText:'Chờ xác nhận',date:'20/05/2024',time:'14:00',customerNote:'Muốn bọc sứ 2 răng cửa và tẩy trắng.',
            doctorName:null,doctorSpec:null,doctorDegree:null,doctorAvatar:null,room:null,diagnosis:null,doctorNote:null,
            services:[{name:'Bọc răng sứ Zirconia',price:6000000,time:'45 phút/răng',qty:2},{name:'Tẩy trắng răng Laser',price:2500000,time:'45 phút',qty:1}],total:14500000}
    };
    var currentCancelId=null;

    function switchTab(t,el){document.querySelectorAll('.tab-content').forEach(function(c){c.classList.remove('active');});document.querySelectorAll('.sidebar-menu-item').forEach(function(i){i.classList.remove('active');});document.getElementById('tab-'+t).classList.add('active');el.classList.add('active');}
    function filterHistory(s,btn){document.querySelectorAll('.filter-chip').forEach(function(c){c.classList.remove('active');});btn.classList.add('active');var stt=0;document.querySelectorAll('#historyTable tbody tr').forEach(function(r){var ok=s==='all'||r.getAttribute('data-status')===s;if(ok){stt++;r.style.display='';r.querySelector('.stt-cell').textContent=stt;}else r.style.display='none';});}

    // Edit info modal
    function openEditModal(){document.getElementById('editInfoModal').classList.add('show');}
    function closeEditModal(){document.getElementById('editInfoModal').classList.remove('show');}
    function saveEditInfo(){
        var phone=document.getElementById('editPhone').value.trim();
        var email=document.getElementById('editEmail').value.trim();
        if(!phone&&!email){alert('Cần ít nhất SĐT hoặc Email');return;}
        document.getElementById('dispPhone').textContent=phone||'Chưa cập nhật';
        document.getElementById('dispEmail').textContent=email||'Chưa cập nhật';
        var dob=document.getElementById('editDob').value;if(dob){var p=dob.split('-');document.getElementById('dispDob').textContent=p[2]+'/'+p[1]+'/'+p[0];}
        document.getElementById('dispGender').textContent=document.getElementById('editGender').value;
        alert('✅ Cập nhật thành công!');closeEditModal();
    }

    // Detail modal
    function showDetail(id){
        var d=appointmentDetails[id];if(!d)return;
        var isPaid=d.status==='paid';
        var html='<div class="receipt-hero"><span class="receipt-status-pill '+d.status+'">'+d.statusText+'</span><h3>Chi tiết phiếu khám</h3><div class="receipt-subtitle">Nha Khoa Kvone - 48 Cao Thắng, Đà Nẵng</div></div><div class="receipt-content">';
        html+='<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">📅</span> Lịch hẹn</div><div class="info-row">';
        html+='<div class="info-cell"><div class="cell-icon">🗓️</div><div class="cell-text"><small>Ngày</small><strong>'+d.date+'</strong></div></div>';
        html+='<div class="info-cell"><div class="cell-icon">🕐</div><div class="cell-text"><small>Giờ</small><strong>'+d.time+'</strong></div></div>';
        html+='<div class="info-cell"><div class="cell-icon">🏥</div><div class="cell-text"><small>Phòng</small><strong>'+(d.room||'<i style="color:#8c8c9a">Chờ phân phòng</i>')+'</strong></div></div>';
        html+='</div></div>';
        if(d.doctorName){html+='<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">👨‍⚕️</span> Bác sĩ</div><div class="doctor-card-mini"><div class="doctor-avatar-mini"><img src="'+d.doctorAvatar+'"></div><div class="doctor-details"><h4>'+d.doctorName+'</h4><p>'+d.doctorDegree+'</p><span class="specialty-tag">'+d.doctorSpec+'</span></div></div></div>';}
        else{html+='<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">👨‍⚕️</span> Bác sĩ</div><div class="medical-empty">⏳ Đang chờ phân bác sĩ...</div></div>';}
        if(d.customerNote){html+='<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">📝</span> Ghi chú của bạn</div><div class="medical-item"><div class="medical-text">'+d.customerNote+'</div></div></div>';}
        if(isPaid&&d.diagnosis){html+='<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">🩺</span> Kết quả khám</div><div class="medical-item"><div class="medical-label">Chẩn đoán</div><div class="medical-text diagnosis">'+d.diagnosis+'</div></div>';if(d.doctorNote)html+='<div class="medical-item"><div class="medical-label">Ghi chú bác sĩ</div><div class="medical-text">'+d.doctorNote+'</div></div>';html+='</div>';}
        html+='<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">💎</span> Dịch vụ</div>';
        if(d.services.length>0){html+='<div class="services-list">';d.services.forEach(function(s){var sub=s.price*s.qty;html+='<div class="service-row"><div class="service-info"><div class="service-name">'+s.name+(s.qty>1?' <small>x'+s.qty+'</small>':'')+'</div><div class="service-position">⏱ '+s.time+'</div></div><div class="service-price">'+sub.toLocaleString('vi-VN')+' đ</div></div>';});html+='</div>';}
        else html+='<div class="medical-empty">Chưa có dịch vụ</div>';
        html+='</div>';
        if(d.total>0)html+='<div class="receipt-total-box"><div class="total-label">'+(isPaid?'Tổng chi phí':'Chi phí dự kiến')+'</div><div class="total-value">'+d.total.toLocaleString('vi-VN')+' đ</div></div>';
        html+='</div>';
        document.getElementById('modalBody').innerHTML=html;document.getElementById('detailModal').classList.add('show');
    }
    function closeModal(){document.getElementById('detailModal').classList.remove('show');}

    // Cancel confirmed → show info modal
    function cancelConfirmed(){document.getElementById('cancelInfoModal').classList.add('show');}
    function closeCancelInfoModal(){document.getElementById('cancelInfoModal').classList.remove('show');}

    // Cancel pending → remove
    function confirmCancel(id){currentCancelId=id;document.getElementById('cancelModal').classList.add('show');}
    function closeCancelModal(){document.getElementById('cancelModal').classList.remove('show');}
    function doCancelAppointment(){alert('✅ Đã hủy!');var r=document.querySelector('tr[data-id="'+currentCancelId+'"]');if(r)r.remove();var stt=1;document.querySelectorAll('#historyTable tbody tr').forEach(function(r){if(r.style.display!=='none')r.querySelector('.stt-cell').textContent=stt++;});closeCancelModal();}

    // Password
    function checkStrength(p){var bar=document.getElementById('strengthBar');var s=0;if(/[a-z]/.test(p))s++;if(/[A-Z]/.test(p))s++;if(/[0-9]/.test(p))s++;if(/[!@#$%^&*]/.test(p))s++;if(p.length>=8)s++;bar.className='password-strength-bar';if(s<=2)bar.classList.add('weak');else if(s<=4)bar.classList.add('medium');else bar.classList.add('strong');}
    function changePassword(e){e.preventDefault();var ok=true;if(!document.getElementById('oldPassword').value){document.getElementById('oldPassGroup').classList.add('error');ok=false;}else document.getElementById('oldPassGroup').classList.remove('error');var np=document.getElementById('newPassword').value;if(!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(np)){document.getElementById('newPassGroup').classList.add('error');ok=false;}else document.getElementById('newPassGroup').classList.remove('error');if(document.getElementById('confirmNewPassword').value!==np){document.getElementById('confirmPassGroup').classList.add('error');ok=false;}else document.getElementById('confirmPassGroup').classList.remove('error');if(ok){alert('✅ Đổi mật khẩu thành công!');document.getElementById('passwordForm').reset();document.getElementById('strengthBar').className='password-strength-bar';}return false;}
    document.getElementById('oldPassword').addEventListener('input',function(){document.getElementById('oldPassGroup').classList.remove('error');});
    document.getElementById('newPassword').addEventListener('input',function(){document.getElementById('newPassGroup').classList.remove('error');});
    document.getElementById('confirmNewPassword').addEventListener('input',function(){document.getElementById('confirmPassGroup').classList.remove('error');});
    // === EDIT APPOINTMENT (pending) ===
    var editSvcList = [];
    var editingId = null;
    var svcData = [
        {id:'1',name:'Khám tổng quát',price:100000,time:'20 phút'},
        {id:'2',name:'Cạo vôi răng',price:200000,time:'30 phút'},
        {id:'3',name:'Trám răng Composite',price:300000,time:'30 phút'},
        {id:'7',name:'Tẩy trắng răng Laser',price:2500000,time:'45 phút'},
        {id:'11',name:'Bọc răng sứ Titan',price:2000000,time:'45 phút/răng'},
        {id:'12',name:'Bọc răng sứ Cercon',price:5000000,time:'45 phút/răng'},
        {id:'13',name:'Bọc răng sứ Zirconia',price:6000000,time:'45 phút/răng'},
        {id:'14',name:'Mặt dán sứ Veneer',price:7000000,time:'60 phút/răng'},
        {id:'15',name:'Cấy ghép Implant',price:15000000,time:'60-90 phút'},
        {id:'9',name:'Lấy tủy răng cửa',price:800000,time:'45 phút'},
        {id:'10',name:'Lấy tủy răng hàm',price:1500000,time:'60 phút'}
    ];

    function openEditAppointment(id) {
        editingId = id;
        var d = appointmentDetails[id];
        if (!d) return;
        editSvcList = d.services.map(function(s) { return {name:s.name, price:s.price, time:s.time, qty:s.qty}; });
        // Fill dropdown
        var sel = document.getElementById('editSvcAdd');
        sel.innerHTML = '<option value="">-- Thêm dịch vụ --</option>';
        svcData.forEach(function(s) {
            sel.innerHTML += '<option value="' + s.id + '" data-price="' + s.price + '" data-time="' + s.time + '">' + s.name + ' | ' + s.price.toLocaleString('vi-VN') + 'đ</option>';
        });
        // Fill date/time/note
        var parts = d.date.split('/');
        document.getElementById('editDate').value = parts[2] + '-' + parts[1] + '-' + parts[0];
        document.getElementById('editTime').value = d.time;
        document.getElementById('editNote').value = d.customerNote || '';
        renderEditSvcList();
        document.getElementById('editAppointmentModal').classList.add('show');
    }

    function closeEditAppointment() { document.getElementById('editAppointmentModal').classList.remove('show'); }

    function addEditSvc() {
        var sel = document.getElementById('editSvcAdd');
        var opt = sel.options[sel.selectedIndex];
        if (!opt.value) return;
        if (editSvcList.find(function(s) { return s.name === opt.textContent.split(' | ')[0]; })) {
            alert('Dịch vụ đã có trong danh sách!'); return;
        }
        editSvcList.push({name: opt.textContent.split(' | ')[0], price: parseInt(opt.getAttribute('data-price')), time: opt.getAttribute('data-time'), qty: 1});
        sel.selectedIndex = 0;
        renderEditSvcList();
    }

    function removeEditSvc(i) { editSvcList.splice(i, 1); renderEditSvcList(); }

    function renderEditSvcList() {
        var c = document.getElementById('editSvcList');
        if (editSvcList.length === 0) { c.innerHTML = '<p style="color:#8c8c9a;font-size:0.85rem;">Chưa có dịch vụ nào</p>'; document.getElementById('editTotal').style.display='none'; return; }
        var html = '', total = 0;
        editSvcList.forEach(function(s, i) {
            var sub = s.price * s.qty;
            total += sub;
            html += '<div style="display:flex;align-items:center;justify-content:space-between;padding:8px 12px;background:#f0f4f8;border-radius:6px;margin-bottom:6px;font-size:0.85rem;">';
            html += '<div style="flex:1;"><strong>' + s.name + '</strong>' + (s.qty > 1 ? ' <small>x' + s.qty + '</small>' : '') + '<br><small style="color:#e67e22;">⏱ ' + s.time + '</small></div>';
            html += '<div style="font-weight:700;color:#0056b3;margin:0 12px;white-space:nowrap;">' + sub.toLocaleString('vi-VN') + ' đ</div>';
            html += '<button type="button" onclick="removeEditSvc(' + i + ')" style="width:24px;height:24px;border-radius:50%;border:1px solid #f8d7da;background:white;color:#e74c3c;cursor:pointer;font-size:0.75rem;">✕</button>';
            html += '</div>';
        });
        c.innerHTML = html;
        document.getElementById('editTotalValue').textContent = total.toLocaleString('vi-VN');
        document.getElementById('editTotal').style.display = 'block';
    }

    function saveEditAppointment() {
        if (editSvcList.length === 0) { alert('Vui lòng chọn ít nhất 1 dịch vụ'); return; }
        if (!document.getElementById('editDate').value) { alert('Vui lòng chọn ngày'); return; }
        alert('✅ Cập nhật lịch hẹn thành công!');
        closeEditAppointment();
    }

    // === HUY LICH DA XAC NHAN (confirm truoc) ===
    function confirmCancelConfirmed(id) { document.getElementById('cancelConfirmedModal').classList.add('show'); }
    function closeCancelConfirmedModal() { document.getElementById('cancelConfirmedModal').classList.remove('show'); }
    function doCancelConfirmed() {
        closeCancelConfirmedModal();
        // Hien thong bao cho xu ly
        document.getElementById('cancelInfoModal').classList.add('show');
    }
    </script>
</body>
</html>
