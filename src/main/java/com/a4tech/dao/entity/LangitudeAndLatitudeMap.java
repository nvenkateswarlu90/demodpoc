package com.a4tech.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="langitude_latitude_map")
public class LangitudeAndLatitudeMap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String latitudeAndLongitude;
	private String distance;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLatitudeAndLongitude() {
		return latitudeAndLongitude;
	}
	public void setLatitudeAndLongitude(String latitudeAndLongitude) {
		this.latitudeAndLongitude = latitudeAndLongitude;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	

}
