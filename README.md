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

## Tài liệu nhanh: các controller/helper chính & JSON hiện tại

Phần này mô tả các file/back-end route quan trọng (đúng theo code hiện tại), và các JSON đang được render/return.

### `src/java/com/dentalclinic/controller/InforServlet.java`
- **Route**: `GET /Infor/*` (ví dụ: `/Infor/Schedule`, `/Infor/service`, `/Infor/Doctor`)
- **Mục đích**: gom 1 trang công khai `dat-lich.jsp` gồm 3 section: đặt lịch, dịch vụ, bác sĩ; URL quyết định sẽ cuộn tới section nào.
- **Request attributes**:
  - `scrollSection`: `datlich | dichvu | bacsi`
  - `serviceListJson`: JSON danh sách dịch vụ (từ DB)
  - `doctorListJson`: JSON danh sách bác sĩ (mẫu/hardcode)
  - `pageTitle`: tiêu đề trang

### `src/java/com/dentalclinic/utils/InforPageHelper.java`
- **Mục đích**:
  - `resolveSection(pathInfo)`: map pathInfo sang `datlich|dichvu|bacsi`
  - `buildServiceListJson()`: lấy `DichVuDAO.getAll()` rồi build JSON
  - `buildSampleDoctorListJson()`: JSON bác sĩ mẫu (không đọc DB)

**JSON hiện tại (Infor)**:
- `serviceListJson` (mỗi phần tử):
  - `id` (number)
  - `name` (string)
  - `desc` (string) (hiện đang trùng `name`)
  - `time` (string, ví dụ `"30 phút"`)
  - `price` (number)
  - `cat` (string) (hiện đang `"all"`)
  - `perUnit` (boolean) + nếu `true` có thêm `unit` = `"răng"`

Ví dụ:

```json
[
  {"id":1,"name":"Khám tổng quát","desc":"Khám tổng quát","time":"30 phút","price":100000,"cat":"all","perUnit":false},
  {"id":2,"name":"Trám răng","desc":"Trám răng","time":"45 phút","price":200000,"cat":"all","perUnit":true,"unit":"răng"}
]
```

- `doctorListJson` (mỗi phần tử):
  - `name`, `specialty`, `degree`, `imgUrl`

### `src/java/com/dentalclinic/utils/RoleNavHelper.java`
- **Mục đích**: điều hướng URL theo vai trò.
- **Các URL chính**:
  - `getWorkspaceUrl(ctx, role)`:
    - Admin: `ctx + "/admin"`
    - Bác sĩ: `ctx + "/doctor/dashboard"`
    - Lễ tân: `ctx + "/reception-dashboard"`
    - Bệnh nhân/khác: `ctx + "/hoso"`
  - `getScheduleUrl/getServiceUrl/getDoctorUrl`: trỏ về trang `/Infor/Schedule#...`

### `src/java/com/dentalclinic/service/TaiKhoanService.java`
- **Mục đích**: đăng nhập (PAP) dùng `TaiKhoanDAO.getTaiKhoanByPhone(phone)` rồi so sánh mật khẩu trực tiếp.
- **Lưu ý**: hiện là plain-text password (chưa hash).

### `src/java/com/dentalclinic/controller/SaveExaminationServlet.java`
- **Route**: `POST /api/doctor/save-examination`
- **Mục đích**: bác sĩ lưu phiếu khám (nháp hoặc hoàn tất).
- **Request JSON**:
  - `lichHenID` (number)
  - `chanDoan` (string)
  - `lyDoKham` (string)
  - `ghiChu` (string)
  - `draft` (boolean)
  - `danhSachDichVu` (array) mỗi phần tử:
    - `dichVuID` (number)
    - `viTriRang` (number, 0 nếu không theo răng)
    - `donGia` (number)
    - `soLuong` (number)

Ví dụ request:

```json
{
  "lichHenID": 101,
  "chanDoan": "Sâu răng",
  "lyDoKham": "Đau răng",
  "ghiChu": "Theo dõi thêm",
  "draft": true,
  "danhSachDichVu": [
    {"dichVuID": 2, "viTriRang": 46, "donGia": 200000, "soLuong": 1}
  ]
}
```

