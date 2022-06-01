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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.via.ChildResource;

import com.anthem.platform.core.models.SocialShareModel;
import com.anthem.platform.core.services.utility.JacksonMapperService;

/**
 * Sling model for Implementation for SocialShare Component.
 *
 * @author nitishsaxena
 */

@Model(adaptables = {
		Resource.class },adapters = {SocialShareModel.class}, resourceType = SocialShareModelImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class SocialShareModelImpl implements SocialShareModel{

	protected static final String RESOURCE_TYPE = "anthem/platform/components/master/social-icon/v1/social-icon";

	@Inject
	@Via(type = ChildResource.class)
	private List<IconList> socialIcons;

	
	
	@Inject
	private JacksonMapperService jacksonMapper;


	private String socialIconLinks;
	

	@PostConstruct
	protected void init() {

		if (socialIcons != null && !socialIcons.isEmpty()) {

			socialIconLinks = jacksonMapper.convertObjectToJson(socialIcons, false);

		}

		
	}

	

	/**
	 * @return the socialIcons
	 */
	public List<IconList> getSocialIcons() {
		return new ArrayList<>(socialIcons);
	}



	/**
	 * @return the socialIconLinks
	 */
	public String getSocialIconLinks() {
		return socialIconLinks;
	}



	@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
	public static class IconList {
		
		@Inject
		private String iconImg;

		@Inject
		private String linkText;

		@Inject
		private String linkUrl;
		
		@Inject
		private String openInNewTab;
		
		@Inject
		private String imageAlt;
		
		@Inject
		private String analyticsTag;

		/**
		 * @return the iconImg
		 */
		public String getIconImg() {
			return iconImg;
		}

		/**
		 * @return the linkText
		 */
		public String getLinkText() {
			return linkText;
		}

		/**
		 * @return the linkUrl
		 */
		public String getLinkUrl() {
			return linkUrl;
		}

		/**
		 * @return the openInNewTab
		 */
		public String getOpenInNewTab() {
			return openInNewTab;
		}
		
		/**
		 * @return the analyticsTag
		 */
		public String getAnalyticsTag() {
			return analyticsTag;
		}

		public String getImageAlt() {
			return imageAlt;
		}

		
	}

	

}
