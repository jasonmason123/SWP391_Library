package com.springdemo.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartItemDto {
    private int bookId;
    private String bookName;
    private String imagePath;
    private double price;

    @Override
    public int hashCode() {
        return java.util.Objects.hash(bookId, bookName, imagePath, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItemDto that = (CartItemDto) obj;
        return bookId == that.bookId;
    }
}
