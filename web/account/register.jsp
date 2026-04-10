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

    <!-- HEADER -->
    <jsp:include page="../components/header.jsp" />

    <!-- TRANG DANG KY -->
    <section class="auth-page">
        <div class="auth-container">

            <!-- Ben trai: Banner -->
            <div class="auth-banner">
                <div class="auth-banner-content">
                    <span class="tooth-icon">🦷</span>
                    <h2>Tạo tài khoản mới</h2>
                    <p>
                        Đăng ký để đặt lịch khám nhanh chóng, 
                        theo dõi lịch sử điều trị và nhận ưu đãi 
                        độc quyền từ Nha Khoa Kvone.
                    </p>
                </div>
            </div>

            <!-- Ben phai: Form -->
            <div class="auth-form-wrapper">
                <div class="auth-form-header">
                    <h2>Đăng ký</h2>
                    <p>Điền thông tin để tạo tài khoản mới</p>
                </div>

                <form class="auth-form" id="registerForm" onsubmit="return handleRegister(event)" autocomplete="off">

                    <!-- Ho ten -->
                    <div class="form-group" id="nameGroup">
                        <label>Họ và tên <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">👤</span>
                            <input type="text" 
                                   class="form-control-icon" 
                                   id="fullName" 
                                   placeholder="Nhập họ và tên"
                                   autocomplete="off">
                        </div>
                        <div class="form-error-msg" id="nameError">Vui lòng nhập họ và tên</div>
                    </div>

                    <!-- So dien thoai -->
                    <div class="form-group" id="phoneGroup">
                        <label>Số điện thoại <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">📞</span>
                            <input type="tel" 
                                   class="form-control-icon" 
                                   id="phone" 
                                   placeholder="Nhập số điện thoại"
                                   maxlength="10"
                                   autocomplete="off">
                        </div>
                        <div class="form-error-msg" id="phoneError">Số điện thoại phải đủ 10 số</div>
                    </div>

                    <!-- Ngay sinh -->
                    <div class="form-group" id="dobGroup">
                        <label>Ngày sinh <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">📅</span>
                            <input type="date" 
                                   class="form-control-icon" 
                                   id="dob"
                                   autocomplete="off">
                        </div>
                        <div class="form-error-msg" id="dobError">Vui lòng chọn ngày sinh</div>
                    </div>

                    <!-- Mat khau -->
                    <div class="form-group" id="passwordGroup">
                        <label>Mật khẩu <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">🔒</span>
                            <input type="password" 
                                   class="form-control-icon" 
                                   id="password" 
                                   placeholder="Nhập mật khẩu (tối thiểu 6 ký tự)"
                                   autocomplete="new-password">
                            <button type="button" class="toggle-password" onclick="togglePass('password', this)">👁</button>
                        </div>
                        <div class="form-error-msg" id="passwordError">Mật khẩu phải có tối thiểu 6 ký tự</div>
                    </div>

                    <!-- Xac nhan mat khau -->
                    <div class="form-group" id="confirmGroup">
                        <label>Xác nhận mật khẩu <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">🔒</span>
                            <input type="password" 
                                   class="form-control-icon" 
                                   id="confirmPassword" 
                                   placeholder="Nhập lại mật khẩu"
                                   autocomplete="new-password">
                            <button type="button" class="toggle-password" onclick="togglePass('confirmPassword', this)">👁</button>
                        </div>
                        <div class="form-error-msg" id="confirmError">Mật khẩu nhập lại không khớp</div>
                    </div>

                    <!-- Nut dang ky -->
                    <button type="submit" class="btn-submit">Đăng ký</button>

                    <!-- Ke ngang -->
                    <div class="auth-divider">hoặc</div>

                    <!-- Chuyen sang dang nhap -->
                    <div class="auth-switch">
                        Đã có tài khoản? 
                        <a href="${pageContext.request.contextPath}/account/login.jsp">Đăng nhập ngay</a>
                    </div>

                </form>
            </div>

        </div>
    </section>

    <!-- POPUP OTP -->
    <div class="otp-overlay" id="otpOverlay">
        <div class="otp-popup">
            <div class="otp-header">
                <span class="otp-icon">📩</span>
                <h3>Xác thực OTP</h3>
                <p>Mã xác thực đã được gửi đến số điện thoại <strong id="otpPhone"></strong></p>
            </div>

            <div class="otp-inputs" id="otpInputs">
                <input type="text" maxlength="1" class="otp-box" data-index="0" autocomplete="off">
                <input type="text" maxlength="1" class="otp-box" data-index="1" autocomplete="off">
                <input type="text" maxlength="1" class="otp-box" data-index="2" autocomplete="off">
                <input type="text" maxlength="1" class="otp-box" data-index="3" autocomplete="off">
                <input type="text" maxlength="1" class="otp-box" data-index="4" autocomplete="off">
                <input type="text" maxlength="1" class="otp-box" data-index="5" autocomplete="off">
            </div>

            <div class="otp-timer">
                Gửi lại mã sau <span id="otpTimer">60</span>s
            </div>

            <button class="otp-btn-confirm" onclick="verifyOTP()">Xác nhận</button>

            <button class="otp-close" onclick="closeOTP()">Hủy</button>
        </div>
    </div>

    <!-- FOOTER -->
    <jsp:include page="../components/footer.jsp" />

    <!-- JavaScript -->
    <script>
        // Chan chon ngay tuong lai cho ngay sinh
        (function() {
            var today = new Date().toISOString().split('T')[0];
            document.getElementById('dob').setAttribute('max', today);
        })();

        // Toggle hien/an mat khau
        function togglePass(inputId, btn) {
            var input = document.getElementById(inputId);
            if (input.type === 'password') {
                input.type = 'text';
                btn.textContent = '🙈';
            } else {
                input.type = 'password';
                btn.textContent = '👁';
            }
        }

        // Xu ly dang ky
        function handleRegister(event) {
            event.preventDefault();
            var isValid = true;

            var name = document.getElementById('fullName').value.trim();
            var phone = document.getElementById('phone').value.trim();
            var dob = document.getElementById('dob').value;
            var password = document.getElementById('password').value;
            var confirm = document.getElementById('confirmPassword').value;

            // Kiem tra ho ten
            if (name.length === 0) {
                document.getElementById('nameGroup').classList.add('error');
                isValid = false;
            } else {
                document.getElementById('nameGroup').classList.remove('error');
            }

            // Kiem tra SDT (dung 10 so)
            var phoneRegex = /^[0-9]{10}$/;
            if (!phoneRegex.test(phone)) {
                document.getElementById('phoneGroup').classList.add('error');
                isValid = false;
            } else {
                document.getElementById('phoneGroup').classList.remove('error');
            }

            // Kiem tra ngay sinh
            if (!dob) {
                document.getElementById('dobGroup').classList.add('error');
                isValid = false;
            } else {
                document.getElementById('dobGroup').classList.remove('error');
            }

            // Kiem tra mat khau (toi thieu 6 ky tu)
            if (password.length < 6) {
                document.getElementById('passwordGroup').classList.add('error');
                isValid = false;
            } else {
                document.getElementById('passwordGroup').classList.remove('error');
            }

            // Kiem tra mat khau trung khop
            if (confirm !== password || confirm.length === 0) {
                document.getElementById('confirmGroup').classList.add('error');
                isValid = false;
            } else {
                document.getElementById('confirmGroup').classList.remove('error');
            }

            if (isValid) {
                showOTP(phone);
            }

            return false;
        }

        // === OTP POPUP ===
        var otpTimerInterval;

        function showOTP(phone) {
            // Hien thi so dien thoai da an mot phan
            var maskedPhone = phone.substring(0, 3) + '****' + phone.substring(7);
            document.getElementById('otpPhone').textContent = maskedPhone;

            // Hien popup
            document.getElementById('otpOverlay').classList.add('show');

            // Focus vao o dau tien
            var firstBox = document.querySelector('.otp-box[data-index="0"]');
            if (firstBox) firstBox.focus();

            // Bat dau dem nguoc 60s
            startOTPTimer();
        }

        function closeOTP() {
            document.getElementById('otpOverlay').classList.remove('show');
            clearInterval(otpTimerInterval);

            // Xoa cac o OTP
            document.querySelectorAll('.otp-box').forEach(function(box) {
                box.value = '';
            });
        }

        function startOTPTimer() {
            var seconds = 60;
            var timerEl = document.getElementById('otpTimer');
            timerEl.textContent = seconds;

            clearInterval(otpTimerInterval);
            otpTimerInterval = setInterval(function() {
                seconds--;
                timerEl.textContent = seconds;
                if (seconds <= 0) {
                    clearInterval(otpTimerInterval);
                    timerEl.textContent = '0';
                }
            }, 1000);
        }

        function verifyOTP() {
            var otpCode = '';
            document.querySelectorAll('.otp-box').forEach(function(box) {
                otpCode += box.value;
            });

            if (otpCode.length < 6) {
                alert('Vui lòng nhập đủ 6 số OTP');
                return;
            }

            // Demo: chap nhan moi ma OTP
            alert('Đăng ký thành công! Vui lòng đăng nhập.');
            closeOTP();
            window.location.href = '${pageContext.request.contextPath}/account/login.jsp';
            // Khi co backend: gui OTP len server xac thuc
        }

        // Auto focus sang o tiep theo khi nhap
        document.querySelectorAll('.otp-box').forEach(function(box, index, boxes) {
            box.addEventListener('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
                if (this.value.length === 1 && index < boxes.length - 1) {
                    boxes[index + 1].focus();
                }
            });

            box.addEventListener('keydown', function(e) {
                // Khi bam Backspace, quay lai o truoc
                if (e.key === 'Backspace' && this.value === '' && index > 0) {
                    boxes[index - 1].focus();
                }
            });
        });

        // Xoa loi khi nguoi dung go lai
        document.getElementById('fullName').addEventListener('input', function() {
            document.getElementById('nameGroup').classList.remove('error');
        });
        document.getElementById('phone').addEventListener('input', function() {
            document.getElementById('phoneGroup').classList.remove('error');
            this.value = this.value.replace(/[^0-9]/g, '');
        });
        document.getElementById('dob').addEventListener('input', function() {
            document.getElementById('dobGroup').classList.remove('error');
        });
        document.getElementById('password').addEventListener('input', function() {
            document.getElementById('passwordGroup').classList.remove('error');
        });
        document.getElementById('confirmPassword').addEventListener('input', function() {
            document.getElementById('confirmGroup').classList.remove('error');
        });
    </script>

</body>
</html>
