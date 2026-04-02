# Hệ thống Quản lý Phòng khám Nha khoa (Dental Clinic Management)

Đồ án cuối kỳ môn Lập trình Java Nâng cao, được xây dựng theo kiến trúc 3 lớp (3-Tier Architecture).

## Danh sách thành viên (Nhóm 5)
1. Võ Quốc Khánh - Vai trò: Frontend (Giao diện công khai dành cho bệnh nhân)
   - Trang chủ
   - Trang Đội ngũ Bác sĩ (Hiển thị danh sách các bác sĩ kèm chuyên khoa, kinh nghiệm (Dữ liệu lấy từ DB))
   - Trang Bảng giá dịch vụ (Liệt kê các dịch vụ nha khoa và giá tiền niêm yết (Dữ liệu lấy từ DB))
   - Trang Đăng nhập & Đăng kí tài khoảng (Thiết kế Modal nhập Mã OTP có đồng hồ đếm ngược 60s) & Mục đăng xuất
   - Trang Đặt lịch khám (Form chọn Ngày, giờ, chọn Bác sĩ và ghi chú tình trạng bệnh)
   - Trang Quản lý tài khoảng:
      + Cập nhật thông tin cá nhân & Đổi mật khẩu
      + Lịch sử khám bệnh & Hóa đơn
      + Quản lý Lịch hẹn (Xem lịch hẹn sắp tới, có nút bấm "Hủy lịch")

2. Nguyễn Anh Hiếu - Vai trò: Frontend (Giao diện nội bộ dành cho Bác sĩ & Lễ tân)
   * Giao diện Lễ tân
      - Trang Quản lý lịch hẹn (Hiển thị danh sách lịch khách đặt online. Có các nút thao tác: "Duyệt" (gọi điện xong xác nhận), "Hủy", và "Tạo lịch hẹn mới thủ công")
      - Trang Tiếp đón & Danh sách Bệnh nhân
      - Trang Tạo Hồ sơ Bệnh nhân: Form nhập liệu chi tiết (Họ tên, ngày sinh, tiền sử bệnh, dị ứng...) dành cho khách đến trực tiếp chưa có tài khoản web
      - Trang Thu ngân: Hiển thị danh sách các ca khám vừa hoàn thành. Bấm vào sẽ ra chi tiết Hóa đơn (tiền dịch vụ + tiền thuốc) để Lễ tân thu tiền và bấm nút "Xác nhận thanh toán"
   * Giao diện Bác sĩ
      - Trang Danh sách Hàng đợi: Hiển thị danh sách bệnh nhân đang ngồi chờ ngoài cửa để Bác sĩ bấm nút "Gọi vào khám"
      - Trang Khám Lâm sàng
         + Phần trên: Hiển thị thông tin hành chính và tiền sử bệnh của khách. Có nút bấm "Xem lịch sử khám bệnh cũ".
         + Phần giữa: Ô nhập liệu Chẩn đoán ban đầu.
         + Phần chỉ định: Dropdown list chọn Dịch vụ ➔ Bấm nút "Thêm" ➔ Dịch vụ rơi xuống một cái bảng bên dưới, có thể thêm nhiều dịch vụ.
         + Phần kê đơn: Nút tick "Có kê thuốc" ➔ Hiện ra khu vực chọn Thuốc và ghi chú liều dùng.
         + Phần dưới cùng: Nút "Hoàn thành ca khám".

3. YKinh Mlo - Vai trò: Backend (Xác thực & Dữ liệu Public)
   - Code kiểm tra Đăng nhập (Tạo Session lưu thông tin và phân quyền VaiTro).
   - Code logic Đăng xuất (Xóa Session và điều hướng về Trang chủ).
   - Viết các hàm cho phép Bệnh nhân cập nhật thông tin cá nhân và đổi mật khẩu.
   - Viết các câu lệnh SQL SELECT để truy xuất lấy danh sách Bác sĩ và lấy Bảng giá rồi trả dữ liệu ra cho file JSP hiển thị lên Trang chủ.
4. Phạm Duy Hưng - Vai trò: Backend (Quản lý Lịch hẹn & Tiếp đón)
   - Đặt lịch Online: Nhận dữ liệu từ form đặt lịch của web, lưu vào bảng LichHen với trạng thái mặc định là "Chờ duyệt".
   - Lễ tân - Xử lý lịch: Viết các hàm cho phép Lễ tân cập nhật bảng LichHen (Duyệt thành "Đã xác nhận", hoặc "Đã hủy" - Chỉ update trạng thái chứ không dùng lệnh DELETE).
   - Lễ tân - Tiếp đón: Code tạo nhanh Hồ sơ Bệnh nhân mới (khi khách đến thẳng phòng khám). Viết hàm chuyển trạng thái lịch hẹn thành "Đã đến" (lúc này tên bệnh nhân sẽ nhảy sang màn hình chờ của Bác sĩ).
   - Truy xuất Lịch sử: Viết hàm để lấy ra lịch sử khám bệnh cho bệnh nhân xem lại.
5. Võ Minh Huy - Vai trò: Trưởng nhóm
   - Khởi tạo SQL Server, chạy Script tạo Bảng. Bơm sẵn dữ liệu mồi để thay thế trang Admin.
   - Setup Base Project, đẩy lên GitHub. Quản lý Merge của team.
   - Tích hợp API bên thứ 3 (Twilio) để gửi mã OTP về số điện thoại lúc đăng ký.
      + Xử lý Logic nhập mã OTP, nhận dữ liệu đăng ký từ form và lưu xuống Db.
   - Luồng Lâm sàng: Code logic khi Bác sĩ ấn "Hoàn thành ca khám".
   - Luồng Thu ngân: Viết thuật toán gom tổng tiền từ Dịch vụ và Đơn thuốc để lưu vào Db. Cập nhật trạng thái thành "Đã thanh toán".

## Công nghệ sử dụng
* Môi trường: Java EE (Servlet/JSP)
* Kiến trúc: 3-Tier (Presentation, Business Logic, Data Access)
* Cơ sở dữ liệu: SQL Server (tương tác qua JDBC)

## Hướng dẫn Cài đặt & Chạy dự án (Dành cho thành viên nhóm)

**Bước 1: Thiết lập Cơ sở dữ liệu**
1. Mở SQL Server Management Studio (SSMS).
2. Mở file `database/clinic_db.sql` có trong thư mục này.
3. Chạy toàn bộ script để tạo Database và dữ liệu mẫu.

**Bước 2: Cấu hình kết nối JDBC**
1. Thống nhất sử dụng tài khoản SQL Server chung là:
   * **User:** `sa`
   * **Password:** `123456`.

**Bước 3: Chạy dự án**
1. Mở dự án.
2. Add server Apache Tomcat (phiên bản 9 hoặc 10).
3. Build và Run project.