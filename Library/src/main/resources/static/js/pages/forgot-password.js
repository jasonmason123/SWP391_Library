$(document).ready(function () {
   $('#form-forgot-password').on('submit', function (e) {
      e.preventDefault();
      $.ajax({
         method: 'POST',
         url: "/Library/isvalidemail?email=" + $('#email').val(),
         success: (response) => {
            if(response=="existed") {
               $('#otpModal').modal('show');
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

//Otp
//--------------------------------------------------------------------------------------------

   $('.verify-btn').on('click', function () {
      console.log(inputOtp);
      $.ajax({
         type: 'POST',
         url: "/Library/auth?email=" + $('#email').val(),
         contentType: 'application/json',
         header: {
            'otpInput': inputOtp
         },
         success: (response) => {
            window.location.replace("/Library/changepassword?auth=" + response);
         },
         error: (jqXHR, textStatus, errorThrown) => {
            console.error("Failed! Error:" + textStatus + ', ' + errorThrown);
         }
      });
   });

//end otp
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
         // var currentUrl = window.location.href;
         // var pathValues = currentUrl.split('/')
         // var lastPath = pathValues[pathValues.length-1];
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