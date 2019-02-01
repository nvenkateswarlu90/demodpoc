package com.a4tech.dao.entity;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name="shipinfo")
@Entity
public class OrderEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int shippingID;
	
	
	@Column(name="deliverdate")
	private Date  delvyDate;


	public int getShippingID() {
		return shippingID;
	}


	public void setShippingID(int shippingID) {
		this.shippingID = shippingID;
	}


	public Date getDelvyDate() {
		return delvyDate;
	}


	public void setDelvyDate(Date delvyDate) {
		this.delvyDate = delvyDate;
	}
	
	
	
	
}
