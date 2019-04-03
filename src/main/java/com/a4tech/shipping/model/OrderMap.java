package com.a4tech.shipping.model;

import com.a4tech.map.model.Elements;

public class OrderMap {
	private ShippingDetails1 shippingDetails;
	private String destination;
	private double distance;

	public OrderMap(ShippingDetails1 shippingDetails, String destination, double distance) {
		//super();
		this.shippingDetails = shippingDetails;
		this.destination = destination;
		this.distance = distance;
	}

	

	public double getDistance() {
		return distance;
	}



	public void setDistance(double distance) {
		this.distance = distance;
	}



	public ShippingDetails1 getShippingDetails() {
		return shippingDetails;
	}

	public void setShippingDetails(ShippingDetails1 shippingDetails) {
		this.shippingDetails = shippingDetails;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	
}
