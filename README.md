# Hệ thống Quản lý Phòng khám Nha khoa (Dental Clinic Management)

Đồ án cuối kỳ môn Lập trình Java Nâng cao, được xây dựng theo kiến trúc 3 lớp (3-Tier Architecture).

## Danh sách thành viên (Nhóm 5)
1. Võ Quốc Khánh - Vai trò: Frontend (Giao diện công khai dành cho bệnh nhân)
   - Trang chủ
      + Thiết kế Header (Menu điều hướng, Logo) và Footer chung để tái sử dụng cho các trang khác.
      + Thiết kế Banner quảng cáo chính.
      + Thiết kế Section giới thiệu ngắn về phòng khám và một số dịch vụ nổi bật.
      + Đảm bảo giao diện Responsive (không bị vỡ bố cục khi xem trên điện thoại).
   - Trang Bác sĩ, dịch vụ & đặt lịch
      + Thiết kế layout lưới hiển thị danh sách Bác sĩ (Ảnh, tên, chuyên khoa).
      + Thiết kế Bảng giá các dịch vụ nha khoa & giá rõ ràng.
      + Thiết kế Form đặt lịch khám:
         + Dropdown chọn Bác sĩ (dữ liệu động).
         + Chọn Ngày (Dùng JS chặn không cho chọn ngày trong quá khứ).
         + Chọn Giờ và ô text ghi chú.
   - Trang quản lý hồ sơ cá nhân
      + Layout có Sidebar bên trái chứa các menu: Thông tin cá nhân, lịch sử đặt khám, đổi mật khẩu.
      + Tab Thông tin: Form cho phép xem và cập nhật (Tên, Tiền sử bệnh, Nhóm máu).
      + Tab Đổi mật khẩu: Cần 3 ô input (Mật khẩu cũ, mới, nhập lại).
      + Tab Lịch sử: Bảng liệt kê các lịch hẹn đã đặt & hóa đơn kèm trạng thái (Chờ duyệt, Đã xác nhận...).
   - Trang đăng nhập & đăng kí tài khoản
      + Thiết kế Form đăng nhập (Số điện thoại, mật khẩu, xác nhận).
      + Thiết kế Form đăng ký (Họ tên, SĐT, ngày sinh, mật khẩu, xác nhận mật khẩu).
      + Xác thực bằng JavaScript: SĐT phải đủ 10 số, mật khẩu nhập lại phải trùng khớp.
      + Thiết kế màn hình popup nhập mã xác thực OTP (sau khi ấn đăng ký).

2. Nguyễn Anh Hiếu - Vai trò: Frontend (Giao diện nội bộ dành cho Bác sĩ & Lễ tân)
   * Giao diện Lễ tân
      - Trang quản lý Lịch hẹn
         + Bảng danh sách toàn bộ lịch hẹn kèm trạng thái.
         + Bộ lọc ở trên cùng: Lọc theo ngày khám, lọc theo Bác sĩ.
         + Thêm các nút thao tác: "Xác nhận", "Hủy lịch", "Đã đến".
         + Dùng thẻ màu để hiển thị các trạng thái khác nhau cho dễ nhìn.
      - Trang quản lý hồ sơ bệnh nhân & thu ngân
         + Thiết kế thanh tìm kiếm bệnh nhân theo tên hoặc SĐT.
         + Bảng danh sách bệnh nhân cơ bản. Click vào sẽ mở ra "Hồ sơ chi tiết"
            + Hiển thị form thông tin cá nhân (Họ tên, tuổi, SĐT, tiền sử bệnh, nhóm máu) - Cho phép lễ tân bấm nút "Chỉnh sửa" và "Lưu".
            + Bảng liệt kê các PhieuKham cũ (Ngày khám, bác sĩ khám, chẩn đoán).
               + Nút "Xem chi tiết" để hiển thị ra danh sách dịch vụ và thuốc của ca khám đó.
         + Thiết kế Nút "Tiếp đón Khách mới".
            + Xây dựng Form tạo Hồ sơ bệnh nhân mới
         + Thiết kế bảng hiển thị danh sách các hóa đơn Chưa thanh toán của bệnh nhân này.
         + Màn hình chi tiết hóa đơn (Liệt kê: Tên Dịch vụ x Đơn giá, Tên Thuốc x Số lượng x Đơn giá).
         + Hiển thị Tổng Tiền Cần Thanh Toán.
         + Thiết kế nút bấm hành động: "Xác nhận thanh toán".
   * Giao diện Bác sĩ
      - Trang Danh sách Hàng đợi
         + Bảng hiển thị danh sách bệnh nhân đang ở trạng thái "Đã đến" (Chờ gọi vào phòng).
         + Nút "Gọi vào khám" (Click vào sẽ chuyển hướng sang trang Khám Lâm Sàng).
      - Trang Khám Lâm sàng
         + Chia Layout 2 cột (hoặc 2 Tabs):
            + Cột 1: Thông tin bệnh nhân (Tên, tuổi, tiền sử bệnh) & Form ghi Chẩn đoán.
            + Cột 2: Nơi kê Dịch vụ và Đơn thuốc.
         + Khu vực Dịch vụ: Nút "Thêm DV", bảng liệt kê DV đang chọn kèm nhập "Vị trí răng".
         + Khu vực Thuốc: Nút "Thêm Thuốc", bảng liệt kê Thuốc kèm "Số lượng" và "Cách dùng".
         + Thiết kế góc dưới cùng hiển thị phần "Tổng tiền tạm tính".
         + Nút "Hoàn thành ca khám".

