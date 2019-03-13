package com.a4tech.dao.entity;

import java.util.Date;

public class OrdersHistory {

	
	private Integer ordHistorySrNo;
	private Date delivaryDate;
	private String truckNo;
	private String districtName;
	private String materialType;
	private Integer totalOrders;
	private Integer totalOrderQty;
	
	public Integer getOrdHistorySrNo() {
		return ordHistorySrNo;
	}
	public void setOrdHistorySrNo(Integer ordHistorySrNo) {
		this.ordHistorySrNo = ordHistorySrNo;
	}
	public Date getDelivaryDate() {
		return delivaryDate;
	}
	public void setDelivaryDate(Date delivaryDate) {
		this.delivaryDate = delivaryDate;
	}
	public String getTruckNo() {
		return truckNo;
	}
	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public Integer getTotalOrders() {
		return totalOrders;
	}
	public void setTotalOrders(Integer totalOrders) {
		this.totalOrders = totalOrders;
	}
	public Integer getTotalOrderQty() {
		return totalOrderQty;
	}
	public void setTotalOrderQty(Integer totalOrderQty) {
		this.totalOrderQty = totalOrderQty;
	}
	
}
