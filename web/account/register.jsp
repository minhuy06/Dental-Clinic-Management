<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Nha Khoa Kvone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    <main class="auth-page">
        <div class="auth-container">
            <div class="auth-box" style="max-width:520px;">
                <div class="auth-header">
                    <h2>Đăng ký tài khoản</h2>
                    <p>Tạo tài khoản để đặt lịch khám nhanh chóng</p>
                </div>
                <form id="registerForm" onsubmit="return handleRegister(event)">
                    <div class="form-group" id="nameGroup">
                        <label>Họ và tên <span style="color:#e74c3c">*</span></label>
                        <input type="text" class="form-control" id="regName" placeholder="VD: Nguyễn Văn An">
                        <div class="form-error">Vui lòng nhập họ và tên</div>
                    </div>
                    <div class="form-row">
                        <div class="form-group" id="phoneGroup">
                            <label>Số điện thoại</label>
                            <input type="tel" class="form-control" id="regPhone" placeholder="VD: 0901234567">
                            <div class="form-error">SĐT không hợp lệ</div>
                        </div>
                        <div class="form-group" id="emailGroup">
                            <label>Email</label>
                            <input type="email" class="form-control" id="regEmail" placeholder="VD: abc@gmail.com">
                            <div class="form-error">Email không hợp lệ</div>
                        </div>
                    </div>
                    <div class="form-group" id="contactError" style="display:none;">
                        <div class="form-error" style="display:block;">Vui lòng nhập SĐT hoặc Email (ít nhất 1)</div>
                    </div>

                    <div class="form-row">
                        <div class="form-group" id="dobGroup">
                            <label>Ngày sinh <span style="color:#e74c3c">*</span></label>
                            <input type="date" class="form-control" id="regDob">
                            <div class="form-error">Vui lòng chọn ngày sinh</div>
                        </div>
                        <div class="form-group">
                            <label>Giới tính <span style="color:#e74c3c">*</span></label>
                            <select class="form-control" id="regGender">
                                <option value="1">Nam</option>
                                <option value="0">Nữ</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" id="passGroup">
                        <label>Mật khẩu <span style="color:#e74c3c">*</span></label>
                        <div class="password-wrapper">
                            <input type="password" class="form-control" id="regPassword" placeholder="Tối thiểu 8 ký tự, gồm hoa/thường/số/đặc biệt" oninput="checkRegStrength(this.value)">
                            <button type="button" class="toggle-pass" onclick="togglePass('regPassword',this)">👁</button>
                        </div>
                        <div class="password-strength"><div class="password-strength-bar" id="regStrengthBar"></div></div>
                        <div class="form-error">Mật khẩu không đạt yêu cầu (cần hoa, thường, số, đặc biệt, tối thiểu 8 ký tự)</div>
                    </div>
                    <div class="form-group" id="confirmGroup">
                        <label>Xác nhận mật khẩu <span style="color:#e74c3c">*</span></label>
                        <div class="password-wrapper">
                            <input type="password" class="form-control" id="regConfirm" placeholder="Nhập lại mật khẩu">
                            <button type="button" class="toggle-pass" onclick="togglePass('regConfirm',this)">👁</button>
                        </div>
                        <div class="form-error">Mật khẩu xác nhận không khớp</div>
                    </div>

                    <button type="submit" class="btn btn-primary btn-lg" style="width:100%;">Đăng ký</button>
                </form>
                <div class="auth-footer">
                    <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/account/login.jsp">Đăng nhập</a></p>
                </div>
            </div>
        </div>
    </main>

    <!-- OTP MODAL -->
    <div class="system-modal-overlay" id="otpModal">
        <div class="system-modal" style="max-width:380px;">
            <span class="sys-icon">📱</span>
            <div class="sys-title">Xác thực OTP</div>
            <div class="sys-msg">Nhập mã 6 số đã gửi đến SĐT/Email của bạn</div>
            <div class="otp-inputs" id="otpInputs">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,0)" onkeydown="otpBack(event,0)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,1)" onkeydown="otpBack(event,1)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,2)" onkeydown="otpBack(event,2)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,3)" onkeydown="otpBack(event,3)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,4)" onkeydown="otpBack(event,4)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,5)" onkeydown="otpBack(event,5)">
            </div>
            <div class="otp-timer" id="otpTimer">Gửi lại mã sau <span id="otpCountdown">60</span>s</div>
            <button class="sys-close" onclick="verifyOtp()" style="margin-top:16px;">Xác nhận</button>
        </div>
    </div>

    <jsp:include page="../components/footer.jsp" />
    <script>
        function togglePass(id,btn){var i=document.getElementById(id);i.type=i.type==='password'?'text':'password';btn.textContent=i.type==='password'?'👁':'🙈';}

        function checkRegStrength(p){var bar=document.getElementById('regStrengthBar');var s=0;if(/[a-z]/.test(p))s++;if(/[A-Z]/.test(p))s++;if(/[0-9]/.test(p))s++;if(/[!@#$%^&*]/.test(p))s++;if(p.length>=8)s++;bar.className='password-strength-bar';if(s<=2)bar.classList.add('weak');else if(s<=4)bar.classList.add('medium');else bar.classList.add('strong');}

        function handleRegister(e){
            e.preventDefault();var ok=true;
            if(!document.getElementById('regName').value.trim()){document.getElementById('nameGroup').classList.add('error');ok=false;}else document.getElementById('nameGroup').classList.remove('error');

            var phone=document.getElementById('regPhone').value.trim();
            var email=document.getElementById('regEmail').value.trim();
            var hasContact=false;
            document.getElementById('phoneGroup').classList.remove('error');
            document.getElementById('emailGroup').classList.remove('error');
            document.getElementById('contactError').style.display='none';

            if(phone){if(!/^(0[0-9]{9})$/.test(phone)){document.getElementById('phoneGroup').classList.add('error');ok=false;}else hasContact=true;}
            if(email){if(!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)){document.getElementById('emailGroup').classList.add('error');ok=false;}else hasContact=true;}
            if(!hasContact){document.getElementById('contactError').style.display='block';ok=false;}

            if(!document.getElementById('regDob').value){document.getElementById('dobGroup').classList.add('error');ok=false;}else document.getElementById('dobGroup').classList.remove('error');

            var pass=document.getElementById('regPassword').value;
            if(!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(pass)){document.getElementById('passGroup').classList.add('error');ok=false;}else document.getElementById('passGroup').classList.remove('error');
            if(document.getElementById('regConfirm').value!==pass){document.getElementById('confirmGroup').classList.add('error');ok=false;}else document.getElementById('confirmGroup').classList.remove('error');

            if(ok){document.getElementById('otpModal').classList.add('show');startOtpTimer();document.querySelector('.otp-box').focus();}
            return false;
        }

        function otpNext(el,i){if(el.value.length===1){var boxes=document.querySelectorAll('.otp-box');if(i<5)boxes[i+1].focus();}}
        function otpBack(e,i){if(e.key==='Backspace'&&!e.target.value&&i>0){var boxes=document.querySelectorAll('.otp-box');boxes[i-1].focus();}}

        var otpInterval;
        function startOtpTimer(){var sec=60;document.getElementById('otpCountdown').textContent=sec;clearInterval(otpInterval);otpInterval=setInterval(function(){sec--;document.getElementById('otpCountdown').textContent=sec;if(sec<=0)clearInterval(otpInterval);},1000);}

        function verifyOtp(){
            var code='';document.querySelectorAll('.otp-box').forEach(function(b){code+=b.value;});
            if(code.length<6){alert('Vui lòng nhập đủ 6 số');return;}
            alert('✅ Đăng ký thành công!');
            document.getElementById('otpModal').classList.remove('show');
            window.location.href='${pageContext.request.contextPath}/account/login.jsp';
        }
    </script>
</body>
</html>
