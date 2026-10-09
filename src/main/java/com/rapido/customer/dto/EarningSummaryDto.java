package com.rapido.customer.dto;

public class EarningSummaryDto {

    private int noofrides;
    private double ridershare;
    private double totaldistance;
	public EarningSummaryDto(int noofrides, double ridershare, double totaldistance) {
		super();
		this.noofrides = noofrides;
		this.ridershare = ridershare;
		this.totaldistance = totaldistance;
	}
	public EarningSummaryDto() {
		super();
	}
	public int getNoofrides() {
		return noofrides;
	}
	public void setNoofrides(int noofrides) {
		this.noofrides = noofrides;
	}
	public double getRidershare() {
		return ridershare;
	}
	public void setRidershare(double ridershare) {
		this.ridershare = ridershare;
	}
	public double getTotaldistance() {
		return totaldistance;
	}
	public void setTotaldistance(double totaldistance) {
		this.totaldistance = totaldistance;
	}

    
}