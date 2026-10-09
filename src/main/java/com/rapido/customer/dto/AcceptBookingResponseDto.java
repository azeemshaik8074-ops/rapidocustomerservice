package com.rapido.customer.dto;

public class AcceptBookingResponseDto {

    private int bookingid;
    private String pickuplocation;
    private double fare;
    private double distance;
    private String customername;
    private long customermobile;
    public AcceptBookingResponseDto(int bookingid, String pickuplocation, double fare, double distance,
			String customername, long customermobile) {
		super();
		this.bookingid = bookingid;
		this.pickuplocation = pickuplocation;
		this.fare = fare;
		this.distance = distance;
		this.customername = customername;
		this.customermobile = customermobile;
	}

	public AcceptBookingResponseDto() {
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public String getPickuplocation() {
        return pickuplocation;
    }

    public void setPickuplocation(String pickuplocation) {
        this.pickuplocation = pickuplocation;
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

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public long getCustomermobile() {
        return customermobile;
    }

    public void setCustomermobile(long customermobile) {
        this.customermobile = customermobile;
    }
}