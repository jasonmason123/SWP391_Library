document.addEventListener('DOMContentLoaded', () => {
    let userName="", soCCCD="", email="", soDienThoai="", matKhau="";

    $('#username').on('change', function () {
        let userNameUnchecked = $('#username').val();
        $.ajax({
            url: "/Library/isvalidtenuser?tenuser=" + userNameUnchecked,
            method: 'POST',
            success: function (response) {
                if(response == "notExist") {
                    userName = userNameUnchecked;
                    $('#duplicated-username').text('');
                } else if(response == "unmatched") {
                    $('#duplicated-username').text('Tên người dùng phải có độ dài 5-20 ký tự, và không có ký tự đặc biệt');
                } else if(response == "existed") {
                    $('#duplicated-username').text('Người dùng đã tồn tại');
                } else {
                    $('#duplicated-username').text('Thông tin không hợp lệ!');
                    console.error("Invalid!");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error: ", textStatus, errorThrown);
            }
        });
    });

    $('#soCCCD').on('change', function () {
        let soCCCDUnchecked = $('#soCCCD').val();
        $.ajax({
            url: "/Library/isvalidsocccd?socccd=" + soCCCDUnchecked,
            method: 'POST',
            success: function (response) {
                if(response == "notExist") {
                    soCCCD = soCCCDUnchecked;
                    $('#duplicated-soCCCD').text('');
                } else if(response == "existed") {
                    $('#duplicated-soCCCD').text('Số CCCD/CMTND này đã được đăng ký')
                } else {
                    $('#duplicated-soCCCD').text('Thông tin không hợp lệ!');
                    console.error("Invalid!");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error: ", textStatus, errorThrown);
            }
        });
    });

    $('#email').on('change', function () {
        let emailUnchecked = $('#email').val();
        $.ajax({
            url: "/Library/isvalidemail?email=" + emailUnchecked,
            method: 'POST',
            success: function (response) {
                if(response == "notExist") {
                    email = emailUnchecked;
                    $('#duplicated-email').text('');
                } else if(response == "existed") {
                    $('#duplicated-email').text('Email này đã được đăng ký')
                } else {
                    $('#duplicated-email').text('Thông tin không hợp lệ!');
                    console.error("Invalid!");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error: ", textStatus, errorThrown);
            }
        });
    });

    $('#soDienThoai').on('change', function () {
        let soDienThoaiUnchecked = $('#soDienThoai').val();
        $.ajax({
            url: "/Library/isvalidsodienthoai?sodienthoai=" + soDienThoaiUnchecked,
            method: 'POST',
            success: function (response) {
                if(response == "notExist") {
                    soDienThoai = soDienThoaiUnchecked;
                    $('#duplicated-soDienThoai').text('');
                } else if(response == "existed") {
                    $('#duplicated-soDienThoai').text('Số điện thoại này đã được đăng ký')
                } else {
                    $('#duplicated-soDienThoai').text('Thông tin không hợp lệ!');
                    console.error("Invalid!");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error: ", textStatus, errorThrown);
            }
        });
    });

    $('#signup-form').on('submit', function (e) {
        e.preventDefault();
        let matKhauUnchecked = $('#matKhau').val();
        let xacNhanMatKhau = $('#xacNhanMatKhau').val();
        if(matKhauUnchecked === xacNhanMatKhau) {
            matKhau = matKhauUnchecked;
            if (userName!=="" && soCCCD!=="" && email!=="" && soDienThoai!=="" && matKhau!=="") {
                $('#missing-information-message').text('');
                $('#otpModal').modal('show');
            } else {
                $('#missing-information-message').text('Vui lòng điền đầy đủ những thông tin bên trên, hoặc kiểm tra lại tính hợp lệ của thông tin nhập vào');
            }
        } else {
            $('#password-not-match').text('Vui lòng nhập lại mật khẩu');
        }
    });

//Otp
//--------------------------------------------------------------------------------------------

    $('.verify-btn').on('click', function () {
        $.ajax({
            type: 'POST',
            url: "/Library/processsignup",
            contentType: 'application/json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('otpInput', inputOtp);
            },
            data: JSON.stringify({
                'tenUser': userName,
                'matKhau': matKhau,
                'email': email,
                'soDienThoai': soDienThoai,
                'soCCCD': soCCCD
            }),
            success: () => {
                console.log("Signup success");
                window.location.replace("/Library/home");
            },
            error: (jqXHR, textStatus, errorThrown) => {
                console.error("Signup failed! Error:" + textStatus + ', ' + errorThrown)
            }
        });
    });

//end otp
//--------------------------------------------------------------------------------------------
});