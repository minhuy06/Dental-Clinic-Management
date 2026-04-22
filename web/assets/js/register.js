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

function handleRegister(e) {
    e.preventDefault();
    var ok = true;

    // Ho ten
    if (!document.getElementById('regName').value.trim()) {
        document.getElementById('nameGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('nameGroup').classList.remove('error');
    }

    // SDT + Email: phai co it nhat 1
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

    // Ngay sinh
    if (!document.getElementById('regDob').value) {
        document.getElementById('dobGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('dobGroup').classList.remove('error');
    }

    // Mat khau
    var pass = document.getElementById('regPassword').value;
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(pass)) {
        document.getElementById('passGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('passGroup').classList.remove('error');
    }

    // Xac nhan mat khau
    if (document.getElementById('regConfirm').value !== pass) {
        document.getElementById('confirmGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('confirmGroup').classList.remove('error');
    }

    if (ok) {
        document.getElementById('otpModal').classList.add('show');
        startOtpTimer();
        document.querySelector('.otp-box').focus();
    }
    return false;
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

function verifyOtp() {
    var code = '';
    document.querySelectorAll('.otp-box').forEach(function(b) { code += b.value; });
    if (code.length < 6) {
        alert('Vui lòng nhập đủ 6 số');
        return;
    }
    alert('✅ Đăng ký thành công!');
    document.getElementById('otpModal').classList.remove('show');
    window.location.href = window.CONTEXT_PATH + '/account/login.jsp';
}
