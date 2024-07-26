$(document).ready(function () {
    $('#deactivate-customer-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/customers/deactivateCustomer?id=' + $('#deactivate-customer-id').val(),
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

    $('#activate-customer-form').on('submit', function (e) {
        e.preventDefault();
        console.log("Reached deactivate");
        $.ajax({
            url: '/Library/management/customers/activateCustomer?id=' + $('#activate-customer-id').val(),
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

    $('#editCustomerForm').on('submit', function (e) {
        e.preventDefault();
        let id = $('#update-customer-id').val();
        let email = $('#email-update').val();
        let sdt = $('#sdt-update').val();

        // Check if both validations pass
        Promise.all([isValidEmail(email), isValidSdt(sdt)]).then(results => {
            let validEmail = results[0];
            let validSdt = results[1];

            if(!validEmail) {
                $('#email-edit-warning').text('Email không hợp lệ hoặc đã tồn tại');
            } else {
                $('#email-edit-warning').text('');
            }
            if(!validSdt) {
                $('#sdt-edit-warning').text('Số điện thoại không hợp lệ hoặc đã tồn tại');
            } else {
                $('#sdt-edit-warning').text('');
            }

            if((validEmail && validSdt) && window.confirm('Bạn có chắc chắn muốn cập nhật?')) {
                $.ajax({
                    method: 'POST',
                    url: '/Library/management/customers/udpateCustomer',
                    data: { customer: id, email: email, sdt: sdt },
                    success: function () {
                        alert('Cập nhật thành công');
                        window.location.reload();
                    },
                    error: function () {
                        alert('Có lỗi');
                    }
                });
            }
        });

        function isValidEmail(email) {
            return new Promise((resolve, reject) => {
                $.ajax({
                    method: 'POST',
                    url: '/Library/isvalidemail?email=' + email,
                    success: function (response) {
                        resolve(response == 'notExist');
                    },
                    error: function () {
                        resolve(false);
                    }
                });
            });
        }

        function isValidSdt(sdt) {
            return new Promise((resolve, reject) => {
                $.ajax({
                    method: 'POST',
                    url: '/Library/isvalidsodienthoai?sodienthoai=' + sdt,
                    success: function (response) {
                        resolve(response == 'notExist');
                    },
                    error: function () {
                        resolve(false);
                    }
                });
            });
        }
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