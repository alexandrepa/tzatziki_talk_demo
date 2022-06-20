package com.oms.demo.tzatziki_quickstart.repositories;

import com.oms.demo.tzatziki_quickstart.beans.dao.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, String> {
}
