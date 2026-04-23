// ==================================================================
// PHẦN 1: CÁC HÀM TIỆN ÍCH GIAO DIỆN (Giữ nguyên)
// ==================================================================
function togglePass(id, btn) {
    var i = document.getElementById(id);
    i.type = i.type === 'password' ? 'text' : 'password';
    btn.textContent = i.type === 'password' ? '👁' : '🙈';
}

function checkRegStrength(p) {
    var bar = document.getElementById('regStrengthBar');
    var s = 0;
    if (/[a-z]/.test(p)) s++;
    if (/[A-Z]/.test(p)) s++;
    if (/[0-9]/.test(p)) s++;
    if (/[!@#$%^&*]/.test(p)) s++;
    if (p.length >= 8) s++;
    bar.className = 'password-strength-bar';
    if (s <= 2) bar.classList.add('weak');
    else if (s <= 4) bar.classList.add('medium');
    else bar.classList.add('strong');
}

function otpNext(el, i) {
    if (el.value.length === 1) {
        var boxes = document.querySelectorAll('.otp-box');
        if (i < 5) boxes[i + 1].focus();
    }
}

function otpBack(e, i) {
    if (e.key === 'Backspace' && !e.target.value && i > 0) {
        var boxes = document.querySelectorAll('.otp-box');
        boxes[i - 1].focus();
    }
}

var otpInterval;
function startOtpTimer() {
    var sec = 60;
    document.getElementById('otpCountdown').textContent = sec;
    clearInterval(otpInterval);
    otpInterval = setInterval(function() {
        sec--;
        document.getElementById('otpCountdown').textContent = sec;
        if (sec <= 0) clearInterval(otpInterval);
    }, 1000);
}

// ==================================================================
// PHẦN 2: XỬ LÝ GỬI FORM ĐĂNG KÝ VÀ YÊU CẦU OTP EMAIL
// ==================================================================
function handleRegister(e) {
    e.preventDefault();
    var ok = true;

    // --- KIỂM TRA LỖI NHẬP LIỆU CỦA FORM ---
    if (!document.getElementById('regName').value.trim()) {
        document.getElementById('nameGroup').classList.add('error');
        ok = false;
    } else { document.getElementById('nameGroup').classList.remove('error'); }

    var phone = document.getElementById('regPhone').value.trim();
    var email = document.getElementById('regEmail').value.trim();
    var hasContact = false;
    
    document.getElementById('phoneGroup').classList.remove('error');
    document.getElementById('emailGroup').classList.remove('error');
    document.getElementById('contactError').style.display = 'none';

    if (phone) {
        if (!/^(0[0-9]{9})$/.test(phone)) {
            document.getElementById('phoneGroup').classList.add('error');
            ok = false;
        } else hasContact = true;
    }
    if (email) {
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            document.getElementById('emailGroup').classList.add('error');
            ok = false;
        } else hasContact = true;
    }
    if (!hasContact) {
        document.getElementById('contactError').style.display = 'block';
        ok = false;
    }

    if (!document.getElementById('regDob').value) {
        document.getElementById('dobGroup').classList.add('error');
        ok = false;
    } else { document.getElementById('dobGroup').classList.remove('error'); }

    var pass = document.getElementById('regPassword').value;
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(pass)) {
        document.getElementById('passGroup').classList.add('error');
        ok = false;
    } else { document.getElementById('passGroup').classList.remove('error'); }

    if (document.getElementById('regConfirm').value !== pass) {
        document.getElementById('confirmGroup').classList.add('error');
        ok = false;
    } else { document.getElementById('confirmGroup').classList.remove('error'); }

    // --- NẾU FORM HỢP LỆ -> GỌI BACKEND JAVA ---
    if (ok) {
        var btnSubmit = e.target.querySelector('button[type="submit"]');
        var originalText = btnSubmit.innerHTML;
        btnSubmit.innerHTML = "Đang gửi OTP...";
        btnSubmit.disabled = true; // Chặn người dùng spam click

        // Đóng gói dữ liệu (Đã vá lỗi: Truyền thêm biến txtEmail)
        var formData = new URLSearchParams();
        formData.append('txtHoTen', document.getElementById('regName').value.trim());
        formData.append('txtSDT', phone);
        formData.append('txtEmail', email); // <-- Dòng quan trọng nhất để Java gửi được Email
        formData.append('txtMatKhau', pass);

        fetch('../RegisterServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        })
        .then(response => response.text())
        .then(result => {
            btnSubmit.innerHTML = originalText;
            btnSubmit.disabled = false;

            if (result.trim() === "SUCCESS") {
                // Mở popup nhập mã khi Java báo gửi Email thành công
                document.getElementById('otpModal').classList.add('show');
                startOtpTimer();
                document.querySelector('.otp-box').focus();
            } else {
                // Đã vá lỗi: Cập nhật lại câu thông báo phù hợp với việc dùng Email
                alert("Lỗi: Không thể gửi mã OTP. Hãy kiểm tra lại địa chỉ Email của bạn!");
            }
        })
        .catch(err => {
            console.error(err);
            alert("Lỗi kết nối đến máy chủ!");
            btnSubmit.innerHTML = originalText;
            btnSubmit.disabled = false;
        });
    }
    return false;
}

// ==================================================================
// PHẦN 3: GỌI SERVLET ĐỂ XÁC THỰC MÃ OTP 
// ==================================================================
function verifyOtp() {
    var code = '';
    document.querySelectorAll('.otp-box').forEach(function(b) { code += b.value; });
    
    if (code.length < 6) {
        alert('Vui lòng nhập đủ 6 số');
        return;
    }

    // Đã vá lỗi: Tìm đúng ID của nút Xác Nhận (Không dùng class .sys-close nữa)
    // Lưu ý: Nhớ thêm id="btnConfirmOtp" vào nút bấm trong file HTML
    var btnVerify = document.getElementById('btnConfirmOtp'); 
    
    // Viết thêm hàm dự phòng lỡ bạn chưa kịp thêm ID vào HTML
    if (!btnVerify) { 
        btnVerify = document.querySelector('button[onclick="verifyOtp()"]'); 
    }

    var originalText = "Xác nhận";
    if (btnVerify) {
        originalText = btnVerify.innerHTML;
        btnVerify.innerHTML = "Đang kiểm tra...";
        btnVerify.disabled = true;
    }

    // Đóng gói mã OTP gửi xuống Servlet
    var formData = new URLSearchParams();
    formData.append('txtOTP', code);

    fetch('../VerifyOTPServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
    })
    .then(response => response.text())
    .then(result => {
        if (btnVerify) {
            btnVerify.innerHTML = originalText;
            btnVerify.disabled = false;
        }

        if (result.trim() === "SUCCESS") {
            alert('✅ Đăng ký thành công!');
            document.getElementById('otpModal').classList.remove('show');
            // Chuyển sang trang đăng nhập
            var basePath = window.CONTEXT_PATH ? window.CONTEXT_PATH : '';
            window.location.href = basePath + '/account/login.jsp?msg=success';
        } else {
            alert('❌ Mã OTP không chính xác. Vui lòng thử lại!');
            // Xóa trắng 6 ô để nhập lại
            var boxes = document.querySelectorAll('.otp-box');
            boxes.forEach(function(b) { b.value = ''; });
            boxes[0].focus();
        }
    })
    .catch(err => {
        console.error(err);
        alert("Lỗi kết nối đến máy chủ!");
        if (btnVerify) {
            btnVerify.innerHTML = originalText;
            btnVerify.disabled = false;
        }
    });
}