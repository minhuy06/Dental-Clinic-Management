# Package dao (Tầng truy cập dữ liệu)

## Thư mục này là nơi **DUY NHẤT** trong dự án được phép giao tiếp trực tiếp với cơ sở dữ liệu SQL Server. Tầng này đóng vai trò như một "thủ kho", chỉ làm nhiệm vụ cất đồ và lấy đồ ra theo đúng yêu cầu.

## Thành phần
- Chứa các **Interface** định nghĩa các hàm thao tác với bảng dữ liệu.
- Chứa các **Class Implementation** thực thi câu lệnh SQL.

## Quy tắc
1. Mọi tương tác với CSDL phải thông qua lớp `CONNECTIONSQLSERVER` ở package `utils`.
2. Khuyến khích sử dụng **Stored Procedure** (`CallableStatement`) hoặc `PreparedStatement` cho các lệnh Thêm, Sửa, Xóa để bảo mật chống SQL Injection.
3. **KHÔNG** xử lý logic nghiệp vụ tại đây. DAO chỉ có nhiệm vụ thực thi SQL và trả về kết quả (dưới dạng đối tượng Model hoặc danh sách List) lên lại cho tầng Service.