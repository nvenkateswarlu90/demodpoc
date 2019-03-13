package com.a4tech.shipping.model;

import java.util.Date;

public class OrderGroup {
	private String delivaryNo;
	private String originalOrderQty;
	private String truckNo;
	private String truckCapacity;
	private int    truckOrderQty;
	private String delivaryDate;
	private String materialType;
	private String districtName;
	private String latitude;
	private String longitude;
	private String nameShipToParty;
	private Date   orderShippingDate;
	private Integer shippingDelivaryId;
	private String  wheelerType;
	
	
	public Integer getShippingDelivaryId() {
		return shippingDelivaryId;
	}
	public void setShippingDelivaryId(Integer shippingDelivaryId) {
		this.shippingDelivaryId = shippingDelivaryId;
	}
	public Date getOrderShippingDate() {
		return orderShippingDate;
	}
	public void setOrderShippingDate(Date orderShippingDate) {
		this.orderShippingDate = orderShippingDate;
	}
	public String getNameShipToParty() {
		return nameShipToParty;
	}
	public void setNameShipToParty(String nameShipToParty) {
		this.nameShipToParty = nameShipToParty;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getDelivaryNo() {
		return delivaryNo;
	}
	public void setDelivaryNo(String delivaryNo) {
		this.delivaryNo = delivaryNo;
	}
	public String getOriginalOrderQty() {
		return originalOrderQty;
	}
	public void setOriginalOrderQty(String originalOrderQty) {
		this.originalOrderQty = originalOrderQty;
	}
	public String getTruckNo() {
		return truckNo;
	}
	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}
	public String getTruckCapacity() {
		return truckCapacity;
	}
	public void setTruckCapacity(String truckCapacity) {
		this.truckCapacity = truckCapacity;
	}
	
	public int getTruckOrderQty() {
		return truckOrderQty;
	}
	public void setTruckOrderQty(int truckOrderQty) {
		this.truckOrderQty = truckOrderQty;
	}
	public String getDelivaryDate() {
		return delivaryDate;
	}
	public void setDelivaryDate(String delivaryDate) {
		this.delivaryDate = delivaryDate;
	}
	public String getWheelerType() {
		return wheelerType;
	}
	public void setWheelerType(String wheelerType) {
		this.wheelerType = wheelerType;
	}

}
