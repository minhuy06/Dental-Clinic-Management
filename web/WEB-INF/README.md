# Thư mục WEB-INF (Vùng cấm)

## Đây là thư mục đặc biệt do máy chủ (Tomcat) quản lý. Bất kỳ tệp tin nào nằm trong này đều **không thể** truy cập trực tiếp thông qua URL trên trình duyệt web.

## Thành phần
1. `web.xml`: Dùng để cấu hình thời gian hết hạn đăng nhập, cấu hình trang báo lỗi, và phân luồng Servlet/Filter.
2. `lib/`: Thư mục chứa các file thư viện `.jar` (như `mssql-jdbc.jar`, `jstl.jar`) nếu dự án không dùng Maven.
3. `classes/`: Chứa các file `.class` đã được biên dịch từ code Java (IDE NetBeans thường tự động ẩn và quản lý thư mục này).

## Quy tắc
1. **TUYỆT ĐỐI KHÔNG** đặt các tệp tài nguyên tĩnh (CSS, JS, Hình ảnh) vào thư mục này, vì trình duyệt sẽ không thể tải được chúng.
2. **KHÔNG** đặt trang `index.jsp` (Trang chủ) vào đây.