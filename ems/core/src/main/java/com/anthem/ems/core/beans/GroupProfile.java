package com.anthem.ems.core.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "digitalTools", "coverages", "findCare", "_id", "accountId", "groupName", "brand", "marketSegment",
		"stateCode", "groupNumber", "groupPreferenceId", "groupLogoPath", "micrositeName", "micrositeStatus",
		"effectiveDate", "disableDate", "oeStartDate", "oeEndDate", "enableWebEnquiries", "preferredEnrollmentToolLink",
		"createdDate", "lastUpdatedDate", "groupContentList", "groupContacts", "_class", "groupContent" })
@Generated("jsonschema2pojo")
public class GroupProfile {

	@JsonProperty("digitalTools")
	public DigitalTools digitalTools;
	@JsonProperty("coverages")
	public Coverages coverages;
	@JsonProperty("findCare")
	public List<String> findCare = null;
	@JsonProperty("_id")
	public String id;
	@JsonProperty("accountId")
	public String accountId;
	@JsonProperty("groupName")
	public String groupName;
	@JsonProperty("brand")
	public String brand;
	@JsonProperty("marketSegment")
	public String marketSegment;
	@JsonProperty("stateCode")
	public String stateCode;
	@JsonProperty("groupNumber")
	public String groupNumber;
	@JsonProperty("groupPreferenceId")
	public String groupPreferenceId;
	@JsonProperty("groupLogoPath")
	public String groupLogoPath;
	@JsonProperty("micrositeName")
	public String micrositeName;
	@JsonProperty("micrositeStatus")
	public String micrositeStatus;
	@JsonProperty("effectiveDate")
	public String effectiveDate;
	@JsonProperty("disableDate")
	public String disableDate;
	@JsonProperty("oeStartDate")
	public String oeStartDate;
	@JsonProperty("oeEndDate")
	public String oeEndDate;
	@JsonProperty("enableWebEnquiries")
	public String enableWebEnquiries;
	@JsonProperty("preferredEnrollmentToolLink")
	public String preferredEnrollmentToolLink;
	@JsonProperty("createdDate")
	public String createdDate;
	@JsonProperty("lastUpdatedDate")
	public String lastUpdatedDate;
	@JsonProperty("groupContentList")
	public List<Object> groupContentList = null;
	@JsonProperty("groupContacts")
	public List<GroupContact> groupContacts = null;
	@JsonProperty("_class")
	public String _class;
	@JsonProperty("groupContent")
	public List<Object> groupContent = null;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(GroupProfile.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
				.append('[');
		sb.append("digitalTools");
		sb.append('=');
		sb.append(((this.digitalTools == null) ? "<null>" : this.digitalTools));
		sb.append(',');
		sb.append("coverages");
		sb.append('=');
		sb.append(((this.coverages == null) ? "<null>" : this.coverages));
		sb.append(',');
		sb.append("findCare");
		sb.append('=');
		sb.append(((this.findCare == null) ? "<null>" : this.findCare));
		sb.append(',');
		sb.append("id");
		sb.append('=');
		sb.append(((this.id == null) ? "<null>" : this.id));
		sb.append(',');
		sb.append("accountId");
		sb.append('=');
		sb.append(((this.accountId == null) ? "<null>" : this.accountId));
		sb.append(',');
		sb.append("groupName");
		sb.append('=');
		sb.append(((this.groupName == null) ? "<null>" : this.groupName));
		sb.append(',');
		sb.append("brand");
		sb.append('=');
		sb.append(((this.brand == null) ? "<null>" : this.brand));
		sb.append(',');
		sb.append("marketSegment");
		sb.append('=');
		sb.append(((this.marketSegment == null) ? "<null>" : this.marketSegment));
		sb.append(',');
		sb.append("stateCode");
		sb.append('=');
		sb.append(((this.stateCode == null) ? "<null>" : this.stateCode));
		sb.append(',');
		sb.append("groupNumber");
		sb.append('=');
		sb.append(((this.groupNumber == null) ? "<null>" : this.groupNumber));
		sb.append(',');
		sb.append("groupPreferenceId");
		sb.append('=');
		sb.append(((this.groupPreferenceId == null) ? "<null>" : this.groupPreferenceId));
		sb.append(',');
		sb.append("groupLogoPath");
		sb.append('=');
		sb.append(((this.groupLogoPath == null) ? "<null>" : this.groupLogoPath));
		sb.append(',');
		sb.append("micrositeName");
		sb.append('=');
		sb.append(((this.micrositeName == null) ? "<null>" : this.micrositeName));
		sb.append(',');
		sb.append("micrositeStatus");
		sb.append('=');
		sb.append(((this.micrositeStatus == null) ? "<null>" : this.micrositeStatus));
		sb.append(',');
		sb.append("effectiveDate");
		sb.append('=');
		sb.append(((this.effectiveDate == null) ? "<null>" : this.effectiveDate));
		sb.append(',');
		sb.append("disableDate");
		sb.append('=');
		sb.append(((this.disableDate == null) ? "<null>" : this.disableDate));
		sb.append(',');
		sb.append("oeStartDate");
		sb.append('=');
		sb.append(((this.oeStartDate == null) ? "<null>" : this.oeStartDate));
		sb.append(',');
		sb.append("oeEndDate");
		sb.append('=');
		sb.append(((this.oeEndDate == null) ? "<null>" : this.oeEndDate));
		sb.append(',');
		sb.append("enableWebEnquiries");
		sb.append('=');
		sb.append(((this.enableWebEnquiries == null) ? "<null>" : this.enableWebEnquiries));
		sb.append(',');
		sb.append("preferredEnrollmentToolLink");
		sb.append('=');
		sb.append(((this.preferredEnrollmentToolLink == null) ? "<null>" : this.preferredEnrollmentToolLink));
		sb.append(',');
		sb.append("createdDate");
		sb.append('=');
		sb.append(((this.createdDate == null) ? "<null>" : this.createdDate));
		sb.append(',');
		sb.append("lastUpdatedDate");
		sb.append('=');
		sb.append(((this.lastUpdatedDate == null) ? "<null>" : this.lastUpdatedDate));
		sb.append(',');
		sb.append("groupContentList");
		sb.append('=');
		sb.append(((this.groupContentList == null) ? "<null>" : this.groupContentList));
		sb.append(',');
		sb.append("groupContacts");
		sb.append('=');
		sb.append(((this.groupContacts == null) ? "<null>" : this.groupContacts));
		sb.append(',');
		sb.append("_class");
		sb.append('=');
		sb.append(((this._class == null) ? "<null>" : this._class));
		sb.append(',');
		sb.append("groupContent");
		sb.append('=');
		sb.append(((this.groupContent == null) ? "<null>" : this.groupContent));
		sb.append(',');
		sb.append("additionalProperties");
		sb.append('=');
		sb.append(((this.additionalProperties == null) ? "<null>" : this.additionalProperties));
		sb.append(',');
		if (sb.charAt((sb.length() - 1)) == ',') {
			sb.setCharAt((sb.length() - 1), ']');
		} else {
			sb.append(']');
		}
		return sb.toString();
	}

}