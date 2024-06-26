--create database LibraryManagement
--drop database LibraryManagement

--11
CREATE TABLE  [dbo].[User]
(
  Id INT NOT NULL IDENTITY(1,1),
  TenUser  NVARCHAR(50) NOT NULL UNIQUE,
  MatKhau  NVARCHAR(100) NOT NULL,
  FlagDel INT NOT NULL DEFAULT 0,
  AvatarLink  NVARCHAR(100),
  Email  NVARCHAR(100) NOT NULL UNIQUE,
  SoCCCD NVARCHAR(20) NOT NULL UNIQUE,
  SoDienThoai NVARCHAR(20) NOT NULL UNIQUE,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (Id)
);

--12
CREATE TABLE [dbo].[NhanVien]
(
  Id INT NOT NULL IDENTITY(1,1),
  TenNhanVien  NVARCHAR(50) NOT NULL,
  MatKhau  NVARCHAR(100) NOT NULL,
  VaiTro NVARCHAR(2) NOT NULL, --0:ADMIN, 1:STAFF
  Email  NVARCHAR(100) NOT NULL UNIQUE,
  SoDienThoai NVARCHAR(20) NOT NULL UNIQUE,
  DiaChi NVARCHAR(100) NOT NULL,
  FlagDel INT NOT NULL DEFAULT 0,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (Id)
)

--10
CREATE TABLE  [dbo].[Sach]
(
  Id INT NOT NULL IDENTITY(1,1),
  TenSach  NVARCHAR(100) NOT NULL,
  MoTa  NVARCHAR(300),
  GiaTien FLOAT NOT NULL,
  DanhGia INT,
  SoLuongTrongKho INT DEFAULT 0,
  LinkAnh  NVARCHAR(100) NOT NULL,
  TacGia  NVARCHAR(100) NOT NULL,
  NhaXuatBan  NVARCHAR(100) NOT NULL,
  FlagDel INT NOT NULL DEFAULT 0,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (Id)
);

--9
CREATE TABLE  [dbo].[YeuCauMuonSach]
(
  Id INT NOT NULL IDENTITY(1,1),
  NgayMuon DATETIME NOT NULL,
  NgayTra DATETIME NOT NULL,
  BoiThuong FLOAT DEFAULT 0,
  QuaHan INT DEFAULT 0,
  TrangThai INT NOT NULL DEFAULT 0, --0:Chua duoc duyet, 1:Da duoc duyet, 2:Dang muon, 3:Da tra, -1:Huy
  NguoiMuonId INT NOT NULL,
  SoTienDatCoc FLOAT NOT NULL,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (Id),
  FOREIGN KEY (NguoiMuonId) REFERENCES  [dbo].[User](Id)
);

--3
CREATE TABLE [dbo].[SachDuocMuon]
(
	SachId INT NOT NULL,
	YeuCauId INT NOT NULL,
	SoLuong INT NOT NULL,
	PRIMARY KEY (SachId, YeuCauId),
	FOREIGN KEY (SachId) REFERENCES [dbo].[Sach](Id),
	FOREIGN KEY (YeuCauId) REFERENCES [dbo].[YeuCauMuonSach](Id)
)

--8
CREATE TABLE  [dbo].[Blog]
(
  Id INT NOT NULL IDENTITY(1,1),
  TieuDe  NVARCHAR(100) NOT NULL,
  NoiDung  NVARCHAR(2000) NOT NULL,
  DanhGia INT,
  NgayTao DATETIME NOT NULL,
  TacGiaId INT NOT NULL,
  FlagDel INT NOT NULL DEFAULT 0,
  PRIMARY KEY (Id),
  FOREIGN KEY (TacGiaId) REFERENCES  [dbo].[User](Id)
);

--2
CREATE TABLE  [dbo].[BinhLuanSach]
(
  NoiDung  NVARCHAR(200) NOT NULL,
  DanhGia INT NOT NULL DEFAULT 0,
  EmailNguoiViet NVARCHAR(100),
  NgayTao DATETIME NOT NULL,
  Id INT NOT NULL IDENTITY(1,1),
  SachId INT NOT NULL,
  UserId INT NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (SachId) REFERENCES  [dbo].[Sach](Id),
  FOREIGN KEY (UserId) REFERENCES  [dbo].[User](Id)
);

--3
CREATE TABLE  [dbo].[BinhLuanBlog]
(
  Id INT NOT NULL IDENTITY(1,1),
  NoiDung  NVARCHAR(200) NOT NULL,
  NgayTao DATETIME NOT NULL,
  BlogId INT NOT NULL,
  UserId INT NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (BlogId) REFERENCES  [dbo].[Blog](Id) ON DELETE CASCADE,
  FOREIGN KEY (UserId) REFERENCES  [dbo].[User](Id) ON DELETE CASCADE
);

--7
CREATE TABLE  [dbo].[DanhMuc]
(
  Id INT NOT NULL IDENTITY(1,1),
  TenDanhMuc NVARCHAR(100) NOT NULL UNIQUE,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (Id)
);

