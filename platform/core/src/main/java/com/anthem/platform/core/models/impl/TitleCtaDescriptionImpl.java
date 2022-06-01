/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anthem.platform.core.models.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ChildResource;

import com.anthem.platform.core.models.AbstractImageComponentModel;
import com.anthem.platform.core.models.TitleCtaDescription;
import com.anthem.platform.core.services.utility.JacksonMapperService;

/**
 * Sling model for Implementation for SocialShare Component.
 *
 * @author nitishsaxena
 */

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class }, adapters = { TitleCtaDescription.class }, resourceType = TitleCtaDescriptionImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class TitleCtaDescriptionImpl extends AbstractImageComponentModel implements TitleCtaDescription {

	protected static final String RESOURCE_TYPE = "anthem/platform/components/master/title-cta-description/v1/title-cta-description";
	
	@ValueMapValue
	private String id;

	@ValueMapValue
	private String titleField;

	@ValueMapValue
	private String type;

	@ValueMapValue
	private String titleFontWeight;

	@ValueMapValue
	private String titleLetterSpacing;
	
	@ValueMapValue
	private String titleTextColor;

	@ValueMapValue
	private String linkURL;

	@ValueMapValue
	private String descRequired;

	@ValueMapValue
	private String enableBluebar;

	@ValueMapValue
	private String description;
	
	@ValueMapValue
	private String descriptionWeight;

	@ValueMapValue
	private String descTextColor;

	@ValueMapValue
	private String linkTextColor;

	@Inject
	@Via("resource")
	private List<IconList> ctaList;

	@Inject
	private JacksonMapperService jacksonMapper;

	private String ctaLinks;

	@PostConstruct
	protected void init() {

		if (ctaList != null && !ctaList.isEmpty()) {

			ctaLinks = jacksonMapper.convertObjectToJson(ctaList, false);

		}

	}

	public List<IconList> getCtaList() {
		return new ArrayList<>(ctaList);
	}

	public String getCtaLinks() {
		return ctaLinks;
	}

	public String getId() {
		return id;
	}

	public String getTitleField() {
		return titleField;
	}

	public String getType() {
		return type;
	}

	public String getTitleFontWeight() {
		return titleFontWeight;
	}

	public String getTitleLetterSpacing() {
		return titleLetterSpacing;
	}

	public String getTitleTextColor() {
		return titleTextColor;
	}

	public String getLinkURL() {
		return linkURL;
	}

	public String getDescRequired() {
		return descRequired;
	}

	public String getEnableBluebar() {
		return enableBluebar;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionWeight() {
		return descriptionWeight;
	}

	public String getDescTextColor() {
		return descTextColor;
	}

	public String getLinkTextColor() {
		return linkTextColor;
	}

	@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
	public static class IconList {

		@Inject
		private String ctaIcon;

		@Inject
		private String ctaLabel;

		@Inject
		private String ctaLink;
		
		@Inject
		private String iconAltText;
		
		@Inject
		private String ctaAnalytics;

		@Inject
		private String openCtaInNewTab;

		public String getCtaIcon() {
			return ctaIcon;
		}

		public String getCtaLabel() {
			return ctaLabel;
		}

		public String getCtaLink() {
			return ctaLink;
		}

		public String getOpenCtaInNewTab() {
			return openCtaInNewTab;
		}
		
		public String getCtaAnalytics() {
			return ctaAnalytics;
		}

		public String getIconAltText() {
			return iconAltText;
		}

	}

}
