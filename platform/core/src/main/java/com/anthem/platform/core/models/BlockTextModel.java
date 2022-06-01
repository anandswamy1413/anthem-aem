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

import com.anthem.platform.core.utils.PlatformUtils;

/**
 * Sling model for block-text Component.
 *
 * @author nitishsaxena
 */

@Model(adaptables = {
		Resource.class }, resourceType = "anthem/platform/components/block-text/v1/block-text", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class BlockTextModel {

	/**
	 * Standard logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BlockTextModel.class);

	/**
	 * Value to enable border
	 */
	@ValueMapValue
	private String borderSwitch;

	/**
	 * Text Section value
	 */
	@ValueMapValue
	private String textSection;

	/**
	 * Post construct initialization method
	 */
	@PostConstruct
	protected void init() {
		LOG.info("Inside BlockTextModel class init method");

	}

	/**
	 * @return the borderSwitch
	 */
	public String getBorderSwitch() {
		return borderSwitch;
	}

	
	
	

	/**
	 * @return the textSection
	 */
	public String getTextSection() {
		return PlatformUtils.getAnthemFormattedPhoneNumber(textSection);
	}
	
	/**
	 * @return the textSection
	 */
	public String getBodyAriaText() {
		return textSection.replaceAll("\\<.*?\\>", "");
	}


}
