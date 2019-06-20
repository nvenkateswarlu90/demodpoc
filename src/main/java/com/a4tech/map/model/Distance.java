package com.a4tech.map.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Distance {
	@JsonProperty("value")
    private String value;
	@JsonProperty("text")
	private String text;
    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

	@Override
	public String toString() {
		return "Distance [text=" + text + ", value=" + value + "]";
	}
    
}
