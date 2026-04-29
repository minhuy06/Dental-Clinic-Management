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
                        <label>Số điện thoại <span style="color:#e74c3c">*</span></label>
                        <input type="text" class="form-control" id="loginAccount" placeholder="Nhập số điện thoại">
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
    <script src="${pageContext.request.contextPath}/assets/js/login.js"></script>
</body>
</html>
