package com.rapido.customer.dto;

import com.rapido.customer.entity.Cordinates;

public class RiderBookingRequest {

	private int bookingid;
	private double fare;
	private double distance;
//	private Cordinates pickup;

	public RiderBookingRequest() {
	}

	public RiderBookingRequest(int bookingid, double fare, double distance) {
		this.bookingid = bookingid;
		this.fare = fare;
		this.distance = distance;
//		this.pickup=pickup;
	}

	public int getBookingid() {
		return bookingid;
	}

	public void setBookingid(int bookingid) {
		this.bookingid = bookingid;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

//	public Cordinates getPickup() {
//		return pickup;
//	}
//
//	public void setPickup(Cordinates pickup) {
//		this.pickup = pickup;
//	}
	
}