/*
    key: bookId
    value: itemData: {
        bookName
        imagePath
        price
        quantity
    }
 */
const cart = new Map();
const CART_NAME = 'cart';
const CART_ADD = 'ADD';
const CART_MINUS = 'MINUS';

function getCart() {
    const cartJSON = localStorage.getItem(CART_NAME);
    if (cartJSON) {
        const parsedCart = JSON.parse(cartJSON);
        Object.entries(parsedCart).forEach(([bookId, itemData]) => {
            cart.set(parseInt(bookId), itemData);
        });
    }
}

function clearCart() {
    if(cart.size!==0) {
        cart.clear();
    }
    $('#cart').empty();
    localStorage.removeItem(CART_NAME);
    displayCart();
}


function saveCart() {
    const cartObject = Object.fromEntries(cart);
    localStorage.setItem(CART_NAME, JSON.stringify(cartObject));
}

function updateCartWithIdPrefix(id, idPrefix='', action) {
    if((getCartSize()>=5 && action===CART_ADD) || (getCartSize()<=0 && action===CART_MINUS)) {
        console.warn('Cannot update cart');
    } else {
        if(cart.has(id)) {
            let comparableQuantity = cart.get(id).quantity;
            let quantityInStock = $('#quantityInStock_' + idPrefix + '' + id).data('quantityinstock');
            if(action===CART_ADD && comparableQuantity<=quantityInStock && comparableQuantity<3) {
                cart.get(id).quantity += 1;
            } else if(action===CART_MINUS) {
                if(comparableQuantity <= 1) {
                    removeFromCart(id);
                } else {
                    cart.get(id).quantity -= 1;
                }
            } else {
                console.warn('Cannot update cart');
            }
        } else {
            cart.set(id, {
                bookName: $('#bookName_' + idPrefix + '' + id).text(),
                imagePath: $('#imagePath_' + idPrefix + '' + id).attr('src'),
                price: $('#price_' + idPrefix + '' + id).data('price'),
                quantity: 1
            });
        }
    }
    saveCart();
    displayCart(idPrefix);
}

function updateCart(id, action) {
    updateCartWithIdPrefix(id, '', action);
}

function removeFromCart(id) {
    if(cart.has(id)) {
        cart.delete(id);
    } else {
        console.log("No item found");
    }
    saveCart();
    displayCart();
}

function getCartSize() {
    let size=0;
    cart.forEach((itemData, bookId) => {
        size += itemData.quantity;
    });
    return size;
}

function displayCart(idPrefix='') {
    const cartList = $('#cart');
    cartList.empty(); // Clear any existing items

    if (cart.size === 0) {
        $('.cart-bottom').hide();
        $('.cart-totals').hide();
        $('.number-of-items').hide();
        cartList.append('<li class="no-items">Chưa có sách được đăng ký mượn</li>');
    } else {
        $('.cart-bottom').show();
        $('.cart-totals').show();
        let totalPrice = 0;
        let totalItems = 0;

        cart.forEach((itemData, bookId) => {
            // Create the HTML structure
            const item = $('<li class="single-cart"></li>');

            // Cart Image
            const cartImg = $('<div class="cart-img"></div>');
            const bookLink = $('<a></a>').attr('href', `/Library/book?book=${bookId}`);
            const bookImg = $('<img>').attr('src', itemData.imagePath).attr('alt', itemData.bookName);
            bookLink.append(bookImg);
            cartImg.append(bookLink);

            // Cart Info
            const cartInfo = $('<div class="cart-info"></div>');
            const bookName = $('<h5></h5>').append($('<a></a>').attr('href', `/Library/book?book=${bookId}`).text(itemData.bookName));

            // Quantity with Plus and Minus Buttons
            const quantityWrapper = $('<div class="quantity-wrapper"></div>');
            const minusButton = $('<button class="quantity-minus">-</button>');
            const quantity = $('<p class="quantity"></p>').text(itemData.quantity);
            const plusButton = $('<button class="quantity-plus">+</button>');

            // Event handler for minus button
            minusButton.click(function(event) {
                event.preventDefault();
                updateCart(bookId, 'MINUS');
            });

            // Event handler for plus button
            plusButton.click(function(event) {
                event.preventDefault();
                updateCartWithIdPrefix(bookId, idPrefix, 'ADD');
            });

            quantityWrapper.append(minusButton, quantity, plusButton);

            const price = $('<p class="price"></p>').text(`${itemData.quantity} x ${itemData.price} VND`);

            // Cart Icon for removing item
            const cartIcon = $('<div class="cart-icon"></div>');
            const removeLink = $('<a></a>').attr('href', '#');
            const removeIcon = $('<i class="fa fa-remove"></i>');

            removeLink.append(removeIcon);
            cartIcon.append(removeLink);

            // Add remove click event
            removeLink.click(function(event) {
                event.preventDefault(); // Prevent default anchor click behavior
                removeFromCart(bookId);
            });

            // Append all elements to item
            cartInfo.append(bookName, quantityWrapper, price);
            item.append(cartImg, cartInfo, cartIcon);
            cartList.append(item); // Add the item to the cart list

            totalPrice += itemData.quantity * itemData.price;
            totalItems += itemData.quantity;
        });

        $('#total-price').text(totalPrice);
        $('.number-of-items').show().text(totalItems);
    }
}

$(document).ready(function () {
    getCart();
    displayCart();
});