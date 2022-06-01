package com.anthem.platform.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Sling model class for Hero Fact Component
 * 
 * @author amitrathor
 *
 */
@Model(adaptables = {
		Resource.class }, resourceType = "anthem/platform/components/master/hero-fact/v1/hero-fact", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class HeroFactModel {

	/**
	 * Standard logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(HeroFactModel.class);

	@ValueMapValue
	private String heading;

	@ValueMapValue
	private String subHeading;

	@ValueMapValue
	private String textAlign;

	/**
	 * Post construct initialization method
	 */
	@PostConstruct
	public final void init() {
		LOG.debug("Map Model Invoked");
	}

	/**
	 * @return the heading
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * @return the subHeading
	 */
	public String getSubHeading() {
		return subHeading;
	}
	
	public String getTextAlign() {
		return textAlign;
	}
	
	/**
	 * @return the headingAriaLabel
	 */
	public String getHeadingAriaLabel() {
		return heading.replaceAll("\\<.*?\\>", "");
	}

	/**
	 * @return the subHeadingAriaLabel
	 */
	public String getSubHeadingAriaLabel() {
		return subHeading.replaceAll("\\<.*?\\>", "");
	}

}
