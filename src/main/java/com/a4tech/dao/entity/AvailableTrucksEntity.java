package com.a4tech.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="available_trucks_pool")
public class AvailableTrucksEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Truck_id")
    private int truckId;
	@Column(name="SL_NO")
	private String slNo;
	@Column(name="VEHICLE_NO")
	private String vehiclNo;
	@Column(name="VEHICLE_TYPE")
	private int vehicleType;
	@Column(name="WHEELS")
	private String wheels;	
	@Column(name="ENTRY_TYPE")
	private String entryType;
	@Column(name="TAGGED_TRANSPORTER")
	private String taggedTranspoter;
	@Column(name="DO_NO")
	private String doNo;
	@Column(name="TAGGED_DATE")
	private String  taggedDate;
	@Column(name="TAGGED_TIME")
	private String taggedTime;
	@Column(name="DELAY")
	private String delay;
	/*@Column(name="REF_TRUCK_VENDOR_ID")
	private String refTruckVendorId;
	*/
	public int getTruckId() {
		return truckId;
	}
	public void setTruckId(int truckId) {
		this.truckId = truckId;
	}
	public String getSlNo() {
		return slNo;
	}
	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}
	public String getVehiclNo() {
		return vehiclNo;
	}
	public void setVehiclNo(String vehiclNo) {
		this.vehiclNo = vehiclNo;
	}
	public int getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(int vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getWheels() {
		return wheels;
	}
	public void setWheels(String wheels) {
		this.wheels = wheels;
	}
	public String getEntryType() {
		return entryType;
	}
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	public String getTaggedTranspoter() {
		return taggedTranspoter;
	}
	public void setTaggedTranspoter(String taggedTranspoter) {
		this.taggedTranspoter = taggedTranspoter;
	}
	public String getDoNo() {
		return doNo;
	}
	public void setDoNo(String doNo) {
		this.doNo = doNo;
	}
	public String getTaggedDate() {
		return taggedDate;
	}
	public void setTaggedDate(String taggedDate) {
		this.taggedDate = taggedDate;
	}
	public String getTaggedTime() {
		return taggedTime;
	}
	public void setTaggedTime(String taggedTime) {
		this.taggedTime = taggedTime;
	}
	public String getDelay() {
		return delay;
	}
	public void setDelay(String delay) {
		this.delay = delay;
	}
	/*public String getRefTruckVendorId() {
		return refTruckVendorId;
	}
	public void setRefTruckVendorId(String refTruckVendorId) {
		this.refTruckVendorId = refTruckVendorId;
	}*/
}
