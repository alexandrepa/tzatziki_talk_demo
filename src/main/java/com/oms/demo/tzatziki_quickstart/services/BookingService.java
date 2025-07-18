package com.oms.demo.tzatziki_quickstart.services;

import com.oms.demo.tzatziki_quickstart.beans.api.BookedItem;
import com.oms.demo.tzatziki_quickstart.beans.api.BookedItemWithPrice;
import com.oms.demo.tzatziki_quickstart.beans.api.ItemToBook;
import com.oms.demo.tzatziki_quickstart.beans.api.OrderInformation;
import com.oms.demo.tzatziki_quickstart.beans.dao.Booking;
import com.oms.demo.tzatziki_quickstart.beans.kafka.generated.OrderStatus;
import com.oms.demo.tzatziki_quickstart.beans.kafka.generated.OrderStatusUpdate;
import com.oms.demo.tzatziki_quickstart.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final StockApiCallService stockApiCallService;
    private final PriceApiCallService priceApiCallService;
    private final BookingRepository bookingRepository;
    private final KafkaTemplate<String, GenericRecord> avroKafkaTemplate;

    public OrderInformation bookOrder(String warehouse, List<ItemToBook> itemToBooks) {
        String orderId = UUID.randomUUID().toString();

        List<BookedItem> bookedItems = stockApiCallService.bookItems(warehouse, itemToBooks);

        List<BookedItemWithPrice> bookedItemWithPrices = bookedItems.stream()
                .map(bookedItem -> BookedItemWithPrice.from(bookedItem, priceApiCallService.getPrice(extractCountry(warehouse), bookedItem.getItemId())))
                .toList();

        bookingRepository.save(
                Booking.builder()
                        .orderId(orderId)
                        .bookedItemsWithPrice(bookedItemWithPrices)
                        .build());

        avroKafkaTemplate.send(new ProducerRecord<>(
                "order_status_update",
                OrderStatusUpdate.newBuilder()
                        .setOrderId(orderId)
                        .setStatus(OrderStatus.CREATED)
                        .build()
        ));

        return OrderInformation.builder()
                .orderId(orderId)
                .bookedItemsWithPrice(bookedItemWithPrices)
                .build();
    }

    private static String extractCountry(String warehouse) {
        return warehouse.substring(0, 2);
    }
}
