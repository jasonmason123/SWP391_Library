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
    const cartMap = new Map();
    const cartJSON = localStorage.getItem(CART_NAME);
    if(cartJSON) {
        const parsedCart = JSON.parse(cartJSON);
        Object.entries(parsedCart).forEach(([itemDataStr, bookId]) => {
            const itemData = JSON.parse(itemDataStr);
            cart.set(bookId, itemData);
        });
    }
}

function saveCart() {
    if(cart.size===0) {
        const cartObj = {};
        cart.forEach((itemData, bookId) => {
            cartObj[bookId] = JSON.stringify(itemData);
        });
        localStorage.setItem(CART_NAME, JSON.stringify(cartObj));
    }
}

function updateCart(id, action) {
    if(cart.has(id)) {
        let comparableQuantity = cart.get(id).quantity;
        let quantityInStock = 0; //get from data-...
        if(action===CART_ADD && getCartSize() <= 5 && comparableQuantity <= quantityInStock) {
            cart.get(id).quantity += 1;
        } else if(action===CART_MINUS) {
            if(comparableQuantity <= 0) {
                removeFromCart(id);
            } else {
                cart.get(id).quantity -= 1;
            }
        } else {
            console.warn('Invalid operation, only accept ADD or MINUS');
        }
    } else {
        cart.set(id, {
            bookName: "", //get data from html attribute data-...
            imagePath: "",
            price: 0,
            quantity: 1
        });
    }
    saveCart();
}

function removeFromCart(id) {
    if(cart.has(id)) {
        cart.delete(id);
    } else {
        console.log("No item found");
    }
    saveCart();
}

function getCartSize() {
    let size=0;
    cart.forEach((itemData, bookId) => {
        size += itemData.quantity;
    });
    return size;
}

function displayCart() {
    const cartList = $('#cart');
    if(cart.size===0) {

    } else {
        cartList.empty();
        cart.forEach((itemData, bookId) => {
            const item = $('<li class="single-cart"></li>');
            const cartImg = $('<div class="cart-img"></div>');
            const bookLink = $('<a></a>').attr('href', `/Library/bookdetail?id=${bookId}`);
            const bookImg = $('<img>').attr('src', itemData.imagePath).attr('alt', itemData.bookName);
            //item.append('');
        });
    }
}

function checkout() {
    let checkoutCart = new Map();
    cart.forEach((itemData, bookId) => {
        checkoutCart.set(bookId, itemData.quantity);
    });
    let ngayMuon = '';
    let ngayTra = '';

    $.ajax({
        method: 'POST',
        url: '/Library/cart/process',
        data: JSON.stringify(checkoutCart),
        success: () => {

        },
        error: () => {

        }
    });
}

$(document).ready(function () {
    getCart();
});

/*
Save cart data on localstorage as a map, with key including bookId, bookName, imagePath and price as key, and value as quantity
When sending cart data to back-end, use a map that only includes bookId as key and value as quantity
 */