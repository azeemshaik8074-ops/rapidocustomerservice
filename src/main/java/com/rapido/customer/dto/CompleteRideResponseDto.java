package com.rapido.customer.dto;

public class CompleteRideResponseDto {

	private int bookingid;
	private int riderid;
	private double fare;
	private String status;
	private String paymentstatus;
	private double ridercharge;
	private double platformcharge;
	

	public CompleteRideResponseDto(int bookingid, int riderid, double fare, String status, String paymentstatus,
			double ridercharge, double platformcharge) {
		super();
		this.bookingid = bookingid;
		this.riderid = riderid;
		this.fare = fare;
		this.status = status;
		this.paymentstatus = paymentstatus;
		this.ridercharge = ridercharge;
		this.platformcharge = platformcharge;
	}

	public CompleteRideResponseDto() {
	}

	public int getBookingid() {
		return bookingid;
	}

	public void setBookingid(int bookingid) {
		this.bookingid = bookingid;
	}

	public int getRiderid() {
		return riderid;
	}

	public void setRiderid(int riderid) {
		this.riderid = riderid;
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

	public String getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}

	public double getRidercharge() {
		return ridercharge;
	}

	public void setRidercharge(double ridercharge) {
		this.ridercharge = ridercharge;
	}

	public double getPlatformcharge() {
		return platformcharge;
	}

	public void setPlatformcharge(double platformcharge) {
		this.platformcharge = platformcharge;
	}
	
}
