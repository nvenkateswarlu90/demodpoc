package com.a4tech.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shipping_final_orders")
public class ShippingFinalOrders {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="truck_no")
	private String    truckNo;
	@Column(name="load_type")
	private String    loadType;
	@Column(name="material_type")
	private String    materialType;
	@Column(name="total_orders")
	private Integer   totalOrders;
	@Column(name="total_order_quantity")
	private Integer   totalOrderQuantity;
	@Column(name="truck_capacity")
	private String    truckCapacity;
	@Column(name="pending_quantity")
	private Integer   pendingQuantity;
	@Column(name="plant")
	private String    plant;
	@Column(name="total_kilometers")
	private String    totalKilometers;
	@Column(name="delivary_date")
	private String    delivaryDate;
	@Column(name="district_name")
	private String    districtName;
	@Column(name="shipping_status")
	private String    shippingStatus;
	@Column(name="estimation_time")
	private String    estimationTime;
	@Column(name="shipping_order_id")
	private Integer   shippingOrderId;
	@Column(name="wheeler_type")
	private String    wheelerType;
	
	public String getEstimationTime() {
		return estimationTime;
	}
	public void setEstimationTime(String estimationTime) {
		this.estimationTime = estimationTime;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getTruckNo() {
		return truckNo;
	}
	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}
	public String getLoadType() {
		return loadType;
	}
	public void setLoadType(String loadType) {
		this.loadType = loadType;
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
	public Integer getTotalOrderQuantity() {
		return totalOrderQuantity;
	}
	public void setTotalOrderQuantity(Integer totalOrderQuantity) {
		this.totalOrderQuantity = totalOrderQuantity;
	}
	public String getTruckCapacity() {
		return truckCapacity;
	}
	public void setTruckCapacity(String truckCapacity) {
		this.truckCapacity = truckCapacity;
	}
	public Integer getPendingQuantity() {
		return pendingQuantity;
	}
	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getTotalKilometers() {
		return totalKilometers;
	}
	public void setTotalKilometers(String totalKilometers) {
		this.totalKilometers = totalKilometers;
	}
	public String getDelivaryDate() {
		return delivaryDate;
	}
	public void setDelivaryDate(String delivaryDate) {
		this.delivaryDate = delivaryDate;
	}
	public String getShippingStatus() {
		return shippingStatus;
	}
	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	public String getWheelerType() {
		return wheelerType;
	}
	public void setWheelerType(String wheelerType) {
		this.wheelerType = wheelerType;
	}
	public Integer getShippingOrderId() {
		return shippingOrderId;
	}
	public void setShippingOrderId(Integer shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}
}
