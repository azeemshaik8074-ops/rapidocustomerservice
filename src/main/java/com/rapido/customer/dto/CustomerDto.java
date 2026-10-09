package com.rapido.customer.dto;

public class CustomerDto {
	private String name;
	private long mobile;
	private String mail;
	private String gender;
	public CustomerDto(String name, long mobile, String mail, String gender) {
		super();
		this.name = name;
		this.mobile = mobile;
		this.mail = mail;
		this.gender = gender;
	}
	public CustomerDto() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMobile() {
		return mobile;
	}
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	

}
