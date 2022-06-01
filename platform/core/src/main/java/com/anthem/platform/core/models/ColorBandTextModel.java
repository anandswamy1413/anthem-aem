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
 * Sling model for color-band-text Component.
 *
 * @author nitishsaxena
 */

@Model(adaptables = {
		Resource.class }, resourceType = "anthem/platform/components/master/color-band-text/v1/color-band-text", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class ColorBandTextModel {

	/**
	 * Standard logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ColorBandTextModel.class);


	/**
	 * Overlay text value
	 */
	@ValueMapValue
	private String overlayText;

	/**
	 * Telephone Icon value
	 */
	@ValueMapValue
	private String telIcon;

	/**
	 * Phone Number Value
	 */
	@ValueMapValue
	private String phoneNumber;

	/**
	 * Text Telephone Details
	 */
	@ValueMapValue
	private String telDetails;

	/**
	 * Timings Text
	 */
	@ValueMapValue
	private String timingText;

	/**
	 * @return the overlayAriaText
	 */
	public String getOverlayAriaText() {
		return overlayText.replaceAll("\\<.*?\\>", "");
	}

	/**
	 * @return the timingText
	 */
	public String getTimingText() {
		return timingText;
	}

	/**
	 * @return the overlayText
	 */
	public String getOverlayText() {
		return PlatformUtils.getAnthemFormattedPhoneNumber(overlayText);
	}

	/**
	 * @return the telIcon
	 */
	public String getTelIcon() {
		return telIcon;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	
	/**
	 * @return the callingNumber
	 */
	public String getCallingNumber() {
		return phoneNumber.replaceAll("[^a-zA-Z0-9]", "");
	}

	/**
	 * @return the telDetails
	 */
	public String getTelDetails() {
		return telDetails;
	}

	/**
	 * Post construct initialization method
	 */
	@PostConstruct
	protected void init() {

		LOG.info("Inside ColorBandTextModel class init method");

	}

}
