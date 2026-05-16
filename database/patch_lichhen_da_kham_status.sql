-- Tách "Đã khám" (chờ thu) và "Đã hoàn thành" (đã thanh toán).
USE DbQuanLyNhaKhoa;
GO

IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_LichHen_TrangThai')
    ALTER TABLE LichHen DROP CONSTRAINT CK_LichHen_TrangThai;
GO

-- Chuyển dữ liệu trước khi thêm CHECK (tránh conflict)
UPDATE lh
SET lh.TrangThai = N'Đã khám'
FROM LichHen lh
LEFT JOIN PhieuKham pk ON pk.LichHen_ID = lh.LichHen_ID
LEFT JOIN HoaDon hd ON hd.PhieuKham_ID = pk.PhieuKham_ID
WHERE lh.TrangThai = N'Đã hoàn thành'
  AND (hd.HoaDon_ID IS NULL OR hd.NgayThanhToan IS NULL);
GO

IF NOT EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_LichHen_TrangThai')
ALTER TABLE LichHen ADD CONSTRAINT CK_LichHen_TrangThai CHECK (
    TrangThai IN (
        N'Chờ xác nhận', N'Đã xác nhận', N'Đã đến', N'Đang khám',
        N'Đã khám', N'Đã hoàn thành', N'Đã hủy'
    )
);
GO
