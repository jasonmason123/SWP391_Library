let orderTotal = 0;
function displayCartTable() {
    const cartTableBody = $('.your-order-table tbody');
    cartTableBody.empty(); // Clear any existing items

    if (cart.size === 0) {
        cartTableBody.append('<tr><td colspan="2" class="no-items">Bạn chưa đăng ký mượn cuốn sách nào.</td></tr>');
        $('.cart-subtotal .amount').text('0.00 đ');
        $('.order-total .amount').text('0.00 đ');
    } else {
        let totalPrice = 0;
        cart.forEach((itemData, bookId) => {
            const itemRow = $('<tr class="cart_item"></tr>');
            const itemName = $('<td class="product-name"></td>').html(`${itemData.bookName} <strong class="product-quantity"></strong>`);
            const itemTotal = $('<td class="product-total"></td>').html(`<span class="amount">${(+itemData.price).toFixed(2)} đ</span>`);
            itemRow.append(itemName, itemTotal);
            cartTableBody.append(itemRow);

            totalPrice += +itemData.price;
        });
        orderTotal = totalPrice;
        // Update the totals
        $('.cart-subtotal .amount').text(`${totalPrice.toFixed(1)} đ`);
        $('.order-total .amount').text(`${totalPrice.toFixed(1)} đ`);
    }
}

$(document).ready(function () {
    getCart();
    displayCartTable();

    const borrowDate = $('#borrowDate');
    const returnDate = $('#returnDate');
    const today = new Date();
    const year = today.getFullYear();
    const month = ('0' + (today.getMonth() + 1)).slice(-2); // Month is zero-based
    const day = ('0' + today.getDate()).slice(-2);
    const formattedToday = `${year}-${month}-${day}`;

    borrowDate.attr('min', formattedToday);

    borrowDate.on('change', function() {
        let borrowDateValue = $(this).val();
        if (borrowDateValue) {
            let borrowDate = new Date(borrowDateValue);

            borrowDate.setDate(borrowDate.getDate() + 1);
            const returnYear = borrowDate.getFullYear();
            const returnMonth = ('0' + (borrowDate.getMonth() + 1)).slice(-2); // Month is zero-based
            const returnDay = ('0' + borrowDate.getDate()).slice(-2);
            const formattedMinReturnDate = `${returnYear}-${returnMonth}-${returnDay}`;

            returnDate.attr('min', formattedMinReturnDate);
            if(returnDate.val() !== null && borrowDateValue >= returnDate.val()) {
                returnDate.val(formattedMinReturnDate);
            }
            $('#returnDateMessage').show();
        } else {
            $('#returnDateMessage').hide();
        }
    });

    $('#create-request-form').on('submit', function (e) {
         e.preventDefault();
         if(confirm("Vui lòng kiểm tra kĩ toàn bộ thông tin. Bạn có chắc chắn muốn tạo yêu cầu không?")) {
             let cartData = new Map();
             cart.forEach((itemData, bookId) => {
                 cartData.set(bookId, itemData.price);
             });
             let data = {
                 clientCart: Object.fromEntries(cartData),
                 ngayMuon: $('#borrowDate').val(),
                 ngayTra: $('#returnDate').val(),
                 diaChiNhanSach: $('#diaChi').val()
             }
             $.ajax({
                 method: 'POST',
                 url:'/Library/cart/process?ngayMuon=' + $('#borrowDate').val() + '&ngayTra=' + $('#returnDate').val(),
                 contentType: 'application/json',
                 data: JSON.stringify(data),
                 success: function () {
                     alert("Yêu cầu của bạn đã được gửi, vui lòng đợi thư viện gửi về email xác nhận");
                     clearCart();
                     window.location.replace("/Library/book");
                 },
                 error: function () {
                     alert("Có lỗi, vui lòng thử lại sau!");
                 }
             });
         }
    });

    $('#borrowDate, #returnDate').on('change', function () {
        let borrowDateValue = $('#borrowDate').val();
        let returnDateValue = $('#returnDate').val();
        if(borrowDateValue!==null && returnDateValue!==null) {
            let borrowDate = new Date(borrowDateValue);
            let returnDate = new Date(returnDateValue);
            let daysBorrow = (returnDate - borrowDate) / (1000 * 60 * 60 * 24);
            let borrowFee = daysBorrow * 1000;
            let orderTotalLocal = orderTotal;
            orderTotalLocal += borrowFee;
            $('#soNgayMuon').text(daysBorrow);
            $('#phiMuon').text(borrowFee);
            $('#borrowFee').removeClass('d-none');
            $('.order-total .amount').text(`${orderTotalLocal.toFixed(0)} đ`)
        }
    })
});