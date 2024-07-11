$(document).ready(function () {

    $('#add-category-form').on('submit', function (e) {
        e.preventDefault();
        modifyCategory(JSON.stringify({
            tenDanhMuc: $("#tenDanhMuc-add").val(),
        }), '/Library/management/category/addCategory');
    });

    $('#update-danhMuc-form').on('submit', function (e) {
        e.preventDefault();
        modifyCategory(JSON.stringify({
            tenDanhMuc: $("#tenDanhMuc-update").val(),
        }), '/Library/management/category/updateCategory?id=' + $('#danhMuc-id-update').val());
    });

    $('#deactivate-danhMuc-form').on('submit', function (e) {
        e.preventDefault();
        let danhMucId = $('#deactivate-danhMuc-id').val();
        $.ajax({
            url: '/Library/management/category/deleteCategory?id=' + danhMucId,
            method: 'POST',
            contentType: 'application/json',
            success: function () {
                $('#row_' + danhMucId).remove();
                alert('Đã xóa danh mục');
                $('#deactivateDanhMucModal').modal('hide');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
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
