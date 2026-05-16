-- Cho phép lịch hẹn chờ admin xếp ca (chưa có bác sĩ)
USE DbQuanLyNhaKhoa;
GO

IF EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = N'FK_LichHen_BacSi')
    ALTER TABLE LichHen DROP CONSTRAINT FK_LichHen_BacSi;
GO

ALTER TABLE LichHen ALTER COLUMN BacSi_ID INT NULL;
GO

ALTER TABLE LichHen ADD CONSTRAINT FK_LichHen_BacSi
    FOREIGN KEY (BacSi_ID) REFERENCES BacSi(BacSi_ID);
GO

IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_LichHen_TrangThai')
    ALTER TABLE LichHen DROP CONSTRAINT CK_LichHen_TrangThai;
GO

ALTER TABLE LichHen ADD CONSTRAINT CK_LichHen_TrangThai CHECK (
    TrangThai IN (
        N'Chờ duyệt', N'Chờ xác nhận', N'Chờ phân ca',
        N'Đã duyệt', N'Đã xác nhận', N'Đã đến', N'Đang khám',
        N'Hoàn thành', N'Đã hoàn thành', N'Đã thanh toán', N'Đã hủy'
    )
);
GO
