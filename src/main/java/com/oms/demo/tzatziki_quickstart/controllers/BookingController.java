package com.oms.demo.tzatziki_quickstart.controllers;

import com.oms.demo.tzatziki_quickstart.beans.api.BookingItem;
import com.oms.demo.tzatziki_quickstart.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders/{orderId}/book", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public String createBooking(@PathVariable("orderId") String orderId, @RequestBody List<BookingItem> bookingItems) {
        return bookingService.bookOrder(orderId, bookingItems) ? "Booking done" : "Unable to book properly";
    }
}
