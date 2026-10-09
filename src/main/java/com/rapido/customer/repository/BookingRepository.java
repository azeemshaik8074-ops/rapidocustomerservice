package com.rapido.customer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rapido.customer.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
	Optional<Booking> findByIdempotentId(String idempotentId);

	List<Booking> findByCustomerId(int customerid);

	List<Booking> findByCustomerIdAndStatus(int customerid, String status);

	List<Booking> findByRiderid(int riderid);

	List<Booking> findByRideridAndStatus(int riderid, String status);

	List<Booking> findByRideridAndStatusAndBookingdateBetween(int riderid, String status, String from, String to);
}
