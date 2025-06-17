package com.oms.demo.tzatziki_quickstart.beans.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderInformation {
    private String orderId;
    private List<BookedItemWithPrice> bookedItemsWithPrice;
}