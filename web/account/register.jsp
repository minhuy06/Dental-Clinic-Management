<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Nha Khoa KVONE</title>
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
                            <label>Email <span style="color:#e74c3c">*</span></label>
                            <input type="email" class="form-control" id="regEmail" placeholder="VD: abc@gmail.com">
                            <div class="form-error">Email không hợp lệ</div>
                        </div>
                    </div>
                    <div class="form-group" id="contactError" style="display:none;">
                        <div class="form-error" style="display:block;">Vui lòng nhập địa chỉ Email để nhận mã OTP</div>
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

    <div class="system-modal-overlay" id="otpModal">
        <div class="system-modal" style="max-width:380px;">
            <span class="sys-icon">✉️</span>
            <div class="sys-title">Xác thực OTP</div>
            <div class="sys-msg">Nhập mã 6 số đã gửi đến Email của bạn</div>
            <div class="otp-inputs" id="otpInputs">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,0)" onkeydown="otpBack(event,0)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,1)" onkeydown="otpBack(event,1)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,2)" onkeydown="otpBack(event,2)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,3)" onkeydown="otpBack(event,3)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,4)" onkeydown="otpBack(event,4)">
                <input type="text" maxlength="1" class="otp-box" oninput="otpNext(this,5)" onkeydown="otpBack(event,5)">
            </div>
            <div class="otp-timer" id="otpTimer">Gửi lại mã sau <span id="otpCountdown">60</span>s</div>
            <button id="btnConfirmOtp" class="sys-confirm btn btn-primary" onclick="verifyOtp()" style="margin-top:16px; width: 100%;">Xác nhận</button>
        </div>
    </div>

    <jsp:include page="../components/footer.jsp" />
    <script>
        // Cung cấp biến môi trường cho Javascript để chuyển trang mượt mà
        window.CONTEXT_PATH = "${pageContext.request.contextPath}";
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/register.js"></script>
</body>
</html>