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
package com.anthem.platform.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling model for Countdown Promo Component.
 *
 * @author nitishsaxena
 */

@Model(adaptables = {
		Resource.class }, resourceType = "anthem/platform/components/master/countdown-promo/v1/countdown-promo", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class CountdownPromoModel {

	/**
	 * Standard logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CountdownPromoModel.class);

	/**
	 * Promotion end date
	 */
	@ValueMapValue
	private String endDate;

	/**
	 * Promotion Text value
	 */
	@ValueMapValue
	private String promoText;

	/**
	 * Post construct initialization method
	 */
	@PostConstruct
	protected void init() {
		LOG.info("Inside CountdownPromoModel class init method");

	}

	
	
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}



	/**
	 * @return the promoText
	 */
	public String getPromoText() {
		return promoText;
	}



	/**
	 * @return the promo text aria
	 */
	public String getPromoAriaText() {
		return promoText.replaceAll("\\<.*?\\>", "");
	}


}
