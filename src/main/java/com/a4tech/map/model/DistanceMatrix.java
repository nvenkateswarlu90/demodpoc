package com.a4tech.map.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceMatrix {
	@JsonProperty("status")
	private String status;
	@JsonProperty("origin_addresses")
	private List<String> originAddresses;
	@JsonProperty("destination_addresses")
	private List<String> destinationAddresses;
	@JsonProperty("rows")
	private List<Rows> rowsList;

	public List<Rows> getRowsList() {
		return rowsList;
	}

	public void setRowsList(List<Rows> rowsList) {
		this.rowsList = rowsList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getOriginAddresses() {
		return originAddresses;
	}

	public void setOriginAddresses(List<String> originAddresses) {
		this.originAddresses = originAddresses;
	}

	public List<String> getDestinationAddresses() {
		return destinationAddresses;
	}

	public void setDestinationAddresses(List<String> destinationAddresses) {
		this.destinationAddresses = destinationAddresses;
	}

}
