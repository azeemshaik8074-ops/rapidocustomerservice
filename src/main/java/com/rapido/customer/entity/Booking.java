package com.rapido.customer.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String bookingdate;
	private String bookingtime;
	private String pickuptime;
	private String droptime;
	private String duration;
	private double fare;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	private String pickup;
	private String drop;
	private String vehicle;
	private String paymentstatus;
	private double distance;
	private int riderid;
	private String status;
	private double ridershare;
	private double platformshare;
	@Column(name = "idempotent_id", unique = true, nullable = false)
	private String idempotentId;
	@OneToOne(cascade = CascadeType.ALL)
	private Cordinates source;

	@OneToOne(cascade = CascadeType.ALL)
	private Cordinates destination;

	public Booking(String bookingdate, String bookingtime, String pickuptime, String droptime, double fare,
			Customer customer, String pickup, String drop, String vehicle, String paymentstatus, double distance,
			int riderid, String status, Cordinates source, Cordinates destination, String duration,String idempotentId,double ridershare,double platformshare) {
		super();
		this.bookingdate = bookingdate;
		this.bookingtime = bookingtime;
		this.pickuptime = pickuptime;
		this.droptime = droptime;
		this.fare = fare;
		this.customer = customer;
		this.pickup = pickup;
		this.drop = drop;
		this.vehicle = vehicle;
		this.paymentstatus = paymentstatus;
		this.distance = distance;
		this.riderid = riderid;
		this.status = status;
		this.source = source;
		this.destination = destination;
		this.duration = duration;
		this.idempotentId=idempotentId;
	}

	public Booking() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getPickuptime() {
		return pickuptime;
	}

	public void setPickuptime(String pickuptime) {
		this.pickuptime = pickuptime;
	}

	public String getDroptime() {
		return droptime;
	}

	public void setDroptime(String droptime) {
		this.droptime = droptime;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public String getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getRiderid() {
		return riderid;
	}

	public void setRiderid(int riderid) {
		this.riderid = riderid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Cordinates getSource() {
		return source;
	}

	public void setSource(Cordinates source) {
		this.source = source;
	}

	public Cordinates getDestination() {
		return destination;
	}

	public void setDestination(Cordinates destination) {
		this.destination = destination;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getIdempotentId() {
		return idempotentId;
	}

	public void setIdempotentId(String idempotentId) {
		this.idempotentId = idempotentId;
	}

	public double getRidershare() {
		return ridershare;
	}

	public void setRidershare(double ridershare) {
		this.ridershare = ridershare;
	}

	public double getPlatformshare() {
		return platformshare;
	}

	public void setPlatformshare(double platformshare) {
		this.platformshare = platformshare;
	}
	
}
