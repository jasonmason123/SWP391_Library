$(document).ready(function () {

    $('#add-theLoai-form').on('submit', function (e) {
        e.preventDefault();
        modifyGenre(JSON.stringify({
            tenTheLoai: $("#tenTheLoai-add").val(),
            danhMucId: $("#tenDanhMuc-add").val()
        }), '/Library/management/addGenre');
    });

    $('#update-theLoai-form').on('submit', function (e) {
        e.preventDefault();
        modifyGenre(JSON.stringify({
            tenTheLoai: $("#tenTheLoai-update").val(),
            danhMucId: $("#tenDanhMuc-update").val() // Gửi đúng ID của danh mục
        }), '/Library/management/updateGenre?id=' + $('#theLoai-id-update').val());
    });

    function modifyGenre(data, url) {
        $.ajax({
            url: url,
            method: 'POST',
            contentType: 'application/json',
            data: data,
            success: function () {
                $('#time-update').text(formatDate(new Date()));
                window.location.reload();
                alert("Thành công");
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
