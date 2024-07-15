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
  VaiTro NVARCHAR(20) NOT NULL, --0:ADMIN, 1:STAFF
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
  MoTa  NVARCHAR(MAX),
  GiaTien FLOAT NOT NULL,
  DanhGia FLOAT NOT NULL DEFAULT 0,
  SoLuotDanhGia INT NOT NULL DEFAULT 0,
  SoLuongTrongKho INT NOT NULL DEFAULT 0,
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
  PhiMuonSach FLOAT NOT NULL,
  DiaChiNhanSach NVARCHAR(MAX),
  PhiVanChuyen FLOAT,
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
	SoTienDatCoc FLOAT NOT NULL,
	PRIMARY KEY (SachId, YeuCauId),
	FOREIGN KEY (SachId) REFERENCES [dbo].[Sach](Id),
	FOREIGN KEY (YeuCauId) REFERENCES [dbo].[YeuCauMuonSach](Id)
)

--8
CREATE TABLE  [dbo].[Blog]
(
  Id INT NOT NULL IDENTITY(1,1),
  TieuDe  NVARCHAR(100) NOT NULL,
  NoiDung  NVARCHAR(MAX) NOT NULL,
  DanhGia FLOAT NOT NULL DEFAULT 0,
  SoLuotDanhGia INT DEFAULT 0,
  NgayTao DATETIME NOT NULL,
  NgayCapNhat DATETIME,
  TacGiaId INT NOT NULL,
  FlagDel INT NOT NULL DEFAULT 0,
  PRIMARY KEY (Id),
  FOREIGN KEY (TacGiaId) REFERENCES  [dbo].[User](Id)
);

--2
CREATE TABLE  [dbo].[BinhLuanSach]
(
  NoiDung  NVARCHAR(MAX),
  DanhGia INT NOT NULL DEFAULT 0,
  NgayTao DATETIME NOT NULL,
  Id INT NOT NULL IDENTITY(1,1),
  SachId INT NOT NULL,
  UserId INT NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (SachId) REFERENCES  [dbo].[Sach](Id),
  FOREIGN KEY (UserId) REFERENCES  [dbo].[User](Id),
  CONSTRAINT UQ_Sach_User UNIQUE (SachId, UserId)
);

--3
CREATE TABLE  [dbo].[BinhLuanBlog]
(
  Id INT NOT NULL IDENTITY(1,1),
  NoiDung NVARCHAR(MAX),
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
  PRIMARY KEY (SachId, TheLoaiId),
  FOREIGN KEY (SachId) REFERENCES  [dbo].[Sach](Id) ON DELETE CASCADE,
  FOREIGN KEY (TheLoaiId) REFERENCES  [dbo].[TheLoai](Id) ON DELETE CASCADE
);

