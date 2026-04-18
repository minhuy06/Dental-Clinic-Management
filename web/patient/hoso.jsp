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
                <!-- SIDEBAR -->
                <div class="profile-sidebar">
                    <div class="sidebar-header">
                        <div class="sidebar-avatar">👤</div>
                        <h3>Nguyễn Văn An</h3>
                        <p>0901000001</p>
                    </div>
                    <div class="sidebar-menu">
                        <div class="sidebar-menu-item <%= "info".equals(activeTab) ? "active" : "" %>" onclick="switchTab('info', this)"><span class="menu-icon">📋</span> Thông tin cá nhân</div>
                        <div class="sidebar-menu-item <%= "history".equals(activeTab) ? "active" : "" %>" onclick="switchTab('history', this)"><span class="menu-icon">📅</span> Quản lý lịch hẹn</div>
                        <div class="sidebar-menu-item <%= "password".equals(activeTab) ? "active" : "" %>" onclick="switchTab('password', this)"><span class="menu-icon">🔒</span> Đổi mật khẩu</div>
                        <div class="sidebar-menu-item logout" onclick="confirmLogout()"><span class="menu-icon">🚪</span> Đăng xuất</div>
                    </div>
                </div>
                <!-- CONTENT -->
                <div class="profile-content">
                    <!-- TAB INFO -->
                    <div class="tab-content <%= "info".equals(activeTab) ? "active" : "" %>" id="tab-info">
                        <div class="profile-content-header"><h2>Thông tin cá nhân</h2><p>Xem và cập nhật thông tin của bạn</p></div>
                        <form class="info-form" id="infoForm" onsubmit="return saveInfo(event)">
                            <div class="form-row">
                                <div class="form-group"><label>Họ và tên <span style="color:#e74c3c">*</span></label><input type="text" class="form-control" id="infoName" value="Nguyễn Văn An"></div>
                                <div class="form-group"><label>Số điện thoại</label><input type="text" class="form-control" value="0901000001" disabled></div>
                            </div>
                            <div class="form-row">
                                <div class="form-group"><label>Email</label><input type="email" class="form-control" id="infoEmail" value="nguyenvanan@gmail.com"></div>
                                <div class="form-group"><label>Ngày sinh</label><input type="date" class="form-control" id="infoDob" value="1990-05-15"></div>
                            </div>
                            <div class="form-row">
                                <div class="form-group"><label>Giới tính</label><select class="form-control" id="infoGender"><option value="1" selected>Nam</option><option value="0">Nữ</option></select></div>
                                <div class="form-group"><label>Nhóm máu</label><select class="form-control" id="infoBlood"><option value="A" selected>A</option><option value="B">B</option><option value="AB">AB</option><option value="O">O</option></select></div>
                            </div>
                            <div class="form-actions"><button type="submit" class="btn btn-primary">💾 Lưu thay đổi</button></div>
                        </form>
                    </div>
                    <!-- TAB HISTORY -->
                    <div class="tab-content <%= "history".equals(activeTab) ? "active" : "" %>" id="tab-history">
                        <div class="profile-content-header"><h2>Quản lý lịch hẹn</h2><p>Xem và quản lý các lịch hẹn của bạn</p></div>
                        <div class="history-toolbar"><div class="history-filter">
                            <button class="filter-chip active" onclick="filterHistory('all',this)">Tất cả</button>
                            <button class="filter-chip" onclick="filterHistory('pending',this)">Chờ xác nhận</button>
                            <button class="filter-chip" onclick="filterHistory('confirmed',this)">Đã xác nhận</button>
                            <button class="filter-chip" onclick="filterHistory('paid',this)">Đã khám</button>
                        </div></div>
                        <div class="history-table-wrapper">
                            <table class="history-table" id="historyTable">
                                <thead><tr><th>STT</th><th>Ngày khám</th><th>Giờ</th><th>Dịch vụ</th><th>Trạng thái</th><th style="text-align:center">Thao tác</th></tr></thead>
                                <tbody>
                                    <tr data-status="paid" data-id="1">
                                        <td class="stt-cell">1</td><td>01/03/2024</td><td>08:00</td><td>Khám tổng quát, Cạo vôi răng</td>
                                        <td><span class="status-badge paid">Đã khám</span></td>
                                        <td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(1)">Xem chi tiết</button><button class="btn-action btn-cancel" disabled>Hủy lịch</button></div></td>
                                    </tr>
                                    <tr data-status="confirmed" data-id="2">
                                        <td class="stt-cell">2</td><td>15/04/2024</td><td>09:30</td><td>Tư vấn chỉnh nha, Chụp X-quang</td>
                                        <td><span class="status-badge confirmed">Đã xác nhận</span></td>
                                        <td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(2)">Xem chi tiết</button><button class="btn-action btn-cancel" onclick="confirmCancel(2)">Hủy lịch</button></div></td>
                                    </tr>
                                    <tr data-status="pending" data-id="3">
                                        <td class="stt-cell">3</td><td>20/05/2024</td><td>14:00</td><td>Bọc răng sứ Zirconia, Tẩy trắng Laser</td>
                                        <td><span class="status-badge pending">Chờ xác nhận</span></td>
                                        <td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(3)">Xem chi tiết</button><button class="btn-action btn-cancel" onclick="confirmCancel(3)">Hủy lịch</button></div></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- TAB PASSWORD -->
                    <div class="tab-content <%= "password".equals(activeTab) ? "active" : "" %>" id="tab-password">
                        <div class="profile-content-header"><h2>Đổi mật khẩu</h2><p>Cập nhật mật khẩu để bảo mật tài khoản</p></div>
                        <form class="password-form" id="passwordForm" onsubmit="return changePassword(event)">
                            <div class="form-group" id="oldPassGroup"><label>Mật khẩu cũ <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="oldPassword" placeholder="Nhập mật khẩu hiện tại"><div class="form-error">Vui lòng nhập mật khẩu cũ</div></div>
                            <div class="form-group" id="newPassGroup"><label>Mật khẩu mới <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="newPassword" placeholder="VD: Abc@1234" oninput="checkStrength(this.value)"><div class="password-strength"><div class="password-strength-bar" id="strengthBar"></div></div><div class="password-hint">Tối thiểu 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt</div><div class="form-error">Mật khẩu không đạt yêu cầu</div></div>
                            <div class="form-group" id="confirmPassGroup"><label>Nhập lại mật khẩu mới <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="confirmNewPassword" placeholder="Nhập lại mật khẩu mới"><div class="form-error">Mật khẩu nhập lại không khớp</div></div>
                            <div class="form-actions"><button type="submit" class="btn btn-primary">🔐 Đổi mật khẩu</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- MODAL CHI TIET -->
    <div class="modal-overlay" id="detailModal" onclick="if(event.target.id==='detailModal')closeModal()">
        <div class="modal-box receipt-modal"><div class="modal-body" id="modalBody"></div><div class="modal-footer"><button class="btn btn-outline" onclick="closeModal()">Đóng</button></div></div>
    </div>
    <!-- MODAL HUY -->
    <div class="modal-overlay" id="cancelModal" onclick="if(event.target.id==='cancelModal')closeCancelModal()">
        <div class="modal-box confirm-box"><div class="modal-header" style="background:#e74c3c"><h3>⚠️ Xác nhận hủy lịch</h3><button class="modal-close" onclick="closeCancelModal()">✕</button></div><div class="modal-body"><span class="confirm-icon">🗓️</span><div class="confirm-title">Bạn chắc chắn muốn hủy lịch hẹn này?</div><div class="confirm-message">Lịch hẹn đã hủy sẽ bị xóa khỏi danh sách.</div></div><div class="modal-footer"><button class="btn btn-outline" onclick="closeCancelModal()">Không</button><button class="btn" onclick="doCancelAppointment()" style="background:#e74c3c;color:white">Có, hủy lịch</button></div></div>
    </div>
    <!-- MODAL LOGOUT -->
    <div class="modal-overlay" id="logoutModal" onclick="if(event.target.id==='logoutModal')closeLogoutModal()">
        <div class="modal-box confirm-box"><div class="modal-header" style="background:linear-gradient(135deg,#e67e22,#d35400)"><h3>⚠️ Xác nhận đăng xuất</h3><button class="modal-close" onclick="closeLogoutModal()">✕</button></div><div class="modal-body"><span class="confirm-icon">🚪</span><div class="confirm-title">Bạn có chắc muốn đăng xuất?</div><div class="confirm-message">Bạn sẽ cần đăng nhập lại để tiếp tục sử dụng.</div></div><div class="modal-footer"><button class="btn btn-outline" onclick="closeLogoutModal()">Hủy</button><button class="btn btn-accent" onclick="doLogout()">Đăng xuất</button></div></div>
    </div>

    <jsp:include page="../components/footer.jsp" />

    <script>
    // === DU LIEU MAU ===
    var appointmentDetails = {
        1: {
            status:'paid', statusText:'Đã khám',
            date:'01/03/2024', time:'08:00',
            customerNote:'Muốn cạo vôi định kỳ, cảm thấy ê buốt khi uống lạnh.',
            // Phan duoc gan sau khi xac nhan
            doctorName:'BS. Nguyễn Hải', doctorSpec:'Tổng quát', doctorDegree:'CKI',
            doctorAvatar:'https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=100&h=100&fit=crop',
            room:'Phòng 101',
            // Phan chi co sau khi da kham
            diagnosis:'Vôi răng nhiều tích tụ ở mặt trong răng hàm dưới. Cần cạo vôi tổng quát.',
            doctorNote:'Khuyến cáo súc miệng nước muối 2 lần/ngày và tái khám sau 6 tháng.',
            services:[
                {name:'Khám tổng quát', price:100000, time:'20 phút'},
                {name:'Cạo vôi răng', price:200000, time:'30 phút'}
            ],
            total:300000
        },
        2: {
            status:'confirmed', statusText:'Đã xác nhận',
            date:'15/04/2024', time:'09:30',
            customerNote:'Răng cửa mọc lệch, muốn tư vấn niềng răng.',
            doctorName:'BS. Trần Tâm', doctorSpec:'Chỉnh nha', doctorDegree:'Thạc sĩ',
            doctorAvatar:'https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=100&h=100&fit=crop',
            room:'Phòng 203',
            diagnosis:null, doctorNote:null,
            services:[
                {name:'Tư vấn chỉnh nha', price:500000, time:'45 phút'},
                {name:'Chụp X-quang toàn cảnh', price:300000, time:'15 phút'}
            ],
            total:800000
        },
        3: {
            status:'pending', statusText:'Chờ xác nhận',
            date:'20/05/2024', time:'14:00',
            customerNote:'Muốn bọc sứ 2 răng cửa và tẩy trắng.',
            doctorName:null, doctorSpec:null, doctorDegree:null, doctorAvatar:null,
            room:null,
            diagnosis:null, doctorNote:null,
            services:[
                {name:'Bọc răng sứ Zirconia', price:6000000, time:'45 phút/răng'},
                {name:'Tẩy trắng răng Laser', price:2500000, time:'45 phút'}
            ],
            total:8500000
        }
    };

    var currentCancelId=null;

    // === TAB ===
    function switchTab(t,el){
        document.querySelectorAll('.tab-content').forEach(function(c){c.classList.remove('active');});
        document.querySelectorAll('.sidebar-menu-item').forEach(function(i){i.classList.remove('active');});
        document.getElementById('tab-'+t).classList.add('active');el.classList.add('active');
    }

    // === SAVE INFO ===
    function saveInfo(e){
        e.preventDefault();
        var name=document.getElementById('infoName').value.trim();
        if(!name){alert('Vui lòng nhập họ và tên');return false;}
        var email=document.getElementById('infoEmail').value.trim();
        if(email&&!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)){alert('Email không hợp lệ');return false;}
        alert('✅ Cập nhật thông tin thành công!');return false;
    }

    // === FILTER ===
    function filterHistory(s,btn){
        document.querySelectorAll('.filter-chip').forEach(function(c){c.classList.remove('active');});btn.classList.add('active');
        var stt=0;document.querySelectorAll('#historyTable tbody tr').forEach(function(r){
            var ok=s==='all'||r.getAttribute('data-status')===s;
            if(ok){stt++;r.style.display='';r.querySelector('.stt-cell').textContent=stt;}else{r.style.display='none';}
        });
    }

    // === MODAL CHI TIET - HIEN THI THEO TRANG THAI ===
    function showDetail(id){
        var d=appointmentDetails[id];if(!d)return;
        var isPaid=d.status==='paid';
        var isConfirmed=d.status==='confirmed';
        var isPending=d.status==='pending';
        var html='';

        // HERO
        html+='<div class="receipt-hero">';
        html+='<span class="receipt-status-pill '+d.status+'">'+d.statusText+'</span>';
        html+='<h3>Chi tiết phiếu khám</h3>';
        html+='<div class="receipt-subtitle">Nha Khoa Kvone - 48 Cao Thắng, Đà Nẵng</div>';
        html+='</div><div class="receipt-content">';

        // THONG TIN LICH HEN (luon hien)
        html+='<div class="receipt-card">';
        html+='<div class="receipt-card-title"><span class="title-icon">📅</span> Thông tin lịch hẹn</div>';
        html+='<div class="info-row">';
        html+='<div class="info-cell"><div class="cell-icon">🗓️</div><div class="cell-text"><small>Ngày khám</small><strong>'+d.date+'</strong></div></div>';
        html+='<div class="info-cell"><div class="cell-icon">🕐</div><div class="cell-text"><small>Giờ khám</small><strong>'+d.time+'</strong></div></div>';
        if(d.room){
            html+='<div class="info-cell"><div class="cell-icon">🏥</div><div class="cell-text"><small>Phòng khám</small><strong>'+d.room+'</strong></div></div>';
        }else{
            html+='<div class="info-cell"><div class="cell-icon">🏥</div><div class="cell-text"><small>Phòng khám</small><strong style="color:var(--text-muted);font-style:italic">Chờ...</strong></div></div>';
        }
        html+='</div></div>';

        // BAC SI (chi hien khi confirmed hoac paid)
        if(d.doctorName){
            html+='<div class="receipt-card">';
            html+='<div class="receipt-card-title"><span class="title-icon">👨‍⚕️</span> Bác sĩ phụ trách</div>';
            html+='<div class="doctor-card-mini"><div class="doctor-avatar-mini"><img src="'+d.doctorAvatar+'" alt="Doctor"></div>';
            html+='<div class="doctor-details"><h4>'+d.doctorName+'</h4><p>Trình độ: '+d.doctorDegree+'</p><span class="specialty-tag">'+d.doctorSpec+'</span></div></div>';
            html+='</div>';
        }else{
            html+='<div class="receipt-card">';
            html+='<div class="receipt-card-title"><span class="title-icon">👨‍⚕️</span> Bác sĩ phụ trách</div>';
            html+='<div class="medical-empty" style="font-style:italic">⏳ Đang chờ...</div>';
            html+='</div>';
        }

        // GHI CHU KHACH HANG (luon hien)
        if(d.customerNote){
            html+='<div class="receipt-card">';
            html+='<div class="receipt-card-title"><span class="title-icon">📝</span> Ghi chú của bạn</div>';
            html+='<div class="medical-item"><div class="medical-text">'+d.customerNote+'</div></div>';
            html+='</div>';
        }

        // CHAN DOAN + GHI CHU BAC SI (chi hien khi da kham - paid)
        if(isPaid && d.diagnosis){
            html+='<div class="receipt-card">';
            html+='<div class="receipt-card-title"><span class="title-icon">🩺</span> Kết quả khám</div>';
            html+='<div class="medical-item"><div class="medical-label">Chẩn đoán</div><div class="medical-text diagnosis">'+d.diagnosis+'</div></div>';
            if(d.doctorNote){
                html+='<div class="medical-item"><div class="medical-label">Ghi chú của bác sĩ</div><div class="medical-text">'+d.doctorNote+'</div></div>';
            }
            html+='</div>';
        }

        // DICH VU (luon hien)
        html+='<div class="receipt-card">';
        html+='<div class="receipt-card-title"><span class="title-icon">💎</span> Dịch vụ đã chọn</div>';
        if(d.services.length>0){
            html+='<div class="services-list">';
            d.services.forEach(function(s){
                html+='<div class="service-row"><div class="service-info"><div class="service-name">'+s.name+'</div><div class="service-position">⏱ '+s.time+'</div></div><div class="service-price">'+s.price.toLocaleString('vi-VN')+' đ</div></div>';
            });
            html+='</div>';
        }else{
            html+='<div class="medical-empty">Chưa có dịch vụ</div>';
        }
        html+='</div>';

        // TONG TIEN
        if(d.total>0){
            html+='<div class="receipt-total-box"><div class="total-label">'+(isPaid?'Tổng chi phí':'Tổng chi phí dự kiến')+'</div><div class="total-value">'+d.total.toLocaleString('vi-VN')+' đ</div></div>';
        }

        html+='</div>';
        document.getElementById('modalBody').innerHTML=html;
        document.getElementById('detailModal').classList.add('show');
    }

    function closeModal(){document.getElementById('detailModal').classList.remove('show');}

    // === HUY LICH ===
    function confirmCancel(id){currentCancelId=id;document.getElementById('cancelModal').classList.add('show');}
    function closeCancelModal(){document.getElementById('cancelModal').classList.remove('show');}
    function doCancelAppointment(){
        alert('✅ Đã hủy lịch hẹn thành công!');
        var row=document.querySelector('tr[data-id="'+currentCancelId+'"]');if(row)row.remove();
        var stt=1;document.querySelectorAll('#historyTable tbody tr').forEach(function(r){if(r.style.display!=='none')r.querySelector('.stt-cell').textContent=stt++;});
        closeCancelModal();
    }

    // === DANG XUAT ===
    function confirmLogout(){document.getElementById('logoutModal').classList.add('show');}
    function closeLogoutModal(){document.getElementById('logoutModal').classList.remove('show');}
    function doLogout(){window.location.href='${pageContext.request.contextPath}/account/logout.jsp';}

    // === DOI MAT KHAU ===
    function checkStrength(p){
        var bar=document.getElementById('strengthBar');var s=0;
        if(/[a-z]/.test(p))s++;if(/[A-Z]/.test(p))s++;if(/[0-9]/.test(p))s++;if(/[!@#$%^&*]/.test(p))s++;if(p.length>=8)s++;
        bar.className='password-strength-bar';if(s<=2)bar.classList.add('weak');else if(s<=4)bar.classList.add('medium');else bar.classList.add('strong');
    }
    function changePassword(e){
        e.preventDefault();var ok=true;
        if(!document.getElementById('oldPassword').value){document.getElementById('oldPassGroup').classList.add('error');ok=false;}else document.getElementById('oldPassGroup').classList.remove('error');
        var np=document.getElementById('newPassword').value;
        if(!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$/.test(np)){document.getElementById('newPassGroup').classList.add('error');ok=false;}else document.getElementById('newPassGroup').classList.remove('error');
        var cp=document.getElementById('confirmNewPassword').value;
        if(cp!==np||!cp){document.getElementById('confirmPassGroup').classList.add('error');ok=false;}else document.getElementById('confirmPassGroup').classList.remove('error');
        if(ok){alert('✅ Đổi mật khẩu thành công!');document.getElementById('passwordForm').reset();document.getElementById('strengthBar').className='password-strength-bar';}
        return false;
    }
    document.getElementById('oldPassword').addEventListener('input',function(){document.getElementById('oldPassGroup').classList.remove('error');});
    document.getElementById('newPassword').addEventListener('input',function(){document.getElementById('newPassGroup').classList.remove('error');});
    document.getElementById('confirmNewPassword').addEventListener('input',function(){document.getElementById('confirmPassGroup').classList.remove('error');});
    </script>
</body>
</html>
