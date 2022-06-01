
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
 * A HeaderModel Sling Model Class.
 * 
 */
@Model(adaptables = {
		Resource.class }, resourceType = "anthem/platform/components/master/header/v1/header", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class HeaderModel {

	private static final Logger log = LoggerFactory.getLogger(HeaderModel.class);

	@ValueMapValue
	private String fileReference;
	
	@ValueMapValue
	private String altText;

	@ValueMapValue
	private String mobileCtaText;

	@ValueMapValue
	private String phoneNumber;
	
	@ValueMapValue
	private String checkBox;

	@ValueMapValue
	private String logoUrlPath;

	@ValueMapValue
	private String timingText;

	@ValueMapValue
	private String telIcon;
	
	@ValueMapValue
	private String telDetails;

	/**
	 * @return the timingText
	 */
	public String getTimingText() {
		return timingText;
	}

	/**
	 * @return the logoUrlPath
	 */
	public String getLogoUrlPath() {
		return logoUrlPath;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return PlatformUtils.findAndReplacePhoneNumber(phoneNumber, PlatformUtils.PHONE_NUMBER_EXPRESSION);
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
	 * @return the fileReference
	 */
	public String getFileReference() {
		return fileReference;
	}
	
	/**
	 * @return the altText
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * @return the mobileCtaText
	 */
	public String getMobileCtaText() {
		return mobileCtaText;
	}

	/**
	 * @return the checkBox
	 */
	public String getCheckBox() {
		return checkBox;
	}

	/**
	 * @return the telIcon
	 */
	public String getTelIcon() {
		return telIcon;
	}

	@PostConstruct
	protected void init() {
		log.info("header model invoked");
	}
}
