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

        var formData = new FormData();
        formData.append('tenSach', $("#tenSach-add").val());
        formData.append('tacGia', $("#tacGia-add").val());
        formData.append('giaTien', $("#giaTien-add").val());
        formData.append('anh', $('#anh-add')[0].files[0]);
        formData.append('soLuongTrongKho', $("#soLuong-add").val());
        formData.append('nhaXuatBan', $('#nhaXuatBan-add').val());
        formData.append('moTa', $('#moTa-add').val());
        formData.append('theLoaiId', $('#theLoai-add').val());

        $.ajax({
            url: '/Library/management/book/addBook',
            type: 'POST',
            data: formData,
            processData: false, // Prevent jQuery from automatically transforming the data into a query string
            contentType: false, // Prevent jQuery from setting the content type
            success: function (response) {
                // Handle the response from the server
                console.log('Success:', response);
                alert("Đã thêm thành công");
                window.location.reload();
            },
            error: function (xhr, status, error) {
                // Handle errors
                console.error('Error:', error);
                alert("Có lỗi");
            }
        });
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
