package com.rapido.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rapido.customer.dto.AcceptBookingResponseDto;
import com.rapido.customer.dto.CompleteRideResponseDto;
import com.rapido.customer.dto.ConfrimRideResponse;
import com.rapido.customer.dto.CordinatesDto;
import com.rapido.customer.dto.CustomerDto;
import com.rapido.customer.dto.EarningSummaryDto;
import com.rapido.customer.dto.ResponseSelectDto;
import com.rapido.customer.dto.ResponseStructure;
import com.rapido.customer.dto.RiderHistoryDto;
import com.rapido.customer.dto.SearchDestLocResponseDto;
import com.rapido.customer.dto.SelectRideDto;
import com.rapido.customer.dto.VerifyOtpResponseDto;
import com.rapido.customer.entity.Booking;
import com.rapido.customer.entity.Customer;
import com.rapido.customer.service.CordinatesService;
import com.rapido.customer.service.CustomerService;

@RestController
public class CustomerController {
	@Autowired
	CustomerService customerservice;
	@Autowired
	CordinatesService cordinateservice;

	@PostMapping("/customer/createaccount")
	public void createacc(@RequestBody CustomerDto customerdto) {
		customerservice.createaccservice(customerdto);
	}

	@DeleteMapping("/customer/deleteaccount")
	public ResponseStructure<Customer> deleteacc(@RequestParam int id) {
		return customerservice.deleteaccservice(id);
	}

	@GetMapping("/customer/findcustomer")
	public ResponseStructure<Customer> findcustomer(@RequestParam int id) {
		return customerservice.findbyidservice(id);
	}

	@GetMapping("/customer/searchdroplocation")
	public List<SearchDestLocResponseDto> searchDropLocation(@RequestParam String searchkey) {

		return customerservice.searchDropLocation(searchkey);
	}

	@PostMapping("/customer/selectride")
	public ResponseSelectDto selectride(@RequestParam int id, @RequestBody SelectRideDto selectridedto) {

		return cordinateservice.selectrideservice(id, selectridedto);
	}

	@PostMapping("/customer/confirmride")
	public ConfrimRideResponse confirmride(@RequestParam int id, @RequestParam String vehicletype,
			@RequestParam String idempotentId, @RequestBody CordinatesDto cordinatesdto) {

		return customerservice.confirmRideService(id, vehicletype, idempotentId, cordinatesdto);
	}

	@PostMapping("/customer/assignrider")
	public AcceptBookingResponseDto assignRider(@RequestParam int riderid, @RequestParam int bookingid) {

		return customerservice.assignRiderService(riderid, bookingid);
	}

	@GetMapping("/customer/moveTowardsPickup")
	public String moveTowardsPickup(@RequestParam int bookingid) {

		return customerservice.moveTowardsPickupService(bookingid);
	}

	@PostMapping("/customer/UpdateToArrived")
	public String updateToArrived(@RequestParam int bookingid) {
		return customerservice.updateToArrivedService(bookingid);
	}

	@PostMapping("/customer/verifyotp")
	public VerifyOtpResponseDto verifyOtp(@RequestParam int bookingid, @RequestParam String otp) {

		return customerservice.verifyOtpService(bookingid, otp);
	}

	@GetMapping("/customer/moveTowardsDrop")
	public String moveTowardsDrop(@RequestParam int bookingid) {

		return customerservice.moveTowardsDropService(bookingid);
	}

	@PostMapping("/customer/UpdateToReached")
	public String updateToReached(@RequestParam int bookingid) {
		return customerservice.updateToReachedService(bookingid);
	}

	@PostMapping("/customer/completeRide")
	public CompleteRideResponseDto completeRide(@RequestParam int bookingid) {

		return customerservice.completeRideService(bookingid);
	}

	@PostMapping("/customer/cancelride")
	public ResponseStructure<String> cancelRide(@RequestParam int bookingid) {
		return customerservice.cancelRideService(bookingid);
	}

	@GetMapping("/customer/ridehistory")
	public List<Booking> rideHistory(@RequestParam int customerid, @RequestParam String status) {

		return customerservice.rideHistoryService(customerid, status);
	}

	@GetMapping("/customer/riderhistory")
	public List<RiderHistoryDto> riderHistory(@RequestParam int riderid, @RequestParam String status) {

		return customerservice.riderHistoryService(riderid, status);
	}

	@GetMapping("/customer/earning/summary")
	public EarningSummaryDto earningSummary(@RequestParam int riderid, @RequestParam String from,
			@RequestParam String to) {

		return customerservice.earningSummaryService(riderid, from, to);
	}

}
