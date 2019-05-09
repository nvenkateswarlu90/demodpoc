package com.a4tech.shipping.model;

import java.util.Date;

public class UsedTrucksModel {
	private String  truckNo;
	private String  vehicalType;
	private Integer  vehicalCapacity;
	private String wheelerType;
	private String  transporterName;
	private Date    shippedDate;
	private String  districtName;
	private String taggedTime;
	
	public String getTaggedTime() {
		return taggedTime;
	}
	public void setTaggedTime(String taggedTime) {
		this.taggedTime = taggedTime;
	}
	public String getTruckNo() {
		return truckNo;
	}
	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}
	public String getVehicalType() {
		return vehicalType;
	}
	public void setVehicalType(String vehicalType) {
		this.vehicalType = vehicalType;
	}
	
	public Integer getVehicalCapacity() {
		return vehicalCapacity;
	}
	public void setVehicalCapacity(Integer vehicalCapacity) {
		this.vehicalCapacity = vehicalCapacity;
	}
	
	public String getWheelerType() {
		return wheelerType;
	}
	public void setWheelerType(String wheelerType) {
		this.wheelerType = wheelerType;
	}
	public String getTransporterName() {
		return transporterName;
	}
	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}
	public Date getShippedDate() {
		return shippedDate;
	}
	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
}
