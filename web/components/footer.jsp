<%-- components/footer.jsp v3 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<footer class="footer" id="contact">
    <div class="container">
        <div class="footer-grid">
            <div class="footer-brand">
                <div class="footer-logo">Nha Khoa <span>Kvone</span></div>
                <p>
                    Hệ thống Nha khoa Kvone - Nơi kiến tạo nụ cười rạng rỡ.
                    Đội ngũ bác sĩ giàu kinh nghiệm, trang thiết bị hiện đại
                    đạt chuẩn quốc tế.
                </p>
                <div class="footer-social">
                    <a href="#" title="Facebook">📘</a>
                    <a href="#" title="Zalo">💬</a>
                    <a href="#" title="YouTube">🎬</a>
                    <a href="#" title="Instagram">📷</a>
                </div>
            </div>

            <div class="footer-col">
                <h4>Dịch vụ</h4>
                <ul>
                    <li><a href="#">Bọc răng sứ thẩm mỹ</a></li>
                    <li><a href="#">Niềng răng chỉnh nha</a></li>
                    <li><a href="#">Trồng răng Implant</a></li>
                    <li><a href="#">Tẩy trắng răng</a></li>
                    <li><a href="#">Khám tổng quát</a></li>
                </ul>
            </div>

            <div class="footer-col">
                <h4>Liên kết</h4>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/dat-lich.jsp">Đặt lịch</a></li>
                    <li><a href="${pageContext.request.contextPath}/dat-lich.jsp">Dịch vụ</a></li>
                    <li><a href="${pageContext.request.contextPath}/dat-lich.jsp#bacsi">Bác sĩ</a></li>
                    <li><a href="${pageContext.request.contextPath}/account/login.jsp">Đăng nhập</a></li>
                </ul>
            </div>

            <div class="footer-col">
                <h4>Liên hệ</h4>
                <ul class="footer-contact">
                    <li>
                        <span class="icon">📍</span>
                        <span>48 Cao Thắng, Hải Châu,<br>TP. Đà Nẵng</span>
                    </li>
                    <li>
                        <span class="icon">📞</span>
                        <span>1900 1533</span>
                    </li>
                    <li>
                        <span class="icon">✉️</span>
                        <span>info@nhakhoakvone.vn</span>
                    </li>
                    <li>
                        <span class="icon">🕐</span>
                        <span>T2 - T7: 8:00 - 20:00<br>CN: 8:00 - 12:00</span>
                    </li>
                </ul>
            </div>
        </div>

        <div class="footer-bottom">
            &copy; 2026 Nha Khoa Kvone. All rights reserved. | ĐH SPKT Đà Nẵng.
        </div>
    </div>
</footer>
