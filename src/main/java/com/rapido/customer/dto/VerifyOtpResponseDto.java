package com.rapido.customer.dto;

public class VerifyOtpResponseDto {

	private boolean verified;
	private String message;
	private int riderid;

	public VerifyOtpResponseDto() {
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getRiderid() {
		return riderid;
	}

	public void setRiderid(int riderid) {
		this.riderid = riderid;
	}
}
