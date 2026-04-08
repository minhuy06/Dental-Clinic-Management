# Package Controller (Tầng Điều Hướng)
## Thành phần 
- Chứa các lớp **Servlet**
## Quy tắc
1. **KHÔNG** viết các câu lệnh SQL (`SELECT`, `INSERT`,...) tại đây.
2. **KHÔNG** viết các đoạn code tính toán logic nghiệp vụ phức tạp (như tính tiền, kiểm tra điều kiện y tế) tại đây.
3. Controller chỉ làm 3 việc:
    - Lấy tham số người dùng nhập.
    - Gọi hàm từ tầng **Service** để xử lý dữ liệu.
    - Chuyển hướng người dùng sang trang JSP tương ứng.