package com.rapido.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rapido.customer.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalException {
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseStructure<String> handleCustomerException()
	{
		ResponseStructure<String> rs=new ResponseStructure<String>();
		rs.setData("customer not found");
		rs.setMessage("customer not found");
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		return rs;
	}
	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseStructure<String> handleBookingException()
	{
		ResponseStructure<String> rs=new ResponseStructure<String>();
		rs.setData("booking with id not found");
		rs.setMessage("not found");
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		return rs;
	}
	@ExceptionHandler(RiderAlreaddyAcceptedException.class)
	public ResponseStructure<String> handleRiderAlreadyAcceptedException()
	{
		ResponseStructure<String> rs=new ResponseStructure<String>();
		rs.setData("rider already accepted");
		rs.setMessage("rider already accepted");
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		return rs;
	}
	

}
