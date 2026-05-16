-- Chạy trên DbQuanLyNhaKhoa khi thanh toán báo: Invalid column name 'TrangThai'.
-- Trigger tg_CapNhatTrangThai_ThanhToan (AFTER UPDATE HoaDon) ghi PhieuKham.TrangThai /
-- LichHen.TrangThai nhưng bảng PhieuKham không còn cột đó → lỗi khi UPDATE HoaDon lúc thu tiền.
-- App chỉ cần NgayThanhToan + PhuongThucThanhToan trên HoaDon.

USE DbQuanLyNhaKhoa;
GO

IF OBJECT_ID(N'dbo.tg_CapNhatTrangThai_ThanhToan', N'TR') IS NOT NULL
    DROP TRIGGER dbo.tg_CapNhatTrangThai_ThanhToan;
GO

PRINT N'Đã gỡ trigger tg_CapNhatTrangThai_ThanhToan. Thử lại nút Xác nhận thanh toán.';
GO
