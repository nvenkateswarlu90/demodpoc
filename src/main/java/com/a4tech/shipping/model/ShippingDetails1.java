package com.a4tech.shipping.model;

public class ShippingDetails1 implements Cloneable{
	
	private Integer id;
	private String delivery;
	private String deference_document;
	private String sold_to_party;
	private String name_of_sold_to_party;
	private String name_of_the_ship_to_party;
	private String material;
	private String actual_delivery_qty;
	private String route_description;
	private String district_name;
	private String plant;
	private String route;
	private String forwarding_agent_name;
	private String distribution_channel;
	private String deliv_date;
	private String delivery_type;
	private String shipping_Point;
	private String district_code;
	private String ship_to_party;
	private String ship_to_long;
	private String ship_to_latt;
		public String getDeference_document() {
		return deference_document;
	}
	public void setDeference_document(String deference_document) {
		this.deference_document = deference_document;
	}
	public String getSold_to_party() {
		return sold_to_party;
	}
	public String getDelivery() {
		return delivery;
	}
	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}
	public void setSold_to_party(String sold_to_party) {
		this.sold_to_party = sold_to_party;
	}
	public String getName_of_sold_to_party() {
		return name_of_sold_to_party;
	}
	public void setName_of_sold_to_party(String name_of_sold_to_party) {
		this.name_of_sold_to_party = name_of_sold_to_party;
	}
	public String getName_of_the_ship_to_party() {
		return name_of_the_ship_to_party;
	}
	public void setName_of_the_ship_to_party(String name_of_the_ship_to_party) {
		this.name_of_the_ship_to_party = name_of_the_ship_to_party;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getActual_delivery_qty() {
		return actual_delivery_qty;
	}
	public void setActual_delivery_qty(String actual_delivery_qty) {
		this.actual_delivery_qty = actual_delivery_qty;
	}
	public String getRoute_description() {
		return route_description;
	}
	public void setRoute_description(String route_description) {
		this.route_description = route_description;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getForwarding_agent_name() {
		return forwarding_agent_name;
	}
	public void setForwarding_agent_name(String forwarding_agent_name) {
		this.forwarding_agent_name = forwarding_agent_name;
	}
	public String getDistribution_channel() {
		return distribution_channel;
	}
	public void setDistribution_channel(String distribution_channel) {
		this.distribution_channel = distribution_channel;
	}
	public String getDeliv_date() {
		return deliv_date;
	}
	public void setDeliv_date(String deliv_date) {
		this.deliv_date = deliv_date;
	}
	public String getDelivery_type() {
		return delivery_type;
	}
	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}
	public String getShipping_Point() {
		return shipping_Point;
	}
	public void setShipping_Point(String shipping_Point) {
		this.shipping_Point = shipping_Point;
	}
	public String getDistrict_code() {
		return district_code;
	}
	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}
	public String getShip_to_party() {
		return ship_to_party;
	}
	public void setShip_to_party(String ship_to_party) {
		this.ship_to_party = ship_to_party;
	}
	public String getShip_to_long() {
		return ship_to_long;
	}
	public void setShip_to_long(String ship_to_long) {
		this.ship_to_long = ship_to_long;
	}
	public String getShip_to_latt() {
		return ship_to_latt;
	}
	public void setShip_to_latt(String ship_to_latt) {
		this.ship_to_latt = ship_to_latt;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	
}
