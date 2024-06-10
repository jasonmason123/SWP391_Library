$(document).ready(function () {
    $('#add-staff-form').on('submit', function (e) {
        e.preventDefault();
        let matKhau = $("#password-add").val();
        let xacNhanMatKhau = $("#confirm-password-add").val();
        if(matKhau === xacNhanMatKhau) {
            $.when(
                $.ajax({
                    url: '/Library/management/isvalidemail?email=' + $('#email-add').val(),
                    method: 'POST',
                    success: (response) => {return response === 'notExist';}
                }),
                $.ajax({
                    url: '/Library/management/isvalidsodienthoai?sodienthoai=' + $('#phoneNo-add').val(),
                    method: 'POST',
                    success: (response) => {return response === 'notExist';}
                }),
            ).done(function (a1, a2) {
                if(a1[0]==='notExist' && a2[0]==='notExist') {
                    modifyStaff(JSON.stringify({
                            tenNhanVien: $("#tenNhanVien-add").val(),
                            matKhau: $("#password-add").val(),
                            email: $("#email-add").val(),
                            soDienThoai: $("#phoneNo-add").val(),
                            diaChi: $("#address-add").val(),
                            vaiTro: $('#role-add').val()
                        }),'/Library/management/addStaff'
                    );
                }
                if(a1[0]==='existed' || a1[0]==='unmatched') {
                    $('#errorMessageEmail-add').text("Email không hợp lệ hoặc đã tồn tại").css('color', 'red');
                }
                if(a2[0]==='existed' || a2[0]==='unmatched') {
                    $('#errorMessagePhoneNo-add').text("Số điện thoại không hợp lệ hoặc đã tồn tại").css('color', 'red');
                }
            });
        } else {
            $('#unmatched-password-message')
                .text("Xác nhận mật khẩu chưa khớp với mật khẩu bạn đã nhập, vui lòng nhập lại").css('color', 'red');
        }
    });

    $('#update-staff-form').on('submit', function (e) {
        e.preventDefault();
        $.when(
            $.ajax({
                url: '/Library/management/isvalidemail?email=' + $('#email-add').val(),
                method: 'POST',
                success: (response) => {return response === 'notExist';}
            }),
            $.ajax({
                url: '/Library/management/isvalidsodienthoai?sodienthoai=' + $('#phoneNo-add').val(),
                method: 'POST',
                success: (response) => {return response === 'notExist';}
            }),
        ).done(function (a1, a2) {
            if(a1[0]==='notExist' && a2[0]==='notExist') {
                modifyStaff(JSON.stringify({
                    email: $("#email-update").val(),
                    soDienThoai: $("#phoneNo-update").val(),
                    diaChi: $("#address-update").val(),
                    vaiTro: $('#role-update').val()
                }),'/Library/management/updateStaff?id=' + $('#staff-id-update').val()
                );
            }
            if(a1[0]==='existed' || a1[0]==='unmatched') {
                $('#errorMessageEmail-update').text("Email không hợp lệ hoặc đã tồn tại").css('color', 'red');
            }
            if(a2[0]==='existed' || a2[0]==='unmatched') {
                $('#errorMessagePhoneNo-update').text("Số điện thoại không hợp lệ hoặc đã tồn tại").css('color', 'red');
            }
        });

        modifyStaff(JSON.stringify({
            email: $("#email-update").val(),
            soDienThoai: $("#phoneNo-update").val(),
            diaChi: $("#address-update").val(),
            vaiTro: $('#role-update').val()
        }),'/Library/management/updateStaff?id=' + $('#staff-id-update').val()
        );
    });

    function modifyStaff(data, url) {
        $.ajax({
            url: url,
            method: 'POST',
            contentType: 'application/json',
            data: data,
            success: function () {
                $('#time-update').text(formatDate(new Date()));
                window.location.reload();
                alert("Đã cập nhật thành công");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    }

    $('#deactivate-staff-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/deactivateStaff?id=' + $('#deactivate-staff-id').val(),
            method: 'POST',
            contentType: 'application/json',
            success: function () {
                $('#time-update').text(formatDate(new Date()));
                window.location.reload();
                alert('Đã vô hiệu hóa thành công');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });

    $('#activate-staff-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/activateStaff?id=' + $('#activate-staff-id').val(),
            method: 'POST',
            contentType: 'application/json',
            success: function () {
                $('#time-update').text(formatDate(new Date()));
                window.location.reload();
                alert('Đã kích hoạt thành công');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.warn('Error:', textStatus, errorThrown);
                alert("Có lỗi");
            }
        });
    });

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