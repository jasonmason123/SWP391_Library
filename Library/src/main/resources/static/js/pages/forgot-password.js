$(document).ready(function () {
   $('#form-forgot-password').on('submit', function (e) {
      e.preventDefault();
      $.ajax({
         method: 'POST',
         url: "/Library/isvalidemail?email=" + $('#email').val(),
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

//Change password
//--------------------------------------------------------------------------------------------

$(document).ready(function () {
   $('#change-password-form').on('submit', function (e) {
      e.preventDefault();
      let matKhauUnchecked = $('#matKhau').val();
      let xacNhanMatKhau = $('#xacNhanMatKhau').val();
      if(matKhauUnchecked === xacNhanMatKhau) {
         let url = new URL(window.location.href);
         let params = new URLSearchParams(url.search);
         let last = params.get("auth");
         $.ajax({
            method: 'POST',
            url: '/Library/processforgotpassword?auth=' + last + "&new=" + matKhauUnchecked,
            success: () => {
               console.log("success");
               window.location.replace("/Library/login")
            },
            error: (jqXHR, textStatus, errorThrown) => {
               console.log("Error: " + textStatus + ', ' + errorThrown);
            }
         });
      } else {
         $('#unmatched-message').text("Xác nhận mật khẩu không khớp, vui lòng nhập lại").css('color', 'red');
      }
   });
});

//end change password
//--------------------------------------------------------------------------------------------