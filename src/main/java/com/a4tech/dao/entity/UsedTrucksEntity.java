package com.a4tech.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "used_trucks")
public class UsedTrucksEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6018686039677921204L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "used_truck_id")
	private Integer usedTruckId;
	@Column(name = "truck_no")
	private String truckNo;
	@Column(name = "vehical_type")
	private String vehicalType;
	@Column(name = "vehical_capacity")
	private Integer vehicalCapacity;
	@Column(name = "wheeler_type")
	private Integer wheelerType;
	@Column(name = "transporter_name")
	private String transporterName;
	@Column(name = "shipped_date")
	@Temporal(TemporalType.DATE)
	private Date shippedDate;
	@Column(name = "district_name")
	private String districtName;
	@Column(name="tagged_time")
	private String taggedTime;
	@Column(name="rated_load")
	private Integer ratedLoad;
	@Column(name="normal_load")
	private Integer normalLoad;

	public Integer getRatedLoad() {
		return ratedLoad;
	}

	public void setRatedLoad(Integer ratedLoad) {
		this.ratedLoad = ratedLoad;
	}

	public Integer getNormalLoad() {
		return normalLoad;
	}

	public void setNormalLoad(Integer normalLoad) {
		this.normalLoad = normalLoad;
	}

	public String getTaggedTime() {
		return taggedTime;
	}

	public void setTaggedTime(String taggedTime) {
		this.taggedTime = taggedTime;
	}

	public Integer getUsedTruckId() {
		return usedTruckId;
	}

	public void setUsedTruckId(Integer usedTruckId) {
		this.usedTruckId = usedTruckId;
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

	public Integer getWheelerType() {
		return wheelerType;
	}

	public void setWheelerType(Integer wheelerType) {
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
