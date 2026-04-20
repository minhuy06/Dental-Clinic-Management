<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Nha Khoa Kvone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    <main class="auth-page">
        <div class="auth-container">
            <div class="auth-box">
                <div class="auth-header">
                    <h2>Đăng nhập</h2>
                    <p>Chào mừng bạn trở lại Nha Khoa Kvone</p>
                </div>
                <form id="loginForm" onsubmit="return handleLogin(event)">
                    <div class="form-group" id="accountGroup">
                        <label>Số điện thoại / Email / Tên người dùng <span style="color:#e74c3c">*</span></label>
                        <input type="text" class="form-control" id="loginAccount" placeholder="Nhập SĐT, email hoặc tên đăng nhập">
                        <div class="form-error">Vui lòng nhập thông tin đăng nhập</div>
                    </div>
                    <div class="form-group" id="passGroup">
                        <label>Mật khẩu <span style="color:#e74c3c">*</span></label>
                        <div class="password-wrapper">
                            <input type="password" class="form-control" id="loginPassword" placeholder="Nhập mật khẩu">
                            <button type="button" class="toggle-pass" onclick="togglePass('loginPassword',this)">👁</button>
                        </div>
                        <div class="form-error">Vui lòng nhập mật khẩu</div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-lg" style="width:100%;">Đăng nhập</button>
                </form>
                <div class="auth-footer">
                    <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/account/register.jsp">Đăng ký ngay</a></p>
                </div>
            </div>
        </div>
    </main>
    <jsp:include page="../components/footer.jsp" />
    <script>
        function togglePass(id, btn) { var inp = document.getElementById(id); inp.type = inp.type === 'password' ? 'text' : 'password'; btn.textContent = inp.type === 'password' ? '👁' : '🙈'; }
        function handleLogin(e) {
            e.preventDefault(); var ok = true;
            var acc = document.getElementById('loginAccount').value.trim();
            var pass = document.getElementById('loginPassword').value;
            if (!acc) { document.getElementById('accountGroup').classList.add('error'); ok = false; } else document.getElementById('accountGroup').classList.remove('error');
            if (!pass) { document.getElementById('passGroup').classList.add('error'); ok = false; } else document.getElementById('passGroup').classList.remove('error');
            if (ok) window.location.href = '${pageContext.request.contextPath}/index.jsp?loginSuccess=true&phone=' + encodeURIComponent(acc);
            return false;
        }
        document.getElementById('loginAccount').addEventListener('input', function() { document.getElementById('accountGroup').classList.remove('error'); });
        document.getElementById('loginPassword').addEventListener('input', function() { document.getElementById('passGroup').classList.remove('error'); });
    </script>
</body>
</html>
