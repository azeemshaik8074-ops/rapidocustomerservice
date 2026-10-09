package com.rapido.customer.dto;

import com.rapido.customer.entity.Cordinates;

public class SelectRideDto {
	
	private Cordinates sourceloc;
	private Cordinates destinationloc;
	public SelectRideDto(Cordinates sourceloc, Cordinates destinationloc) {
		super();
		this.sourceloc = sourceloc;
		this.destinationloc = destinationloc;
	}
	public SelectRideDto() {
		super();
	}
	public Cordinates getSourceloc() {
		return sourceloc;
	}
	public void setSourceloc(Cordinates sourceloc) {
		this.sourceloc = sourceloc;
	}
	public Cordinates getDestinationloc() {
		return destinationloc;
	}
	public void setDestinationloc(Cordinates destinationloc) {
		this.destinationloc = destinationloc;
	}
	
	

}