--5
CREATE TABLE [dbo].[Tag]
(
  Id INT NOT NULL IDENTITY(1,1),
  TenTag NVARCHAR(50) NOT NULL UNIQUE,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (Id)
);

--6
CREATE TABLE  [dbo].[TheLoai]
(
  Id INT NOT NULL IDENTITY(1,1),
  TenTheLoai NVARCHAR(100) NOT NULL,
  DanhMucId INT NOT NULL,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (Id),
  FOREIGN KEY (DanhMucId) REFERENCES  [dbo].[DanhMuc](Id)
);

--1
CREATE TABLE  [dbo].[TagTheLoai]
(
  SachId INT NOT NULL,
  TheLoaiId INT NOT NULL,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (SachId, TheLoaiId),
  FOREIGN KEY (SachId) REFERENCES  [dbo].[Sach](Id) ON DELETE CASCADE,
  FOREIGN KEY (TheLoaiId) REFERENCES  [dbo].[TheLoai](Id) ON DELETE CASCADE
);

--4
CREATE TABLE  [dbo].[BlogTag]
(
  BlogId INT NOT NULL,
  TagId INT NOT NULL,
  DateCreated DATETIME NOT NULL,
  DateUpdated DATETIME,
  PRIMARY KEY (BlogId, TagId),
  FOREIGN KEY (BlogId) REFERENCES  [dbo].[Blog](Id) ON DELETE CASCADE,
  FOREIGN KEY (TagId) REFERENCES  [dbo].[Tag](Id) ON DELETE CASCADE
);

--drop table [TagTheLoai]
--drop table [BinhLuanSach]
--drop table [BinhLuanBlog]
--drop table [BlogTag]
--drop table [Tag]
--drop table [TheLoai]
--drop table [DanhMuc]
--drop table [Blog]
--drop table [SachDuocMuon]
--drop table [YeuCauMuonSach]
--drop table [Sach]
--drop table [User]
--drop table [NhanVien]

--Insert data
INSERT INTO DanhMuc (TenDanhMuc, DateCreated)
VALUES (N'Thơ', GETDATE()),
       (N'Truyện/Tiểu thuyết', GETDATE()),
       (N'Sách giáo khoa', GETDATE()),
       (N'Báo/Tạp chí', GETDATE()),
       (N'Nghiên cứu khoa học', GETDATE());

-- Insert sub-items for 'Thơ'
INSERT INTO TheLoai (TenTheLoai, DanhMucId, DateCreated)
VALUES (N'Nôm', 1, GETDATE()),
       (N'Hiện đại', 1, GETDATE()),
       (N'Thơ mới', 1, GETDATE()),
       (N'Thơ thời chiến', 1, GETDATE());

-- Insert sub-items for 'Truyện/Tiểu thuyết'
INSERT INTO TheLoai (TenTheLoai, DanhMucId, DateCreated)
VALUES (N'Truyện ngụ ngôn', 2, GETDATE()),
       (N'Truyện ngắn', 2, GETDATE()),
       (N'Truyện cười', 2, GETDATE()),
       (N'Truyện tranh', 2, GETDATE()),
       (N'Thần thoại', 2, GETDATE()),
       (N'Trinh thám', 2, GETDATE()),
       (N'Khoa học viễn tưởng', 2, GETDATE()),
       (N'Lãng mạn', 2, GETDATE()),
       (N'Tâm lý', 2, GETDATE());

-- Insert sub-items for 'Sách giáo khoa'
INSERT INTO TheLoai (TenTheLoai, DanhMucId, DateCreated)
VALUES (N'Toán học', 3, GETDATE()),
       (N'Vật lý', 3, GETDATE()),
       (N'Hóa học', 3, GETDATE()),
       (N'Văn học', 3, GETDATE()),
       (N'Kinh tế', 3, GETDATE()),
       (N'Chính trị/Xã hội', 3, GETDATE()),
       (N'Thiên văn học', 3, GETDATE()),
       (N'Khoa học máy tính', 3, GETDATE());

-- Insert sub-items for 'Báo/Tạp chí'
INSERT INTO TheLoai (TenTheLoai, DanhMucId, DateCreated)
VALUES (N'Tuổi trẻ', 4, GETDATE()),
       (N'VNExpress', 4, GETDATE()),
       (N'Thanh Niên', 4, GETDATE()),
       (N'New York Times', 4, GETDATE()),
       (N'The Washington Post', 4, GETDATE());

-- Insert sub-items for 'Nghiên cứu khoa học'
INSERT INTO TheLoai (TenTheLoai, DanhMucId, DateCreated)
VALUES (N'Toán học', 5, GETDATE()),
       (N'Vật lý', 5, GETDATE()),
       (N'Hóa học', 5, GETDATE()),
       (N'Văn học', 5, GETDATE()),
       (N'Kinh tế', 5, GETDATE()),
       (N'Chính trị/Xã hội', 5, GETDATE()),
       (N'Thiên văn học', 5, GETDATE()),
       (N'Khoa học máy tính', 5, GETDATE());