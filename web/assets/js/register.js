(function initRegisterBootstrap() {
    if (typeof AppBootstrap !== 'undefined') {
        var cp = AppBootstrap.getMetaContent('context-path');
        if (cp) window.CONTEXT_PATH = cp;
    }
})();

var REGISTER_URL = (window.CONTEXT_PATH || '') + '/RegisterServlet';

function togglePass(id, btn) {
    var i = document.getElementById(id);
    i.type = i.type === 'password' ? 'text' : 'password';
    btn.textContent = i.type === 'password' ? '👁' : '🙈';
}

async function postRegister(params) {
    var res = await fetch(REGISTER_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
        body: params.toString()
    });
    return (await res.text()).replace(/^\uFEFF/, '').trim();
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
    var el = document.getElementById('otpCountdown');
    if (el) el.textContent = sec;
    clearInterval(otpInterval);
    otpInterval = setInterval(function() {
        sec--;
        if (el) el.textContent = sec;
        if (sec <= 0) clearInterval(otpInterval);
    }, 1000);
}

function mapRegisterError(code) {
    switch (code) {
        case 'PHONE_EXISTS':
            return 'Số điện thoại đã được đăng ký. Vui lòng đăng nhập hoặc dùng SĐT khác.';
        case 'INVALID_PHONE':
            return 'Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0).';
        case 'INVALID':
            return 'Vui lòng điền đầy đủ thông tin bắt buộc.';
        default:
            return 'Không thể tiếp tục đăng ký. Vui lòng thử lại.';
    }
}

async function handleRegister(e) {
    e.preventDefault();
    var ok = true;

    if (!document.getElementById('regName').value.trim()) {
        document.getElementById('nameGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('nameGroup').classList.remove('error');
    }

    var phone = document.getElementById('regPhone').value.trim();
    document.getElementById('phoneGroup').classList.remove('error');
    if (!phone) {
        document.getElementById('phoneGroup').classList.add('error');
        ok = false;
    } else if (!/^(0[0-9]{9})$/.test(phone)) {
        document.getElementById('phoneGroup').classList.add('error');
        ok = false;
    }

    if (!document.getElementById('regDob').value) {
        document.getElementById('dobGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('dobGroup').classList.remove('error');
    }

    var pass = document.getElementById('regPassword').value;
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(pass)) {
        document.getElementById('passGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('passGroup').classList.remove('error');
    }

    if (document.getElementById('regConfirm').value !== pass) {
        document.getElementById('confirmGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('confirmGroup').classList.remove('error');
    }

    if (!ok) return false;

    var btnSubmit = e.target.querySelector('button[type="submit"]');
    var originalText = btnSubmit.innerHTML;
    btnSubmit.innerHTML = 'Đang xử lý...';
    btnSubmit.disabled = true;

    var formData = new URLSearchParams();
    formData.append('txtHoTen', document.getElementById('regName').value.trim());
    formData.append('txtSDT', phone);
    formData.append('txtMatKhau', pass);
    formData.append('txtNgaySinh', document.getElementById('regDob').value);
    formData.append('txtGioiTinh', document.getElementById('regGender').value);

    try {
        var result = await postRegister(formData);
        btnSubmit.innerHTML = originalText;
        btnSubmit.disabled = false;

        if (result === 'SUCCESS') {
            document.getElementById('otpModal').classList.add('show');
            startOtpTimer();
            document.querySelectorAll('.otp-box').forEach(function(b) { b.value = ''; });
            var first = document.querySelector('.otp-box');
            if (first) first.focus();
        } else {
            AppNotify.error(mapRegisterError(result));
        }
    } catch (err) {
        console.error('[register] prepare error:', err);
        AppNotify.error('Lỗi kết nối đến máy chủ!');
        btnSubmit.innerHTML = originalText;
        btnSubmit.disabled = false;
    }

    return false;
}

async function verifyOtp() {
    var code = '';
    document.querySelectorAll('.otp-box').forEach(function(b) { code += b.value; });

    if (code.length < 6) {
        AppNotify.warn('Vui lòng nhập đủ 6 số');
        return;
    }

    var btnVerify = document.querySelector('#otpModal .sys-close');
    var originalText = btnVerify.innerHTML;
    btnVerify.innerHTML = 'Đang kiểm tra...';
    btnVerify.disabled = true;

    var formData = new URLSearchParams();
    formData.append('action', 'verify');
    formData.append('txtOTP', code);

    try {
        var result = await postRegister(formData);
        btnVerify.innerHTML = originalText;
        btnVerify.disabled = false;

        if (result === 'SUCCESS') {
            await AppNotify.success('Đăng ký thành công!');
            document.getElementById('otpModal').classList.remove('show');
            var basePath = window.CONTEXT_PATH ? window.CONTEXT_PATH : '';
            window.location.href = basePath + '/account/login.jsp?msg=success';
        } else if (result === 'FAILED') {
            AppNotify.error('Mã xác thực không đúng. Vui lòng nhập 123456 (mã demo).');
            document.querySelectorAll('.otp-box').forEach(function(b) { b.value = ''; });
            document.querySelector('.otp-box').focus();
        } else if (result === 'EXPIRED') {
            AppNotify.warn('Phiên đăng ký đã hết hạn. Vui lòng gửi lại form đăng ký.');
            document.getElementById('otpModal').classList.remove('show');
        } else {
            AppNotify.error(mapRegisterError(result));
        }
    } catch (err) {
        console.error('[register] verify error:', err);
        AppNotify.error('Lỗi kết nối đến máy chủ!');
        btnVerify.innerHTML = originalText;
        btnVerify.disabled = false;
    }
}
