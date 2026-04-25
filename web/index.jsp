<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if ("true".equals(request.getParameter("loginSuccess"))) {
        session.setAttribute("loggedInUser", "Khách hàng");
    }
    if ("true".equals(request.getParameter("logout"))) {
        session.invalidate();
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    String loggedInUser = (String) session.getAttribute("loggedInUser");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nha Khoa Kvone - Kiến Tạo Nụ Cười</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
</head>
<body>
    <jsp:include page="components/header.jsp" />
    <main>
        <!-- SLIDESHOW -->
        <section class="slideshow" id="slideshow">
            <div class="slideshow-track" id="slideshowTrack">
                <div class="slide"><img src="https://images.unsplash.com/photo-1629909613654-28e377c37b09?w=1400&h=500&fit=crop" alt="Slide"><div class="slide-overlay"><div class="slide-content"><span class="slide-badge">Ưu đãi tháng 4</span><h2>Giảm 30% Tẩy Trắng Răng Laser</h2><p>Công nghệ Laser hiện đại, an toàn. Răng trắng sáng tự nhiên chỉ sau 1 buổi.</p></div></div></div>
                <div class="slide"><img src="https://images.unsplash.com/photo-1606811841689-23dfddce3e95?w=1400&h=500&fit=crop" alt="Slide"><div class="slide-overlay"><div class="slide-content"><span class="slide-badge">Hot Deal</span><h2>Niềng Răng Trả Góp 0% Lãi Suất</h2><p>Niềng răng mắc cài sứ hoặc Invisalign với chính sách trả góp không lãi suất.</p></div></div></div>
                <div class="slide"><img src="https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?w=1400&h=500&fit=crop" alt="Slide"><div class="slide-overlay"><div class="slide-content"><span class="slide-badge">Khuyến mãi</span><h2>Bọc Răng Sứ Chỉ Từ 2 Triệu/Răng</h2><p>Răng sứ Cercon, Zirconia cao cấp. Bảo hành lên đến 10 năm.</p></div></div></div>
                <div class="slide"><img src="https://images.unsplash.com/photo-1598256989800-fe5f95da9787?w=1400&h=500&fit=crop" alt="Slide"><div class="slide-overlay"><div class="slide-content"><span class="slide-badge">Mới</span><h2>Cấy Ghép Implant - Tặng CT Scan</h2><p>Trồng răng Implant Titanium bền vững trọn đời. Miễn phí CT Scan.</p></div></div></div>
            </div>
            <button class="slide-btn-prev" onclick="changeSlide(-1)">&#10094;</button>
            <button class="slide-btn-next" onclick="changeSlide(1)">&#10095;</button>
            <div class="slide-dots" id="slideDots"></div>
        </section>

        <!-- STATS -->
        <section class="stats-bar"><div class="container"><div class="stats-grid">
            <div class="stat-item"><div class="stat-number">15+</div><div class="stat-label">Năm kinh nghiệm</div></div>
            <div class="stat-item"><div class="stat-number">20</div><div class="stat-label">Bác sĩ chuyên khoa</div></div>
            <div class="stat-item"><div class="stat-number">10,000+</div><div class="stat-label">Khách hàng tin tưởng</div></div>
            <div class="stat-item"><div class="stat-number">98%</div><div class="stat-label">Tỉ lệ hài lòng</div></div>
        </div></div></section>

        <!-- ABOUT -->
        <section class="about-section" id="about"><div class="container"><div class="about-grid">
            <div class="about-image fade-in">
                <img src="https://images.unsplash.com/photo-1629909613654-28e377c37b09?w=600&h=400&fit=crop" alt="Phong kham">
                <div class="experience-badge"><div class="number">15+</div><div class="text">Năm kinh nghiệm</div></div>
            </div>
            <div class="about-content fade-in">
                <div class="subtitle">Về chúng tôi</div>
                <h2>Hệ thống nha khoa đạt chuẩn quốc tế</h2>
                <p>Nha Khoa Kvone tự hào là hệ thống nha khoa tiêu chuẩn quốc tế tại Đà Nẵng, phục vụ hơn 10,000 khách hàng.</p>
                <div class="about-features">
                    <div class="about-feature"><span class="check">✓</span> Bác sĩ chuyên khoa hàng đầu</div>
                    <div class="about-feature"><span class="check">✓</span> Công nghệ hiện đại nhất</div>
                    <div class="about-feature"><span class="check">✓</span> Vật liệu nhập khẩu chính hãng</div>
                    <div class="about-feature"><span class="check">✓</span> Bảo hành dài hạn</div>
                    <div class="about-feature"><span class="check">✓</span> Đặt lịch online 24/7</div>
                    <div class="about-feature"><span class="check">✓</span> Tư vấn miễn phí</div>
                </div>
                <a href="javascript:void(0)" onclick="showSystemModal()" class="btn btn-primary">Tìm hiểu thêm →</a>
            </div>
        </div></div></section>

        <!-- SERVICES -->
        <section class="services-section" id="services"><div class="container">
            <div class="section-title fade-in"><h2>Dịch vụ nổi bật</h2><p>Đa dạng dịch vụ nha khoa chất lượng cao</p></div>
            <div class="services-grid">
                <div class="service-card fade-in" onclick="showSystemModal()" style="cursor:pointer;"><div class="service-icon">💎</div><h3>Bọc Răng Sứ</h3><p>Răng sứ Cercon, Zirconia, Veneer mang lại nụ cười hoàn hảo.</p></div>
                <div class="service-card fade-in" onclick="showSystemModal()" style="cursor:pointer;"><div class="service-icon">😁</div><h3>Niềng Răng</h3><p>Chỉnh nha mắc cài kim loại, sứ hoặc Invisalign.</p></div>
                <div class="service-card fade-in" onclick="showSystemModal()" style="cursor:pointer;"><div class="service-icon">🦷</div><h3>Trồng Răng Implant</h3><p>Phục hồi răng mất bằng trụ Implant Titanium trọn đời.</p></div>
                <div class="service-card fade-in" onclick="showSystemModal()" style="cursor:pointer;"><div class="service-icon">✨</div><h3>Tẩy Trắng Răng</h3><p>Công nghệ Laser an toàn, trắng sáng chỉ sau 1 buổi.</p></div>
            </div>
            <div class="text-center mt-3"><a href="${pageContext.request.contextPath}/dat-lich.jsp#dichvu" class="btn btn-outline btn-lg">Xem tất cả dịch vụ & bảng giá →</a></div>
        </div></section>

        <!-- REVIEWS -->
        <section class="reviews-section" id="reviews"><div class="container">
            <div class="section-title fade-in"><h2>Đánh giá từ khách hàng</h2><p>Những chia sẻ chân thực từ khách hàng đã sử dụng dịch vụ</p></div>

            <% if (loggedInUser != null) { %>
            <div class="review-form-box fade-in">
                <h4>✍️ Viết đánh giá của bạn</h4>
                <div class="review-stars-input" id="reviewStarsInput">
                    <span class="star-input" onclick="setRating(1)">☆</span>
                    <span class="star-input" onclick="setRating(2)">☆</span>
                    <span class="star-input" onclick="setRating(3)">☆</span>
                    <span class="star-input" onclick="setRating(4)">☆</span>
                    <span class="star-input" onclick="setRating(5)">☆</span>
                </div>
                <textarea class="form-control" id="reviewText" placeholder="Chia sẻ trải nghiệm của bạn tại Nha Khoa Kvone..." rows="3"></textarea>
                <button class="btn btn-primary" onclick="submitReview()" style="margin-top:10px;">Gửi đánh giá</button>
            </div>
            <% } else { %>
            <div class="review-login-prompt fade-in">
                <p>🔒 Bạn cần <a href="${pageContext.request.contextPath}/account/login.jsp"><strong>đăng nhập</strong></a> để viết đánh giá</p>
            </div>
            <% } %>

            <div class="reviews-list">
                <div class="review-card fade-in">
                    <div class="review-header"><div class="review-avatar">NA</div><div class="review-info"><div class="review-name">Nguyễn An</div><div class="review-date">15/03/2024</div></div><div class="review-stars">★★★★★</div></div>
                    <p class="review-text">Phòng khám rất sạch sẽ, bác sĩ tư vấn nhiệt tình. Mình cạo vôi và trám răng rất nhanh gọn, không đau. Sẽ quay lại!</p>
                </div>
                <div class="review-card fade-in">
                    <div class="review-header"><div class="review-avatar">TL</div><div class="review-info"><div class="review-name">Trần Linh</div><div class="review-date">22/02/2024</div></div><div class="review-stars">★★★★☆</div></div>
                    <p class="review-text">Niềng răng ở đây gần 1 năm, kết quả rất ưng ý. Nhân viên thân thiện, đặt lịch online tiện lợi.</p>
                </div>
                <div class="review-card fade-in">
                    <div class="review-header"><div class="review-avatar">PM</div><div class="review-info"><div class="review-name">Phạm Minh</div><div class="review-date">10/01/2024</div></div><div class="review-stars">★★★★★</div></div>
                    <p class="review-text">Tẩy trắng răng Laser chỉ 45 phút mà răng trắng sáng hẳn. Giá cả hợp lý, không phát sinh phụ phí.</p>
                </div>
            </div>
        </div></section>
    </main>
    <jsp:include page="components/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/index.js"></script>
</body>
</html>
