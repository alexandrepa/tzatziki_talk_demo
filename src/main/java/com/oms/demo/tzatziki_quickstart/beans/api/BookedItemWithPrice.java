package com.oms.demo.tzatziki_quickstart.beans.api;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookedItemWithPrice extends BookedItem {
    private Double price;

    @Builder
    public BookedItemWithPrice(String itemId, Integer quantity, Double price) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }

    public static BookedItemWithPrice from(BookedItem bookedItem, Double price) {
        return BookedItemWithPrice.builder()
                .itemId(bookedItem.itemId)
                .quantity(bookedItem.quantity)
                .price(price)
                .build();
    }
}
