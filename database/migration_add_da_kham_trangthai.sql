-- Sửa lỗi: bác sĩ "Xác nhận hoàn tất" không cập nhật được — thiếu N'Đã khám' trong CHECK constraint.
-- Chạy script này trên DB đang dùng (SQL Server Management Studio / Azure Data Studio).

IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_LichHen_TrangThai')
    ALTER TABLE LichHen DROP CONSTRAINT CK_LichHen_TrangThai;
GO

ALTER TABLE LichHen ADD CONSTRAINT CK_LichHen_TrangThai CHECK (
    TrangThai IN (
        N'Chờ duyệt', N'Chờ xác nhận', N'Chờ phân ca',
        N'Đã duyệt', N'Đã xác nhận', N'Đã đến', N'Đang khám', N'Đã khám',
        N'Hoàn thành', N'Đã hoàn thành', N'Đã thanh toán', N'Đã hủy'
    )
);
GO
