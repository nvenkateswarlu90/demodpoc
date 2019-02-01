package com.a4tech.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shippinginitialdata")
public class ShippingEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "delivery")
	private String delivery;

	@Column(name = "deference_document")
	private String deference_document;

	@Column(name = "sold_to_party")
	private String sold_to_party;

	@Column(name = "name_of_sold_to_party")
	private String name_of_sold_to_party;

	@Column(name = "name_of_the_ship_to_party")
	private String name_of_the_ship_to_party;

	@Column(name = "material")
	private String material;

	@Column(name = "actual_delivery_qty")
	private String actual_delivery_qty;

	@Column(name = "route_description")
	private String route_description;

	@Column(name = "district_name")
	private String district_name;

	@Column(name = "plant")
	private String plant;

	@Column(name = "route")
	private String route;

	@Column(name = "forwarding_agent_name")
	private String forwarding_agent_name;

	@Column(name = "distribution_channel")
	private String distribution_channel;

	@Column(name = "deliv_date")
	private String deliv_date;

	@Column(name = "delivery_type")
	private String delivery_type;

	@Column(name = "shipping_Point")
	private String shipping_Point;

	@Column(name = "district_code")
	private String district_code;

	@Column(name = "ship_to_party")
	private String ship_to_party;

	@Column(name = "ship_to_long")
	private String ship_to_long;

	@Column(name = "ship_to_latt")
	private String ship_to_latt;
	@Column(name="is_order_group")
	private String isOrderGroup;

	public String getIsOrderGroup() {
		return isOrderGroup;
	}

	public void setIsOrderGroup(String isOrderGroup) {
		this.isOrderGroup = isOrderGroup;
	}

	public String getDeference_document() {
		return deference_document;
	}

	public void setDeference_document(String deference_document) {
		this.deference_document = deference_document;
	}

	public String getSold_to_party() {
		return sold_to_party;
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

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
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


	
}
