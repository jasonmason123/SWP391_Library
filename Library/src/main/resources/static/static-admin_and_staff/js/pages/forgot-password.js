$(document).ready(function () {
    $('#form-forgot-password').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            method: 'POST',
            url: "/Library/management/isvalidemail?email=" + $('#inputEmail').val(),
            success: (response) => {
                if(response=="existed") {
                    getLink();
                } else {
                    $('#text-danger').text("Email không tồn tại, hoặc không hợp lệ").css('color', 'red');
                }
            },
            error: (jqXHR, textStatus, errorThrown) => {
                console.error("Error:" + textStatus + ', ' + errorThrown);
                $('#text-danger').text("Email không tồn tại, hoặc không hợp lệ").css('color', 'red');
            }
        });
    });

//Send change password link
//--------------------------------------------------------------------------------------------

    function getLink() {
        $.ajax({
            type: 'POST',
            url: "/Library/management/auth?email=" + $('#inputEmail').val(),
            contentType: 'application/json',
            success: () => {
                alert("Chúng tôi đã gửi thông báo tới email của bạn, vui lòng kiểm tra mail");
            },
            error: (jqXHR, textStatus, errorThrown) => {
                console.error("Failed! Error:" + textStatus + ', ' + errorThrown);
                $('#text-danger').text("Có lỗi! Vui lòng thử lại sau").css('color', 'red');
            }
        });
    }

//end send change password link
//--------------------------------------------------------------------------------------------

});