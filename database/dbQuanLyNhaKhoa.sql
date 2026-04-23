CREATE DATABASE DbQuanLyNhaKhoa;
GO
USE DbQuanLyNhaKhoa;
GO

-- Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    TaiKhoan_ID INT IDENTITY(1,1),
    SoDienThoai VARCHAR(15) NOT NULL,
    MatKhau VARCHAR(100) NOT NULL,
    VaiTro NVARCHAR(30) NOT NULL, 
    TrangThai NVARCHAR(50) DEFAULT N'Hoạt động',
    Email varchar(100) UNIQUE,
    CONSTRAINT PK_TaiKhoan PRIMARY KEY (TaiKhoan_ID),
    CONSTRAINT UQ_TaiKhoan_SoDienThoai UNIQUE (SoDienThoai)
);
GO

-- Bảng BenhNhan
CREATE TABLE BenhNhan (
    BenhNhan_ID INT IDENTITY(1,1),
    TaiKhoan_ID INT, 
    HoTen NVARCHAR(100) NOT NULL,
    NgaySinh DATE,
    GioiTinh BIT, 
    TienSuBenh NVARCHAR(500),
    NhomMau VARCHAR(10),
    CONSTRAINT PK_BenhNhan PRIMARY KEY (BenhNhan_ID)
);
GO

-- TẠO FILTERED INDEX CHO BỆNH NHÂN (Ép UNIQUE nhưng bỏ qua NULL)
CREATE UNIQUE NONCLUSTERED INDEX UQ_BenhNhan_TaiKhoanID 
ON BenhNhan(TaiKhoan_ID) WHERE TaiKhoan_ID IS NOT NULL;
GO

-- Bảng Bác Sĩ
CREATE TABLE BacSi (
    BacSi_ID INT IDENTITY(1,1),
    TaiKhoan_ID INT NOT NULL,
    HoTen NVARCHAR(100) NOT NULL,
    NgaySinh DATE,
    GioiTinh BIT,
    ChuyenKhoa NVARCHAR(100) NOT NULL,
    AnhDaiDien VARCHAR(255),
    TrinhDo NVARCHAR(50),
    CONSTRAINT PK_BacSi PRIMARY KEY (BacSi_ID),
    CONSTRAINT UQ_BacSi_TaiKhoanID UNIQUE (TaiKhoan_ID)
);
GO

-- Bảng Lễ Tân
CREATE TABLE LeTan (
    LeTan_ID INT IDENTITY(1,1),
    TaiKhoan_ID INT NOT NULL,
    HoTen NVARCHAR(100) NOT NULL,
    NgaySinh DATE,
    GioiTinh BIT,
    CaLamViec NVARCHAR(50),
    CONSTRAINT PK_LeTan PRIMARY KEY (LeTan_ID),
    CONSTRAINT UQ_LeTan_TaiKhoanID UNIQUE (TaiKhoan_ID)
);
GO

-- Bảng Dịch Vụ
CREATE TABLE DichVu (
    DichVu_ID INT IDENTITY(1,1),
    TenDichVu NVARCHAR(100) NOT NULL,
    GiaTien MONEY NOT NULL,
    CONSTRAINT PK_DichVu PRIMARY KEY (DichVu_ID)
);
GO

-- Bảng Thuốc
CREATE TABLE Thuoc (
    Thuoc_ID INT IDENTITY(1,1),
    TenThuoc NVARCHAR(100) NOT NULL,
    DonViTinh NVARCHAR(20) NOT NULL,
    GiaTien MONEY NOT NULL,
    CONSTRAINT PK_Thuoc PRIMARY KEY (Thuoc_ID)
);
GO

-- Bảng Lịch Hẹn
CREATE TABLE LichHen (
    LichHen_ID INT IDENTITY(1,1),
    BenhNhan_ID INT NOT NULL,
    BacSi_ID INT NOT NULL,
    NgayKham DATE NOT NULL,
    GioKham TIME NOT NULL,
    GhiChu NVARCHAR(255),
    TrangThai NVARCHAR(50) DEFAULT N'Chờ duyệt',
    CONSTRAINT PK_LichHen PRIMARY KEY (LichHen_ID)
);
GO

-- Bảng Phiếu Khám
CREATE TABLE PhieuKham (
    PhieuKham_ID INT IDENTITY(1,1),
    LichHen_ID INT NULL, 
    ChanDoan NVARCHAR(200),
    NgayTao DATETIME DEFAULT GETDATE(), 
    TrangThai NVARCHAR(100) DEFAULT N'Chờ thanh toán',
    CONSTRAINT PK_PhieuKham PRIMARY KEY (PhieuKham_ID)
);
GO

CREATE UNIQUE NONCLUSTERED INDEX UQ_PhieuKham_LichHenID 
ON PhieuKham(LichHen_ID) WHERE LichHen_ID IS NOT NULL;
GO

-- Bảng Chi Tiết Dịch Vụ
CREATE TABLE ChiTietDichVu (
    ChiTietDichVu_ID INT IDENTITY(1,1),
    PhieuKham_ID INT NOT NULL,
    DichVu_ID INT NOT NULL,
    DonGia MONEY NOT NULL,
    ViTriRang NVARCHAR(50),
    CONSTRAINT PK_ChiTietDichVu PRIMARY KEY (ChiTietDichVu_ID)
);
GO

-- Bảng Chi Tiết Đơn Thuốc
CREATE TABLE ChiTietDonThuoc (
    ChiTietThuoc_ID INT IDENTITY(1,1),
    PhieuKham_ID INT NOT NULL,
    Thuoc_ID INT NOT NULL,
    SoLuong INT NOT NULL,
    CachSuDung NVARCHAR(255),
    CONSTRAINT PK_ChiTietDonThuoc PRIMARY KEY (ChiTietThuoc_ID)
);
GO

-- Bảng Hóa Đơn
CREATE TABLE HoaDon (
    HoaDon_ID INT IDENTITY(1,1),
    PhieuKham_ID INT NOT NULL,
    LeTan_ID INT NOT NULL,
    TongTien MONEY NOT NULL,
    NgayThanhToan DATETIME,
    TrangThai NVARCHAR(50) DEFAULT N'Chưa thanh toán',
    CONSTRAINT PK_HoaDon PRIMARY KEY (HoaDon_ID),
    CONSTRAINT UQ_HoaDon_PhieuKham UNIQUE (PhieuKham_ID) -- Đảm bảo quan hệ 1-1
);
GO


-- Ràng buộc khóa ngoại cho Bệnh Nhân, Bác Sĩ, Lễ Tân
ALTER TABLE BenhNhan
ADD CONSTRAINT FK_BenhNhan_TaiKhoan FOREIGN KEY (TaiKhoan_ID) REFERENCES TaiKhoan(TaiKhoan_ID) ON DELETE CASCADE;
GO

ALTER TABLE BacSi
ADD CONSTRAINT FK_BacSi_TaiKhoan FOREIGN KEY (TaiKhoan_ID) REFERENCES TaiKhoan(TaiKhoan_ID) ON DELETE CASCADE;
GO

ALTER TABLE LeTan
ADD CONSTRAINT FK_LeTan_TaiKhoan FOREIGN KEY (TaiKhoan_ID) REFERENCES TaiKhoan(TaiKhoan_ID) ON DELETE CASCADE;
GO

-- Ràng buộc khóa ngoại cho Lịch Hẹn
ALTER TABLE LichHen
ADD CONSTRAINT FK_LichHen_BenhNhan FOREIGN KEY (BenhNhan_ID) REFERENCES BenhNhan(BenhNhan_ID);
go

ALTER TABLE LichHen
ADD CONSTRAINT FK_LichHen_BacSi FOREIGN KEY (BacSi_ID) REFERENCES BacSi(BacSi_ID);
GO

-- Ràng buộc khóa ngoại cho Phiếu Khám
ALTER TABLE PhieuKham
ADD CONSTRAINT FK_PhieuKham_LichHen FOREIGN KEY (LichHen_ID) REFERENCES LichHen(LichHen_ID) ON DELETE SET NULL;
GO

-- Ràng buộc khóa ngoại cho Chi Tiết Dịch Vụ
ALTER TABLE ChiTietDichVu
ADD CONSTRAINT FK_ChiTietDichVu_PhieuKham FOREIGN KEY (PhieuKham_ID) REFERENCES PhieuKham(PhieuKham_ID) ON DELETE CASCADE;
GO

ALTER TABLE ChiTietDichVu
ADD CONSTRAINT FK_ChiTietDichVu_DichVu FOREIGN KEY (DichVu_ID) REFERENCES DichVu(DichVu_ID);
GO

-- Ràng buộc khóa ngoại cho Chi Tiết Đơn Thuốc
ALTER TABLE ChiTietDonThuoc
ADD CONSTRAINT FK_ChiTietDonThuoc_PhieuKham FOREIGN KEY (PhieuKham_ID) REFERENCES PhieuKham(PhieuKham_ID);

