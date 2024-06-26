$(document).ready(function () {


    $('#reject-blog-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/rejectBlog?id=' + $('#reject-blog-id').val(),
            method: 'POST',
            contentType: 'application/json',
            success: function () {
                window.location.reload();
                $('#row_' + $('#reject-blog-id').val() + '"])').remove();
                alert('abc');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });

    $('#accept-blog-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/acceptBlog?id=' + $('#accept-blog-id').val(),
            method: 'POST',
            contentType: 'application/json',
            success: function () {
                window.location.reload();
                $('#row_' + $('#accept-blog-id').val() + '"])').remove();
                alert('hehe');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });


});
