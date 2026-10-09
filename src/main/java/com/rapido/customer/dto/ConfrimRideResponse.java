package com.rapido.customer.dto;

public class ConfrimRideResponse {
	private int bookingid;
	private int customerid;
	private String vehicletype;
	private double fare;
	private String otp;

	private String status;

	public ConfrimRideResponse(int bookingid, int customerid, String vehicletype, double fare, String status,String otp) {
		super();
		this.bookingid = bookingid;
		this.customerid = customerid;
		this.vehicletype = vehicletype;
		this.fare = fare;
		this.otp=otp;

		this.status = status;
	}

	public ConfrimRideResponse() {
		super();
	}

	public int getBookingid() {
		return bookingid;
	}

	public void setBookingid(int bookingid) {
		this.bookingid = bookingid;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public String getVehicletype() {
		return vehicletype;
	}

	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
	

}
