package com.rapido.customer.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rapido.customer.dto.AcceptBookingResponseDto;
import com.rapido.customer.dto.CompleteRideResponseDto;
import com.rapido.customer.dto.ConfrimRideResponse;
import com.rapido.customer.dto.CordinatesDto;
import com.rapido.customer.dto.CustomerDto;
import com.rapido.customer.dto.EarningSummaryDto;
import com.rapido.customer.dto.ResponseStructure;
import com.rapido.customer.dto.RiderHistoryDto;
import com.rapido.customer.dto.SearchDestLocResponseDto;
import com.rapido.customer.dto.SelectRideDto;
import com.rapido.customer.dto.VerifyOtpResponseDto;
import com.rapido.customer.entity.Booking;
import com.rapido.customer.entity.Cordinates;
import com.rapido.customer.entity.Customer;
import com.rapido.customer.exception.BookingNotFoundException;
import com.rapido.customer.exception.CustomerNotFoundException;
import com.rapido.customer.exception.RiderAlreaddyAcceptedException;
import com.rapido.customer.repository.BookingRepository;
import com.rapido.customer.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerrepository;
	@Autowired
	BookingRepository bookingrepository;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RedisService redisservice;

	public void createaccservice(CustomerDto customerdto) {
		Customer customer = new Customer();
		customer.setName(customerdto.getName());
		customer.setMobile(customerdto.getMobile());
		customer.setMail(customerdto.getMail());
		customer.setGender(customerdto.getGender());
		customerrepository.save(customer);
		int id = customer.getId();
		String otp = String.format("%04d", id % 10000);
		customer.setOtp(otp);
		customerrepository.save(customer);

	}

	public ResponseStructure<Customer> deleteaccservice(int id) {
		Customer customer = customerrepository.findById(id).orElseThrow(() -> new CustomerNotFoundException());
		customerrepository.delete(customer);
		ResponseStructure<Customer> response = new ResponseStructure<Customer>();
		response.setData(customer);
		response.setMessage("customer withbid is deleted");
		response.setStatuscode(HttpStatus.FOUND.value());
		return response;
	}

	public ResponseStructure<Customer> findbyidservice(int id) {
		Customer customer = customerrepository.findById(id).orElseThrow(() -> new CustomerNotFoundException());
		ResponseStructure<Customer> response = new ResponseStructure<Customer>();
		response.setData(customer);
		response.setMessage("customer with id is found");
		response.setStatuscode(HttpStatus.FOUND.value());
		return response;
	}

	public List<SearchDestLocResponseDto> searchDropLocation(String searchkey) {

		String token = "pk.ed0ebe59f7a6d7e4937a2d6c6c0e697e";

		String url = "https://us1.locationiq.com/v1/search" + "?key=" + token + "&q=" + searchkey + "&format=json";

		SearchDestLocResponseDto[] response = restTemplate.getForObject(url, SearchDestLocResponseDto[].class);

		return Arrays.asList(response);
	}

	public ConfrimRideResponse confirmRideService(int id, String vehicletype, String idempotentId,
			CordinatesDto cordinatesdto) {

		// 1. Check customer
		Customer customer = customerrepository.findById(id).orElseThrow(() -> new CustomerNotFoundException());
		Optional<Booking> existingBooking = bookingrepository.findByIdempotentId(idempotentId);

		if (existingBooking.isPresent()) {

			Booking booking = existingBooking.get();

			// Return the already-created booking
			ConfrimRideResponse response = new ConfrimRideResponse();

			response.setBookingid(booking.getId());
			response.setCustomerid(id);
			response.setStatus(booking.getStatus());
			response.setFare(booking.getFare());
			response.setVehicletype(booking.getVehicle());

			String otp = booking.getCustomer().getOtp();
			response.setOtp(otp);

			return response;
		}

		// 2. Get ride data from Redis
		Map<Object, Object> data = redisservice.getData(id);

		if (data == null || data.isEmpty()) {
			throw new RuntimeException("Ride details not found in Redis");
		}

		// 3. Get fare according to vehicle type
		String fareString;

		switch (vehicletype.toLowerCase()) {

		case "bike":
			fareString = data.get("bike").toString();
			break;

		case "auto":
			fareString = data.get("auto").toString();
			break;

		case "cab":
			fareString = data.get("cab").toString();
			break;

		default:
			throw new IllegalArgumentException("Invalid vehicle type. Use bike, auto or cab");
		}

		double fare = Double.parseDouble(fareString);

		// 4. Create Booking
		Booking booking = new Booking();
		double sourceLat = Double.parseDouble(data.get("sourceLat").toString());

		double sourceLon = Double.parseDouble(data.get("sourceLon").toString());

		double destinationLat = Double.parseDouble(data.get("destinationLat").toString());

		double destinationLon = Double.parseDouble(data.get("destinationLon").toString());
		Cordinates source = new Cordinates(sourceLat, sourceLon);

		Cordinates destination = new Cordinates(destinationLat, destinationLon);

		booking.setCustomer(customer);

		// Pickup and drop
		// See note below about Address vs String
		booking.setPaymentstatus("pending");

		booking.setFare(fare);

		booking.setVehicle(vehicletype);

		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();

		booking.setBookingdate(today.toString());
		booking.setBookingtime(now.toString());

		booking.setPickuptime(null);
		booking.setDroptime(null);

		booking.setStatus("pending");
		booking.setPickup(data.get("pickup").toString());
		booking.setDrop(data.get("drop").toString());
		booking.setSource(source);
		booking.setDestination(destination);
		double distance = Double.parseDouble(data.get("distance").toString());
		booking.setDistance(distance);
		booking.setIdempotentId(idempotentId);

		// 5. Save booking
		Booking savedBooking = bookingrepository.save(booking);
//		double sourceLat = Double.parseDouble(data.get("sourceLat").toString());
//
//		double sourceLon = Double.parseDouble(data.get("sourceLon").toString());
		List<String> nearbyriders = redisservice.findNearbyRiders(vehicletype, cordinatesdto.getLatitude(),
				cordinatesdto.getLongitude());

		// push the ride details to the riders

		for (String riderId : nearbyriders) {

			redisservice.pushRideRequestToRider(riderId, savedBooking.getId(), fare, distance);
		}
		// create booking info fare=val,distance=val and bookingid=val ---> map;

		ConfrimRideResponse response = new ConfrimRideResponse();
		response.setBookingid(savedBooking.getId());
		response.setCustomerid(id);
		response.setStatus("finding_riders");
		response.setFare(fare);
		response.setVehicletype(vehicletype);
		String otp = booking.getCustomer().getOtp();
		response.setOtp(otp);

//		redisservice.deleteData(id);
		return response;

	}

	public AcceptBookingResponseDto assignRiderService(int riderid, int bookingid) {

		Booking booking = bookingrepository.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());

		// Check whether another rider already accepted
		if (!booking.getStatus().equalsIgnoreCase("pending")) {
			throw new RiderAlreaddyAcceptedException();
		}

		// Assign rider
		booking.setRiderid(riderid);
		booking.setStatus("riderAssigned");

		bookingrepository.save(booking);

		// Get customer associated with this booking
		Customer customer = booking.getCustomer();

		AcceptBookingResponseDto response = new AcceptBookingResponseDto();

		response.setBookingid(booking.getId());
		response.setFare(booking.getFare());

		response.setPickuplocation(booking.getPickup());
		response.setCustomername(customer.getName());
		response.setCustomermobile(customer.getMobile());
		response.setDistance(booking.getDistance());

		return response;
	}

	public String moveTowardsPickupService(int bookingid) {

		Booking booking = bookingrepository.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());

		Cordinates pickup = booking.getSource();

		if (pickup == null) {
			throw new RuntimeException("Pickup coordinates not found");
		}

		String url = "https://www.google.com/maps/dir/?api=1" + "&destination=" + pickup.getLatitude() + ","
				+ pickup.getLongitude() + "&travelmode=driving";

		return url;
	}

	public String updateToArrivedService(int bookingid) {
		Booking booking = bookingrepository.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());
		booking.setStatus("riderReachedPickup");
		bookingrepository.save(booking);
		return "rider has reached to pickup location";
	}

	public VerifyOtpResponseDto verifyOtpService(int bookingid, String otp) {

		Booking booking = bookingrepository.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());

		Customer customer = booking.getCustomer();

		if (customer == null) {
			throw new RuntimeException("Customer not found");
		}

		// OTP from database
		String actualOtp = customer.getOtp();

		// Convert API int OTP to String
		String enteredOtp = String.valueOf(otp);

		VerifyOtpResponseDto response = new VerifyOtpResponseDto();

		if (actualOtp.equals(enteredOtp)) {

			booking.setStatus("rideStarted");
			LocalTime now = LocalTime.now();
			booking.setPickuptime(now.toString());

			bookingrepository.save(booking);

			response.setVerified(true);
			response.setMessage("OTP verified successfully");

		} else {

			response.setVerified(false);
			response.setMessage("Invalid OTP");
		}

		return response;
	}

	public String moveTowardsDropService(int bookingid) {
		Booking booking = bookingrepository.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());

		Cordinates pickup = booking.getDestination();

		if (pickup == null) {
			throw new RuntimeException("Destination coordinates not found");
		}

		String url = "https://www.google.com/maps/dir/?api=1" + "&destination=" + pickup.getLatitude() + ","
				+ pickup.getLongitude() + "&travelmode=driving";

		return url;
	}

	public String updateToReachedService(int bookingid) {
		Booking booking = bookingrepository.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());
		booking.setStatus("riderReachedDestination");
		bookingrepository.save(booking);
		return "rider has reached to Drop location";
	}

	public CompleteRideResponseDto completeRideService(int bookingid) {

		Booking booking = bookingrepository.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());

		// 1. Update booking status
		booking.setStatus("completed");

		// 2. Update payment status
		booking.setPaymentstatus("completed");

		// 3. Set drop time
		LocalTime now = LocalTime.now();
		booking.setDroptime(now.toString());

		// 4. Calculate platform charge and rider charge
		double fare = booking.getFare();

		double platformCharge = fare * 0.10;
		double riderCharge = fare - platformCharge;

		booking.setPlatformshare(platformCharge);
		booking.setRidershare(riderCharge);

		// 5. Calculate ride duration
		LocalTime pickupTime = LocalTime.parse(booking.getPickuptime());
		LocalTime dropTime = LocalTime.parse(booking.getDroptime());

		double duration = Duration.between(pickupTime, dropTime).toMinutes();

		booking.setDuration(String.valueOf(duration));

		// 6. Save everything in one go
		Booking savedBooking = bookingrepository.save(booking);

		// 7. Create response
		CompleteRideResponseDto response = new CompleteRideResponseDto();

		response.setBookingid(savedBooking.getId());
		response.setRiderid(savedBooking.getRiderid());
		response.setFare(savedBooking.getFare());
		response.setStatus(savedBooking.getStatus());
		response.setPaymentstatus(savedBooking.getPaymentstatus());
		response.setPlatformcharge(savedBooking.getPlatformshare());
		response.setRidercharge(savedBooking.getRidershare());
		return response;
	}

	public ResponseStructure<String> cancelRideService(int bookingid) {

		Booking booking = bookingrepository.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());

		String status = booking.getStatus();

		if (status.equalsIgnoreCase("confirmed") || status.equalsIgnoreCase("riderAssigned")) {

			booking.setStatus("customercancel");
			bookingrepository.save(booking);
			ResponseStructure<String> response = new ResponseStructure<String>();
			response.setData("cancled the ride by customer");
			response.setMessage("cancled the ride by customer");
			response.setStatuscode(HttpStatus.NOT_FOUND.value());
			return response;

		} else if (status.equalsIgnoreCase("started")) {
			ResponseStructure<String> response = new ResponseStructure<String>();
			response.setMessage("Ride cannot be cancelled in current status: " + status);

			return response;

		} else {
			ResponseStructure<String> response = new ResponseStructure<String>();
			response.setMessage("Ride cannot be cancelled in current status: " + status);

			return response;
		}
	}

	public List<Booking> rideHistoryService(int customerid, String status) {

		if (status.equalsIgnoreCase("all")) {

			return bookingrepository.findByCustomerId(customerid);

		} else if (status.equalsIgnoreCase("completed")) {

			return bookingrepository.findByCustomerIdAndStatus(customerid, "completed");

		} else if (status.equalsIgnoreCase("canceled")) {

			return bookingrepository.findByCustomerIdAndStatus(customerid, "customercancel");

		} else {

			throw new IllegalArgumentException("Invalid status. Use all, completed or canceled");
		}
	}

	public List<RiderHistoryDto> riderHistoryService(int riderid, String status) {

		List<Booking> bookings;

		if (status.equalsIgnoreCase("all")) {

			bookings = bookingrepository.findByRiderid(riderid);

		} else if (status.equalsIgnoreCase("completed")) {

			bookings = bookingrepository.findByRideridAndStatus(riderid, "completed");

		} else if (status.equalsIgnoreCase("ridercancel")) {

			bookings = bookingrepository.findByRideridAndStatus(riderid, "ridercancel");

		} else {

			throw new IllegalArgumentException("Invalid status. Use all, completed or ridercancel");
		}

		List<RiderHistoryDto> history = new ArrayList<>();

		for (Booking booking : bookings) {

			RiderHistoryDto dto = new RiderHistoryDto();

			dto.setBookingid(booking.getId());
			dto.setPickup(booking.getPickup());
			dto.setDrop(booking.getDrop());
			dto.setVehicle(booking.getVehicle());
			dto.setFare(booking.getFare());
			dto.setDistance(booking.getDistance());
			dto.setBookingdate(booking.getBookingdate());
			dto.setBookingtime(booking.getBookingtime());
			dto.setDuration(booking.getDuration());
			dto.setStatus(booking.getStatus());
			dto.setPaymentstatus(booking.getPaymentstatus());

			history.add(dto);
		}

		return history;
	}

	public EarningSummaryDto earningSummaryService(int riderid, String from, String to) {

		List<Booking> bookings = bookingrepository.findByRideridAndStatusAndBookingdateBetween(riderid, "completed",
				from, to);

		int noofrides = bookings.size();

		double ridershare = 0;
		double totaldistance = 0;

		for (Booking booking : bookings) {

			ridershare += booking.getRidershare();

			totaldistance += booking.getDistance();
		}

		EarningSummaryDto response = new EarningSummaryDto();

		response.setNoofrides(noofrides);
		response.setRidershare(ridershare);
		response.setTotaldistance(totaldistance);

		return response;
	}

}
