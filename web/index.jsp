<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

    <!-- HEADER -->
    <jsp:include page="components/header.jsp" />

    <main>

        <!-- ==================== SLIDESHOW BANNER ==================== -->
        <section class="slideshow" id="slideshow">
            <div class="slideshow-track" id="slideshowTrack">

                <!-- Slide 1 -->
                <div class="slide">
                    <img src="https://images.unsplash.com/photo-1629909613654-28e377c37b09?w=1400&h=500&fit=crop"
                         alt="Phong kham nha khoa">
                    <div class="slide-overlay">
                        <div class="slide-content">
                            <span class="slide-badge">Ưu đãi tháng 4</span>
                            <h2>Giảm 30% Tẩy Trắng Răng Laser</h2>
                            <p>Công nghệ Laser hiện đại, an toàn. Răng trắng sáng tự nhiên chỉ sau 1 buổi điều trị.</p>
                            <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="btn btn-accent btn-lg">
                                Xem chi tiết
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Slide 2 -->
                <div class="slide">
                    <img src="https://images.unsplash.com/photo-1606811841689-23dfddce3e95?w=1400&h=500&fit=crop"
                         alt="Bac si nha khoa">
                    <div class="slide-overlay">
                        <div class="slide-content">
                            <span class="slide-badge">Hot Deal</span>
                            <h2>Niềng Răng Trả Góp 0% Lãi Suất</h2>
                            <p>Niềng răng mắc cài sứ hoặc Invisalign với chính sách trả góp hấp dẫn, không lãi suất.</p>
                            <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="btn btn-accent btn-lg">
                                Tư vấn ngay
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Slide 3 -->
                <div class="slide">
                    <img src="https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?w=1400&h=500&fit=crop"
                         alt="Rang su tham my">
                    <div class="slide-overlay">
                        <div class="slide-content">
                            <span class="slide-badge">Khuyến mãi</span>
                            <h2>Bọc Răng Sứ Chỉ Từ 2 Triệu/Răng</h2>
                            <p>Răng sứ Cercon, Zirconia cao cấp nhập khẩu. Bảo hành lên đến 10 năm.</p>
                            <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="btn btn-accent btn-lg">
                                Đặt lịch ngay
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Slide 4 -->
                <div class="slide">
                    <img src="https://images.unsplash.com/photo-1598256989800-fe5f95da9787?w=1400&h=500&fit=crop"
                         alt="Implant nha khoa">
                    <div class="slide-overlay">
                        <div class="slide-content">
                            <span class="slide-badge">Mới</span>
                            <h2>Cấy Ghép Implant - Tặng CT Scan Miễn Phí</h2>
                            <p>Trồng răng Implant Titanium bền vững trọn đời. Miễn phí chụp CT Scan khi đặt lịch online.</p>
                            <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="btn btn-accent btn-lg">
                                Đặt lịch khám
                            </a>
                        </div>
                    </div>
                </div>

            </div>

            <!-- Nut dieu huong -->
            <button class="slide-btn-prev" onclick="changeSlide(-1)">&#10094;</button>
            <button class="slide-btn-next" onclick="changeSlide(1)">&#10095;</button>

            <!-- Cham indicator -->
            <div class="slide-dots" id="slideDots"></div>
        </section>

        <!-- ==================== STATS BAR ==================== -->
        <section class="stats-bar">
            <div class="container">
                <div class="stats-grid">
                    <div class="stat-item">
                        <div class="stat-number">15+</div>
                        <div class="stat-label">Năm kinh nghiệm</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">20</div>
                        <div class="stat-label">Bác sĩ chuyên khoa</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">10,000+</div>
                        <div class="stat-label">Khách hàng tin tưởng</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">98%</div>
                        <div class="stat-label">Tỉ lệ hài lòng</div>
                    </div>
                </div>
            </div>
        </section>

        <!-- ==================== GIOI THIEU ==================== -->
        <section class="about-section" id="about">
            <div class="container">
                <div class="about-grid">
                    <div class="about-image fade-in">
                        <img src="https://images.unsplash.com/photo-1629909613654-28e377c37b09?w=600&h=400&fit=crop"
                             alt="Phong kham hien dai">
                        <div class="experience-badge">
                            <div class="number">15+</div>
                            <div class="text">Năm kinh nghiệm</div>
                        </div>
                    </div>
                    <div class="about-content fade-in">
                        <div class="subtitle">Về chúng tôi</div>
                        <h2>Hệ Thống Nha Khoa Đạt Chuẩn Quốc Tế</h2>
                        <p>
                            Nha Khoa Kvone tự hào là hệ thống nha khoa tiêu chuẩn quốc tế
                            tại Đà Nẵng. Với hơn 15 năm hoạt động, phục vụ hơn 10,000 khách
                            hàng với sứ mệnh mang lại nụ cười tự tin và sức khỏe răng miệng
                            toàn diện.
                        </p>
                        <div class="about-features">
                            <div class="about-feature"><span class="check">✓</span> Bác sĩ chuyên khoa hàng đầu</div>
                            <div class="about-feature"><span class="check">✓</span> Công nghệ hiện đại nhất</div>
                            <div class="about-feature"><span class="check">✓</span> Vật liệu nhập khẩu chính hãng</div>
                            <div class="about-feature"><span class="check">✓</span> Bảo hành dài hạn</div>
                            <div class="about-feature"><span class="check">✓</span> Đặt lịch online 24/7</div>
                            <div class="about-feature"><span class="check">✓</span> Tư vấn miễn phí</div>
                        </div>
                        <a href="#" class="btn btn-primary">Tìm hiểu thêm →</a>
                    </div>
                </div>
            </div>
        </section>

        <!-- ==================== 4 DICH VU ==================== -->
        <section class="services-section" id="services">
            <div class="container">
                <div class="section-title fade-in">
                    <h2>Dịch Vụ Nổi Bật</h2>
                    <p>Đa dạng dịch vụ nha khoa chất lượng cao với công nghệ tiên tiến</p>
                </div>
                <div class="services-grid">
                    <div class="service-card fade-in">
                        <div class="service-icon">💎</div>
                        <h3>Bọc Răng Sứ</h3>
                        <p>Răng sứ Cercon, Zirconia, Veneer mang lại nụ cười hoàn hảo tự nhiên.</p>
                        <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="link-more">Xem thêm →</a>
                    </div>
                    <div class="service-card fade-in">
                        <div class="service-icon">😁</div>
                        <h3>Niềng Răng</h3>
                        <p>Chỉnh nha mắc cài kim loại, sứ hoặc khay trong suốt Invisalign.</p>
                        <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="link-more">Xem thêm →</a>
                    </div>
                    <div class="service-card fade-in">
                        <div class="service-icon">🦷</div>
                        <h3>Trồng Răng Implant</h3>
                        <p>Phục hồi răng mất bằng trụ Implant Titanium bền vững trọn đời.</p>
                        <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="link-more">Xem thêm →</a>
                    </div>
                    <div class="service-card fade-in">
                        <div class="service-icon">✨</div>
                        <h3>Tẩy Trắng Răng</h3>
                        <p>Công nghệ Laser an toàn, răng trắng sáng tự nhiên chỉ sau 1 buổi.</p>
                        <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="link-more">Xem thêm →</a>
                    </div>
                </div>
                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="btn btn-outline btn-lg">
                        Xem tất cả dịch vụ & bảng giá →
                    </a>
                </div>
            </div>
        </section>

        <!-- ==================== CTA ==================== -->
        <section class="cta-section">
            <div class="container">
                <h2>Bạn cần tư vấn? Hãy liên hệ ngay!</h2>
                <p>Đội ngũ bác sĩ tư vấn miễn phí. Đặt lịch online nhanh chóng, tiện lợi.</p>
                <div class="cta-buttons">
                    <a href="${pageContext.request.contextPath}/dat-lich.jsp" class="btn btn-accent btn-lg">
                        Đặt lịch khám ngay
                    </a>
                    <a href="tel:19001533" class="btn btn-outline-white btn-lg">
                        📞 Gọi 1900 1533
                    </a>
                </div>
            </div>
        </section>

    </main>

    <!-- FOOTER -->
    <jsp:include page="components/footer.jsp" />

    <!-- JavaScript -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>
