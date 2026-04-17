<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lịch, Dịch vụ & Bác sĩ - Nha Khoa Kvone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/datlich.css">
</head>
<body>
    <jsp:include page="components/header.jsp" />
    <main>
        <!-- HERO -->
        <section class="page-hero">
            <div class="page-hero-bg"><img src="https://images.unsplash.com/photo-1629909613654-28e377c37b09?w=1400&h=400&fit=crop" alt="Nha khoa"></div>
            <div class="page-hero-overlay"></div>
            <div class="page-hero-content">
                <div class="hero-breadcrumb"><a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a><span class="separator">›</span><span>Đặt lịch, Dịch vụ & Bác sĩ</span></div>
                <h1>Chăm Sóc Răng Miệng Toàn Diện</h1>
                <p>Đặt lịch khám nhanh chóng, dịch vụ đa dạng, bác sĩ chuyên khoa hàng đầu</p>
            </div>
        </section>

        <!-- ==================== DAT LICH ==================== -->
        <section class="booking-section" id="datlich">
            <div class="container">
                <div class="section-title"><h2>Đặt Lịch Khám</h2></div>
                <p class="section-intro">Điền thông tin để đặt lịch hẹn. Phòng khám sẽ liên hệ xác nhận trong 30 phút.</p>
                <div class="booking-wrapper">
                    <div class="booking-info">
                        <h3>📍 Thông tin phòng khám</h3>
                        <ul>
                            <li>🏥 48 Cao Thắng, Hải Châu, Đà Nẵng</li>
                            <li>📞 1900 1533</li>
                            <li>🕐 T2 - T7: 8:00 - 17:00</li>
                            <li>🕐 Chủ nhật: 8:00 - 12:00</li>
                        </ul>
                        <div class="booking-note"><strong>Lưu ý:</strong> Vui lòng đến trước giờ hẹn 15 phút. Hủy lịch xin thông báo trước 2 giờ.</div>
                    </div>
                    <form class="booking-form" id="bookingForm" onsubmit="return handleBooking(event)">
                        <div class="form-group" id="doctorGroup">
                            <label>Chọn bác sĩ <span style="color:#e74c3c">*</span></label>
                            <select class="form-control" id="doctorSelect">
                                <option value="">-- Chọn bác sĩ --</option>
                                <option value="1">BS. Nguyễn Hải - Tổng quát</option>
                                <option value="2">BS. Trần Tâm - Chỉnh nha</option>
                                <option value="3">BS. Lê Quang - Phục hình răng</option>
                                <option value="4">BS. Phạm Hương - Thẩm mỹ</option>
                                <option value="5">BS. Hoàng Quân - Phẫu thuật miệng</option>
                                <option value="6">BS. Đặng Ngân - Nhổ răng</option>
                            </select>
                            <div class="form-error">Vui lòng chọn bác sĩ</div>
                        </div>
                        <div class="form-group" id="serviceGroup">
                            <label>Chọn dịch vụ <span style="color:#e74c3c">*</span></label>
                            <select class="form-control" id="serviceSelect" onchange="updatePrice()">
                                <option value="" data-price="0" data-time="">-- Chọn dịch vụ --</option>
                                <option value="1" data-price="100000" data-time="20 phút">Khám tổng quát</option>
                                <option value="2" data-price="200000" data-time="30 phút">Cạo vôi răng</option>
                                <option value="3" data-price="300000" data-time="30 phút">Trám răng Composite</option>
                                <option value="4" data-price="100000" data-time="15 phút">Nhổ răng sữa</option>
                                <option value="5" data-price="1000000" data-time="30 phút">Nhổ răng khôn mọc thẳng</option>
                                <option value="6" data-price="3000000" data-time="60 phút">Nhổ răng khôn mọc ngầm</option>
                                <option value="7" data-price="2500000" data-time="45 phút">Tẩy trắng răng Laser</option>
                                <option value="8" data-price="1500000" data-time="20 phút">Tẩy trắng răng tại nhà</option>
                                <option value="9" data-price="800000" data-time="45 phút">Lấy tủy răng cửa</option>
                                <option value="10" data-price="1500000" data-time="60 phút">Lấy tủy răng hàm</option>
                                <option value="11" data-price="2000000" data-time="45 phút/răng">Bọc răng sứ Titan</option>
                                <option value="12" data-price="5000000" data-time="45 phút/răng">Bọc răng sứ Cercon</option>
                                <option value="13" data-price="6000000" data-time="45 phút/răng">Bọc răng sứ Zirconia</option>
                                <option value="14" data-price="7000000" data-time="60 phút/răng">Mặt dán sứ Veneer</option>
                                <option value="15" data-price="15000000" data-time="60-90 phút">Cấy ghép Implant</option>
                                <option value="16" data-price="30000000" data-time="60-90 phút">Cấy ghép Implant cao cấp</option>
                                <option value="17" data-price="25000000" data-time="60 phút/lần">Niềng răng mắc cài kim loại</option>
                                <option value="18" data-price="35000000" data-time="60 phút/lần">Niềng răng mắc cài sứ</option>
                                <option value="19" data-price="80000000" data-time="45 phút/lần">Niềng răng Invisalign</option>
                                <option value="20" data-price="500000" data-time="30 phút">Điều trị viêm nha chu</option>
                            </select>
                            <div class="form-error">Vui lòng chọn dịch vụ</div>
                            <div class="price-display" id="priceDisplay" style="display:none;">
                                Giá dịch vụ: <strong id="priceValue"></strong> VNĐ &nbsp;|&nbsp; ⏱ Thời gian dự kiến: <strong id="timeValue"></strong>
                            </div>
                        </div>
                        <div class="form-group" id="dateGroup">
                            <label>Chọn ngày khám <span style="color:#e74c3c">*</span></label>
                            <input type="date" class="form-control" id="bookingDate">
                            <div class="form-error">Vui lòng chọn ngày khám</div>
                        </div>
                        <div class="form-group" id="timeGroup">
                            <label>Chọn giờ khám <span style="color:#e74c3c">*</span></label>
                            <div class="time-grid" id="timeGrid"></div>
                            <input type="hidden" id="bookingTime">
                            <div class="form-error">Vui lòng chọn giờ khám</div>
                        </div>
                        <div class="form-group">
                            <label>Ghi chú</label>
                            <textarea class="form-control" id="bookingNote" placeholder="Mô tả triệu chứng hoặc yêu cầu..." rows="3"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-lg" style="width:100%;">Đặt lịch hẹn</button>
                    </form>
                </div>
            </div>
        </section>

        <div class="section-divider"><span class="divider-icon">💎</span></div>

        <!-- ==================== DICH VU ==================== -->
        <section class="services-price-section" id="dichvu">
            <div class="container">
                <div class="section-title"><h2>Dịch Vụ</h2></div>
                <p class="section-intro">Giá niêm yết công khai, minh bạch. Không phát sinh chi phí ẩn.</p>
                <div class="price-table-wrapper">
                    <table class="price-table" id="priceTable">
                        <thead><tr><th>STT</th><th>Dịch vụ</th><th>⏱ Thời gian</th><th>Giá (VNĐ)</th></tr></thead>
                        <tbody>
                            <tr><td>1</td><td>Khám tổng quát<br><small style="color:var(--text-muted)">Kiểm tra răng miệng toàn diện</small></td><td class="time-cell">🕐 20 phút</td><td class="price">100,000</td></tr>
                            <tr><td>2</td><td>Cạo vôi răng<br><small style="color:var(--text-muted)">Loại bỏ mảng bám, vôi răng</small></td><td class="time-cell">🕐 30 phút</td><td class="price">200,000</td></tr>
                            <tr><td>3</td><td>Trám răng Composite<br><small style="color:var(--text-muted)">Trám thẩm mỹ bằng vật liệu Composite</small></td><td class="time-cell">🕐 30 phút</td><td class="price">300,000</td></tr>
                            <tr><td>4</td><td>Nhổ răng sữa<br><small style="color:var(--text-muted)">Nhổ răng sữa cho trẻ em</small></td><td class="time-cell">🕐 15 phút</td><td class="price">100,000</td></tr>
                            <tr><td>5</td><td>Nhổ răng khôn mọc thẳng<br><small style="color:var(--text-muted)">Nhổ răng khôn không phẫu thuật</small></td><td class="time-cell">🕐 30 phút</td><td class="price">1,000,000</td></tr>
                            <tr><td>6</td><td>Nhổ răng khôn mọc ngầm<br><small style="color:var(--text-muted)">Tiểu phẫu nhổ răng khôn phức tạp</small></td><td class="time-cell">🕐 60 phút</td><td class="price">3,000,000</td></tr>
                            <tr><td>7</td><td>Tẩy trắng răng Laser<br><small style="color:var(--text-muted)">Tẩy trắng bằng công nghệ Laser</small></td><td class="time-cell">🕐 45 phút</td><td class="price">2,500,000</td></tr>
                            <tr><td>8</td><td>Tẩy trắng răng tại nhà<br><small style="color:var(--text-muted)">Máng tẩy trắng sử dụng tại nhà</small></td><td class="time-cell">🕐 20 phút</td><td class="price">1,500,000</td></tr>
                            <tr><td>9</td><td>Lấy tủy răng cửa<br><small style="color:var(--text-muted)">Điều trị tủy răng cửa bị viêm</small></td><td class="time-cell">🕐 45 phút</td><td class="price">800,000</td></tr>
                            <tr><td>10</td><td>Lấy tủy răng hàm<br><small style="color:var(--text-muted)">Điều trị tủy răng hàm nhiều chân</small></td><td class="time-cell">🕐 60 phút</td><td class="price">1,500,000</td></tr>
                            <tr class="hidden-row"><td>11</td><td>Bọc răng sứ Titan<br><small style="color:var(--text-muted)">Răng sứ kim loại Titan tiêu chuẩn</small></td><td class="time-cell">🕐 45 phút/răng</td><td class="price">2,000,000</td></tr>
                            <tr class="hidden-row"><td>12</td><td>Bọc răng sứ Cercon<br><small style="color:var(--text-muted)">Răng sứ Cercon cao cấp Đức</small></td><td class="time-cell">🕐 45 phút/răng</td><td class="price">5,000,000</td></tr>
                            <tr class="hidden-row"><td>13</td><td>Bọc răng sứ Zirconia<br><small style="color:var(--text-muted)">Răng sứ Zirconia nguyên khối</small></td><td class="time-cell">🕐 45 phút/răng</td><td class="price">6,000,000</td></tr>
                            <tr class="hidden-row"><td>14</td><td>Mặt dán sứ Veneer<br><small style="color:var(--text-muted)">Dán sứ siêu mỏng thẩm mỹ</small></td><td class="time-cell">🕐 60 phút/răng</td><td class="price">7,000,000</td></tr>
                            <tr class="hidden-row"><td>15</td><td>Cấy ghép Implant tiêu chuẩn<br><small style="color:var(--text-muted)">Trụ Implant Titanium tiêu chuẩn</small></td><td class="time-cell">🕐 60-90 phút</td><td class="price">15,000,000</td></tr>
                            <tr class="hidden-row"><td>16</td><td>Cấy ghép Implant cao cấp<br><small style="color:var(--text-muted)">Trụ Implant cao cấp Châu Âu</small></td><td class="time-cell">🕐 60-90 phút</td><td class="price">30,000,000</td></tr>
                            <tr class="hidden-row"><td>17</td><td>Niềng răng mắc cài kim loại<br><small style="color:var(--text-muted)">Chỉnh nha mắc cài kim loại truyền thống</small></td><td class="time-cell">🕐 60 phút/lần</td><td class="price">25,000,000</td></tr>
                            <tr class="hidden-row"><td>18</td><td>Niềng răng mắc cài sứ<br><small style="color:var(--text-muted)">Chỉnh nha mắc cài sứ thẩm mỹ</small></td><td class="time-cell">🕐 60 phút/lần</td><td class="price">35,000,000</td></tr>
                            <tr class="hidden-row"><td>19</td><td>Niềng răng Invisalign<br><small style="color:var(--text-muted)">Khay trong suốt chỉnh nha hiện đại</small></td><td class="time-cell">🕐 45 phút/lần</td><td class="price">80,000,000</td></tr>
                            <tr class="hidden-row"><td>20</td><td>Điều trị viêm nha chu<br><small style="color:var(--text-muted)">Điều trị viêm nướu, viêm nha chu</small></td><td class="time-cell">🕐 30 phút</td><td class="price">500,000</td></tr>
                            <tr class="hidden-row"><td>21</td><td>Chụp X-quang răng<br><small style="color:var(--text-muted)">Chụp phim X-quang kỹ thuật số</small></td><td class="time-cell">🕐 10 phút</td><td class="price">150,000</td></tr>
                            <tr class="hidden-row"><td>22</td><td>Chụp CT Cone Beam<br><small style="color:var(--text-muted)">Chụp CT 3D toàn hàm</small></td><td class="time-cell">🕐 15 phút</td><td class="price">500,000</td></tr>
                            <tr class="hidden-row"><td>23</td><td>Trám răng thẩm mỹ<br><small style="color:var(--text-muted)">Trám thẩm mỹ phục hồi hình dáng</small></td><td class="time-cell">🕐 30 phút</td><td class="price">500,000</td></tr>
                            <tr class="hidden-row"><td>24</td><td>Điều trị tủy răng trẻ em<br><small style="color:var(--text-muted)">Lấy tủy răng sữa cho trẻ</small></td><td class="time-cell">🕐 30 phút</td><td class="price">600,000</td></tr>
                            <tr class="hidden-row"><td>25</td><td>Nhổ răng vĩnh viễn thường<br><small style="color:var(--text-muted)">Nhổ răng lung lay, răng sâu nặng</small></td><td class="time-cell">🕐 20 phút</td><td class="price">500,000</td></tr>
                            <tr class="hidden-row"><td>26</td><td>Phẫu thuật cắt lợi trùm<br><small style="color:var(--text-muted)">Cắt lợi trùm răng khôn</small></td><td class="time-cell">🕐 30 phút</td><td class="price">800,000</td></tr>
                            <tr class="hidden-row"><td>27</td><td>Ghép xương hàm<br><small style="color:var(--text-muted)">Ghép xương chuẩn bị cấy Implant</small></td><td class="time-cell">🕐 60 phút</td><td class="price">5,000,000</td></tr>
                            <tr class="hidden-row"><td>28</td><td>Nâng xoang hàm<br><small style="color:var(--text-muted)">Nâng xoang hỗ trợ Implant hàm trên</small></td><td class="time-cell">🕐 90 phút</td><td class="price">8,000,000</td></tr>
                            <tr class="hidden-row"><td>29</td><td>Răng giả tháo lắp nhựa<br><small style="color:var(--text-muted)">Hàm giả nhựa dẻo tháo lắp</small></td><td class="time-cell">🕐 45 phút/lần</td><td class="price">2,000,000</td></tr>
                            <tr class="hidden-row"><td>30</td><td>Răng giả tháo lắp kim loại<br><small style="color:var(--text-muted)">Hàm giả khung kim loại bền chắc</small></td><td class="time-cell">🕐 60 phút/lần</td><td class="price">4,000,000</td></tr>
                            <tr class="hidden-row"><td>31</td><td>Cầu răng sứ (3 đơn vị)<br><small style="color:var(--text-muted)">Phục hình cầu răng sứ 3 đơn vị</small></td><td class="time-cell">🕐 60 phút/lần</td><td class="price">9,000,000</td></tr>
                            <tr class="hidden-row"><td>32</td><td>Điều trị cười hở lợi<br><small style="color:var(--text-muted)">Phẫu thuật chỉnh hình nướu</small></td><td class="time-cell">🕐 45 phút</td><td class="price">3,000,000</td></tr>
                            <tr class="hidden-row"><td>33</td><td>Tạo hình nướu thẩm mỹ<br><small style="color:var(--text-muted)">Chỉnh sửa đường viền nướu</small></td><td class="time-cell">🕐 30 phút</td><td class="price">2,500,000</td></tr>
                            <tr class="hidden-row"><td>34</td><td>Điều trị đau khớp thái dương<br><small style="color:var(--text-muted)">Điều trị rối loạn khớp hàm</small></td><td class="time-cell">🕐 30 phút</td><td class="price">1,500,000</td></tr>
                            <tr class="hidden-row"><td>35</td><td>Máng chống nghiến răng<br><small style="color:var(--text-muted)">Máng bảo vệ răng khi ngủ</small></td><td class="time-cell">🕐 30 phút</td><td class="price">1,200,000</td></tr>
                            <tr class="hidden-row"><td>36</td><td>Trám răng sữa trẻ em<br><small style="color:var(--text-muted)">Trám răng sữa bị sâu cho bé</small></td><td class="time-cell">🕐 15 phút</td><td class="price">150,000</td></tr>
                            <tr class="hidden-row"><td>37</td><td>Bôi Fluoride phòng sâu<br><small style="color:var(--text-muted)">Bôi gel Fluoride ngừa sâu răng</small></td><td class="time-cell">🕐 15 phút</td><td class="price">200,000</td></tr>
                            <tr class="hidden-row"><td>38</td><td>Trám bít hố rãnh<br><small style="color:var(--text-muted)">Bít hố rãnh phòng sâu răng hàm</small></td><td class="time-cell">🕐 20 phút</td><td class="price">250,000</td></tr>
                            <tr class="hidden-row"><td>39</td><td>Khám nha khoa định kỳ trẻ em<br><small style="color:var(--text-muted)">Khám kiểm tra răng miệng cho bé</small></td><td class="time-cell">🕐 15 phút</td><td class="price">80,000</td></tr>
                            <tr class="hidden-row"><td>40</td><td>Tư vấn thiết kế nụ cười DSD<br><small style="color:var(--text-muted)">Digital Smile Design - thiết kế nụ cười số</small></td><td class="time-cell">🕐 30 phút</td><td class="price">1,000,000</td></tr>
                        </tbody>
                    </table>
                </div>
                <div class="text-center mt-3">
                    <button class="btn btn-outline" id="showMoreBtn" onclick="toggleServices()">Xem thêm dịch vụ ↓</button>
                </div>
            </div>
        </section>

        <div class="section-divider on-gray"><span class="divider-icon">✨</span></div>

        <!-- ==================== BAC SI ==================== -->
        <section class="doctors-section" id="bacsi">
            <div class="container">
                <div class="section-title"><h2>Đội Ngũ Bác Sĩ</h2></div>
                <p class="section-intro">Các bác sĩ được đào tạo chuyên sâu, nhiều năm kinh nghiệm.</p>
                <div class="doctors-grid">
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Nguyễn Hải</h3><span class="doctor-specialty">Tổng quát</span><span class="doctor-degree">CKI</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Trần Tâm</h3><span class="doctor-specialty">Chỉnh nha</span><span class="doctor-degree">Thạc sĩ</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Lê Quang</h3><span class="doctor-specialty">Phục hình răng</span><span class="doctor-degree">Tiến sĩ</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1614608682850-e0d6ed316d47?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Phạm Hương</h3><span class="doctor-specialty">Thẩm mỹ</span><span class="doctor-degree">CKI</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1537368910025-700350fe46c7?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Hoàng Quân</h3><span class="doctor-specialty">Phẫu thuật miệng</span><span class="doctor-degree">CKII</span></div></div>
                    <div class="doctor-card"><div class="doctor-img"><img src="https://images.unsplash.com/photo-1582750433449-648ed127bb54?w=300&h=300&fit=crop" alt="BS"></div><div class="doctor-info"><h3>BS. Đặng Ngân</h3><span class="doctor-specialty">Nhổ răng</span><span class="doctor-degree">Cử nhân</span></div></div>
                </div>
            </div>
        </section>
    </main>
    <jsp:include page="components/footer.jsp" />

    <script>
    // Chan ngay qua khu
    (function(){var t=new Date().toISOString().split('T')[0];document.getElementById('bookingDate').setAttribute('min',t);})();

    // Tao luoi gio - thay doi theo ngay
    var weekdayTimes=['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','13:00','13:30','14:00','14:30','15:00','15:30','16:00','16:30'];
    var sundayTimes=['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30'];

    function buildTimeGrid(times){
        var grid=document.getElementById('timeGrid');
        grid.innerHTML='';
        document.getElementById('bookingTime').value='';
        times.forEach(function(t){
            var btn=document.createElement('div');
            btn.className='time-slot';
            btn.textContent=t;
            btn.onclick=function(){
                document.querySelectorAll('.time-slot').forEach(function(s){s.classList.remove('selected');});
                this.classList.add('selected');
                document.getElementById('bookingTime').value=t;
                document.getElementById('timeGroup').classList.remove('error');
            };
            grid.appendChild(btn);
        });
    }

    buildTimeGrid(weekdayTimes);

    document.getElementById('bookingDate').addEventListener('change',function(){
        var selected=new Date(this.value);
        var day=selected.getDay();
        if(day===0){buildTimeGrid(sundayTimes);}
        else{buildTimeGrid(weekdayTimes);}
        document.getElementById('dateGroup').classList.remove('error');
    });

    // Hien thi gia + thoi gian dich vu
    function updatePrice(){
        var sel=document.getElementById('serviceSelect');
        var opt=sel.options[sel.selectedIndex];
        var price=opt.getAttribute('data-price');
        var time=opt.getAttribute('data-time');
        var display=document.getElementById('priceDisplay');
        if(price&&price!=='0'){
            document.getElementById('priceValue').textContent=parseInt(price).toLocaleString('vi-VN');
            document.getElementById('timeValue').textContent=time;
            display.style.display='block';
        }else{display.style.display='none';}
        document.getElementById('serviceGroup').classList.remove('error');
    }

    // Toggle dich vu
    var servicesExpanded=false;
    function toggleServices(){
        servicesExpanded=!servicesExpanded;
        document.querySelectorAll('.hidden-row').forEach(function(r){r.style.display=servicesExpanded?'table-row':'none';});
        document.getElementById('showMoreBtn').textContent=servicesExpanded?'Thu gọn ↑':'Xem thêm dịch vụ ↓';
    }

    // Booking validation
    function handleBooking(e){
        e.preventDefault();var ok=true;
        if(!document.getElementById('doctorSelect').value){document.getElementById('doctorGroup').classList.add('error');ok=false;}else{document.getElementById('doctorGroup').classList.remove('error');}
        if(!document.getElementById('serviceSelect').value){document.getElementById('serviceGroup').classList.add('error');ok=false;}else{document.getElementById('serviceGroup').classList.remove('error');}
        if(!document.getElementById('bookingDate').value){document.getElementById('dateGroup').classList.add('error');ok=false;}else{document.getElementById('dateGroup').classList.remove('error');}
        if(!document.getElementById('bookingTime').value){document.getElementById('timeGroup').classList.add('error');ok=false;}else{document.getElementById('timeGroup').classList.remove('error');}
        if(ok){
            alert('Đặt lịch thành công! Phòng khám sẽ liên hệ xác nhận.');
            document.getElementById('bookingForm').reset();
            document.getElementById('priceDisplay').style.display='none';
            document.querySelectorAll('.time-slot').forEach(function(s){s.classList.remove('selected');});
        }
        return false;
    }

    document.getElementById('doctorSelect').addEventListener('change',function(){document.getElementById('doctorGroup').classList.remove('error');});
    </script>
</body>
</html>
