# Package Service (Tầng Nghiệp Vụ)

## Chứa toàn bộ các **quy tắc nghiệp vụ (Business Logic)** của phòng khám nha khoa, đứng giữa và làm cầu nối để `controller` và `dao` giao tiếp với nhau.

## Thành phần
- Chứa các **Interface** định nghĩa nghiệp vụ.
- Chứa các **Class Implementation** thực thi nghiệp vụ.

## Quy tắc
1. Tầng này nhận dữ liệu đã được nhào nặn từ Controller truyền xuống.
2. Tại đây thực hiện các khâu: Kiểm tra tính hợp lệ của dữ liệu, tính toán doanh thu, xử lý các logic phức tạp trước khi lưu.
3. Nếu dữ liệu hợp lệ, tầng `service` sẽ gọi hàm từ tầng `dao` để thực thi việc lưu trữ vào CSDL.