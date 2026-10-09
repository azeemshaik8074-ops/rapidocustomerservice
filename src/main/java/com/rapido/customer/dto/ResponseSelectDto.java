package com.rapido.customer.dto;

import java.util.Map;

import com.rapido.customer.entity.Cordinates;

public class ResponseSelectDto {

	private String source;
	private String destination;
	private double distance;
	private Map<String, Double> fare;

	public ResponseSelectDto() {
		super();
	}

	public ResponseSelectDto(String source, String destination, double distance, Map<String, Double> fare) {
		super();
		this.source = source;
		this.destination = destination;
		this.distance = distance;
		this.fare = fare;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Map<String, Double> getFare() {
		return fare;
	}

	public void setFare(Map<String, Double> fare) {
		this.fare = fare;
	}

	
}