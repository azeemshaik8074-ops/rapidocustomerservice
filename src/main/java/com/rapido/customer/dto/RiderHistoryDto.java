package com.rapido.customer.dto;

public class RiderHistoryDto {

	private int bookingid;
	private String pickup;
	private String drop;
	private String vehicle;
	private double fare;
	private double distance;
	private String bookingdate;
	private String bookingtime;
	private String duration;
	private String status;
	private String paymentstatus;
	public RiderHistoryDto(int bookingid, String pickup, String drop, String vehicle, double fare, double distance,
			String bookingdate, String bookingtime, String duration, String status, String paymentstatus) {
		super();
		this.bookingid = bookingid;
		this.pickup = pickup;
		this.drop = drop;
		this.vehicle = vehicle;
		this.fare = fare;
		this.distance = distance;
		this.bookingdate = bookingdate;
		this.bookingtime = bookingtime;
		this.duration = duration;
		this.status = status;
		this.paymentstatus = paymentstatus;
	}
	public RiderHistoryDto() {
		super();
	}
	public int getBookingid() {
		return bookingid;
	}
	public void setBookingid(int bookingid) {
		this.bookingid = bookingid;
	}
	public String getPickup() {
		return pickup;
	}
	public void setPickup(String pickup) {
		this.pickup = pickup;
	}
	public String getDrop() {
		return drop;
	}
	public void setDrop(String drop) {
		this.drop = drop;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
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
	public String getBookingdate() {
		return bookingdate;
	}
	public void setBookingdate(String bookingdate) {
		this.bookingdate = bookingdate;
	}
	public String getBookingtime() {
		return bookingtime;
	}
	public void setBookingtime(String bookingtime) {
		this.bookingtime = bookingtime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
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
	
}
