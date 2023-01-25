package com.oms.demo.tzatziki_quickstart.controllers;

import com.oms.demo.tzatziki_quickstart.beans.api.ItemToBook;
import com.oms.demo.tzatziki_quickstart.beans.api.OrderInformation;
import com.oms.demo.tzatziki_quickstart.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/warehouses/{warehouseId}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class WarehouseController {
    private final BookingService bookingService;

    @PostMapping
    public OrderInformation bookOrderForWarehouse(@PathVariable("warehouseId") String warehouseId, @RequestBody List<ItemToBook> itemToBooks) {
        return bookingService.bookOrder(warehouseId, itemToBooks);
    }
}
