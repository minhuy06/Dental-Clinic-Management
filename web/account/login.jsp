<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Nha Khoa Smile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body>

    <!-- HEADER -->
    <jsp:include page="../components/header.jsp" />

    <!-- TRANG DANG NHAP -->
    <section class="auth-page">
        <div class="auth-container">

            <!-- Ben trai: Banner -->
            <div class="auth-banner">
                <div class="auth-banner-content">
                    <span class="tooth-icon">🦷</span>
                    <h2>Chào mừng trở lại!</h2>
                    <p>
                        Đăng nhập để đặt lịch khám, theo dõi lịch sử 
                        điều trị và quản lý hồ sơ sức khỏe răng miệng 
                        của bạn.
                    </p>
                </div>
            </div>

            <!-- Ben phai: Form -->
            <div class="auth-form-wrapper">
                <div class="auth-form-header">
                    <h2>Đăng nhập</h2>
                    <p>Nhập số điện thoại và mật khẩu để tiếp tục</p>
                </div>

                <form class="auth-form" id="loginForm" onsubmit="return handleLogin(event)">

                    <!-- So dien thoai -->
                    <div class="form-group" id="phoneGroup">
                        <label>Số điện thoại <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">📱</span>
                            <input type="tel" 
                                   class="form-control-icon" 
                                   id="phone" 
                                   placeholder="Nhập số điện thoại (VD: 0901234567)"
                                   maxlength="10"
                                   autocomplete="tel">
                        </div>
                        <div class="form-error-msg" id="phoneError">Số điện thoại phải đủ 10 số</div>
                    </div>

                    <!-- Mat khau -->
                    <div class="form-group" id="passwordGroup">
                        <label>Mật khẩu <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">🔒</span>
                            <input type="password" 
                                   class="form-control-icon" 
                                   id="password" 
                                   placeholder="Nhập mật khẩu"
                                   autocomplete="current-password">
                            <button type="button" class="toggle-password" onclick="togglePassword()">
                                👁
                            </button>
                        </div>
                        <div class="form-error-msg" id="passwordError">Vui lòng nhập mật khẩu</div>
                    </div>

                    <!-- Nho mat khau + Quen -->
                    <div class="form-options">
                        <label class="remember-me">
                            <input type="checkbox"> Nhớ đăng nhập
                        </label>
                        <a href="#" class="forgot-link">Quên mật khẩu?</a>
                    </div>

                    <!-- Nut dang nhap -->
                    <button type="submit" class="btn-submit">Đăng nhập</button>

                    <!-- Ke ngang -->
                    <div class="auth-divider">hoặc</div>

                    <!-- Chuyen sang dang ky -->
                    <div class="auth-switch">
                        Chưa có tài khoản? 
                        <a href="${pageContext.request.contextPath}/account/register.jsp">Đăng ký ngay</a>
                    </div>

                </form>
            </div>

        </div>
    </section>

    <!-- FOOTER -->
    <jsp:include page="../components/footer.jsp" />

    <!-- JavaScript xac thuc form -->
    <script>
        // Toggle hien/an mat khau
        function togglePassword() {
            var input = document.getElementById('password');
            var btn = document.querySelector('.toggle-password');
            if (input.type === 'password') {
                input.type = 'text';
                btn.textContent = '🙈';
            } else {
                input.type = 'password';
                btn.textContent = '👁';
            }
        }

        // Xu ly dang nhap
        function handleLogin(event) {
            event.preventDefault();
            var isValid = true;

            var phone = document.getElementById('phone').value.trim();
            var password = document.getElementById('password').value;

            // Kiem tra so dien thoai
            var phoneGroup = document.getElementById('phoneGroup');
            var phoneRegex = /^[0-9]{10}$/;
            if (!phoneRegex.test(phone)) {
                phoneGroup.classList.add('error');
                isValid = false;
            } else {
                phoneGroup.classList.remove('error');
            }

            // Kiem tra mat khau
            var passwordGroup = document.getElementById('passwordGroup');
            if (password.length === 0) {
                passwordGroup.classList.add('error');
                isValid = false;
            } else {
                passwordGroup.classList.remove('error');
            }

            if (isValid) {
                // Gui form den backend (Servlet)
                // Hien tai chua co backend nen chi thong bao
                // Demo: luu ten vao session thong qua URL (backend se xu ly thuc te)
                window.location.href = '${pageContext.request.contextPath}/index.jsp?loginSuccess=true&phone=' + encodeURIComponent(phone);
                // Khi co backend, dung: document.getElementById('loginForm').submit();
            }

            return false;
        }

        // Xoa loi khi nguoi dung go lai
        document.getElementById('phone').addEventListener('input', function() {
            document.getElementById('phoneGroup').classList.remove('error');
            // Chi cho nhap so
            this.value = this.value.replace(/[^0-9]/g, '');
        });

        document.getElementById('password').addEventListener('input', function() {
            document.getElementById('passwordGroup').classList.remove('error');
        });
    </script>

</body>
</html>
