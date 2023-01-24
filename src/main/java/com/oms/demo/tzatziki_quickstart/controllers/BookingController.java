package com.oms.demo.tzatziki_quickstart.controllers;

import com.oms.demo.tzatziki_quickstart.beans.api.BookedItemWithPrice;
import com.oms.demo.tzatziki_quickstart.beans.api.ItemToBook;
import com.oms.demo.tzatziki_quickstart.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders/{orderId}/create", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public List<BookedItemWithPrice> createOrder(@PathVariable("orderId") String orderId, @RequestBody List<ItemToBook> itemToBooks) {
        return bookingService.bookOrder(orderId, itemToBooks);
    }
}