3. YKinh Mlo - Vai trò: Backend (Xác thực & Dữ liệu Public)
   - Xác thực đăng nhập, đăng xuất & phân quyền
      + Viết câu lệnh SQL/Hàm kiểm tra SĐT và Mật khẩu trong bảng TaiKhoan.
      + Tạo Session lưu thông tin User sau khi đăng nhập thành công.
      + Phân quyền (Role-based): Điều hướng đúng trang dựa trên VaiTro.
      + Xử lý đăng xuất (Điều hướng về trang chủ).
   - Quản lý tài khoản cá nhân
      + Viết hàm UPDATE cho phép đổi thông tin cá nhân (Tên, Tiền sử bệnh...).
      + Viết hàm đổi mật khẩu (Kiểm tra mật khẩu cũ khớp mới cho phép đổi mật khẩu mới).
   - Truy xuất dữ liệu bác sĩ & dịch vụ
      + Viết hàm SELECT lấy danh sách Bác sĩ (Kèm ảnh đại diện, chuyên khoa).
      + Viết hàm SELECT lấy Bảng giá Dịch vụ.
      + Đổ dữ liệu ra file JSP cho trang chủ.
4. Phạm Duy Hưng - Vai trò: Backend (Quản lý Lịch hẹn & Tiếp đón)
   - Quản lý đặt lịch & lịch hẹn
      + Nhận dữ liệu đặt lịch (Ngày, Giờ, Bác sĩ, Ghi chú) từ Form và INSERT vào bảng LichHen với Default là 'Chờ xác nhận'.
      + Viết hàm lọc danh sách Lịch hẹn.
      + Viết hàm cập nhật trạng thái lịch thành "Đã xác nhận"/"Đã đến"/"Đã hủy"/"Đã thanh toán".
   - Tiếp đón & hàng đợi
      + Xử lý khách đã đặt lịch: Cập nhật trạng thái LichHen thành "Đã đến" -> nhảy vào hàng đợi của Bác sĩ.
      + Xử lý khách vãng lai: Tạo nhanh dòng dữ liệu vào bảng BenhNhan (TaiKhoan_ID = NULL) và tạo LichHen ảo trạng thái "Đã đến".
   - Lịch sử khám bệnh
      + Viết hàm SELECT JOIN các bảng PhieuKham, ChiTietDichVu, ChiTietDonThuoc để xuất ra lịch sử khám cho một bệnh nhân cụ thể.
5. Võ Minh Huy - Vai trò: Trưởng nhóm
   - Xây dựng nền móng dự án
      + Chạy file Script tạo bảng và bơm dữ liệu mồi vào SQL Server.
      + Setup Base Project trên IDE NetBeans.
      + Viết class DBConnection.java để cả team dùng chung.
      + Đẩy project gốc lên GitHub.
   - Tích hợp Twilio API & luồng đăng ký
      + Tích hợp thư viện Twilio để gửi SMS.
      + Lưu mã OTP tạm thời.
      + Nếu đúng OTP: INSERT dữ liệu vào bảng TaiKhoan và BenhNhan.
   - Khám lâm sàng
      + INSERT vào bảng PhieuKham.
      + Lấy PhieuKham_ID vừa tạo, dùng vòng lặp INSERT vào ChiTietDichVu.
      + Tiếp tục dùng vòng lặp INSERT vào ChiTietDonThuoc.
      + Dùng Transaction (Commit/Rollback) để đảm bảo nếu lỗi giữa chừng thì không bị lưu rác.
   - Thu ngân & hóa đơn
      + Xử lý tính tổng tiền.
      + INSERT vào bảng HoaDon khi lễ tân bấm thanh toán.
      + Viết Trigger tự động chạy để update trạng thái.

## Công nghệ sử dụng
* Môi trường: Java EE (Servlet/JSP)
* Kiến trúc: 3-Tier (Presentation, Business Logic, Data Access)
* Cơ sở dữ liệu: SQL Server (tương tác qua JDBC)

## Hướng dẫn Cài đặt & Chạy dự án (Dành cho thành viên nhóm)

**Bước 1: Thiết lập Cơ sở dữ liệu**
1. Mở SQL Server Management Studio (SSMS).
2. Mở file `database/DbQuanLyNhaKhoa.sql` có trong thư mục này.
3. Chạy toàn bộ script để tạo Database và dữ liệu mẫu.

**Bước 2: Cấu hình kết nối JDBC**
1. Thống nhất sử dụng tài khoản SQL Server chung là:
   * **User:** `sa`
   * **Password:** `123456`.

**Bước 3: Chạy dự án**
1. Mở dự án.
2. Add server Apache Tomcat (phiên bản 9 hoặc 10).
3. Build và Run project.