$('document').ready(function () {
    $('#id-form-login').on('submit', function (e) {
        e.preventDefault();
        console.log("Submitted");
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
                console.log('Success');
                window.location.replace("/Library/home");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                $('#error-message').text("Tên người dùng hoặc mật khẩu không chính xác").css('color', 'red');
            }
        });
    });
});