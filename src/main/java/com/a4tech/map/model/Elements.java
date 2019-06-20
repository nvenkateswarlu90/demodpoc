package com.a4tech.map.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Elements {
	@JsonProperty("status")
	 private String status;
	@JsonProperty("duration")
	private Duration duration;
	@JsonProperty("distance")
    private Distance distance;
    public Duration getDuration ()
    {
        return duration;
    }

    public void setDuration (Duration duration)
    {
        this.duration = duration;
    }

    public Distance getDistance ()
    {
        return distance;
    }

    public void setDistance (Distance distance)
    {
        this.distance = distance;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

	@Override
	public String toString() {
		return "Elements [duration=" + duration + ", distance=" + distance
				+ ", status=" + status + "]";
	}
    
}
