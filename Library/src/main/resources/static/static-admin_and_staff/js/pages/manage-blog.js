$(document).ready(function () {
    $('#deactivate-blog-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/manageDanhSachBaiViet/hideBlog?id=' + $('#deactivate-blog-id').val(),
            method: 'POST',
            contentType: 'application/json',
            success: function () {

                window.location.reload();
                alert('Đã ẩn bài viết thành công');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });

    $('#activate-blog-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/manageDanhSachBaiViet/showBlog?id=' + $('#activate-blog-id').val(),
            method: 'POST',
            contentType: 'application/json',
            success: function () {

                window.location.reload();
                alert('Đã bỏ ẩn bài viết thành công');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });
});
