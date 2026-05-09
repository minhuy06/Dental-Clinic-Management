function togglePass(id, btn) {
    var i = document.getElementById(id);
    i.type = i.type === 'password' ? 'text' : 'password';
    btn.textContent = i.type === 'password' ? '👁' : '🙈';
}

var REGISTER_CONFIG = {
    USE_MOCK: true,
    API_BASE: (window.CONTEXT_PATH || ''),
    MOCK_DELAY_MS: 120
};

function registerDelay(ms) {
    return new Promise(function(resolve) { setTimeout(resolve, ms); });
}

async function registerRequest(url, bodyParams) {
    var res = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: bodyParams.toString()
    });
    return res.text();
}

var registerMockApi = {
    register: async function(formData) {
        await registerDelay(REGISTER_CONFIG.MOCK_DELAY_MS);
        return 'SUCCESS';
    },
    verifyOtp: async function(formData) {
        await registerDelay(REGISTER_CONFIG.MOCK_DELAY_MS);
        return formData.get('txtOTP') === '123456' ? 'SUCCESS' : 'FAILED';
    }
};

var registerApi = {
    register: function(formData) {
        return registerRequest('../RegisterServlet', formData);
    },
    verifyOtp: function(formData) {
        return registerRequest('../VerifyOTPServlet', formData);
    }
};

var registerDataSource = REGISTER_CONFIG.USE_MOCK ? registerMockApi : registerApi;

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

// === DANG KY - GOI RegisterServlet ===

async function handleRegister(e) {
    e.preventDefault();
    var ok = true;

    // Kiem tra ho ten
    if (!document.getElementById('regName').value.trim()) {
        document.getElementById('nameGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('nameGroup').classList.remove('error');
    }

    // Kiem tra SDT (bat buoc)
    var phone = document.getElementById('regPhone').value.trim();
    document.getElementById('phoneGroup').classList.remove('error');

    if (!phone) {
        document.getElementById('phoneGroup').classList.add('error');
        ok = false;
    } else if (!/^(0[0-9]{9})$/.test(phone)) {
        document.getElementById('phoneGroup').classList.add('error');
        ok = false;
    }

    // Kiem tra ngay sinh
    if (!document.getElementById('regDob').value) {
        document.getElementById('dobGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('dobGroup').classList.remove('error');
    }

    // Kiem tra mat khau
    var pass = document.getElementById('regPassword').value;
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(pass)) {
        document.getElementById('passGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('passGroup').classList.remove('error');
    }

    // Kiem tra xac nhan mat khau
    if (document.getElementById('regConfirm').value !== pass) {
        document.getElementById('confirmGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('confirmGroup').classList.remove('error');
    }

    // Neu form hop le -> goi backend RegisterServlet
    if (ok) {
        var btnSubmit = e.target.querySelector('button[type="submit"]');
        var originalText = btnSubmit.innerHTML;
        btnSubmit.innerHTML = 'Đang gửi OTP...';
        btnSubmit.disabled = true;

        var formData = new URLSearchParams();
        formData.append('txtHoTen', document.getElementById('regName').value.trim());
        formData.append('txtSDT', phone);
        formData.append('txtMatKhau', pass);

        try {
            var result = await registerDataSource.register(formData);
            btnSubmit.innerHTML = originalText;
            btnSubmit.disabled = false;
            if (result.trim() === 'SUCCESS') {
                document.getElementById('otpModal').classList.add('show');
                startOtpTimer();
                document.querySelector('.otp-box').focus();
            } else {
                alert('Lỗi: Không thể gửi SMS. Hãy kiểm tra lại số điện thoại!');
            }
        } catch (err) {
            console.error('[register] submit error:', err);
            alert('Lỗi kết nối đến máy chủ!');
            btnSubmit.innerHTML = originalText;
            btnSubmit.disabled = false;
        }
    }

    return false;
}

// === XAC THUC OTP - GOI VerifyOTPServlet ===

async function verifyOtp() {
    var code = '';
    document.querySelectorAll('.otp-box').forEach(function(b) { code += b.value; });

    if (code.length < 6) {
        alert('Vui lòng nhập đủ 6 số');
        return;
    }

    // Nut xac nhan trong otpModal (class sys-close)
    var btnVerify = document.querySelector('#otpModal .sys-close');
    var originalText = btnVerify.innerHTML;
    btnVerify.innerHTML = 'Đang kiểm tra...';
    btnVerify.disabled = true;

    var formData = new URLSearchParams();
    formData.append('txtOTP', code);

    try {
        var result = await registerDataSource.verifyOtp(formData);
        btnVerify.innerHTML = originalText;
        btnVerify.disabled = false;
        if (result.trim() === 'SUCCESS') {
            alert('✅ Đăng ký thành công!');
            document.getElementById('otpModal').classList.remove('show');
            var basePath = window.CONTEXT_PATH ? window.CONTEXT_PATH : '';
            window.location.href = basePath + '/account/login.jsp?msg=success';
        } else {
            alert('❌ Mã OTP không chính xác. Vui lòng thử lại!');
            var boxes = document.querySelectorAll('.otp-box');
            boxes.forEach(function(b) { b.value = ''; });
            boxes[0].focus();
        }
    } catch (err) {
        console.error('[register] verify otp error:', err);
        alert('Lỗi kết nối đến máy chủ!');
        btnVerify.innerHTML = originalText;
        btnVerify.disabled = false;
    }
}