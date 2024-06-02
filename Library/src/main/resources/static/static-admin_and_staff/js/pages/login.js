$('document').ready(function () {
    $('#staff-login-form').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: '/Library/management/processlogin',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                userName: $("#inputUsername").val(),
                password: $("#inputPassword").val(),
                rememberMe: $("#rememberme").prop("checked")
            }),
            success: function () {
                console.log('Success');
                window.location.replace("/Library/manage/manageStaff");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                $('#error-message').text("Tên người dùng hoặc mật khẩu không chính xác").css('color', 'red');
            }
        });
    });
});