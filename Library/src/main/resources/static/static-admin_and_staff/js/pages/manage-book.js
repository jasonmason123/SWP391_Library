$(document).ready(function () {

    $('#deactivate-book-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/book/hideBook?id=' + $('#deactivate-book-id').val(),
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
            url: '/Library/management/book/showBook?id=' + $('#activate-book-id').val(),
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

    $('#searchCategory').on('change', function () {
        $('#searchGenre').prop('disabled', true);
        $('#searchForm').submit();
    });

    $('#searchGenre').on('change', function () {
        $('#searchGenre').prop('disabled', false);
        $('#searchForm').submit();
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
            }),'/Library/management/book/addBook'
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
            }),'/Library/management/book/updateBook?id=' + $('#book-id-update').val()
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
});
