package com.oms.demo.tzatziki_quickstart.beans.dao;

import com.oms.demo.tzatziki_quickstart.beans.api.BookedItemWithPrice;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String orderId;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<BookedItemWithPrice> bookedItemsWithPrice;
}
