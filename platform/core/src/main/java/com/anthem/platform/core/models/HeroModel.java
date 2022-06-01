package com.anthem.platform.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.utils.PlatformUtils;

/**
 * Sling model class for hero Component.
 *
 */

@Model(adaptables = {
		Resource.class }, resourceType = "anthem/platform/components/master/hero/v1/hero", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
    @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
    @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")
})
public class HeroModel {

	private static final Logger log = LoggerFactory.getLogger(HeroModel.class);
	@ValueMapValue
	private String fileReference;

	@ValueMapValue
    private String altText;
	
	@ValueMapValue
    private String bodyText;
	
	@ValueMapValue
    private String subText;
	
	@ValueMapValue
    private String alignText;
	
	@ValueMapValue
	private String phoneNumberHero;
	
	@ValueMapValue
	private String telHeroDetails;
	
	@ValueMapValue
	private String mobileFileReference;
	
	@ValueMapValue
	private String tabletFileReference;
	
	@ValueMapValue
	private String textColor;
	
	@ValueMapValue
	private String tabletAlignText;
	
	@ValueMapValue
	private String mobileAlignText;
	
	@Inject
	ResourceResolver resourceResolver;
	
	
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
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * @return the alignText
	 */
	public String getAlignText() {
		return alignText;
	}

	/**
	 * @return the phoneNumberHero
	 */
	public String getPhoneNumberHero() {
		return phoneNumberHero;
	}

	/**
	 * @return the telHeroDetails
	 */
	public String getTelHeroDetails() {
		return telHeroDetails;
	}
	
	public String getBodyAriaText() {
		return bodyText.replaceAll("\\<.*?\\>", "");
	}
	
	


	public String getMobileFileReference() {
		return mobileFileReference;
	}

	public String getTabletFileReference() {
		return tabletFileReference;
	}

	public String getTextColor() {
		return textColor;
	}
	
	

	public String getTabletAlignText() {
		return tabletAlignText;
	}

	public String getMobileAlignText() {
		return mobileAlignText;
	}
	
	
	public String getSubText() {
		return subText;
	}

	@PostConstruct
	protected void init() {
     log.info("Hero Model Invoked");
     this.subText = PlatformUtils.getAnthemFormattedPhoneNumberAndTty(subText);
     this.bodyText = PlatformUtils.getAnthemFormattedPhoneNumberAndTty(bodyText);
	}


}