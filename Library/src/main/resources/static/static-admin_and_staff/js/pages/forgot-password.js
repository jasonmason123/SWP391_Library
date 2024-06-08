$(document).ready(function () {
    $('#form-forgot-password').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            method: 'POST',
            url: "/Library/management/isvalidemail?email=" + $('#email').val(),
            success: (response) => {
                if(response=="existed") {
                    $('#messageModal').modal('show');
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
            url: "/Library/auth?email=" + $('#email').val(),
            contentType: 'application/json',
            success: () => {
                console.error("Success");
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