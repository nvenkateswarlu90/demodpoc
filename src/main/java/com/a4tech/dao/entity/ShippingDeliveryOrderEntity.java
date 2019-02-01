package com.a4tech.dao.entity;

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
@Table(name = "shipping_delivery_order")
public class ShippingDeliveryOrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shipping_delivery_id")
	private Integer id;
	@Column(name = "truck_no")
	private String truckNo;
	@Column(name = "shipping_delivery_date")
	@Temporal(TemporalType.DATE)
	private Date shippingDeliveryDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTruckNo() {
		return truckNo;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public Date getShippingDeliveryDate() {
		return shippingDeliveryDate;
	}

	public void setShippingDeliveryDate(Date shippingDeliveryDate) {
		this.shippingDeliveryDate = shippingDeliveryDate;
	}
}
