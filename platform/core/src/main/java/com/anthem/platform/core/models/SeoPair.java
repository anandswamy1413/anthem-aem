package com.anthem.platform.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

@Model(adaptables = Resource.class)
public class SeoPair {

	@Inject
	@Optional
	private String tagType;

	@Inject
	@Optional
	private String tagName;

	@Inject
	@Optional
	private String tagValue;

	public String getTagType() {
		return tagType;
	}

	public String getTagName() {
		return tagName;
	}

	public String getTagValue() {
		return tagValue;
	}
}