- **Response JSON**:
  - `{"success": true|false, "message": "..." }`

### `src/java/com/dentalclinic/controller/ReceptionDeskApiServlet.java`
- **Routes**:
  - `GET /api/patients` → trả danh sách bệnh nhân cho lễ tân
  - `PUT /api/patients/update` → cập nhật bệnh nhân (JSON body)
  - `GET /api/reports/revenue-summary` → dashboard doanh thu lễ tân
  - `POST`/`DELETE` hiện bị chặn (trả lỗi)

**JSON hiện tại**:
- `GET /api/patients` → mảng `ReceptionPatientRow` (xem fields trong `BenhNhanDAO.ReceptionPatientRow`).
- `PUT /api/patients/update`:
  - Request body: JSON có `id` + các field của `ReceptionPatientInput` (`fullName`, `phone`, `birthDate`, `gender`, ...).
  - Response: `{success:true}` hoặc `{success:false,message:"..."}`
- `GET /api/reports/revenue-summary`:
  - Response: object do `ReceptionReportDAO.buildDashboardPayload(...)` tạo (tổng quan + danh sách giao dịch).

### `src/java/com/dentalclinic/controller/ReceptionPagesServlet.java`
- **Route pages (JSP)**:
  - `GET /reception-patient` → forward `/reception/benhnhan.jsp`, attribute `patientListJson`
  - `GET /reception-report` → forward `/reception/baocao.jsp`, attribute `reportDataJson`

### `src/java/com/dentalclinic/controller/ReceptionServlet.java`
- **Route**: `GET/POST /reception-dashboard`

**GET**: render `/reception/index.jsp`
- **Request attributes (quan trọng)**:
  - `allLichHen`: list `LichHen` (đã gắn `tenDichVuList` và `paymentStatus`)
  - `serviceListJson`: JSON dịch vụ cho frontend lễ tân (khác format Infor)
  - `roomListJson`: JSON phòng khám
  - `selectedDateNgay`: yyyy-MM-dd
  - `statTotal/statPending/statConfirmed/statCompleted`: số liệu dashboard
  - `receptionViewMode` (`day|week|month|year`), `receptionLoc` (`all|pending|confirmed|completed`), `receptionToolbarLabel`, `receptionHistBanner`

**JSON hiện tại (Reception dashboard)**:
- `serviceListJson` (mỗi phần tử):
  - `id` (number), `name` (string), `price` (number), `time` (string, `"x phút"`),
  - `perUnit` (boolean) + nếu `true` có `unit:"răng"`
- `roomListJson` (mỗi phần tử): `{id:number, name:string}`

Ví dụ:

```json
[
  {"id":1,"name":"Khám tổng quát","price":100000,"time":"30 phút","perUnit":false},
  {"id":2,"name":"Trám răng","price":200000,"time":"45 phút","perUnit":true,"unit":"răng"}
]
```

```json
[
  {"id":1,"name":"Phòng 1"},
  {"id":2,"name":"Phòng 2"}
]
```

**POST actions** (trả JSON, `Content-Type: application/json`):
- `action=approve`: `{success:true}` (set `TrangThai = "Đã xác nhận"`)
- `action=set-status` (pending/approved/arrived/cancelled): `{success:true}`
- `action=pay`: `{success:true}` hoặc `{success:false,message:"..."}`
- `action=delete`: `{success:true}`
- `action=get-detail`: trả chi tiết lịch để edit:
  - `{success:true,id,patientName,patientPhone,ngayKham,gioKham,bacSiId,phongId,ghiChu,dichVuIds:[...],dichVuItems:[{id,quantity}] }`
- `action=update-booking`: `{success:true}`
- `action=get-payment-detail`: `{success:true,id,patientName,patientPhone,ngayKham,gioKham,doctorName,subtotal,items:[{name,quantity,unitPrice,lineTotal}] }`

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