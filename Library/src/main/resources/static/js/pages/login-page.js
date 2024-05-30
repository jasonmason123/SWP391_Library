$('document').ready(function () {
    $('#id-form-login').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: '/Library/processlogin',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                userName: $("#login-username").val(),
                password: $("#login-password").val(),
                rememberMe: $("#rememberme").prop("checked")
            }),
            success: function () {
                window.location.replace("/Library/home");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $('#error-message').text('Tên người dùng hoặc mật khẩu không hợp lệ').css('color', 'red');
                console.warn('Error:', textStatus, errorThrown);
            }
        });
    });
});