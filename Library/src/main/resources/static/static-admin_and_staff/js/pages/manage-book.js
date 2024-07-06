$(document).ready(function () {


    $('#deactivate-book-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/hideBook?id=' + $('#deactivate-book-id').val(),
            method: 'POST',
            contentType: 'application/json',
            success: function () {
                $('#time-update').text(formatDate(new Date()));
                window.location.reload();
                alert('Đã ẩn sách thành công');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });

    $('#activate-book-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/showBook?id=' + $('#activate-book-id').val(),
            method: 'POST',
            contentType: 'application/json',
            success: function () {
                $('#time-update').text(formatDate(new Date()));
                window.location.reload();
                alert('Đã bỏ ẩn sách thành công');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });

    $('#add-book-form').on('submit', function (e) {
        e.preventDefault();
        modifyBook(JSON.stringify({
                            tenSach: $("#tenSach-add").val(),
                            linkAnh: $("#anh-add").val(),
                            tacGia:  $("#tacGia-add").val(),
                            giaTien: $("#giaTien-add").val(),
                            soLuongTrongKho: $("#soLuong-add").val(),
                            nhaXuatBan:  $('#nhaXuatBan-add').val(),
                            moTa: $('#moTa-add').val(),
                            theLoaiId:$('#theLoai-add').val()
            }),'/Library/management/addBook'
                    );




    });

    $('#update-book-form').on('submit', function (e) {
        e.preventDefault();

                modifyBook(JSON.stringify({
                    tenSach: $("#tenSach-update").val(),
                    linkAnh: $("#anh-update").val(),
                    tacGia:  $("#tacGia-update").val(),
                    giaTien: $("#giaTien-update").val(),
                    soLuongTrongKho: $("#soLuong-update").val(),
                    nhaXuatBan:  $('#nhaXuatBan-update').val(),
                    moTa: $('#moTa-update').val(),
                    danhGia: $('#danhGia-update').val(),
                    theLoaiId:$('#theLoai-update').val()
                }),'/Library/management/updateBook?id=' + $('#book-id-update').val()
                );

    });

    function modifyBook(data, url) {
        $.ajax({
            url: url,
            method: 'POST',
            contentType: 'application/json',
            data: data,
            success: function () {
                $('#time-update').text(formatDate(new Date()));
                window.location.reload();
                alert("Đã cập nhật thành công");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    }
    function formatDate(date) {
        let year = date.getFullYear();
        let month = (date.getMonth() + 1).toString().padStart(2, '0');
        let day = date.getDate().toString().padStart(2, '0');
        let hours = date.getHours().toString().padStart(2, '0');
        let minutes = date.getMinutes().toString().padStart(2, '0');
        let seconds = date.getSeconds().toString().padStart(2, '0');

        return `${hours}:${minutes}:${seconds} ngày ${year}/${month}/${day}`;
    }
    $('#searchButton').on('click', function () {
        const category = $('#searchCategory').val();
        $.ajax({
            url: '/Library/management/searchBookByCategory?category=' + category,
            method: 'GET',
            contentType: 'application/json',
            success: function (data) {
                // Cập nhật bảng hiển thị sách với kết quả tìm kiếm
                updateBookTable(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });

    function updateBookTable(books) {
        // Cập nhật bảng thông tin sách
        const tableBody = $('#dataTable tbody');
        tableBody.empty();
        books.forEach(book => {
            tableBody.append(`
                <tr>
                    <td>${book.tenSach}</td>
                    <td><img src="${book.linkAnh}" /></td>
                    <td>${book.tacGia}</td>
                    <td>${book.giaTien}</td>
                    <td>${book.soLuongTrongKho}</td>
                    <td class="text-center">
                        ${book.flagDel === 0 ? '<div class="badge badge-success">Hiển thị</div>' : '<div class="badge badge-danger">Bị ẩn</div>'}
                    </td>
                    <td class="d-flex justify-content-center">
                        <button type="button" class="btn btn-light" onclick="openModalViewBookDetail(${book.id})"><i class="fa fa-eye text-primary"></i></button>
                        <button type="button" class="btn btn-light" onclick="openModalUpdateBook(${book.id})"><i class="fa fa-pen text-primary"></i></button>
                        ${book.flagDel === 0 ? `<button type="button" class="btn btn-light" onclick="openModalDeactivateBook(${book.id})"><i class="fa fa-times text-primary"></i></button>` : `<button type="button" class="btn btn-light" onclick="openModalActivateBook(${book.id})"><i class="fa fa-check text-primary"></i></button>`}
                    </td>
                </tr>
            `);
        });
    }
});
