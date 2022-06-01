package com.anthem.ems.core.beans;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "medical", "dental", "vision", "healthAndWellness", "pharmacy", "otherBenefits" })
@Generated("jsonschema2pojo")
public class Coverages {

	@JsonProperty("medical")
	public Boolean medical;
	@JsonProperty("dental")
	public Boolean dental;
	@JsonProperty("vision")
	public Boolean vision;
	@JsonProperty("healthAndWellness")
	public Boolean healthAndWellness;
	@JsonProperty("pharmacy")
	public Boolean pharmacy;
	@JsonProperty("otherBenefits")
	public Boolean otherBenefits;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}