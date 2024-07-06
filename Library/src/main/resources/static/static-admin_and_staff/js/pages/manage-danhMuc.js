$(document).ready(function () {

    $('#add-category-form').on('submit', function (e) {
        e.preventDefault();
        modifyCategory(JSON.stringify({
            tenDanhMuc: $("#tenDanhMuc-add").val(),
        }), '/Library/management/addCategory');
    });

    $('#update-danhMuc-form').on('submit', function (e) {
        e.preventDefault();
        modifyCategory(JSON.stringify({
            tenDanhMuc: $("#tenDanhMuc-update").val(),
        }), '/Library/management/updateCategory?id=' + $('#danhMuc-id-update').val());
    });

    function modifyCategory(data, url) {
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
