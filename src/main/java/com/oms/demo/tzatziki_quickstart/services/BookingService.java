package com.oms.demo.tzatziki_quickstart.services;

import com.oms.demo.tzatziki_quickstart.beans.api.BookingItem;
import com.oms.demo.tzatziki_quickstart.beans.dao.Booking;
import com.oms.demo.tzatziki_quickstart.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    public boolean bookOrder(String orderId, List<BookingItem> bookingItems){
        bookingItems.stream().map(item -> Booking.builder()
                .orderId(orderId)
                .item(item.getItem())
                .quantity(item.getQuantity())
                .build()).forEach(bookingRepository::save);

        return true;
    }
}
