package com.a4tech.shipping.model;

public class FtpLoginBean {
	private String asiNumber;
	private String userName;
	private String password;
	private String environemtType;
	public String getEnvironemtType() {
		return environemtType;
	}
	public void setEnvironemtType(String environemtType) {
		this.environemtType = environemtType;
	}
	public String getAsiNumber() {
		return asiNumber;
	}
	public void setAsiNumber(String asiNumber) {
		this.asiNumber = asiNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
