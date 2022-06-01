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
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Sling model class for hero Component.
 *
 */

@Model(adaptables = {
		Resource.class}, resourceType = "anthem/platform/components/master/icon-and-text/v1/icon-and-text", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
    @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
    @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")
})
public class IconAndTextModel{

	private static final Logger log = LoggerFactory.getLogger(IconAndTextModel.class);
    
	@Inject
	private Resource products;

	/**
	 * @return the products
	 */
	public Resource getProducts() {
		return products;
	}

	
	public String getIconAriaText() {
		return products.listChildren().next().getValueMap().get("iconText", String.class).replaceAll("\\<.*?\\>", "");
	}
	
    
	@PostConstruct
	protected void init() {
     log.info("IconAndText Model Invoked");
	}


}