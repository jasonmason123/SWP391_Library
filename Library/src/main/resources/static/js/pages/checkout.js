function displayCartTable() {
    const cartTableBody = $('.your-order-table tbody');
    cartTableBody.empty(); // Clear any existing items

    if (cart.size === 0) {
        cartTableBody.append('<tr><td colspan="2" class="no-items">Bạn chưa đăng ký mượn cuốn sách nào.</td></tr>');
        $('.cart-subtotal .amount').text('0.00 VNĐ');
        $('.order-total .amount').text('0.00 VNĐ');
    } else {
        let totalPrice = 0;
        cart.forEach((itemData, bookId) => {
            const itemRow = $('<tr class="cart_item"></tr>');
            const itemName = $('<td class="product-name"></td>').html(`${itemData.bookName} <strong class="product-quantity"> × ${itemData.quantity}</strong>`);
            const itemTotal = $('<td class="product-total"></td>').html(`<span class="amount">${(itemData.price * itemData.quantity).toFixed(2)} VNĐ</span>`);
            itemRow.append(itemName, itemTotal);
            cartTableBody.append(itemRow);

            totalPrice += itemData.price * itemData.quantity;
        });

        // Update the totals
        const finalTotal = totalPrice;
        $('.cart-subtotal .amount').text(`${totalPrice.toFixed(2)} VNĐ`);
        $('.order-total .amount').text(`${finalTotal.toFixed(2)} VNĐ`);
    }
}

$(document).ready(function () {
    getCart();
    displayCartTable();

    const borrowDate = $('#borrowDate');
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

            borrowDate.setDate(borrowDate.getDate() + 60);

            let day = borrowDate.getDate();
            let month = borrowDate.getMonth() + 1; // Months are zero-based
            let year = borrowDate.getFullYear();
            let formattedReturnDate = `${day < 10 ? '0' + day : day}/${month < 10 ? '0' + month : month}/${year}`;

            $('#returnDate').text(formattedReturnDate);
            $('#returnDateMessage').show();
        } else {
            $('#returnDateMessage').hide();
        }
    });

    $('#create-request-form').on('submit', function (e) {
         e.preventDefault();
         let cartData = new Map();
         cart.forEach((itemData, bookId) => {
             cartData.set(bookId, itemData.quantity);
         });
         $.ajax({
            method: 'POST',
            url:'/Library/cart/process?ngayMuon=' + $('#borrowDate').val(),
             contentType: 'application/json',
             data: JSON.stringify(Object.fromEntries(cartData)),
             success: function () {
                alert("Yêu cầu của bạn đã được gửi, vui lòng đợi thư viện gửi về email xác nhận");
                clearCart();
                window.location.replace("/Library/book");
             },
             error: function () {
                 alert("Có lỗi, vui lòng thử lại sau!");
             }
         });
    });
});