ALTER TABLE ChiTietDonThuoc
ADD CONSTRAINT FK_ChiTietDonThuoc_Thuoc FOREIGN KEY (Thuoc_ID) REFERENCES Thuoc(Thuoc_ID);
GO

-- Ràng buộc khóa ngoại cho Hóa Đơn
ALTER TABLE HoaDon
ADD CONSTRAINT FK_HoaDon_PhieuKham FOREIGN KEY (PhieuKham_ID) REFERENCES PhieuKham(PhieuKham_ID);

ALTER TABLE HoaDon
ADD CONSTRAINT FK_HoaDon_LeTan FOREIGN KEY (LeTan_ID) REFERENCES LeTan(LeTan_ID);
GO

-- INSERT dữ liệu mồi
-- 1. INSERT TÀI KHOẢN
INSERT INTO TaiKhoan (SoDienThoai, MatKhau, VaiTro, TrangThai, Email) VALUES
-- 1 Admin
('0763612967', 'Abc23456', N'Quản trị viên', N'Hoạt động', 'admin.kvone@gmail.com'),

-- 20 Bệnh nhân
('0901000001', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan01@gmail.com'), ('0901000002', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan02@gmail.com'), 
('0901000003', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan03@gmail.com'), ('0901000004', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan04@gmail.com'),
('0901000005', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan05@gmail.com'), ('0901000006', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan06@gmail.com'), 
('0901000007', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan07@gmail.com'), ('0901000008', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan08@gmail.com'),
('0901000009', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan09@gmail.com'), ('0901000010', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan10@gmail.com'), 
('0901000011', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan11@gmail.com'), ('0901000012', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan12@gmail.com'),
('0901000013', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan13@gmail.com'), ('0901000014', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan14@gmail.com'), 
('0901000015', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan15@gmail.com'), ('0901000016', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan16@gmail.com'),
('0901000017', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan17@gmail.com'), ('0901000018', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan18@gmail.com'), 
('0901000019', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan19@gmail.com'), ('0901000020', '123456', N'Bệnh nhân', N'Hoạt động', 'benhnhan20@gmail.com'),

-- 20 Bác sĩ
('0902000001', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi01@gmail.com'), ('0902000002', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi02@gmail.com'), 
('0902000003', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi03@gmail.com'), ('0902000004', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi04@gmail.com'),
('0902000005', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi05@gmail.com'), ('0902000006', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi06@gmail.com'), 
('0902000007', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi07@gmail.com'), ('0902000008', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi08@gmail.com'),
('0902000009', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi09@gmail.com'), ('0902000010', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi10@gmail.com'), 
('0902000011', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi11@gmail.com'), ('0902000012', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi12@gmail.com'),
('0902000013', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi13@gmail.com'), ('0902000014', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi14@gmail.com'), 
('0902000015', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi15@gmail.com'), ('0902000016', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi16@gmail.com'),
('0902000017', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi17@gmail.com'), ('0902000018', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi18@gmail.com'), 
('0902000019', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi19@gmail.com'), ('0902000020', '123456', N'Bác sĩ', N'Hoạt động', 'bacsi20@gmail.com'),

-- 20 Lễ tân
('0903000001', '123456', N'Lễ tân', N'Hoạt động', 'letan01@gmail.com'), ('0903000002', '123456', N'Lễ tân', N'Hoạt động', 'letan02@gmail.com'), 
('0903000003', '123456', N'Lễ tân', N'Hoạt động', 'letan03@gmail.com'), ('0903000004', '123456', N'Lễ tân', N'Hoạt động', 'letan04@gmail.com'),
('0903000005', '123456', N'Lễ tân', N'Hoạt động', 'letan05@gmail.com'), ('0903000006', '123456', N'Lễ tân', N'Hoạt động', 'letan06@gmail.com'), 
('0903000007', '123456', N'Lễ tân', N'Hoạt động', 'letan07@gmail.com'), ('0903000008', '123456', N'Lễ tân', N'Hoạt động', 'letan08@gmail.com'),
('0903000009', '123456', N'Lễ tân', N'Hoạt động', 'letan09@gmail.com'), ('0903000010', '123456', N'Lễ tân', N'Hoạt động', 'letan10@gmail.com'), 
('0903000011', '123456', N'Lễ tân', N'Hoạt động', 'letan11@gmail.com'), ('0903000012', '123456', N'Lễ tân', N'Hoạt động', 'letan12@gmail.com'),
('0903000013', '123456', N'Lễ tân', N'Hoạt động', 'letan13@gmail.com'), ('0903000014', '123456', N'Lễ tân', N'Hoạt động', 'letan14@gmail.com'), 
('0903000015', '123456', N'Lễ tân', N'Hoạt động', 'letan15@gmail.com'), ('0903000016', '123456', N'Lễ tân', N'Hoạt động', 'letan16@gmail.com'),
('0903000017', '123456', N'Lễ tân', N'Hoạt động', 'letan17@gmail.com'), ('0903000018', '123456', N'Lễ tân', N'Hoạt động', 'letan18@gmail.com'), 
('0903000019', '123456', N'Lễ tân', N'Hoạt động', 'letan19@gmail.com'), ('0903000020', '123456', N'Lễ tân', N'Hoạt động', 'letan20@gmail.com');
GO

-- 2. INSERT BỆNH NHÂN
INSERT INTO BenhNhan (TaiKhoan_ID, HoTen, NgaySinh, GioiTinh, TienSuBenh, NhomMau) VALUES
(1, N'Nguyễn Văn An', '1990-05-15', 1, N'Không', 'A'), (2, N'Trần Thị Bích', '1985-08-20', 0, N'Dị ứng thuốc tê', 'B'),
(3, N'Lê Hoàng Nam', '2000-11-10', 1, N'Tiểu đường', 'O'), (4, N'Phạm Thu Hà', '1995-02-28', 0, N'Không', 'AB'),
(5, N'Hoàng Vĩnh Khang', '1982-07-07', 1, N'Cao huyết áp', 'A'), (6, N'Đặng Phương Hoa', '1998-12-12', 0, N'Không', 'B'),
(7, N'Bùi Tấn Tài', '1975-03-30', 1, N'Hen suyễn', 'O'), (8, N'Ngô Mai Phương', '2002-09-05', 0, N'Không', 'A'),
(9, N'Đoàn Văn Thịnh', '1988-06-18', 1, N'Bệnh tim mạch', 'AB'), (10, N'Lý Thảo Ly', '1993-01-22', 0, N'Không', 'B'),
(11, N'Vũ Gia Huy', '1999-04-14', 1, N'Không', 'O'), (12, N'Đinh Bảo Ngọc', '1980-10-31', 0, N'Viêm gan B', 'A'),
(13, N'Trương Công Phát', '1970-05-05', 1, N'Không', 'O'), (14, N'Lâm Bích Ngọc', '1996-08-08', 0, N'Không', 'B'),
(15, N'Hồ Quốc Toàn', '1987-12-01', 1, N'Không', 'AB'), (16, N'Châu Tú Anh', '1991-03-15', 0, N'Dị ứng Penicillin', 'A'),
(17, N'Phan Đình Phùng', '1984-07-25', 1, N'Không', 'O'), (18, N'Tạ Thanh Nhàn', '1979-11-19', 0, N'Không', 'B'),
(19, N'Vương Thái Bảo', '2005-02-14', 1, N'Không', 'A'), (20, N'Dương Tuyết Mai', '1994-06-21', 0, N'Tiểu đường', 'AB');
GO

-- 3. INSERT BÁC SĨ
INSERT INTO BacSi (TaiKhoan_ID, HoTen, NgaySinh, GioiTinh, ChuyenKhoa, AnhDaiDien, TrinhDo) VALUES
(21, N'BS. Nguyễn Hải', '1975-01-10', 1, N'Tổng quát', '/img/bs1.jpg', N'CKI'),
(22, N'BS. Trần Tâm', '1980-05-20', 0, N'Chỉnh nha', '/img/bs2.jpg', N'Thạc sĩ'),
(23, N'BS. Lê Quang', '1978-08-15', 1, N'Phục hình răng', '/img/bs3.jpg', N'Tiến sĩ'),
(24, N'BS. Phạm Hương', '1985-02-28', 0, N'Thẩm mỹ', '/img/bs4.jpg', N'CKI'),
(25, N'BS. Hoàng Quân', '1972-11-05', 1, N'Phẫu thuật miệng', '/img/bs5.jpg', N'CKII'),
(26, N'BS. Đặng Ngân', '1982-09-12', 0, N'Nhổ răng', '/img/bs6.jpg', N'Cử nhân'),
(27, N'BS. Bùi Long', '1979-04-18', 1, N'Tổng quát', '/img/bs7.jpg', N'Thạc sĩ'),
(28, N'BS. Ngô Yến', '1988-07-22', 0, N'Chỉnh nha', '/img/bs8.jpg', N'CKI'),
(29, N'BS. Đoàn Phong', '1976-12-30', 1, N'Phục hình răng', '/img/bs9.jpg', N'Tiến sĩ'),
(30, N'BS. Lý Trang', '1983-03-14', 0, N'Thẩm mỹ', '/img/bs10.jpg', N'Thạc sĩ'),
(31, N'BS. Vũ Hoàng', '1974-06-05', 1, N'Phẫu thuật miệng', '/img/bs11.jpg', N'CKII'),
(32, N'BS. Đinh Cẩm', '1986-10-10', 0, N'Nhổ răng', '/img/bs12.jpg', N'CKI'),
(33, N'BS. Trương Toàn', '1981-01-25', 1, N'Tổng quát', '/img/bs13.jpg', N'Thạc sĩ'),
(34, N'BS. Lâm Thu', '1989-05-19', 0, N'Chỉnh nha', '/img/bs14.jpg', N'Cử nhân'),
(35, N'BS. Hồ Vĩ', '1977-08-08', 1, N'Phục hình răng', '/img/bs15.jpg', N'CKI'),
(36, N'BS. Châu Loan', '1984-12-01', 0, N'Thẩm mỹ', '/img/bs16.jpg', N'Thạc sĩ'),
(37, N'BS. Phan Trung', '1973-02-14', 1, N'Phẫu thuật miệng', '/img/bs17.jpg', N'Tiến sĩ'),
(38, N'BS. Tạ My', '1987-07-07', 0, N'Nhổ răng', '/img/bs18.jpg', N'CKI'),
(39, N'BS. Vương Đạt', '1980-09-09', 1, N'Tổng quát', '/img/bs19.jpg', N'Thạc sĩ'),
(40, N'BS. Dương Quyên', '1985-11-11', 0, N'Chỉnh nha', '/img/bs20.jpg', N'Cử nhân');
GO

-- 4. INSERT LỄ TÂN
INSERT INTO LeTan (TaiKhoan_ID, HoTen, NgaySinh, GioiTinh, CaLamViec) VALUES
(41, N'Lê Ái', '1995-01-01', 0, N'Sáng'), (42, N'Vũ Bình', '1996-02-02', 1, N'Chiều'),
(43, N'Ngô Cẩm', '1997-03-03', 0, N'Tối'), (44, N'Đỗ Dung', '1994-04-04', 0, N'Hành chính'),
(45, N'Lý Ân', '1998-05-05', 1, N'Sáng'), (46, N'Đinh Giang', '1999-06-06', 0, N'Chiều'),
(47, N'Phan Hiếu', '1993-07-07', 1, N'Tối'), (48, N'Bùi Kim', '1995-08-08', 0, N'Hành chính'),
(49, N'Trần Liên', '1996-09-09', 0, N'Sáng'), (50, N'Lâm Mạnh', '1997-10-10', 1, N'Chiều'),
(51, N'Hồ Nga', '1998-11-11', 0, N'Tối'), (52, N'Châu Oanh', '1994-12-12', 0, N'Hành chính'),
(53, N'Vương Phương', '1999-01-15', 0, N'Sáng'), (54, N'Đoàn Quân', '1995-02-18', 1, N'Chiều'),
(55, N'Tạ Quyên', '1996-03-20', 0, N'Tối'), (56, N'Phạm Sang', '1997-04-22', 1, N'Hành chính'),
(57, N'Hoàng Thư', '1998-05-25', 0, N'Sáng'), (58, N'Trương Tín', '1994-06-28', 1, N'Chiều'),
(59, N'Dương Uyên', '1999-07-30', 0, N'Tối'), (60, N'Nguyễn Vy', '1995-08-14', 0, N'Hành chính');
GO

-- 5. INSERT DỊCH VỤ
INSERT INTO DichVu (TenDichVu, GiaTien) VALUES
(N'Khám tổng quát', 100000), (N'Cạo vôi răng', 200000), (N'Trám răng Composite', 300000),
(N'Nhổ răng sữa', 100000), (N'Nhổ răng khôn mọc thẳng', 1000000), (N'Nhổ răng khôn mọc ngầm', 3000000),
(N'Tẩy trắng răng Laser', 2500000), (N'Tẩy trắng răng tại nhà', 1500000), (N'Lấy tủy răng cửa', 800000),
(N'Lấy tủy răng hàm', 1500000), (N'Bọc răng sứ Titan', 2000000), (N'Bọc răng sứ Cercon', 5000000),
(N'Bọc răng sứ Zirconia', 6000000), (N'Mặt dán sứ Veneer', 7000000), (N'Cấy ghép Implant', 15000000),
(N'Cấy ghép Implant Cao cấp', 30000000), (N'Niềng răng mắc cài kim loại', 25000000), (N'Niềng răng mắc cài sứ', 35000000),
(N'Niềng răng Invisalign', 80000000), (N'Điều trị viêm nha chu', 500000);
GO

-- 6. INSERT THUỐC
INSERT INTO Thuoc (TenThuoc, DonViTinh, GiaTien) VALUES
(N'Paracetamol 500mg', N'Viên', 5000), (N'Ibuprofen 400mg', N'Viên', 8000),
(N'Amoxicillin 500mg', N'Viên', 10000), (N'Augmentin 1g', N'Viên', 25000),
(N'Alphachoay', N'Viên', 6000), (N'Efferalgan 500mg sủi', N'Viên', 7000),
(N'Rodogyl', N'Viên', 12000), (N'Metronidazole 250mg', N'Viên', 5000),
(N'Spiramycin 1.5 MIU', N'Viên', 15000), (N'Cefuroxim 500mg', N'Viên', 20000),
(N'Medrol 16mg', N'Viên', 15000), (N'Prednisolone 5mg', N'Viên', 3000),
(N'Panadol Extra', N'Viên', 6000), (N'Betadine súc miệng', N'Chai', 80000),
(N'Kin Gingival súc miệng', N'Chai', 120000), (N'Sensodyne kem y tế', N'Tuýp', 90000),
(N'Clindamycin 300mg', N'Viên', 18000), (N'Vitamin C 500mg', N'Viên', 4000),
(N'Calcium D3', N'Viên', 5000), (N'Nước muối sinh lý 0.9%', N'Chai', 10000);
GO

-- 7. INSERT LỊCH HẸN
INSERT INTO LichHen (BenhNhan_ID, BacSi_ID, NgayKham, GioKham, GhiChu, TrangThai) VALUES
(1, 1, '2024-03-01', '08:00', N'Khám định kỳ', N'Đã thanh toán'), (2, 2, '2024-03-02', '09:00', N'Tư vấn niềng răng', N'Đã thanh toán'),
(3, 3, '2024-03-03', '10:00', N'Làm lại răng sứ', N'Đã thanh toán'), (4, 4, '2024-03-04', '14:00', N'Muốn tẩy trắng', N'Đã thanh toán'),
(5, 5, '2024-03-05', '15:00', N'Đau răng khôn', N'Đã thanh toán'), (6, 6, '2024-03-06', '16:00', N'Nhổ răng sữa cho bé', N'Đã thanh toán'),
(7, 7, '2024-03-07', '08:30', N'Cạo vôi răng', N'Đã thanh toán'), (8, 8, '2024-03-08', '09:30', N'Tái khám chỉnh nha', N'Đã thanh toán'),
(9, 9, '2024-03-09', '10:30', N'Rớt răng tạm', N'Đã thanh toán'), (10, 10, '2024-03-10', '14:30', N'Khám thẩm mỹ', N'Đã thanh toán'),
(11, 11, '2024-03-11', '15:30', N'Đau nhức dữ dội', N'Đã thanh toán'), (12, 12, '2024-03-12', '16:30', N'Nhổ răng lung lay', N'Đã thanh toán'),
(13, 13, '2024-03-13', '08:00', N'Trám răng mẻ', N'Đã thanh toán'), (14, 14, '2024-03-14', '09:00', N'Tái khám niềng', N'Đã thanh toán'),
(15, 15, '2024-03-15', '10:00', N'Cắm Implant', N'Đã thanh toán'), (16, 16, '2024-03-16', '14:00', N'Bọc sứ Veneer', N'Đã thanh toán'),
(17, 17, '2024-03-17', '15:00', N'Cắt lợi', N'Đã thanh toán'), (18, 18, '2024-03-18', '16:00', N'Nhổ răng sâu', N'Đã thanh toán'),
(19, 19, '2024-03-19', '08:30', N'Khám tổng quát', N'Đã thanh toán'), (20, 20, '2024-03-20', '09:30', N'Tái khám', N'Đã thanh toán');
GO

-- 8. INSERT PHIẾU KHÁM (20 Dòng, chỉ chèn LichHen_ID, ChanDoan)
INSERT INTO PhieuKham (LichHen_ID, ChanDoan, NgayTao, TrangThai) VALUES
(1, N'Vôi răng nhiều, cần cạo vôi', '2024-03-01 08:30:00', N'Đã thanh toán'), (2, N'Răng mọc lệch lạc', '2024-03-02 09:30:00', N'Đã thanh toán'),
(3, N'Răng sứ cũ bị vỡ mẻ', '2024-03-03 10:30:00', N'Đã thanh toán'), (4, N'Màu răng xỉn vàng', '2024-03-04 14:30:00', N'Đã thanh toán'),
(5, N'Răng 48 mọc ngầm đâm ngang', '2024-03-05 15:30:00', N'Đã thanh toán'), (6, N'Răng 51 lung lay nhiều', '2024-03-06 16:30:00', N'Đã thanh toán'),
(7, N'Viêm nướu nhẹ', '2024-03-07 09:00:00', N'Đã thanh toán'), (8, N'Thay thun niềng răng', '2024-03-08 10:00:00', N'Đã thanh toán'),
(9, N'Gắn lại răng tạm', '2024-03-09 11:00:00', N'Đã thanh toán'), (10, N'Tư vấn làm 16 răng sứ', '2024-03-10 15:00:00', N'Đã thanh toán'),
(11, N'Viêm tủy cấp răng 46', '2024-03-11 16:00:00', N'Đã thanh toán'), (12, N'Nha chu viêm nặng', '2024-03-12 17:00:00', N'Đã thanh toán'),
(13, N'Sâu ngà răng 24', '2024-03-13 08:30:00', N'Đã thanh toán'), (14, N'Thay dây cung thép', '2024-03-14 09:30:00', N'Đã thanh toán'),
(15, N'Cắm 1 trụ Implant', '2024-03-15 10:30:00', N'Đã thanh toán'), (16, N'Dán 6 mặt sứ Veneer', '2024-03-16 14:30:00', N'Đã thanh toán'),
(17, N'Cười hở lợi', '2024-03-17 15:30:00', N'Đã thanh toán'), (18, N'Răng 27 vỡ lớn', '2024-03-18 16:30:00', N'Đã thanh toán'),
(19, N'Sâu men răng 15, 16', '2024-03-19 09:00:00', N'Đã thanh toán'), (20, N'Kiểm tra lại sau nhổ', '2024-03-20 10:00:00', N'Đã thanh toán');
GO

-- 9. INSERT CHI TIẾT DỊCH VỤ
INSERT INTO ChiTietDichVu (PhieuKham_ID, DichVu_ID, DonGia, ViTriRang) VALUES
(1, 2, 200000, N'Toàn hàm'), (2, 1, 100000, N'Không'), 
(3, 12, 5000000, N'Răng 21'), (4, 7, 2500000, N'Toàn hàm'),
(5, 6, 3000000, N'Răng 48'), (6, 4, 100000, N'Răng 51'),
(7, 2, 200000, N'Toàn hàm'), (8, 17, 25000000, N'Hai hàm'),
(9, 1, 100000, N'Răng 36'), (10, 1, 100000, N'Toàn hàm'),
(11, 10, 1500000, N'Răng 46'), (12, 5, 1000000, N'Răng 36'),
(13, 3, 300000, N'Răng 24'), (14, 17, 25000000, N'Hai hàm'),
(15, 16, 30000000, N'Răng 46'), (16, 14, 7000000, N'Răng 13-23'),
(17, 20, 500000, N'Hàm trên'), (18, 5, 1000000, N'Răng 27'),
(19, 3, 300000, N'Răng 15'), (20, 1, 100000, N'Vết thương');
GO

-- 10. INSERT CHI TIẾT ĐƠN THUỐC
INSERT INTO ChiTietDonThuoc (PhieuKham_ID, Thuoc_ID, SoLuong, CachSuDung) VALUES
(1, 14, 1, N'Súc miệng sáng tối'), (2, 1, 10, N'Uống khi đau'),
(3, 4, 14, N'Sáng 1 viên, tối 1 viên sau ăn'), (4, 16, 1, N'Chải răng hàng ngày'),
(5, 4, 14, N'Sáng 1 viên, tối 1 viên sau ăn'), (6, 1, 5, N'Uống nửa viên khi đau'),
(7, 14, 1, N'Súc miệng sáng tối'), (8, 1, 10, N'Uống khi đau'),
(9, 1, 10, N'Uống khi đau'), (10, 18, 20, N'Ngày uống 1 viên'),
(11, 4, 14, N'Sáng 1 viên, tối 1 viên'), (12, 4, 14, N'Sáng 1 viên, tối 1 viên'),
(13, 1, 10, N'Uống khi đau'), (14, 1, 10, N'Uống khi đau'),
(15, 4, 20, N'Sáng 1 viên, tối 1 viên'), (16, 1, 10, N'Uống khi đau'),
(17, 4, 14, N'Sáng 1 viên, tối 1 viên'), (18, 4, 14, N'Sáng 1 viên, tối 1 viên'),
(19, 1, 10, N'Uống khi đau'), (20, 14, 1, N'Súc miệng sau ăn');
GO

-- 11. INSERT HÓA ĐƠN
INSERT INTO HoaDon (PhieuKham_ID, LeTan_ID, TongTien, NgayThanhToan, TrangThai) VALUES
(1, 1, 280000, '2024-03-01 09:00:00', N'Đã thanh toán'), (2, 2, 150000, '2024-03-02 10:00:00', N'Đã thanh toán'),
(3, 3, 5350000, '2024-03-03 11:00:00', N'Đã thanh toán'), (4, 4, 2590000, '2024-03-04 15:00:00', N'Đã thanh toán'),
(5, 5, 3350000, '2024-03-05 16:00:00', N'Đã thanh toán'), (6, 6, 125000, '2024-03-06 17:00:00', N'Đã thanh toán'),
(7, 7, 280000, '2024-03-07 09:30:00', N'Đã thanh toán'), (8, 8, 25050000, '2024-03-08 10:30:00', N'Đã thanh toán'),
(9, 9, 150000, '2024-03-09 11:30:00', N'Đã thanh toán'), (10, 10, 180000, '2024-03-10 15:30:00', N'Đã thanh toán'),
(11, 11, 1850000, '2024-03-11 16:30:00', N'Đã thanh toán'), (12, 12, 1350000, '2024-03-12 17:30:00', N'Đã thanh toán'),
(13, 13, 350000, '2024-03-13 09:00:00', N'Đã thanh toán'), (14, 14, 25050000, '2024-03-14 10:00:00', N'Đã thanh toán'),
(15, 15, 30500000, '2024-03-15 11:00:00', N'Đã thanh toán'), (16, 16, 7050000, '2024-03-16 15:00:00', N'Đã thanh toán'),
(17, 17, 850000, '2024-03-17 16:00:00', N'Đã thanh toán'), (18, 18, 1350000, '2024-03-18 17:00:00', N'Đã thanh toán'),
(19, 19, 350000, '2024-03-19 09:30:00', N'Đã thanh toán'), (20, 20, 180000, '2024-03-20 10:30:00', N'Đã thanh toán');
GO


-- RÀNG BUỘC KIỂM TRA
-- Bảng Tài Khoản
ALTER TABLE TaiKhoan
ADD CONSTRAINT CK_TaiKhoan_VaiTro CHECK (VaiTro IN (N'Bệnh nhân', N'Bác sĩ', N'Lễ tân', N'Quản trị viên'));
GO

ALTER TABLE TaiKhoan
ADD CONSTRAINT CK_TaiKhoan_TrangThai CHECK (TrangThai IN (N'Hoạt động', N'Bị khóa', N'Chờ xác thực'));
GO

-- Bảng Bệnh Nhân
ALTER TABLE BenhNhan
ADD CONSTRAINT CK_BenhNhan_NgaySinh CHECK (NgaySinh <= GETDATE() AND NgaySinh >= '1900-01-01');

ALTER TABLE BenhNhan
ADD CONSTRAINT CK_BenhNhan_NhomMau CHECK (NhomMau IN ('A', 'B', 'AB', 'O', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'));
GO

-- Bảng Bác Sĩ
ALTER TABLE BacSi
ADD CONSTRAINT CK_BacSi_NgaySinh CHECK (NgaySinh <= GETDATE() AND NgaySinh >= '1900-01-01');

ALTER TABLE BacSi
ADD CONSTRAINT CK_BacSi_ChuyenKhoa CHECK (ChuyenKhoa IN (N'Tổng quát', N'Chỉnh nha', N'Phục hình răng', N'Thẩm mỹ', N'Phẫu thuật miệng', N'Nhổ răng'));

ALTER TABLE BacSi
ADD CONSTRAINT CK_BacSi_TrinhDo CHECK (TrinhDo IN (N'Cử nhân', N'Thạc sĩ', N'Tiến sĩ', N'CKI', N'CKII'));
GO

-- Bảng Lễ Tân
ALTER TABLE LeTan
ADD CONSTRAINT CK_LeTan_NgaySinh CHECK (NgaySinh <= GETDATE() AND NgaySinh >= '1900-01-01');

ALTER TABLE LeTan
ADD CONSTRAINT CK_LeTan_CaLamViec CHECK (CaLamViec IN (N'Sáng', N'Chiều', N'Tối', N'Hành chính'));
GO

-- Bảng Lịch Hẹn
ALTER TABLE LichHen
ADD CONSTRAINT CK_LichHen_NgayKham CHECK (NgayKham >= '2024-01-01');

ALTER TABLE LichHen
ADD CONSTRAINT CK_LichHen_TrangThai CHECK (TrangThai IN (N'Chờ xác nhận', N'Đã xác nhận', N'Đã đến', N'Đã thanh toán', N'Đã hủy'));
GO

-- Bảng Phiếu Khám
ALTER TABLE PhieuKham
ADD CONSTRAINT CK_PhieuKham_NgayTao CHECK (NgayTao <= GETDATE() AND NgayTao >= '2020-01-01');

ALTER TABLE PhieuKham
ADD CONSTRAINT CK_PhieuKham_TrangThai CHECK (TrangThai IN (N'Chờ thanh toán', N'Đã thanh toán', N'Đã hủy'));
GO

-- Bảng Hóa Đơn
ALTER TABLE HoaDon
ADD CONSTRAINT CK_HoaDon_NgayThanhToan CHECK (NgayThanhToan <= GETDATE() AND NgayThanhToan >= '2024-01-01');

ALTER TABLE HoaDon
ADD CONSTRAINT CK_HoaDon_TrangThai CHECK (TrangThai IN (N'Chưa thanh toán', N'Đã thanh toán', N'Đã hủy'));
GO

-- Ràng buộc các thuộc tính Tiền tệ & Số lượng phải > 0
ALTER TABLE DichVu ADD CONSTRAINT CK_DichVu_GiaTien CHECK (GiaTien > 0);
ALTER TABLE Thuoc ADD CONSTRAINT CK_Thuoc_GiaTien CHECK (GiaTien > 0);
ALTER TABLE ChiTietDichVu ADD CONSTRAINT CK_ChiTietDichVu_DonGia CHECK (DonGia > 0);
ALTER TABLE ChiTietDonThuoc ADD CONSTRAINT CK_ChiTietDonThuoc_SoLuong CHECK (SoLuong > 0);
ALTER TABLE HoaDon ADD CONSTRAINT CK_HoaDon_TongTien CHECK (TongTien > 0);
GO

-- TRIGGER cập nhập trạng thái
CREATE TRIGGER tg_CapNhatTrangThai_ThanhToan
ON HoaDon
AFTER UPDATE
AS
BEGIN
    -- Chỉ chạy Trigger nếu cột TrangThai thực sự có sự thay đổi
    IF UPDATE(TrangThai)
    BEGIN
        BEGIN TRY
            -- Mở khóa giao dịch an toàn
            BEGIN TRANSACTION;

            -- Khai báo biến hứng dữ liệu từ dòng vừa được Update
            DECLARE @HoaDon_ID INT;
            DECLARE @PhieuKham_ID INT;
            DECLARE @TrangThaiMoi NVARCHAR(50);

            -- Bảng 'inserted' là bảng tạm của SQL chứa dữ liệu mới nhất sau khi Update
            SELECT @HoaDon_ID = inserted.HoaDon_ID, 
                   @PhieuKham_ID = inserted.PhieuKham_ID, 
                   @TrangThaiMoi = inserted.TrangThai
            FROM inserted;

            -- Nếu trạng thái mới là Đã thanh toán thì kích hoạt chuỗi hành động
            IF @TrangThaiMoi = N'Đã thanh toán'
            BEGIN
                -- 1. Cập nhật Phiếu Khám
                UPDATE PhieuKham
                SET TrangThai = N'Đã thanh toán'
                WHERE PhieuKham_ID = @PhieuKham_ID;

                -- 2. Cập nhật Lịch Hẹn (Thông qua ID của Phiếu Khám)
                UPDATE LichHen
                SET TrangThai = N'Đã thanh toán'
                WHERE LichHen_ID = (SELECT LichHen_ID FROM PhieuKham WHERE PhieuKham_ID = @PhieuKham_ID);
            END

            -- Ghi nhận toàn bộ thay đổi vào CSDL
            COMMIT TRANSACTION;
        END TRY
        BEGIN CATCH
            -- Nếu có bất kỳ lỗi nào, quay ngược thời gian phục hồi lại trạng thái cũ
            IF @@TRANCOUNT > 0
            BEGIN
                ROLLBACK TRANSACTION;
            END;
            
            -- Ném mã lỗi lên màn hình/văng lỗi về cho Java bắt Exception
            THROW;
        END CATCH
    END
END;
GO

-- Xóa bảng Thuoc, ChiTietDonThuoc
Drop table ChiTietDonThuoc
go
Drop table Thuoc
go

-- Thêm Phương thức thanh toán, phiếu giảm giá cho HoaDon
alter table HoaDon
add PhuongThucThanhToan nvarchar(50)
go
alter table HoaDon
add PhieuGiamGia decimal 
go

-- Insert giá trị cho PhuongThucThanhToan và PhieuGiamGia
UPDATE HoaDon
SET PhuongThucThanhToan = N'Tiền mặt',
    PhieuGiamGia = 0
WHERE PhuongThucThanhToan IS NULL;
go

-- Xóa thuộc tính: HoTen, NgaySinh, GioiTinh của 3 bảng BacSi, BenhNhan và LeTan. Thêm 3 thuộc tính đó vào bảng TaiKhoan để tránh trùng lặp dữ liệu
alter table TaiKhoan
add HoTen NVARCHAR(100),
    NgaySinh DATE,
    GioiTinh BIT
go

-- Kéo dữ liệu ở BacSi sang
update t
set t.HoTen = b.HoTen,
    t.NgaySinh = b.NgaySinh,
    t.GioiTinh = b.GioiTinh
from TaiKhoan as t
join BacSi as b on b.TaiKhoan_ID = t.TaiKhoan_ID

-- Kéo dữ liệu ở LeTan sang
update t
set t.HoTen = l.HoTen,
    t.NgaySinh = l.NgaySinh,
    t.GioiTinh = l.GioiTinh
from TaiKhoan as t
join LeTan as l on l.TaiKhoan_ID = t.TaiKhoan_ID

-- Kéo dữ liệu ở BenhNhan sang
update t
set t.HoTen = b.HoTen,
    t.NgaySinh = b.NgaySinh,
    t.GioiTinh = b.GioiTinh
from TaiKhoan as t
join BenhNhan as b on b.TaiKhoan_ID = t.TaiKhoan_ID

-- Xóa 3 thuộc tính
alter table BacSi
drop constraint CK_BacSi_NgaySinh
go
alter table BacSi
drop column HoTen, NgaySinh, GioiTinh
go

alter table BenhNhan
drop Constraint CK_BenhNhan_NgaySinh
go
alter table BenhNhan
drop column HoTen, NgaySinh, GioiTinh
go

alter table LeTan
drop Constraint CK_LeTan_NgaySinh
go
alter table LeTan
drop column HoTen, NgaySinh, GioiTinh
go

-- Sửa bảng PhieuKham
alter table PhieuKham
add TrieuChung nvarchar(100), LyDoKham nvarchar(100), GhiChu nvarchar(200), BacSi_ID int
go
update PhieuKham
set TrieuChung = N'Đau răng, chảy máu chân răng',
    LyDoKham = N'Cảm thấy nhức răng',
    GhiChu = N'Mua thuốc kháng sinh',
    BacSi_ID = 1
where TrieuChung is null
go

-- Khóa ngoại BacSi_ID
ALTER TABLE PhieuKham
ADD CONSTRAINT FK_PhieuKham_BacSi FOREIGN KEY (BacSi_ID) REFERENCES BacSi(BacSi_ID) ON DELETE SET NULL;
GO

-- Tạo bảng CaLam và LichLamViec
create table CaLam(
    Ca_Id int identity(1,1),
    TenCa nvarchar(30) not null,
    GioBatDau time not null,
    GioKetThuc time not null,
    Constraint PK_CaLam primary key(Ca_ID)
);
go

create table LichLamViec(
    Lich_ID int identity(1,1),
    TaiKhoan_ID int not null,
    Ca_ID int not null,
    NgayLam date not null,
    TrangThai nvarchar(30) not null,
    Constraint PK_LichLamViec primary key(Lich_ID)
);
go

-- Khóa ngoại
alter table LichLamViec
    add constraint FK_LichLamViec_CaLam foreign key(Ca_ID) references CaLam(Ca_ID)
ALTER TABLE LichLamViec
ADD CONSTRAINT FK_LichLamViec_TaiKhoan FOREIGN KEY (TaiKhoan_ID) REFERENCES TaiKhoan(TaiKhoan_ID);
GO
-- Insert dữ liệu cho 2 bảng
INSERT INTO CaLam (TenCa, GioBatDau, GioKetThuc)
VALUES 
(N'Ca Sáng', '08:00:00', '12:00:00'),
(N'Ca Chiều', '13:30:00', '17:30:00'),
(N'Ca Tối', '18:00:00', '21:00:00');

-- THỨ HAI (20/04/2026) - 6 dòng
INSERT INTO LichLamViec (TaiKhoan_ID, Ca_ID, NgayLam, TrangThai) VALUES 
(21, 1, '2026-04-20', N'Đang làm việc'), (41, 1, '2026-04-20', N'Đang làm việc'),
(22, 2, '2026-04-20', N'Đang làm việc'), (42, 2, '2026-04-20', N'Đang làm việc'),
(23, 3, '2026-04-20', N'Đang làm việc'), (43, 3, '2026-04-20', N'Đang làm việc');

-- THỨ BA (21/04/2026) - 6 dòng
INSERT INTO LichLamViec (TaiKhoan_ID, Ca_ID, NgayLam, TrangThai) VALUES 
(24, 1, '2026-04-21', N'Đang làm việc'), (44, 1, '2026-04-21', N'Đang làm việc'),
(25, 2, '2026-04-21', N'Đang làm việc'), (45, 2, '2026-04-21', N'Đang làm việc'),
(26, 3, '2026-04-21', N'Đang làm việc'), (46, 3, '2026-04-21', N'Xin nghỉ');

-- THỨ TƯ (22/04/2026) - 6 dòng
INSERT INTO LichLamViec (TaiKhoan_ID, Ca_ID, NgayLam, TrangThai) VALUES 
(27, 1, '2026-04-22', N'Đang làm việc'), (47, 1, '2026-04-22', N'Đang làm việc'),
(28, 2, '2026-04-22', N'Đang làm việc'), (48, 2, '2026-04-22', N'Đang làm việc'),
(29, 3, '2026-04-22', N'Đã kín lịch'),   (49, 3, '2026-04-22', N'Đang làm việc');

-- THỨ NĂM (23/04/2026) - 4 dòng
INSERT INTO LichLamViec (TaiKhoan_ID, Ca_ID, NgayLam, TrangThai) VALUES 
(30, 1, '2026-04-23', N'Đang làm việc'), (50, 1, '2026-04-23', N'Đang làm việc'),
(31, 2, '2026-04-23', N'Đang làm việc'), (51, 2, '2026-04-23', N'Đang làm việc');

-- Xóa thuộc tính CaLamViec của LeTan
alter table LeTan
    drop Constraint CK_LeTan_CaLamViec
alter table LeTan
    drop column CaLamViec

-- Xóa thuộc tính TienSuBenh của BenhNhan
alter table BenhNhan
    drop column TienSuBenh

-- Thêm bảng PhongKham
CREATE TABLE PhongKham (
    Phong_ID INT IDENTITY(1,1) PRIMARY KEY,
    TenPhong NVARCHAR(50) NOT NULL,
    TrangThai NVARCHAR(50) DEFAULT N'Trống' -- Các trạng thái: Trống, Đang khám, Bảo trì
);
go

INSERT INTO PhongKham (TenPhong, TrangThai)
VALUES 
(N'Phòng Khám 1', N'Trống'),
(N'Phòng Khám 2', N'Đang khám'),
(N'Phòng Khám 3', N'Trống'),
(N'Phòng Phẫu Thuật 1', N'Trống'),
(N'Phòng Phẫu Thuật 2', N'Bảo trì'),
(N'Phòng Chỉnh Nha 1', N'Đang khám'),
(N'Phòng Chỉnh Nha 2', N'Trống'),
(N'Phòng VIP 1', N'Trống'),
(N'Phòng X-Quang', N'Trống'),
(N'Phòng Khám Dự Phòng', N'Ngừng hoạt động');
GO

alter table PhongKham
    add constraint CK_PhongKham_TrangThai check (TrangThai in (N'Trống', N'Đang khám', N'Bảo trì', N'Ngừng hoạt động'))

-- Thêm thuộc tính Phong_ID cho PhieuKham
ALTER TABLE PhieuKham ADD Phong_ID INT;
GO
UPDATE PhieuKham 
SET Phong_ID = 1 
WHERE Phong_ID IS NULL;
GO
ALTER TABLE PhieuKham 
ALTER COLUMN Phong_ID INT NOT NULL;
GO

-- Thêm khóa ngoại từ PhieuKham tới PhongKham
alter table PhieuKham
    add constraint FK_PhieuKham_PhongKham foreign key(Phong_ID) references PhongKham(Phong_ID) ON UPDATE CASCADE;
go

-- Thêm thuộc tính DichVu_ID cho LichHen (để NULL nếu không bắt buộc khách chọn ngay)
ALTER TABLE LichHen
    ADD DichVu_ID INT;
go
update LichHen
    set DichVu_Id = 1
    where DichVu_ID is null
go
ALTER TABLE LichHen
    ADD CONSTRAINT FK_LichHen_DichVu FOREIGN KEY (DichVu_ID) REFERENCES DichVu(DichVu_ID);

-- Tạo bảng TinNhan (CSKH)
CREATE TABLE TinNhan (
    TinNhan_ID INT IDENTITY(1,1) PRIMARY KEY,
    NguoiGui_ID INT NOT NULL,
    NguoiNhan_ID INT NOT NULL,
    NoiDung NVARCHAR(MAX) NOT NULL,
    ThoiGianGui DATETIME DEFAULT GETDATE(),
    TrangThai BIT DEFAULT 0, -- 0: Chưa đọc, 1: Đã đọc

    -- Tạo khóa ngoại liên kết với bảng TaiKhoan
    CONSTRAINT FK_TinNhan_NguoiGui FOREIGN KEY (NguoiGui_ID) REFERENCES TaiKhoan(TaiKhoan_ID),
    CONSTRAINT FK_TinNhan_NguoiNhan FOREIGN KEY (NguoiNhan_ID) REFERENCES TaiKhoan(TaiKhoan_ID)
);
go

-- Thêm 20 dòng dữ liệu mẫu vào bảng TinNhan dựa trên TaiKhoan_ID thực tế
INSERT INTO TinNhan (NguoiGui_ID, NguoiNhan_ID, NoiDung, ThoiGianGui, TrangThai) 
VALUES
-- Kịch bản 1: Bệnh nhân 1 (TaiKhoan_ID = 1) hỏi tư vấn niềng răng Lễ tân 1 (TaiKhoan_ID = 41)
(1, 41, N'Chào phòng khám, cho mình hỏi chi phí niềng răng mắc cài kim loại hiện nay là bao nhiêu ạ?', DATEADD(day, -3, GETDATE()), 1),
(41, 1, N'Dạ chào bạn, chi phí niềng răng mắc cài kim loại bên mình dao động từ 25-35 triệu tùy tình trạng mức độ lệch lạc của răng ạ.', DATEADD(day, -3, GETDATE()), 1),
(1, 41, N'Mình thấy dạo này có niềng răng trong suốt, giá cả thế nào bạn nhỉ?', DATEADD(day, -3, GETDATE()), 1),
(41, 1, N'Dạ niềng răng trong suốt Invisalign thì chi phí cao hơn, khoảng từ 80-120 triệu. Bác sĩ sẽ cần scan 3D răng để lên phác đồ chính xác nhất cho bạn.', DATEADD(day, -3, GETDATE()), 1),
(1, 41, N'Vậy cuối tuần này phòng khám có lịch trống không? Mình qua khám thử.', DATEADD(day, -2, GETDATE()), 1),
(41, 1, N'Dạ thứ 7 tuần này lúc 9h sáng bên em còn trống lịch bác sĩ Nam, em đặt lịch khám tư vấn miễn phí cho mình nhé?', DATEADD(day, -2, GETDATE()), 1),
(1, 41, N'Ok bạn, đặt lịch cho mình nhé.', DATEADD(day, -2, GETDATE()), 1),
(41, 1, N'Dạ phòng khám đã xác nhận lịch hẹn của bạn vào 9h00 sáng Thứ 7. Rất mong được đón tiếp bạn!', DATEADD(day, -2, GETDATE()), 1),

-- Kịch bản 2: Bệnh nhân 2 (TaiKhoan_ID = 2) bị đau răng khôn khẩn cấp nhắn Lễ tân 2 (TaiKhoan_ID = 42)
(2, 42, N'Bạn ơi, răng trong cùng của mình tự nhiên sưng to và đau quá, há miệng cũng khó khăn.', DATEADD(day, -1, GETDATE()), 1),
(42, 2, N'Dạ chào anh/chị, triệu chứng của mình rất có thể là do mọc răng khôn bị biến chứng viêm lợi trùm rồi ạ.', DATEADD(day, -1, GETDATE()), 1),
(2, 42, N'Bây giờ mình qua khám luôn được không? Đau quá không chịu nổi.', DATEADD(day, -1, GETDATE()), 1),
(42, 2, N'Dạ anh/chị qua ngay nhé, bên em sẽ ưu tiên sắp xếp bác sĩ kiểm tra chụp X-Quang và xử lý giảm đau luôn cho mình ạ.', DATEADD(day, -1, GETDATE()), 1),
(2, 42, N'Cảm ơn bạn, khoảng 15 phút nữa mình tới.', DATEADD(day, -1, GETDATE()), 1),

-- Kịch bản 3: Chăm sóc khách hàng sau điều trị cho Bệnh nhân 3 (TaiKhoan_ID = 3)
(3, 41, N'Hôm qua mình mới tẩy trắng răng xong, nay uống nước đá thấy hơi ê buốt, có sao không bạn?', DATEADD(hour, -5, GETDATE()), 1),
(41, 3, N'Dạ chào bạn, triệu chứng ê buốt nhẹ từ 1-2 ngày đầu sau khi tẩy trắng là phản ứng hoàn toàn bình thường do men răng đang thích nghi lại ạ.', DATEADD(hour, -4, GETDATE()), 1),
(41, 3, N'Bạn lưu ý tránh đồ ăn quá nóng, quá lạnh hoặc sậm màu trong thời gian này nhé. Có thể súc miệng bằng nước muối sinh lý ấm ạ.', DATEADD(hour, -4, GETDATE()), 1),
(3, 41, N'À vâng, mình hiểu rồi, cảm ơn phòng khám.', DATEADD(hour, -1, GETDATE()), 1),

-- Kịch bản 4: Tin nhắn mới (Chưa đọc - TrangThai = 0) để test hiển thị Badge thông báo trên Java
(1, 41, N'Bạn ơi, ngày mai mình có việc đột xuất, có thể lùi lịch khám sang buổi chiều lúc 15h được không?', GETDATE(), 0),
(2, 42, N'Bác sĩ kê cho mình đơn thuốc hôm qua nhưng mình làm mất tờ giấy rồi, bạn xem lại hồ sơ chụp gửi lại giúp mình qua đây được không?', GETDATE(), 0),
(3, 41, N'Phòng khám cho mình hỏi bên mình có hỗ trợ trả góp qua thẻ tín dụng khi bọc răng sứ không nhỉ?', GETDATE(), 0);
go

-- Thêm BacSi_Id vào ChiTietDichVu
ALTER TABLE ChiTietDichVu 
ADD BacSi_ID INT;
go
UPDATE ChiTietDichVu 
SET BacSi_ID = 1 
WHERE BacSi_ID IS NULL;

ALTER TABLE ChiTietDichVu 
ALTER COLUMN BacSi_ID INT NOT NULL;

ALTER TABLE ChiTietDichVu 
    ADD CONSTRAINT FK_ChiTietDichVu_BacSi FOREIGN KEY (BacSi_ID) REFERENCES BacSi(BacSi_ID);


-- Thêm Phong_ID vào LichLamViec
ALTER TABLE LichLamViec
ADD Phong_ID INT;
go
UPDATE LichLamViec
SET Phong_ID = 1
WHERE Phong_ID IS NULL;
go

ALTER TABLE LichLamViec
ALTER COLUMN Phong_ID INT NOT NULL;

ALTER TABLE LichLamViec
    ADD CONSTRAINT FK_LichLamViec_PhongKham FOREIGN KEY (Phong_ID) REFERENCES PhongKham(Phong_ID)
go

-- Thêm thời gian dự kiến cho dịch vụ
ALTER TABLE DichVu
ADD ThoiLuongDuKien INT;
go
UPDATE DichVu
SET ThoiLuongDuKien = 30
WHERE ThoiLuongDuKien IS NULL;

ALTER TABLE DichVu
ALTER COLUMN ThoiLuongDuKien INT NOT NULL;

-- Thêm bảng ChuyenKhoa n-n BacSi
CREATE TABLE ChuyenKhoa (
    ChuyenKhoa_ID INT IDENTITY(1,1) PRIMARY KEY,
    TenChuyenKhoa NVARCHAR(100) NOT NULL
);
go
-- Tạo bảng trung gian thể hiện 1 bác sĩ có nhiều chuyên khoa
CREATE TABLE BacSi_ChuyenKhoa (
    BacSi_ID INT NOT NULL,
    ChuyenKhoa_ID INT NOT NULL,
    Constraint PK_BacSi_ChuyenKhoa PRIMARY KEY (BacSi_ID, ChuyenKhoa_ID),
    CONSTRAINT FK_BSCK_BacSi FOREIGN KEY (BacSi_ID) REFERENCES BacSi(BacSi_ID),
    CONSTRAINT FK_BSCK_ChuyenKhoa FOREIGN KEY (ChuyenKhoa_ID) REFERENCES ChuyenKhoa(ChuyenKhoa_ID)
);
go
-- Xóa ChuyenKhoa khỏi BacSi
alter table BacSi
    drop Constraint CK_BacSi_ChuyenKhoa
alter table BacSi
    drop column ChuyenKhoa

-- Thêm dữ liệu
INSERT INTO ChuyenKhoa (TenChuyenKhoa) 
VALUES
(N'Nha khoa tổng quát'),
(N'Chỉnh nha'),
(N'Nha khoa thẩm mỹ'),
(N'Phẫu thuật & Implant'),
(N'Nha khoa Trẻ em');

INSERT INTO BacSi_ChuyenKhoa (BacSi_ID, ChuyenKhoa_ID)
VALUES 
-- 1. Nhóm Bác sĩ Tổng quát (ID Chuyên khoa = 1)
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1),

-- 2. Nhóm Bác sĩ Chỉnh nha - Niềng răng (ID Chuyên khoa = 2)
(8, 2), (9, 2), (10, 2), (11, 2),

-- 3. Nhóm Bác sĩ Thẩm mỹ - Bọc sứ (ID Chuyên khoa = 3)
(12, 3), (13, 3), (14, 3), (15, 3),

-- 4. Nhóm Bác sĩ Phẫu thuật & Implant (ID Chuyên khoa = 4)
(16, 4), (17, 4), (18, 4),

-- 5. Nhóm Bác sĩ Trẻ em (ID Chuyên khoa = 5)
(19, 5), (20, 5),

-- CÁC BÁC SĨ ĐA NĂNG (KIÊM NHIỆM 2 CHUYÊN KHOA)
-- Đây là lý do chúng ta cần bảng trung gian này!

-- Bác sĩ 1: Vừa làm Tổng quát, vừa khám Trẻ em
(1, 5),  

-- Bác sĩ 12: Tay nghề cao, vừa làm Thẩm mỹ, vừa khám Tổng quát
(12, 1), 

-- Bác sĩ 16: Trưởng khoa, vừa Cắm Implant, vừa làm Thẩm mỹ
(16, 3);

-- Tạo bảng PhongKham_DichVu xử lý quan hệ n-n
CREATE TABLE PhongKham_DichVu (
    Phong_ID INT NOT NULL,
    DichVu_ID INT NOT NULL,    
    CONSTRAINT PK_PhongKham_DichVu PRIMARY KEY (Phong_ID, DichVu_ID),
    CONSTRAINT FK_PKDV_Phong FOREIGN KEY (Phong_ID) REFERENCES PhongKham(Phong_ID),
    CONSTRAINT FK_PKDV_DichVu FOREIGN KEY (DichVu_ID) REFERENCES DichVu(DichVu_ID)
);
go

INSERT INTO PhongKham_DichVu (Phong_ID, DichVu_ID)
VALUES
-- 1. Phòng Khám 1, 2, 3 (ID: 1, 2, 3) 
-- Chuyên: Khám tổng quát, cạo vôi, trám răng, viêm nha chu
(1, 1), (1, 2), (1, 3), (1, 20),
(2, 1), (2, 2), (2, 3), (2, 20),
(3, 1), (3, 2), (3, 3), (3, 20),

-- 2. Phòng Phẫu Thuật 1 & 2 (ID: 4, 5)
-- Chuyên: Nhổ răng, Lấy tủy, Cấy ghép Implant
(4, 4), (4, 5), (4, 6), (4, 9), (4, 10), (4, 15), (4, 16),
(5, 4), (5, 5), (5, 6), (5, 9), (5, 10), (5, 15), (5, 16),

-- 3. Phòng Chỉnh Nha 1 & 2 (ID: 6, 7)
-- Chuyên: Tẩy trắng, Bọc sứ, Dán sứ, Niềng răng
(6, 7), (6, 8), (6, 11), (6, 12), (6, 13), (6, 14), (6, 17), (6, 18), (6, 19),
(7, 7), (7, 8), (7, 11), (7, 12), (7, 13), (7, 14), (7, 17), (7, 18), (7, 19),

-- 4. Phòng VIP 1 (ID: 8)
-- Chuyên: Làm các dịch vụ cao cấp nhất (Implant, Invisalign, Sứ Zirconia...)
(8, 1),  -- Vẫn phải khám tổng quát
(8, 13), -- Bọc răng sứ Zirconia
(8, 14), -- Mặt dán sứ Veneer
(8, 16), -- Cấy ghép Implant cao cấp
(8, 19), -- Niềng răng Invisalign

-- 5. Phòng X-Quang (ID: 9)
-- (Giả định tạm thời map với Khám tổng quát vì bảng Dịch vụ chưa có mã X-Quang)
(9, 1),

-- 6. Phòng Khám D (Phòng dự phòng) (ID: 10)
-- Chỉ làm các ca cơ bản khi quá tải
(10, 1), (10, 2);

go
-- Xử lý thêm ChuyenKhoa_ID vào DichVu
ALTER TABLE DichVu ADD ChuyenKhoa_ID INT;
go
UPDATE DichVu
SET ChuyenKhoa_ID = CASE 
    -- 1: Nha khoa tổng quát (Khám, Cạo vôi, Trám, Viêm nha chu, Lấy tủy)
    WHEN DichVu_ID IN (1, 2, 3, 9, 10, 20) THEN 1 
    -- 2: Chỉnh nha (Niềng răng)
    WHEN DichVu_ID IN (17, 18, 19) THEN 2
    -- 3: Nha khoa thẩm mỹ (Bọc sứ, Tẩy trắng, Dán sứ)
    WHEN DichVu_ID IN (7, 8, 11, 12, 13, 14) THEN 3
    -- 4: Phẫu thuật & Implant (Cắm Implant, Nhổ răng khôn)
    WHEN DichVu_ID IN (5, 6, 15, 16) THEN 4
    -- 5: Nha khoa Trẻ em (Nhổ răng sữa)
    WHEN DichVu_ID IN (4) THEN 5
END;

ALTER TABLE DichVu ALTER COLUMN ChuyenKhoa_ID INT NOT NULL;

ALTER TABLE DichVu
ADD CONSTRAINT FK_DichVu_ChuyenKhoa 
FOREIGN KEY (ChuyenKhoa_ID) REFERENCES ChuyenKhoa(ChuyenKhoa_ID)

-- Xóa NhomMau ở bảng BenhNhan
alter table BenhNhan
    drop Constraint CK_BenhNhan_NhomMau
alter table BenhNhan
    drop column NhomMau
-- Thêm TienSuBenh
ALTER TABLE BenhNhan 
    ADD TienSuBenh NVARCHAR(MAX);
go
UPDATE BenhNhan
SET TienSuBenh = N'Chưa ghi nhận'
WHERE TienSuBenh IS NULL;
GO

-- Xử lý khi đặt lịch chọn nhiều dịch vụ và số răng
ALTER TABLE LichHen DROP CONSTRAINT FK_LichHen_DichVu;
ALTER TABLE LichHen DROP COLUMN DichVu_ID;
GO

-- Thêm thuộc tính TinhTheoRang để nhận biết dịch vụ nào cần chọn số răng
alter table DichVu
    add TinhTheoRang bit
go
update DichVu
    set TinhTheoRang = case
        WHEN DichVu_ID IN (
        3,  -- Trám răng Composite
        4,  -- Nhổ răng sữa
        5,  -- Nhổ răng khôn mọc thẳng
        6,  -- Nhổ răng khôn mọc ngầm
        9,  -- Lấy tủy răng cửa
        10, -- Lấy tủy răng hàm
        11, -- Bọc răng sứ Titan
        12, -- Bọc răng sứ Cercon
        13, -- Bọc răng sứ Zirconia
        14, -- Mặt dán sứ Veneer
        15, -- Cấy ghép Implant
        16  -- Cấy ghép Implant Cao cấp
    ) THEN 1

    -- NHÓM 2: CÁC DỊCH VỤ TRỌN GÓI / TOÀN HÀM (Set = 0)
    WHEN DichVu_ID IN (
        1,  -- Khám tổng quát
        2,  -- Cạo vôi răng
        7,  -- Tẩy trắng răng Laser
        8,  -- Tẩy trắng răng tại nhà
        17, -- Niềng răng mắc cài kim loại
        18, -- Niềng răng mắc cài sứ
        19, -- Niềng răng Invisalign
        20  -- Điều trị viêm nha chu
    ) THEN 0
END;
alter table DichVu
    alter column TinhTheoRang bit not null
go

-- Tạo bảng Chi Tiết Lịch Hẹn
CREATE TABLE ChiTietLichHen (
    ChiTietLichHen_ID INT IDENTITY(1,1) PRIMARY KEY,
    LichHen_ID INT NOT NULL,
    DichVu_ID INT NOT NULL,
    
    SoLuong INT DEFAULT 1,          -- Mặc định là 1, nếu bọc 3 răng sứ thì lưu số 3
    
    CONSTRAINT FK_CTLH_LichHen FOREIGN KEY (LichHen_ID) REFERENCES LichHen(LichHen_ID) ON DELETE CASCADE,
    CONSTRAINT FK_CTLH_DichVu FOREIGN KEY (DichVu_ID) REFERENCES DichVu(DichVu_ID)
);
GO

-- Thêm dữ liệu cho ChiTietLichHen
INSERT INTO ChiTietLichHen (LichHen_ID, DichVu_ID, SoLuong) VALUES
(1, 1, 1),(1, 2, 1),(2, 1, 1),
(2, 17, 1),(4, 7, 1),(7, 2, 1),
(8, 1, 1),(9, 1, 1),(14, 1, 1),
(19, 1, 1),(11, 1, 1),(11, 10, 1),
(17, 20, 1),

-- CÁC CA TIỂU PHẪU / TRÁM RĂNG (Số lượng 2 - 4)
(5, 1, 1),
(5, 6, 2), -- Số lượng: 2

(6, 1, 1),
(6, 4, 3), -- Số lượng: 3

(12, 1, 1),
(12, 5, 2), -- Số lượng: 2

(13, 1, 1),
(13, 3, 4), -- Số lượng: 4

(15, 1, 1),
(15, 15, 2), -- Số lượng: 2

(18, 5, 1), -- Số lượng: 1
(18, 6, 1), -- Số lượng: 1

-- CÁC CA THẨM MỸ LỚN / VIP (Số lượng 6 - 16)
(3, 1, 1),
(3, 13, 6), -- Số lượng: 6

(10, 1, 1),
(10, 14, 8), -- Số lượng: 8 

(16, 1, 1),
(16, 14, 16), -- Số lượng: 16 

(20, 7, 1),  -- Tẩy trắng (Số lượng: 1 vì là trọn gói)
(20, 11, 4);
GO

-- Xóa bảng TinNhan
alter table TinNhan
    drop constraint FK_TinNhan_NguoiGui
alter table TinNhan
    drop constraint FK_TinNhan_NguoiNhan
drop table TinNhan
go

-- TẠO BẢNG HoSo
CREATE TABLE HoSo (
    HoSo_ID     INT IDENTITY(1,1) PRIMARY KEY,
    BenhNhan_ID INT NOT NULL UNIQUE,
    DiaChi      NVARCHAR(255),
    DiUngThuoc  NVARCHAR(500),
    TienSuBenh  NVARCHAR(500),
    TrangThai   NVARCHAR(50) NOT NULL DEFAULT N'Đang điều trị',
    NgayDangKy  DATE NOT NULL DEFAULT GETDATE(),
    GhiChu      NVARCHAR(1000),

    CONSTRAINT FK_HoSo_BenhNhan FOREIGN KEY (BenhNhan_ID) REFERENCES BenhNhan(BenhNhan_ID)
);
go

-- CHUYỂN TienSuBenh TỪ BenhNhan SANG HoSo
INSERT INTO HoSo (BenhNhan_ID, TienSuBenh, NgayDangKy)
SELECT BenhNhan_ID, TienSuBenh, GETDATE()
FROM BenhNhan;

-- Xóa cột TienSuBenh khỏi BenhNhan sau khi đã chuyển xong
ALTER TABLE BenhNhan DROP COLUMN TienSuBenh;

-- 3. INSERT 20 DÒNG DỮ LIỆU MẪU
UPDATE TOP (20) HoSo
SET 
    DiaChi = CASE (HoSo_ID % 5)
        WHEN 0 THEN N'123 Lê Duẩn, Đà Nẵng'
        WHEN 1 THEN N'456 Nguyễn Văn Linh, Đà Nẵng'
        WHEN 2 THEN N'78 đường 2/9, Đà Nẵng'
        WHEN 3 THEN N'12 Quang Trung, Đà Nẵng'
        ELSE N'99 Điện Biên Phủ, Đà Nẵng' END,
    DiUngThuoc = CASE (HoSo_ID % 4)
        WHEN 0 THEN N'Không'
        WHEN 1 THEN N'Dị ứng Penicillin'
        WHEN 2 THEN N'Dị ứng thuốc tê'
        ELSE N'Dị ứng Aspirin' END,
    TrangThai = CASE (HoSo_ID % 3)
        WHEN 0 THEN N'Đang điều trị'
        WHEN 1 THEN N'Đã hoàn thành'
        ELSE N'Tạm dừng' END,
    GhiChu = N'Dữ liệu mẫu hệ thống';