--4
CREATE TABLE  [dbo].[BlogTag]
(
  BlogId INT NOT NULL,
  TagId INT NOT NULL,
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

--delete from [TagTheLoai]
--delete from [BinhLuanSach]
--dbcc checkident ('BinhLuanSach', RESEED, 0)
--delete from [BinhLuanBlog]
--dbcc checkident ('BinhLuanBlog', RESEED, 0)
--delete from [BlogTag]
--delete from [Tag]
--dbcc checkident ('Tag', RESEED, 0)
--delete from [TheLoai]
--dbcc checkident ('TheLoai', RESEED, 0)
--delete from [DanhMuc]
--dbcc checkident ('DanhMuc', RESEED, 0)
--delete from [Blog]
--dbcc checkident ('Blog', RESEED, 0)
--delete from [SachDuocMuon]
--delete from [YeuCauMuonSach]
--dbcc checkident ('YeuCauMuonSach', RESEED, 0)
--delete from [Sach]
--dbcc checkident ('Sach', RESEED, 0)
--delete from [User]
--dbcc checkident ('[User]', RESEED, 0)
--delete from [NhanVien]
--dbcc checkident ('NhanVien', RESEED, 0)

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

-- Insert sub-items for 'Truyện/Tiểu thuyết'
INSERT INTO TheLoai (TenTheLoai, DanhMucId, DateCreated)
VALUES (N'Truyện người lớn', 2, GETDATE()),
       (N'Truyện dài', 2, GETDATE()),
       (N'Truyện kinh dị', 2, GETDATE());

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

-- Inserting books for each TheLoai under 'Thơ' (DanhMucId = 1)

-- For 'Nôm' (TheLoaiId = 1)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Tổng tuyển tập truyên thơ nôm các dân tộc thiểu số Việt Nam', N'Tổng tuyển tập truyên thơ nôm các dân tộc thiểu số Việt Nam', 50.0, 0, 100, N'img/product/nom1.jpg', N'Trịnh Khắc Mạnh', N'Nhà xuất bản khoa học xã hội', 0, GETDATE()),
(N'Thơ Hồ Xuân Hương', N'"Thơ Xuân Hương là đời Xuân Hương, là người của Xuân Hương trong đó. Thơ Xuân Hương là hồn, là xác, là mắt nhìn, tay sờ, chân đi, là nụ cười, nước mắt của Xuân Hương, là cá tính, là số phận của Xuân Hương. Người xưa nói: Không đổ máu huyết của mình vào trong văn, thì văn không hay. Đúng thế! Xuân Hương đã làm như thế". [Trích "Đời tức là văn, văn tức là đời" của Xuân Diệu].

 "Thơ Hồ Xuân Hương", cuốn sách nằm trong tủ sách "Tinh hoa Văn chương Việt" do NXB Văn học phát hành gồm 5 phần cơ bản: Thơ Nôm truyền tụng; Câu đối lưu truyền; Lưu hương ký; Năm bài thơ đề Vịnh Hạ Long; Chùm bài viết về Hồ Xuân Hương Với những nội dung trên, cuốn sách "Thơ Hồ Xuân Hương" sẽ giúp bạn đọc hiểu được không chỉ tác phẩm của bà mà còn hiểu về thân thế, sự nghiệp, những phát hiện mới về tiểu sử, và một số vấn đề tranh luận, đánh giá khác về "Bà chúa thơ Nôm" - Hồ Xuân Hương.', 60.0, 0, 100, N'img/product/nom2.jpg', N'Hồ Xuân Hương', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Thơ Nôm Lê Thánh Tông', N'Tác phẩm của Trương Hán Siêu còn lại không nhiều: 3 bài ký, 1 bài phú và 6 bài thơ. Bạch Đằng giang phú là tác phẩm nổi tiếng nhất của ông và cũng là bài phú nổi tiếng nhất trong số 13 bài phú chữ Hán hiện còn biết ở đời Trần (1225- 1400). Nét chủ đạo của ngòi bút Trương Hán Siêu là tinh thần yêu quý non sông đất nước, tự hào đối với truyền thống lịch sử vẻ vang, oanh liệt của dân tộc. Bên cạnh đấy, cũng bàng bạc trong thơ văn ông một sắc thái trữ tình hoài cổ, tuy không mấy nặng nề. Nghệ thuật ngôn ngữ của Trương Hán Siêu tinh tế, lắng đọng, man mác ở trong thơ, gân cốt chắc nịch trong phú, uyển chuyển, mềm mại trong ký.', 70.0, 0, 100, N'img/product/nom3.jpg', N'Lê Thánh Tông', N'Nhà Xuât Bản Bản Đồng Nai', 0, GETDATE()),
(N'Truyện Thơ Lục Vân Tiên', N'Nhân dịp kỷ niệm 200 năm sinh của cụ Đồ Chiểu và kỷ niệm 30 năm quan hệ Việt Nam - Hàn Quốc, Hội Di sản Văn hóa tỉnh Bến Tre phối hợp với quý doanh nghiệp Hàn Quốc tại TPHCM, Đại sứ quán Hàn Quốc tại Việt Nam và các học giả, dịch giả Việt Nam - Hàn Quốc thực hiện dự án sách Truyện thơ Nôm Lục Vân Tiên tác giả Nguyễn Đình Chiểu song ngữ Việt - Hàn.', 80.0, 0, 100, N'img/product/nom4.jpg', N'TS.Phạm Văn Luân', N'Đại học Quốc gia TP.HCM', 0, GETDATE()),
(N'Truyện Kiều', N'Trong lịch sử mấy ngàn năm của dân tộc ta, chưa bao giờ có tác phẩm nào được nhân dân yêu quý như Truyện Kiều. Mặc dù thể chế chính trị quốc gia thay đổi theo từng giai đoạn nhưng lòng người say mê Truyện Kiều không hề thay đổi. Vì sao vậy? Đào Nguyên Phổ, trong lời tựa cho quyển Đoạn trường tân thanh (tức Truyện Kiều) in năm 1902 đã trả lời: “Lời lẽ xinh xắn mà văn hoa, âm điệu ngân vang mà tròn trịa, tài liệu chọn rất rộng, sự việc kể rất tường, lượm lặt những diễm khúc tình tứ của cổ nhân, lại góp đến cả phương ngôn ngạn ngữ nước nhà, mặn mà, vụn vặt không sót, quê mùa tao nhã đều thu… khiến người cười, khiến người khóc, khiến người vui, khiến người buồn, khiến người giở đi giở lại ngàn lần, càng đọc thuộc lại càng không biết chán, thật là một khúc Nam âm tuyệt xướng, một điệu tình phổ bực đầu vậy”…

Viết về Kiều lấy gợi ý từ một tác phẩm văn xuôi nước ngoài, Nguyễn Du đã ký thác tâm sự về thời ông đang sống, về thân phận của người phụ nữ trong xã hội phong kiến, về khát vọng được sống và yêu. Từ điển Encyclopædia Britannica đã viết: "Truyện thơ của Nguyễn Du diễn đạt những đau đớn riêng tư và thể hiện lòng nhân đạo sâu sắc thông qua sự khai thác học thuyết của nhà Phật về nghiệp quả báo ứng cho những tội lỗi cá nhân”.

Từ đời này qua đời khác, dân ta say mê “khúc Nam âm tuyệt xướng” này rồi truyền cho nhau nhiều cách thưởng thức tác phẩm qua các trò ngâm Kiều, tập Kiều, lẩy Kiều, đố Kiều và cả bói Kiều nữa. Và trên hai trăm năm qua, những công trình tìm hiểu, thẩm bình, nghiên cứu Truyện Kiều không bao giờ gián đoạn.', 90.0, 0, 100, N'img/product/nom5.jpg', N'Nguyễn Du', N'Nhà xuất bản Trẻ', 0, GETDATE());

-- For 'Hiện đại' (TheLoaiId = 2)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Nguyễn Duy Nhà Thơ Hiện Đại Việt Nam', N'Nhà thơ Nguyễn Duy (sinh 1947) xuất hiện trên thi đàn Việt Nam từ giải nhất cuộc thi thơ báo Văn Nghệ (Hội Nhà văn Việt Nam) năm 1973. Từ đó thơ ông ngày càng phát triển và lan truyền sâu rộng trong cả giới văn chương và công chúng bạn đọc rộng rãi. Nói về thơ Nguyễn Duy mọi người thường nói đến tính chất dân gian hiện đại, ai cũng dễ thấy thơ ông có sự khác biệt so với thơ chung đương thời. 

Nhưng để nói ra được cái chất dân gian hiện đại ấy, cái khác biệt ấy của thơ Nguyễn Duy một cách thuyết phục về mặt khoa học văn chương thì không đơn giản, dễ dàng. Nhà nghiên cứu văn học Chu Văn Sơn sinh thời đã có một bài viết công phu đầy cảm xúc nhan đề "Nguyễn Duy thi sĩ thảo dân" chỉ ra được một số đặc điểm nổi bật trong sáng tác của nhà thơ. Và giờ đây nhà nghiên cứu Lã Nguyên (La Khắc Hòa) đã có hẳn một chuyên luận về thơ Nguyễn Duy để định danh ông là nhà thơ hiện đại Việt Nam.', 55.0, 0, 100, N'img/product/hiendai1.jpg', N'La Nguyễn', N'Nhà xuất bản khoa học và xã hội', 0, GETDATE()),
(N'Thơ Việt Nam hiện đại tiến trình & hiện đại', N'Nghiền ngẫm thành tựu của những lần giao thoa thơ ca Việt nam với thơ ca thế giới, PGS.TS Nguyễn Đăng Điệp đã rút ra nhận định khái quát đích đáng, không chỉ có ích cho những người làm công việc nghiên cứu văn học mà chính những người sáng tác thơ ca phải đặc biệt lưu tâm, tự rút ra những kinh nghiệm sáng tác. Ông ủng hộ thơ ca phải đổi mới, nếu không cách tân, không vượt thoát thì tự thân mỗi nhà thơ và rộng ra là cả nền thơ ca Việt Nam sẽ tự tạo cho mình một độ ỳ trong sáng tạo. Song, từ kinh nghiệm trong quá khứ, không phải mọi cách tân đều tồn tại trước thử thách của thời gian, vì thế các nhà thơ cần biết “làm giàu bản sắc thơ ca dân tộc trên cơ sở bồi đắp nguồn dưỡng chất mới; “phải chống lại sự sao chép vì sao chép trước sau gì cũng bị đào thải”. Về phía người đọc, PGS.TS Nguyễn Đăng Điệp cũng cho rằng, người đọc hôm nay phải dần quen với thơ ca đương đại vốn bị kêu ca là khó nhớ, khó thuộc bởi vì thơ ca đương đại không còn chú tâm vào gọt đẽo ngôn từ như trước.

Dự báo về thơ và người đọc trong xu hướng toàn cầu hóa, PGS.TS Nguyễn Đăng Điệp có cái nhìn khá lạc quan. Việc thơ ca hiện nay bùng nổ vô số cách diễn đạt, gần như mỗi người làm thơ có lối thơ cho riêng mình thay vì đồng nhất như trước đây là điều đáng mừng; như thế nền thơ ca mới có mặt bằng rộng để có thể làm nên một vài đỉnh cao mới. Sự đa dạng của thơ ca đương đại sẽ làm phân hóa người đọc (vốn đã không còn nhiều trong thời buổi bùng nổ truyền thông) nên vai trò phê bình thơ phải tinh sắc để định hướng cho người đọc là cần thiết và rất quan trọng.', 65.0, 0, 100, N'img/product/hiendai2.jpg', N'Nguyễn Đăng Hiệp', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Huy Cận về tác giả và tác phẩm', N'Cuốn Huy Cận Về Tác Gia Và Tác Phẩm tập hợp những bài nghiên cứu, phê bình của các nhà văn, nhà thơ, các cán bộ giảng dạy, các nhà nghiên cứu phê bình văn học, các nhà nghiên cứu văn hóa nước ngoài đã được công bố trên sách, báo, tạp chí. Các bài viết này được sắp xếp theo thứ tự thời gian và chủ đề, để bạn đọc có thể hình dung một cách tương đối có hệ thống và toàn diện về giá trị nội dung một cách tương đối có hệ thống và toàn diện về giá trị nội dung và nghệ thuật của thơ Huy Cận.
Sách gồm 4 phần:
Phần một - Những chặng đường thơ Huy Cận.
Phần hai - Thế giới nghệ thuật thơ Huy Cận.
Phần ba - Những hàng châu ngọc trong thơ Huy Cận.
Phần bốn - Thơ Huy Cận trong đôi mắt bạn bè ở nước ngoài.', 75.0, 0, 100, N'img/product/hiendai3.jpg', N'Trần Khanh Thành', N'Nhà xuất bản giáo dục', 0, GETDATE()),
(N'Sông muôn đời vẫn thế', N'Cái khác nhiều nhất là sự phát hiện một Trần Sang già dặn mà tinh tế hơn, trĩu nặng suy tư hơn, một Trần Sang vẫn chan chứa trữ tình nhưng đau đáu nỗi niềm thế sự hơn… Ở đó, tôi bắt gặp một đồng nghiệp văn chương ý thức sâu sắc về cái nghiệp mà mình đa mang, một ngòi bút thơ tử tế, có trách nhiệm với nghệ thuật và với cả cuộc đời này.

Như tựa đề cả tập, Sang hay viết về những dòng sông. Những dòng sông từ cuộc đời chảy vào thơ anh, những dòng sông từ quê anh chảy ra phố, những dòng sông từ hiện thực chảy vào cả siêu thực để Sang “vịn câu thơ” lần bước vào dòng chảy hiện đại, thậm chí hậu hiện đại…

Trong bài cảm nhận này, tôi chỉ xin chia sẻ những điều thú vị, tâm đắc của mình, từ góc nhìn không gian dòng sông trong “Sông muôn đời vẫn thế”.', 85.0, 0, 100, N'img/product/hiendai4.jpg', N'Trần Sang', N'Nhà xuất bản Nghệ Thuật An Giang', 0, GETDATE()),
(N'Vũ Khúc Tày', N'Y Phương là một trong số ít những nhà thơ xuất sắc của bộ phận thơ dân tộc thiểu số hiện đại nói riêng, của nền thơ Việt Nam hiện đại nói chung. Sau những tập thơ nổi tiếng như “Tiếng hát tháng giêng”, “Đàn then”, “Thơ Y Phương”, “Nói với con”… “Vũ khúc Tày”  là tập thơ mới nhất của nhà thơ người Tày này. Điểm đặc biệt ở tập thơ song ngữ Việt - Tày này là cả 108 bài thơ trong tập hầu hết đều là thơ tình.

Thơ Y Phương giản dị như suối nguồn và sâu, nhìn xuống đáy thi thoảng gặp những hạt vàng lấp lánh - đó là những biểu tượng độc đáo có tính mơ hồ đa nghĩa, Người tri âm gọi đó là vàng mười. Người vô tình gọi đó là hạt cát. Nhưng chính những biểu tượng ấy là minh chứng cho tính hiện đại và cá tính sáng tạo, độc đáo của nhà thơ, bên cạnh tính truyền thống biểu hiện trong đề tài quen thuộc, trong hệ thống thi ảnh đậm sắc thái văn hóa miền núi nói chung, văn hóa Tày nói riêng.

          Nổi bật trong tập thơ này là một tâm thế khát khao yêu thương, đa phần hồi cố, hoài niệm để bâng khuân trân trọng, pha chút ngậm ngùi, một phần nhỏ dành cho tình yêu trong hiện tại vẫn cháy rừng rực như một ngọn lửa trẻ trung và mãnh liệt. Không thể không nhắc tới trong “Vũ khúc Tày” có một số lượng không nhỏ viết về quê hương và con người miền núi với bao ngợi ca và tự hào. Nhưng sâu thẳm trong bài ca song ngữ “Vũ khúc Tày” này, rất kín đáo và không dễ nhận ra là nỗi cô đơn của một thi sỹ đã tự gọi mình là “Người đá”, “Người sông”, “Ông già trăm năm cô đơn”… Điều đáng quý và đáng trân trọng là nỗi cô đơn ấy cũng như suối nguồn Cao Bằng quê hương ông: - Suối dù vui hay buồn, giận hay thương, khổ đau hay hạnh phúc đều trong vắt, mát lành./.', 95.0, 0, 100, N'img/product/hiendai5.jpg', N'Y Phương', N'Nhà xuất bản đại học thái nguyên', 0, GETDATE());

-- For 'Thơ mới' (TheLoaiId = 3)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Thơ mới 1', N'Cuốn sách sưu tập, tổng hợp, thống kê các nguồn tài liệu trên sách báo, ấn phẩm xuất bản trước năm 1945 nhằm đưa đến một cái nhìn hệ thống, toàn cảnh về tất cả các vấn đề, sự kiện, hiện tượng liên quan đến phong trào Thơ mới Hà Nội. Với định hướng đó, đúng như tên gọi, cuốn sách lựa chọn cách trình bày tư liệu theo thể thức biên niên với 14 mục tương ứng với 14 năm - quãng thời gian Thơ mới xuất hiện và thoái trào, từ 1932 đến 1945. Trong mỗi năm, sự kiện sẽ được sắp xếp, trình bày theo cấp độ từng tháng, từng ngày theo đúng trật tự xuất hiện là thời điểm được xuất bản trên các ấn phẩm.

Với cách bố cục này, với sự phong phú của khốỉ lượng tư liệu được lựa chọn, cuốn sách thực sự như “một cuốn phim quay chậm” toàn cảnh phong trào Thơ mới Hà Nội. Ở đó, bức tranh lịch sử phong trào được phục hiện một cách chân thực, sinh động, toàn diện: bối cảnh xuất hiện; quá trình “đấu tranh" với Thơ cũ để từng bước khắng định giá trị, địa vị trên văn đàn; những quan niệm làm thơ, làm thơ mới thời bấy giờ; sự ra đời của những nhà thơ, thể thơ mới, những tập thơ, bài thơ mới; không khí sinh hoạt văn chương sôi động, tự do và dân chủ diễn ra đầu thế kỷ XX v.v... Có cả những sự kiện cụ thể, những đánh giá, bình luận chi tiết; lại cũng có cả những bài viết đánh giá khái quát phong trào trên một bình diện rộng... Dõi theo hành trình Thơ mới Hà Nội qua biên niên sự kiện, người đọc nhận thấy diện mạo chung và những đặc điểm cơ bản nhất của phong trào, không phải bằng những tổng kết, đánh giá, chiêm nghiệm có độ lùi lớn về thời gian, dưới tác động của nhiều phương pháp luận mà bằng chính sự tươi mới, sinh động, chân thực, khách quan của chính những sự kiện đã diễn ra thời bấy giờ.', 60.0, 0, 100, N'img/product/thomoi1.jpg', N'Nguyễn Hữu Sơn', N'Nhà xuất bản Hà Nội', 0, GETDATE()),
(N'Thơ mới những bước thăng trầm', N'Trong lịch sử lý luận, phê bình văn học có những cuốn sách mang tính chất đánh dấu sự phát triển tư duy, nhận thức về đối tượng nghiên cứu. Chuyên luận "Thơ mới những bước thăng trầm" của Giáo sư Lê Đình Kỵ (Nxb Tp Hồ Chí Minh, 1988, tái bản 1993) là một cuốn sách như thế. Từ chỗ đứng hiện tại là thời điểm đầu Đổi mới tác giả chuyên luận đã chọn điểm tựa vững chắc là quan điểm lịch sử cụ thể. Những trang thẩm bình tinh tế của tác giả về các hiện tượng Thơ mới cũng đã đồng thời làm tăng thêm sức thuyết phục cho chuyên luận. Cần nói ngay rằng nhiều ý kiến của Lê Đình Kỵ ngày hôm nay đã trở thành hiển nhiên theo quy luật mọi cái mới rồi sẽ trở nên không mới nữa. Nhưng ở thời điểm ra đời cuốn sách đã có đóng góp cần được ghi nhận một cách xứng đáng cho quá trình tiếp nhận Thơ mới (1932-1945), đặc biệt là về các phương diện lý luận, phương pháp luận.', 70.0, 0, 100, N'img/product/thomoi2.jpg', N'GS. Lê Đĩnh Kỵ', N'Nhà xuất bản TP. Hồ Chí Minh', 0, GETDATE()),
(N'Thơ mới 1932-1945 tuyển chọn', N'Đầu những năm 30 của thế kỷ XX, trong văn hóa Việt Nam diễn ra một cuộc vận động đổi mới thơ ca mạnh mẽ, làm xuất hiện một loạt nhà thơ mới, với cá tính sáng tác độc đáo, với những tác phẩm đặc sắc. Cuộc cách tân thơ ca này đi vào văn học sử với tên gọi đã trở nên quen thuộc là Phong trào Thơ mới, được xem như dấu son đậm trên bước chuyển vào thời kỳ phát triển mới của thơ ca và văn học tiếng Việt. Liền trong mấy năm gần đây, các tập sách và bộ sách về Thơ mới, về các tác giả xuất hiện từ Thơ mới... Cuốn sách này sẽ giới thiệu đến bạn đọc những bài thơ đặc sắc của các tác giả như:
Thế Lữ (1907 - 1989)
Lưu Trọng Lư (1911 - 1991)
Hàn Mặc Tử (1912 - 1940)
Trần Huyền Trân (1913 - 1989)
Nguyễn Nhược Pháp (1914 - 1938)
Bích Khê (1916 - 1946)', 80.0, 0, 100, N'img/product/thomoi3.jpg', N'Nhiều tác giả', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Thơ mới những chuyện chưa bao giờ cũ', N'Đầu những năm 30 của thế kỷ XX, trong văn hóa Việt Nam diễn ra một cuộc vận động đổi mới thơ ca mạnh mẽ, làm xuất hiện một loạt nhà thơ mới, với cá tính sáng tác độc đáo, với những tác phẩm đặc sắc. Cuộc cách tân thơ ca này đi vào văn học sử với tên gọi đã trở nên quen thuộc là Phong trào Thơ mới, được xem như dấu son đậm trên bước chuyển vào thời kỳ phát triển mới của thơ ca và văn học tiếng Việt. Liền trong mấy năm gần đây, các tập sách và bộ sách về Thơ mới, về các tác giả xuất hiện từ Thơ mới... Cuốn sách này sẽ giới thiệu đến bạn đọc những bài thơ đặc sắc của các tác giả như:
Thế Lữ (1907 - 1989)
Lưu Trọng Lư (1911 - 1991)
Hàn Mặc Tử (1912 - 1940)
Trần Huyền Trân (1913 - 1989)
Nguyễn Nhược Pháp (1914 - 1938)
Bích Khê (1916 - 1946)', 90.0, 0, 100, N'img/product/thomoi4.jpg', N'Nguyễn Hữu Sơn', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Thơ mới những chuyện chưa bao giờ cũ( bìa mới)', N'Đầu những năm 30 của thế kỷ XX, trong văn hóa Việt Nam diễn ra một cuộc vận động đổi mới thơ ca mạnh mẽ, làm xuất hiện một loạt nhà thơ mới, với cá tính sáng tác độc đáo, với những tác phẩm đặc sắc. Cuộc cách tân thơ ca này đi vào văn học sử với tên gọi đã trở nên quen thuộc là Phong trào Thơ mới, được xem như dấu son đậm trên bước chuyển vào thời kỳ phát triển mới của thơ ca và văn học tiếng Việt. Liền trong mấy năm gần đây, các tập sách và bộ sách về Thơ mới, về các tác giả xuất hiện từ Thơ mới... Cuốn sách này sẽ giới thiệu đến bạn đọc những bài thơ đặc sắc của các tác giả như:
Thế Lữ (1907 - 1989)
Lưu Trọng Lư (1911 - 1991)
Hàn Mặc Tử (1912 - 1940)
Trần Huyền Trân (1913 - 1989)
Nguyễn Nhược Pháp (1914 - 1938)
Bích Khê (1916 - 1946)', 100.0, 0, 100, N'img/product/thomoi5.jpg', N'Nguyễn Hữu Sơn', N'Nhà xuất bản văn học', 0, GETDATE());

-- For 'Thơ thời chiến' (TheLoaiId = 4)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Nhật kí trong tù', N'Đây là ấn phẩm kỷ niệm 133 năm Ngày sinh của Bác (19-5-1890 / 19-5-2023) và tròn 80 năm (1943-2023) ngày Bác viết tập thơ “Ngục trung nhật ký”.

Tháng 8-1942, Nguyễn Ái Quốc lấy tên Hồ Chí Minh với danh nghĩa là đại biểu của Việt Nam độc lập đồng minh và Phân bộ quốc tế phản xâm lược của Việt Nam sang Trung Quốc công tác. Khi đến Túc Vinh, Quảng Tây, Người bị chính quyền Tưởng Giới Thạch bắt giam vô cớ, và từ đây bắt đầu hành trình 13 tháng đầy gian nan, cực khổ trải qua 18 nhà lao của 13 huyện thuộc tỉnh Quảng Tây. Trong những tháng ngày đó, Người đã sáng tác tập thơ “Nhật ký trong tù”.

Mỗi bài thơ là tiếng lòng của tác giả, khắc họa sâu sắc tâm hồn, những suy nghĩ, tình cảm của Bác trong thời gian bị giam cầm nơi đất khách. Đó là lòng yêu nước tha thiết, mong được trở về hòa mình vào cuộc chiến đấu của đồng bào. Mặc dù chịu bao khổ cực, áp bức nhưng Người luôn dành tình yêu thương, sự quan tâm đến mọi người, đặc biệt là các bạn tù. Tình yêu thương bao la, vô bờ bến của Bác không chỉ dành cho mọi kiếp người, không phân biệt giai cấp, dân tộc mà còn là tình yêu thiên nhiên, hòa mình vào muôn cảnh vật. Toát lên từ toàn bộ tập “Nhật ký trong tù” là một tinh thần lạc quan cách mạng, niềm tin vào ngày mai tươi sáng, ý chí kiên cường, bền bỉ, lòng quyết tâm sắt đá của Người. Bản lĩnh của người chiến sĩ cộng sản, sức mạnh tinh thần lớn lao đã đưa Người vượt qua thử thách, đến với ngày tự do, trở về Tổ quốc thân yêu, lãnh đạo toàn dân giành độc lập, tự do cho dân tộc.', 65.0, 0, 100, N'img/product/thothoichien1.jpg', N'Hồ Chí Minh', N'Nhà xuất bản Chính trị quốc gia sự thật', 0, GETDATE()),
(N'Những lá thư thời chiến Việt Nam tuyển tập', N'Nội dung cuốn sách tuyển tập lá thư của những người lính chiến trường trong tổng số hàng triệu lá thư mà gia đình các anh, chị đã gửi đến, đã ghi tạc và hiển hiện chân thực một thế hệ thanh niên hào hoa ra trận, mang trong mình tình yêu Tổ quốc, yêu quê hương, yêu gia đình mãnh liệt; một lòng tin tuyệt đối vào Đảng, vào Bác Hồ; một ý chí chiến đấu cao, sự sẵn sàng hy sinh, dâng hiến tuổi thanh xuân.', 75.0, 0, 100, N'img/product/thothoichien2.jpg', N'Đặng Vương Hưng', N'Nhà xuất bản Chính trị quốc gia Sự thật', 0, GETDATE()),
(N'Một thoáng xanh xưa', N'Tập thơ “Một thoáng xanh xưa” của nhà thơ Võ Thị Hồng Tơ dành nhiều tình cảm với người lính, người mẹ, người chị, người em… chịu nhiều đau thương mất mát sau chiến tranh. Đồng thời ca ngợi tình yêu đôi lứa một lòng thủy chung son sắt và tình cảm tri ân của đồng đội với những người đã khuất. Ở mỗi số phận nhà thơ như hóa thân vào nhân vật để nói thay cho họ.

 ', 85.0, 0, 100, N'img/product/thothoichien3.jpg', N'Võ Thị Hồng Tơ', N'Nhà xuất bản tổng hợp Đà Nẵng', 0, GETDATE()),
(N'Đi bên mùa lá rung', N'Từng là lính biên phòng ở thời điểm đầu những năm 80 của thế kỷ trước, sau này, được đọc thơ Hoàng Quý, thấy anh nói thay được nỗi lòng mình trong những ngày giáp Tết: “Anh lính biên phòng ôm súng gác/ Sương vỡ như ngọc, sương như mưa/ Giờ này chắc mẹ ngồi cơi lửa/ Mơ chiều ba mươi con mẹ về” ("Ghi ở Hoàng Liên núi"). Biên cương nhiều sương, “sương như mưa” thì dễ hiểu nhưng “sương vỡ như ngọc” thì hình như mới có trong thơ Hoàng Quý.', 95.0, 0, 100, N'img/product/thothoichien4.jpg', N'Hoàng Qúy', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Đợi anh về', N'Tập thơ “Đợi anh về” bao gồm 180 bài thơ của 24 nhà thơ tiêu biểu nhất trong nền thơ ca Chiến tranh Vệ quốc như Ximonov, Olga Berggolts, Tvardovxki, Anna Akhmatova, Evtusenko… Hai dịch giả Nguyễn Huy Hoàng và Nguyễn Văn Minh đã dày công chọn lọc và dịch các bài thơ trong tập này.

Các tác phẩm được chọn trong tập thơ thể hiện chân thực về cuộc chiến, qua đó thể hiện tính cách con người Nga, văn hóa Nga. Cuộc chiến đó có sự tàn khốc, bi thương, nhưng hào hùng và đầy lạc quan, tin tưởng. Tinh thần của tập thơ giống với điều mà nhà mỹ học Borev đã nói: “Đó là những tiếng khóc đau thương về sự hy sinh và mất mát, nhưng đồng thời cũng là tiếng ca vinh quang về sự bất tử”.

Đây là lần đầu tiên, thơ ca chiến tranh Nga được giới thiệu một cách hệ thống và chọn lọc, giúp cho bạn đọc Việt Nam có một cái nhìn tổng thể về một mảng văn học đặc trưng và nổi tiếng, góp phần tôn vinh nền văn học Nga. Tuyển thơ “Đợi anh về” được mong đợi sẽ đóng góp như là một chiếc cầu nối giữa Văn học Nga và Văn học Việt Nam.', 105.0, 0, 100, N'img/product/thothoichien5.jpg', N'Nguyễn Huy Hoàng-Nguyễn Văn Minh', N'Nhà xuất bản Thông tin và Truyền thông', 0, GETDATE());

-- Inserting books for each TheLoai under 'Truyện/Tiểu thuyết' (DanhMucId = 2)

-- For 'Truyện ngụ ngôn' (TheLoaiId = 5)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'TRUYỆN NGỤ NGÔN THẾ GIỚI CHỌN LỌC', N'Con gà mái của người chủ nọ đẻ ra những quả trứng vàng. Chờ đợi cứ sau mỗi ngày có được một ủa trứng vàng, người chủ thấy sốt ruột quá nghĩ bụng: “Hẳn trong đó phải có cục vàng to lắm”.

Anh ta liền mổ gà mái để lấy cục vàng. Song khi mổ con gà mái ra cũng chẳng thấy khác gì con gà mái khác.    

 – Trứng vàng-

Tập Truyện ngụ ngôn thế giới chọn lọc được góp lại từ muôn phương, những truyện ngụ ngôn tuyệt hay của thế giới. Hy vọng, cuốn sách sẽ giúp các bạn tìm ra nhiều điều thú vị và bổ ích.', 20.0, 0, 100, N'img/product/ngungon1.jpg', N'Nguyễn Trọng Báu', N'Nhà xuất bản văn học', 0, GETDATE()),--
(N'Truyện ngụ ngôn Ê-Dốp', N'Cuốn sách tập hợp những câu chuyện ngụ ngôn kinh điển của tác giả Aesop. Những câu chuyện này đều gửi gắm những thông điệp ý nghĩa và gần gũi trong cuộc sống hàng ngày. Cuốn sách mang đến một thế giới trong sáng, thuần khiết với những trải nghiệm hồn nhiên. Hy vọng các bạn nhỏ khi đọc sách có thể tiếp thu những điều bổ ích, lý thú và trưởng thành hơn trong suy nghĩ.', 25.0, 0, 100, N'img/product/ngungon2.jpg', N'Mạnh Chương(dịch)', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Truyện ngụ ngôn ngụ ngôn & dân gian', N'Trong cuốn sách, chúng ta có thể thấy được những câu chuyện điển hình quen thuộc với tất cả mọi người, mỗi câu chuyện được chuyển thể thành từng bài theo áng thơ “Ngũ ngôn Tứ tuyệt”. Tập thơ này thể hiện rất rõ tư tưởng của tác giả về điều xấu, điều tốt, về lẽ phải, lẽ trái. Các bài thơ ngắn, đơn giản và dễ hiểu nên ai ai cũng có thể cảm nhận được kể cả các bạn đọc giả nhỏ tuổi.
Bên cạnh những điểm nổi bật của cuốn truyện thơ, điều mà tác giả muốn gửi gắm tới bạn đọc đó là những điển tích xưa vẫn để lại nhiều bài học đạo đức, thiết thực với cuộc sống hiện tại.', 30.0, 0, 100, N'img/product/ngungon3.jpg', N'Đỗ Trọng Kim', N'Nhà xuất bản Hội nhà văn', 0, GETDATE()),
(N'Những Câu Chuyện Ngụ Ngôn Hay Nhất', N'"Cái gì mà chúng ta học được ở tuổi thơ thì luôn còn mãi."

- Miguel de Cervantes Saavedra -

Những câu chuyện ngụ ngôn hay nhất là cuốn sách in màu với các hình mình họa hết sức ngộ nghĩnh, sống động, được lồng ghép khéo léo vào những cốt truyện quen thuộc như Con Cáo và chùm nho, Ếch ngồi đáy giếng... hoặc những mẩu chuyện dí dỏm, hài hước như Chàng lười, Cô gái vắt sữa bò, Con Sói biết vẫy đuôi... Đọc hết cả cuốn sách, các bạn nhỏ sẽ được làm quen với rất nhiều nhân vật đáng yêu, thông minh, chăm chỉ... và hiểu được nhiều điều qua các nhân vật tham lam, độc ác, gian xảo... Đặc biệt, cuối mỗi câu chuyện là Lời bàn ngắn gọn, dễ hiểu và mang tính gợi mở để các em hiểu và tự suy nghĩ, rút ra bài học cho bản thân. Cuốn sách chắc chắn sẽ là một lựa chọn thú vị, mang đến những giây phút bổ ích cho các bạn học sinh sau giờ học căng thẳng. ', 35.0, 0, 100, N'img/product/ngungon4.jpg', N'Trương Thái Hà Giang( dịch)', N'Nhà xuất bản thanh niên', 0, GETDATE()),
(N'Ngụ ngôn thế giới hay nhất', N'Từ chút chít, chiêm chiếp đến gầm gừ, gào rú… đời sống muôn loài lần lượt xuất hiện trong những tập sách xinh đẹp này.
Nhà xuất bản Kim Đồng trân trọng giới thiệu đến bạn đọc kho tàng những điều kì thú từ động vật do người kể chuyện được yêu mến nhất nước Anh, Michael Morpurgo, tuyển chọn.', 40.0, 0, 100, N'img/product/ngungon5.jpg', N'Michael Morpurgo', N'Nhà xuất bản Kim Đồng', 0, GETDATE());

-- For 'Tiểu thuyết lịch sử' (TheLoaiId = 6)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Trường Ca Achilles', N'Hy Lạp vào thời hoàng kim của các anh hùng. Patroclus là một hoàng tử trẻ vụng về, bị trục xuất tới vương quốc Phthia và được nuôi dạy dưới sự che chở của vua Peleus cùng cậu con trai hoàng kim của ngài, Achilles. “Người Hy Lạp vĩ đại nhất” – mạnh mẽ, đẹp đẽ, và mang nửa dòng máu của mộtnữ thần – Achilles là tất cả những gì mà Patroclus không bao giờ có được. Nhưng bất chấp sự khác biệt giữa họ, hai cậu bé trở thành chiến hữu trung thành của nhau. Tình cảm giữa họ càng đậm sâu khi cả hai lớn lên thành những chàng trai trẻ, thành thạo trong kĩ nghệ chiến đấu và y dược.

Khi tin tức truyền tới rằng nàng Helen xứ Sparta đã bị bắt cóc, những chiến binh Hy Lạp, bị trói buộc bởi lời thề máu, phải nhân danh nàng mà vây hãm thành Troy. Bị cám dỗ bởi lời hứa hẹn về một số mệnh huy hoàng, Achilles tham gia hàng ngũ của họ. Bị giằng xé giữa tình yêu và nỗi lo sợ dành cho người bạn của mình, Patroclus ra trận theo Achilles. Họ không hay biết rằng các nữ thần Số Phận sẽ thử thách cả hai người họ hơn bao giờ hết và đòi hỏi một sự hi sinh khủng khiếp.

Dựa trên nền tảng của sử thi Iliad, câu chuyện về cuộc chiến thành Troy vĩ đại đã được Madeline Miller kể lại với tiết tấu dồn dập, lôi cuốn, và không kém phần xúc động, đánh dấu sự khởi đầu của một sự nghiệp rực rỡ.', 30.0, 0, 100, N'img/product/tieuthuyetls1.jpg', N'Madeline Miller', N'Nhà xuất bản Kim Đồng', 0, GETDATE()),
(N'Harry Potter Tập 8: Harry Porter And The Cursed Child - Parts One & Two', N'Dựa trên tác phẩm mới nhất của J.K Rowling, Jack Thorne và John Tiffany, đây là một kịch bản viết bởi Jack Thorne, Harry Potter và đứa trẻ bị nguyền rủa là phần 8 của series Harry Potter và là truyện Harry Potter đầu tiên được lên sân khấu. Vở kịch đã được công chiếu toàn thế giới vào ngày 30.07.2016.

 

Làm Harry Potter không hề dễ và chẳng dễ dàng hơn khi Harry giờ đây là nhân viên làm việc quá sức mình tại bộ phát thuật, đồng thời lại là chồng và cha của ba đứa trẻ đi học.


Trong khi Harry phải vật lộn với quá khứ không chịu nguôi đi, người con trai nhỏ nhất là Albus phải gánh chịu di sản nặng nề của gia đình mà cậu bé không bao giờ muốn. Với tương lai và quá khứ bị hợp thể oái ăm, cả cha lẫn con nhận ra một sự thật khó chịu: Đôi lúc, bóng tối phủ đến từ những nơi không ngờ nhất.

 

Cho đến cuối quyển sách này, chân dung của đứa trẻ bị nguyền rủa vẫn là một ẩn số, liệu đó là Albus - con trai của Harry Potter hay là Scorpius Malfoy - con trai của Draco - kẻ thù của Harry thời đi học?', 35.0, 0, 100, N'img/product/tieuthuyetls2.jpg', N' J.K. Rowling', N'Nhà xuất bản Trẻ', 0, GETDATE()),
(N'HARRY POTTER VÀ HOÀNG TỬ LAI', N'Đây là năm thứ 6 của Harry Potter tại trường Hogwarts. Trong lúc những thế lực hắc ám của Voldemort gieo rắc nỗi kinh hoàng và sợ hãi ở khắp nơi, mọi chuyện càng lúc càng trở nên rõ ràng hơn đối với Harry, rằng cậu sẽ sớm phải đối diện với định mệnh của mình. Nhưng liệu Harry đã sẵn sàng vượt qua những thử thách đang chờ đợi phía trước?

Trong cuộc phiêu lưu tăm tối và nghẹt thở nhất của mình, J.K.Rowling bắt đầu tài tình tháo gỡ từng mắc lưới phức tạp mà cô đã mạng lên, cũng là lúc chúng ta khám phá ra sự thật về Harry, cụ Dumblebore, thầy Snape và, tất nhiên, Kẻ Chớ Gọi Tên Ra…

‘Diễn biến nhanh, huyền bí, hấp dẫn và chặt chẽ trong từng chi tiết.’ - Daily Mail', 40.0, 0, 100, N'img/product/tieuthuyetls3.jpg', N'J.K.Rowling', N'Nhà xuất bản Trẻ', 0, GETDATE()),
(N'Harry Potter Và Tên Tù Nhân Ngục Azkaban', N'Harry Potter may mắn sống sót đến tuổi 13, sau nhiều cuộc tấn công của Chúa tể hắc ám.

Nhưng hy vọng có một học kỳ yên ổn với Quidditch của cậu đã tiêu tan thành mây khói khi một kẻ điên cuồng giết người hàng loạt vừa thoát khỏi nhà tù Azkaban, với sự lùng sục của những cai tù là giám ngục.

Dường như trường Hogwarts là nơi an toàn nhất cho Harry lúc này. Nhưng có phải là sự trùng hợp khi cậu luôn cảm giác có ai đang quan sát mình từ bóng đêm, và những điềm báo của giáo sư Trelawney liệu có chính xác?

"Câu chuyện được kể với trí tưởng tượng bay bổng, sự hài hước bất tận có thể quyến rũ cả người lớn lẫn trẻ em."

- Sunday Express
', 45.0, 0, 100, N'img/product/tieuthuyetls4.jpg', N'J.K.Rowling', N'Nhà xuất bản Trẻ', 0, GETDATE()),
(N'Trăm năm cô đơn', N'Trăm Năm Cô Đơn

“Chỉ với một bước nhảy, Gabriel García Márquez đã vụt lên ngang hàng với Günter Grass và Vladimir Nabokov.”

- The New York Times

“Xuất sắc, hỗn độn, với tầm ảnh hưởng lớn ảnh…  Một thiên tiểu thuyết vĩ đại và đầy tính nhạc.”

- The Times

Ở Mỹ Latin, ai ai cũng đọc tác phẩm này, từ các giáo sư cho đến dân lao động và cả gái điếm.”

- The Sydney Morning Herald', 50.0, 0, 100, N'img/product/tieuthuyetls5.jpg', N'Gabriel García Márquez', N'Nhà xuất bản Văn Học', 0, GETDATE());

-- For 'Truyện dài' (TheLoaiId = 7)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'The Stand', N'Đầu tiên là những ngày của bệnh dịch hạch, Rồi đến những giấc mơ,

Những giấc mơ cảnh báo về sự xuất hiện của người đàn ông bóng tối, Kẻ bỏ đạo của cái chết, đôi giày cao gót mòn vẹt của anh ta lê bước trên những con đường đêm, Lãnh chúa của ngôi nhà mồ và Hoàng tử của Ác ma,

Thời gian của anh ấy đã đến, Đế chế của anh ta phát triển ở phía tây và Ngày tận thế hiện ra lờ mờ,', 35.0, 0, 100, N'img/product/truyendai1.jpg', N'Stephen King', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'The Dark Tower', N'Tòa tháp bóng tối đình đám của StephenKing đã có tại Thesaurus', 40.0, 0, 100, N'img/product/truyendai2.jpg', N'Stephen King', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Hồi ức về những cô gái điếm buồn của tôi', N'“Mỗi một cuộc sống, mỗi một số phận đều có những nỗi niềm, những suy tư, trăn trở riêng và đáng để ta suy ngẫm…” - First News phối hợp với Nhà xuất bản Tổng Hợp phát hành một tác phẩm nổi tiếng "Hồi Ức Về Những Cô Gái Điếm Buồn Của Tôi" dựa trên tác phẩm nổi tiếng “Memoria de mis putas tristes” của nhà văn Gabriel García Márques', 45.0, 0, 100, N'img/product/truyendai3.jpg', N'Gabriel García Márquez', N'Nhà xuất bản tổng hợp tp Hồ CHí Minh', 0, GETDATE()),
(N'The WasteLands', N'Tòa tháp bóng tối đình đám của StephenKing đã có tại Thesaurus', 50.0, 0, 100, N'img/product/truyendai4.jpg', N'Stephen King', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'The Gunslinger', N'Tòa tháp bóng tối đình đám của StephenKing đã có tại Thesaurus', 55.0, 0, 100, N'img/product/truyendai5.jpg', N'Stephen King', N'Nhà xuất bản văn học', 0, GETDATE());

-- For 'Tiểu thuyết tình cảm' (TheLoaiId = 8)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Hãy Kể giấc mơ của em', N'Hãy kể giấc mơ của em” có văn phong và cốt truyện rất giống với phong cách nhà văn đương đại mà tôi hâm mộ Guillaume Musso. Mở đầu “Hãy kể giấc mơ của em” là một bài đồng dao vô nghĩa của Anh: “Một xu một quận chỉ - Một xu một cái kim - Đó là cách tiêu tiền - Bốp! Đi đời con chồn”. Các vụ án giết người xảy ra, với 05 người đàn ông đều bị đâm chết một cách man rợ ở 05 địa điểm khác nhau và đều bị cắt lìa bộ phận nhạy cảm nhất. Cảnh sát vào cuộc, hồ sơ cho thấy có 03 đối tượng tình nghi, gồm: Alette Peters đáng yêu và kiêu hãnh, Toni Prescott đầy nhục cảm và hận thù, Ashley Patterson yếu mềm và đầy tổn thương. Nhưng những mẫu vân tay và AND tại hiện trường các vụ án của cả 03 nghi phạm lại hoàn toàn trùng khớp với nhau, điều đó chứng tỏ 03 người họ chỉ là 01 người duy nhất.
Vậy đó là ai? Và cứ thế, Sidney Sheldon với tài dẫn dắt cực kỳ ảo diệu cuốn người đọc vào quá trình điều tra, xét xử, tranh biện, tuyên án và thực thi bản án làm người đọc đi từ bất ngờ này đến sửng sốt khác. Những bí mật hé lộ, logic từ quá khứ đến hiện tại vô cùng hoàn hảo về bộ ba Alette - Toni - Ashley. Trong 03 người ai là kẻ giết người? ai là nạn nhân?', 40.0, 0, 100, N'img/product/tieuthuyettc1.jpg', N'Sidney Sheldon', N'Nhà xuất bản nhân dân', 0, GETDATE()),
(N'Chạng vạng', N'Bella Swan chuyển đến Forks và gặp gỡ Edward Cullen, một ma cà rồng, từ đó bắt đầu mối tình đầy nguy hiểm và mê hoặc giữa hai người.', 45.0, 0, 100, N'img/product/tieuthuyettc2.jpg', N'Stephenie Meyer', N'Nhà xuất bản trẻ', 0, GETDATE()),
(N'Hừng Đông', N'Sau khi Edward rời bỏ Bella để bảo vệ cô, Bella phải đối mặt với nỗi đau và tìm sự an ủi từ người bạn Jacob Black, một người sói, dẫn đến nhiều xung đột.', 50.0, 0, 100, N'img/product/tieuthuyettc3.jpg', N'Stephenie Meyer', N'Nhà xuất bản trẻ', 0, GETDATE()),
(N'Trăng non', N'Bella bị giằng xé giữa tình yêu với Edward và tình bạn sâu sắc với Jacob, trong khi những hiểm nguy từ thế lực bên ngoài đe dọa cuộc sống của họ.', 55.0, 0, 100, N'img/product/tieuthuyettc4.jpg', N'Stephenie Meyer', N'Nhà xuất bản trẻ', 0, GETDATE()),
(N'Nhật Thực', N' Bella và Edward kết hôn và đối mặt với những thử thách lớn hơn khi Bella mang thai một đứa con nửa người nửa ma cà rồng, đặt gia đình Cullen vào nguy hiểm tột cùng.', 60.0, 0, 100, N'img/product/tieuthuyettc5.jpg', N'Stephenie Meyer', N'Nhà xuất bản trẻ', 0, GETDATE());

-- For 'Tiểu thuyết phiêu lưu' (TheLoaiId = 9)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Moby Dick', N'Cuộc hành trình bi tráng của thuyền trưởng Ahab và thủy thủ đoàn trên tàu Pequod trong cuộc săn đuổi cá voi trắng khổng lồ Moby Dick, biểu tượng của sự ám ảnh và thù hận.1', 45.0, 0, 100, N'img/product/tieuthuyetpl1.jpg', N'Herman Melville', N'Nhà xuất bản trẻ', 0, GETDATE()),
(N'2 vạn dặm dưới đáy biển', N'Thuyền trưởng Nemo dẫn dắt giáo sư Aronnax và các bạn đồng hành trên con tàu ngầm Nautilus trong cuộc hành trình khám phá các đại dương đầy kỳ thú và bí ẩn.', 50.0, 0, 100, N'img/product/tieuthuyetpl2.jpg', N'Jules Verne', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Bụi Sao', N'Chàng trai trẻ Tristran Thorn bước vào thế giới thần tiên để tìm một ngôi sao băng nhằm chiếm được trái tim của người mình yêu, nhưng phát hiện ra những điều kỳ diệu và nguy hiểm không ngờ tới.', 55.0, 0, 100, N'img/product/tieuthuyetpl3.jpg', N'Neil Gaiman', N'Nhà xuất bản trẻ', 0, GETDATE()),
(N'Thuật sĩ-Điều ước cuối cùng', N'Tập truyện ngắn giới thiệu Geralt of Rivia, một thợ săn quái vật chuyên nghiệp, với những cuộc phiêu lưu kỳ lạ và đầy mạo hiểm, khám phá sự phức tạp giữa thiện và ác.', 60.0, 0, 100, N'img/product/tieuthuyetpl4.jpg', N'Andrzej Sapkowski', N'Nhà xuất bản văn học', 0, GETDATE()),
(N'Thuật sĩ', N'Geralt phải bảo vệ cô bé Ciri, người mang trong mình sức mạnh khủng khiếp, khi các thế lực chính trị và ma thuật đe dọa xé toạc thế giới.', 65.0, 0, 100, N'img/product/tieuthuyetpl5.jpg', N'Andrzej Sapkowski', N'Nhà xuất bản trẻ', 0, GETDATE());

-- Inserting books for each TheLoai under 'Sách giáo khoa' (DanhMucId = 3)

-- For 'Tiểu học' (TheLoaiId = 10)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Sách giáo khoa toán 1', N'Sách giáo khoa toán 1', 15.0, 0, 100, N'img/product/sachgiaokhoa_tieuhoc1.jpg', N'Nhiều tác giả', N'Nhà xuất bản đại học sư phạm', 0, GETDATE()),
(N'Sách giáo khoa tiếng anh 3', N'Sách giáo khoa tiếng anh 3', 20.0, 0, 100, N'img/product/sachgiaokhoa_tieuhoc2.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa âm nhạc 1', N'Sách giáo khoa âm nhạc 1', 25.0, 0, 100, N'img/product/sachgiaokhoa_tieuhoc3.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa đạo đức 1', N'Sách giáo khoa đạo đức 1', 30.0, 0, 100, N'img/product/sachgiaokhoa_tieuhoc4.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa Tiểu học toán', N'Sách giáo khoa Tiểu học toán', 35.0, 0, 100, N'img/product/sachgiaokhoa_tieuhoc5.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE());

-- For 'Trung học cơ sở' (TheLoaiId = 11)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Sách giáo khoa Trung học cơ sở ngữ văn', N'Sách giáo khoa Trung học cơ sở ngữ văn', 20.0, 0, 100, N'img/product/sachgiaokhoa_thcs1.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa Trung học cơ sở tin học', N'Sách giáo khoa Trung học cơ sở tin học-phiên bản dành cho giáo viên', 25.0, 0, 100, N'img/product/sachgiaokhoa_thcs2.jpg', N'Nhiều tác giả', N'Nhà xuất bản đại học sư phạm', 0, GETDATE()),
(N'Tài liệu giáo dục địa phương tỉnh Sóc Trăng', N'Tài liệu giáo dục địa phương tỉnh Sóc Trăng', 30.0, 0, 100, N'img/product/sachgiaokhoa_thcs3.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa Trung học cơ sở toán 9', N'Mô tả Sách giáo khoa Trung học cơ sở 4', 35.0, 0, 100, N'img/product/sachgiaokhoa_thcs4.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa Trung học cơ sở toán 7', N'Mô tả Sách giáo khoa Trung học cơ sở 5', 40.0, 0, 100, N'img/product/sachgiaokhoa_thcs5.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE());

-- For 'Trung học phổ thông' (TheLoaiId = 12)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Sách giáo khoa giải tích 12', N'Sách giáo khoa giải tích 12', 25.0, 0, 100, N'img/product/sachgiaokhoa_thpt1.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa giải tích & đại số 11', N'Sách giáo khoa giải tích & đại số 11', 30.0, 0, 100, N'img/product/sachgiaokhoa_thpt2.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa hình học 12', N'Sách giáo khoa hình học 12', 35.0, 0, 100, N'img/product/sachgiaokhoa_thpt3.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa đại số 10', N'Sách giáo khoa đại số 10', 40.0, 0, 100, N'img/product/sachgiaokhoa_thpt4.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE()),
(N'Sách giáo khoa ngữ văn 10', N'Sách giáo khoa ngữ văn 10', 45.0, 0, 100, N'img/product/sachgiaokhoa_thpt5.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục đại học Huế', 0, GETDATE());

-- For 'Sách tham khảo' (TheLoaiId = 13)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Sách tham khảo toán 10', N'Hỗ trợ em chinh phục toán 10', 30.0, 0, 100, N'img/product/sachthamkhao1.jpg', N'Hoàng Xuân Nhàn', N'Nhà xuất bản Đại học quốc gia Hà Nội', 0, GETDATE()),
(N'Sách tham khảo toán 11', N'Hỗ trợ em chinh phục toán 11', 35.0, 0, 100, N'img/product/sachthamkhao2.jpg', N'Hoàng Xuân Nhàn', N'Nhà xuất bản Đại học Sư phạm', 0, GETDATE()),
(N'Sách học tốt ngữ văn 6', N'Hỗ trợ em chinh phục ngữ văn 6', 40.0, 0, 100, N'img/product/sachthamkhao3.jpg', N'Hoàng Thị Lâm Nho', N'Nhà xuất bản Thanh niên', 0, GETDATE()),
(N'Sách học tốt ngữ văn 6(tap 1 va 2)', N'Hỗ trợ em chinh phục ngữ văn 6', 45.0, 0, 100, N'img/product/sachthamkhao4.jpg', N'Hoàng Vân', N'Nhà xuất bản Đà Nẵng', 0, GETDATE()),
(N'Sách học tốt ngữ văn 7', N'Hỗ trợ em chinh phục ngữ văn 7', 50.0, 0, 100, N'img/product/sachthamkhao5.jpg', N'Hoàng Thị Lâm Nho', N'Nhà xuất bản Giáo dục Việt Nam', 0, GETDATE());

-- Inserting books for each TheLoai under 'Báo/Tạp chí' (DanhMucId = 4)

-- For 'Tuổi trẻ' (TheLoaiId = 14)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Mặt trời nhỏ(bản giáng sinh)', N'Ấn phẩm phụ của báo gia đình việt nam', 15.0, 0, 100, N'img/product/tuoitre1.jpg', N'Nhiều tác giả', N'báo gia đình Việt Nam', 0, GETDATE()),
(N'Mặt trời nhỏ', N'Ấn phẩm phụ của báo gia đình việt nam', 18.0, 0, 100, N'img/product/tuoitre2.jpg', N'Nhiều tác giả', N'báo gia đình Việt Nam', 0, GETDATE()),
(N'Người nhện đối đầu Mysterio', N'Người nhện đối đầu Mysterio', 22.0, 0, 100, N'img/product/tuoitre3.jpg', N'Nhiều tác giả', N'Nhà xuất bản Kim Đồng', 0, GETDATE()),
(N'Người nhện đối đầu Người nước', N'Người nhện đối đầu Người nước', 27.0, 0, 100, N'img/product/tuoitre4.jpg', N'Nhiều tác giả', N'Nhà xuất bản Kim Đồng', 0, GETDATE()),
(N'Người nhện đối đầu Venom', N'Người nhện đối đầu Venom', 30.0, 0, 100, N'img/product/tuoitre5.jpg', N'Nhiều tác giả', N'Nhà xuất bản Kim Đồng', 0, GETDATE());

-- For 'Người lao động' (TheLoaiId = 15)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Báo Người lao động 1', N'Báo Người lao động 1', 20.0, 0, 100, N'img/product/nguoilaodong1.jpg', N'Nhiều tác giả', N'Cơ quan của thành ủy thành phố Hồ Chí Minh', 0, GETDATE()),
(N'Báo Người lao động 2', N'Báo Người lao động  2', 25.0, 0, 100, N'img/product/nguoilaodong2.jpg', N'Nhiều tác giả', N'Cơ quan của thành ủy thành phố Hồ Chí Minh', 0, GETDATE()),
(N'Báo Người lao động 3', N'Báo Người lao động 3', 30.0, 0, 100, N'img/product/nguoilaodong3.jpg', N'Nhiều tác giả', N'Cơ quan của thành ủy thành phố Hồ Chí Minh', 0, GETDATE()),
(N'Báo Người lao động 4', N'Báo Người lao động 4', 35.0, 0, 100, N'img/product/nguoilaodong4.jpg', N'Nhiều tác giả', N'Cơ quan của thành ủy thành phố Hồ Chí Minh', 0, GETDATE()),
(N'Báo Người lao động 5', N'Báo Người lao động 5', 40.0, 0, 100, N'img/product/nguoilaodong5.jpg', N'Nhiều tác giả', N'Cơ quan của thành ủy thành phố Hồ Chí Minh', 0, GETDATE());

-- For 'Công an' (TheLoaiId = 16)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Báo Công an 1', N'Báo Công an 1', 18.0, 0, 100, N'img/product/congan1.jpg', N'Nhiều tác giả', N'Cơ quan của đảng ủy công an trung ương và bộ công an', 0, GETDATE()),
(N'Báo Công an 2', N'Báo Công an 2', 22.0, 0, 100, N'img/product/congan2.jpg', N'Nhiều tác giả', N'Cơ quan của đảng ủy công an trung ương và bộ công an', 0, GETDATE()),
(N'Báo Công an 3', N'Báo Công an 3', 26.0, 0, 100, N'img/product/congan3.jpg', N'Nhiều tác giả', N'Cơ quan của đảng ủy công an trung ương và bộ công an', 0, GETDATE()),
(N'Báo Công an 4', N'Báo Công an 4', 30.0, 0, 100, N'img/product/congan4.jpg', N'Nhiều tác giả', N'Cơ quan của đảng ủy công an trung ương và bộ công an', 0, GETDATE()),
(N'Báo Công an 5', N'Báo Công an 5', 35.0, 0, 100, N'img/product/congan5.jpg', N'Nhiều tác giả', N'Cơ quan của đảng ủy công an trung ương và bộ công an', 0, GETDATE());

-- For 'Tạp chí thời trang' (TheLoaiId = 17)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Tạp chí thời trang Details', N'Tạp chí thời trang Details', 25.0, 0, 100, N'img/product/tapchithoitrang1.jpg', N'Nhiều tác giả', N'Details', 0, GETDATE()),
(N'Tạp chí thời trang Vougue', N'Tạp chí thời trang Vougue', 30.0, 0, 100, N'img/product/tapchithoitrang2.jpg', N'Nhiều tác giả', N'Vogue', 0, GETDATE()),
(N'Tạp chí thời trang Fashion', N'Tạp chí thời trang Fashion', 35.0, 0, 100, N'img/product/tapchithoitrang3.jpg', N'Nhiều tác giả', N'Fashion', 0, GETDATE()),
(N'Tạp chí thời trang men"s style', N'Tạp chí thời trang men"s style', 40.0, 0, 100, N'img/product/tapchithoitrang4.jpg', N'Nhiều tác giả', N'Nmen"s style', 0, GETDATE()),
(N'Tạp chí thời trang Vougue', N'Tạp chí thời trang Vougue', 45.0, 0, 100, N'img/product/tapchithoitrang5.jpg', N'Nhiều tác giả', N'Vougue', 0, GETDATE());

-- For 'Tạp chí khoa học' (TheLoaiId = 18)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Tạp chí khoa học Science', N'Tạp chí khoa học Science', 30.0, 0, 100, N'img/product/tapchikhoahoc1.jpg', N'Nhiều tác giả', N'Hiệp hội khoa học Hoa Kỳ', 0, GETDATE()),
(N'Tạp chí khoa học Science-Human Reproduction', N'Tạp chí khoa học Science-Human Reproduction', 35.0, 0, 100, N'img/product/tapchikhoahoc2.jpg', N'Nhiều tác giả', N'Hiệp hội khoa học Hoa Kỳ', 0, GETDATE()),
(N'Tạp chí khoa học Science Neutral.Net', N'Tạp chí khoa học Science Neutral.Net', 40.0, 0, 100, N'img/product/tapchikhoahoc3.jpg', N'Nhiều tác giả', N'Hiệp hội khoa học Hoa Kỳ', 0, GETDATE()),
(N'Tạp chí khoa học Popular Science', N'Tạp chí khoa học Popular Science', 45.0, 0, 100, N'img/product/tapchikhoahoc4.jpg', N'Nhiều tác giả', N'Hiệp hội khoa học Hoa Kỳ', 0, GETDATE()),
(N'Tạp chí khoa học Science Illustrard', N'Tạp chí khoa học Science Illustrard', 50.0, 0, 100, N'img/product/tapchikhoahoc5.jpg', N'Nhiều tác giả', N'Hiệp hội khoa học Hoa Kỳ', 0, GETDATE());

-- Inserting books for each TheLoai under 'Khác' (DanhMucId = 5)

-- For 'Sách văn học' (TheLoaiId = 19)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Nhà Giả Kim', N'Một câu chuyện phiêu lưu đầy cảm hứng về cuộc hành trình tìm kiếm ước mơ và ý nghĩa của cuộc sống.', 60.0, 0, 100, N'img/product/sachvanhoc1.jpg', N'Paulo Coelho', N'Nhà xuất bản Văn Học', 0, GETDATE()),
(N'Hành Tinh Cát', N'Một tác phẩm khoa học viễn tưởng kinh điển về cuộc chiến tranh giành quyền lực và sinh tồn trên hành tinh sa mạc Arrakis.', 65.0, 0, 100, N'img/product/sachvanhoc2.jpg', N'Frank Herbert', N'Nhà xuất bản Trẻ', 0, GETDATE()),
(N'Circe', N'Câu chuyện huyền thoại về Circe, nữ thần và phù thủy, từ cuộc sống cô độc đến sức mạnh và sự kiên định trong thế giới của thần thoại Hy Lạp.', 70.0, 0, 100, N'img/product/sachvanhoc3.jpg', N'Madeline Miller', N'Nhà xuất bản Văn Hóa - Văn Nghệ', 0, GETDATE()),
(N'Gia Tộc Dối Trá', N'Truyện về những bí mật đen tối và những mối quan hệ phức tạp trong một gia đình giàu có.', 75.0, 0, 100, N'img/product/sachvanhoc4.jpg', N'E. Lockhart', N'Nhà xuất bản Kim Đồng', 0, GETDATE()),
(N'Nanh Trắng', N'Truyện về hành trình sinh tồn và trưởng thành của một chú sói trong môi trường khắc nghiệt của vùng Bắc Cực.', 80.0, 0, 100, N'img/product/sachvanhoc5.jpg', N'Jack London', N'Nhà xuất bản Thanh Niên', 0, GETDATE());


-- For 'Sách kỹ năng sống' (TheLoaiId = 20)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Đắc Nhân Tâm', N'Một trong những cuốn sách kỹ năng sống nổi tiếng nhất, giúp con người cải thiện kỹ năng giao tiếp và quản lý mối quan hệ.', 70.0, 0, 100, N'img/product/sachkynangsong1.jpg', N'Dale Carnegie', N'Nhà xuất bản Tổng hợp TP.HCM', 0, GETDATE()),
(N'Hoàn Thành Mọi Công Việc Không Hề Khó', N'Cuốn sách hướng dẫn các phương pháp quản lý thời gian và công việc hiệu quả.', 75.0, 0, 100, N'img/product/sachkynangsong2.jpg', N'David Allen', N'Nhà xuất bản Lao động', 0, GETDATE()),
(N'Đọc Dùm Bạn Các Sách Về Kỹ Năng Sống', N'Bản tóm tắt và phân tích những cuốn sách kỹ năng sống nổi tiếng, giúp bạn nhanh chóng nắm bắt được những kiến thức quý báu.', 80.0, 0, 100, N'img/product/sachkynangsong3.jpg', N'Nguyễn Hiến Lê', N'Nhà xuất bản Trẻ', 0, GETDATE()),
(N'Vạch Danh Giới', N'Cuốn sách giúp bạn hiểu rõ về giới hạn cá nhân và cách thiết lập chúng để có một cuộc sống cân bằng và lành mạnh.', 85.0, 0, 100, N'img/product/sachkynangsong4.jpg', N'Dr. Henry Cloud & Dr. John Townsend', N'Nhà xuất bản Phụ nữ Việt Nam', 0, GETDATE()),
(N'Gieo Suy Nghĩ Gặt Thành Công', N'Cuốn sách này sẽ truyền cảm hứng và chỉ ra những tư duy tích cực để đạt được thành công trong cuộc sống.', 90.0, 0, 100, N'img/product/sachkynangsong5.jpg', N'Napoleon Hill', N'Nhà xuất bản Văn hóa - Văn nghệ', 0, GETDATE());

-- For 'Sách khoa học' (TheLoaiId = 21)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'Science of Science', N'Cuốn sách giải thích khoa học về khoa học, khám phá các nguyên tắc cơ bản và phương pháp nghiên cứu.', 70.0, 0, 100, N'img/product/sachkhoahoc1.jpg', N'Nhiều tác giả', N'Nhà xuất bản Khoa học và Kỹ thuật', 0, GETDATE()),
(N'The Scient Book', N'Tổng hợp những kiến thức khoa học quan trọng và thú vị từ các lĩnh vực khác nhau.', 75.0, 0, 100, N'img/product/sachkhoahoc2.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục', 0, GETDATE()),
(N'The Nature of Science', N'Mô tả và phân tích bản chất của khoa học, giúp người đọc hiểu rõ hơn về quy trình và tác động của nghiên cứu khoa học.', 80.0, 0, 100, N'img/product/sachkhoahoc3.jpg', N'Nhiều tác giả', N'Nhà xuất bản Văn hóa - Thông tin', 0, GETDATE()),
(N'World of Science', N'Khám phá thế giới khoa học với những phát hiện và tiến bộ nổi bật qua các thời kỳ.', 85.0, 0, 100, N'img/product/sachkhoahoc4.jpg', N'Nhiều tác giả', N'Nhà xuất bản Trẻ', 0, GETDATE()),
(N'World of Science', N'Tập hợp những bài viết và nghiên cứu khoa học từ các nhà khoa học hàng đầu thế giới.', 90.0, 0, 100, N'img/product/sachkhoahoc5.jpg', N'Nhiều tác giả', N'Nhà xuất bản Thanh Niên', 0, GETDATE());

-- For 'Sách công nghệ' (TheLoaiId = 22)
INSERT INTO Sach (TenSach, MoTa, GiaTien, DanhGia, SoLuongTrongKho, LinkAnh, TacGia, NhaXuatBan, FlagDel, DateCreated)
VALUES 
(N'The Nature of Technology', N'Cuốn sách giải thích bản chất và sự phát triển của công nghệ trong xã hội hiện đại.', 80.0, 0, 100, N'img/product/sachcongnghe1.jpg', N'W. Brian Arthur', N'Nhà xuất bản Khoa học và Kỹ thuật', 0, GETDATE()),
(N'Information Technology', N'Giới thiệu các khái niệm cơ bản và ứng dụng của công nghệ thông tin trong nhiều lĩnh vực khác nhau.', 85.0, 0, 100, N'img/product/sachcongnghe2.jpg', N'Nhiều tác giả', N'Nhà xuất bản Giáo dục', 0, GETDATE()),
(N'Fundamental of Information Technology', N'Tổng hợp những kiến thức cơ bản về công nghệ thông tin và các kỹ năng cần thiết.', 90.0, 0, 100, N'img/product/sachcongnghe3.jpg', N'Nhiều tác giả', N'Nhà xuất bản Văn hóa - Thông tin', 0, GETDATE()),
(N'Chip War', N'Khám phá cuộc chiến công nghệ và cạnh tranh toàn cầu trong ngành công nghiệp bán dẫn.', 95.0, 0, 100, N'img/product/sachcongnghe4.jpg', N'Chris Miller', N'Nhà xuất bản Trẻ', 0, GETDATE()),
(N'Technology is Not Neutral', N'Thảo luận về tác động của công nghệ và những vấn đề đạo đức liên quan.', 100.0, 0, 100, N'img/product/sachcongnghe5.jpg', N'Stephanie Hare', N'Nhà xuất bản Thanh Niên', 0, GETDATE());

--Insert into tagtheloai
INSERT INTO [dbo].[TagTheLoai] (SachId, TheLoaiId)
VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 2),
(8, 2),
(9, 2),
(10, 2),
(11, 3),
(12, 3),
(13, 3),
(14, 3),
(15, 3),
(16, 4),
(17, 4),
(18, 4),
(19, 4),
(20, 4),
(21, 5),
(22, 5),
(23, 5),
(24, 5),
(25, 5),
(26, 9),
(26, 12),
(26, 36),
(27, 36),
(27, 9),
(28, 9),
(28, 36),
(29, 36),
(29, 9),
(30, 36),
(31, 36),
(31, 37),
(32, 36),
(32, 37),
(33, 36),
(33, 12),
(33, 13),
(34, 36),
(34, 37),
(35, 36),
(35, 37),
(36, 13),
(36, 12),
(36, 10),
(36, 6),
(37, 36),
(37, 12),
(38, 12),
(38, 36),
(39, 12),
(39, 36),
(40, 12),
(40, 36),
(41, 36),
(42, 6),
(43, 6),
(43, 9),
(44, 6),
(44, 9),
(45, 6),
(45, 9),
(46, 14),
(47, 18),
(48, 18),
(49, 18),
(50, 14),
(51, 17),
(52, 18),
(53, 18),
(54, 14),
(55, 14),
(56, 14),
(57, 14),
(58, 14),
(59, 14),
(60, 17),
(61, 14),
(62, 14),
(63, 17),
(64, 17),
(65, 17),
(66, 4),
(66, 8),
(67, 8),
(67, 4),
(68, 8),
(69, 8),
(68, 4),
(69, 4),
(70, 8),
(70, 4),
(71, 24),
(72, 24),
(73, 24),
(74, 24),
(75, 24),
(76, 23),
(77, 23),
(78, 23),
(79, 23),
(80, 23),
(81, 25),
(82, 26),
(83, 25),
(84, 26),
(85, 26),
(86, 26),
(87, 26),
(88, 26),
(89, 26),
(90, 26),
(91, 13),
(91, 6),
(92, 6),
(92, 11),
(93, 36),
(93, 9),
(94, 6),
(94, 13),
(95, 6),
(96, 13),
(97, 19),
(98, 19),
(99, 13),
(100, 19),
(101, 34),
(102, 34),
(103, 34),
(104, 34),
(105, 34),
(106, 34),
(107, 34),
(108, 34),
(109, 34),
(110, 34);

--Blog tags

INSERT INTO [Tag] (TenTag, DateCreated)
VALUES
(N'Khoa học', GETDATE()),
(N'Lịch sử', GETDATE()),
(N'Viễn tưởng', GETDATE()),
(N'Trinh thám', GETDATE()),
(N'Trending', GETDATE());

-- Finalize and check

select * from [Sach]