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

import com.anthem.platform.core.utils.PlatformUtils;

/**
 * Sling model for color-band-text Component.
 *
 * @author nitishsaxena
 */

@Model(adaptables = {
		Resource.class }, resourceType = "anthem/platform/components/master/lead-gen-form/v1/lead-gen-form", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class LeadGenFormModel {

	/**
	 * Standard logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LeadGenFormModel.class);

	/**
	 * Form Heading Value
	 */
	@ValueMapValue
	private String formHeadline;

	/**
	 * Form Field Required Value
	 */
	@ValueMapValue
	private String firstNameRequired;

	/**
	 * Form Placeholder Text Value
	 */
	@ValueMapValue
	private String firstNamePlaceholder;

	/**
	 * Form Field Required Value
	 */
	@ValueMapValue
	private String lastNameRequired;

	/**
	 * Form Placeholder Text Value
	 */
	@ValueMapValue
	private String lastNamePlaceholder;

	/**
	 * Form Field Required Value
	 */
	@ValueMapValue
	private String emailRequired;

	/**
	 * Form Placeholder Text Value
	 */
	@ValueMapValue
	private String emailPlaceholder;

	/**
	 * Form Field Required Value
	 */
	@ValueMapValue
	private String zipcodeRequired;

	/**
	 * Form Placeholder Text Value
	 */
	@ValueMapValue
	private String zipcodePlaceholder;

	/**
	 * Form Field Error Message
	 */
	@ValueMapValue
	private String zipcodeErrorMsg;

	/**
	 * Form Field Required Value
	 */
	@ValueMapValue
	private String phoneNumRequired;

	/**
	 * Form Placeholder Text Value
	 */
	@ValueMapValue
	private String phoneNumPlaceholder;

	/**
	 * Placeholder for county dropdown
	 */
	@ValueMapValue
	private String countyPlaceholder;

	/**
	 * Form Field Required Value
	 */
	@ValueMapValue
	private String countyRequired;

	/**
	 * CTA Label Text For Stage 1
	 */
	@ValueMapValue
	private String stageOneCta;

	/**
	 *  Label Text For checkbox
	 */
	@ValueMapValue
	private String checkboxLabel;

	/**
	 * Generic System Error
	 */
	@ValueMapValue
	private String systemError;

	/**
	 * Form Submit Confirmation
	 */

	@ValueMapValue
	private String confirmationMsg;

	/**
	 * Legal Disclaimer Text
	 */
	@ValueMapValue
	private String legalDisclaimer;

	/**
	 * OLS Default Link
	 */
	@ValueMapValue
	private String olsDefaultLink;

	/**
	 * OLS Deep Link
	 */
	@ValueMapValue
	private String olsDeepLink;

	@ValueMapValue
	private String requiredFieldAdvisory;
	

	@PostConstruct
	protected void init() {

		LOG.info("Inside LeadGenFormModel class init method");

	}

	/**
	 * @return the formHeadline
	 */
	public String getFormHeadline() {
		return formHeadline;
	}

	/**
	 * @return the firstNameRequired
	 */
	public String getFirstNameRequired() {
		return firstNameRequired;
	}

	/**
	 * @return the firstNamePlaceholder
	 */
	public String getFirstNamePlaceholder() {
		return firstNamePlaceholder;
	}

	

	/**
	 * @return the lastNameRequired
	 */
	public String getLastNameRequired() {
		return lastNameRequired;
	}

	/**
	 * @return the lastNamePlaceholder
	 */
	public String getLastNamePlaceholder() {
		return lastNamePlaceholder;
	}

	
	/**
	 * @return the emailRequired
	 */
	public String getEmailRequired() {
		return emailRequired;
	}

	/**
	 * @return the emailPlaceholder
	 */
	public String getEmailPlaceholder() {
		return emailPlaceholder;
	}


	/**
	 * @return the zipcodeRequired
	 */
	public String getZipcodeRequired() {
		return zipcodeRequired;
	}

	/**
	 * @return the zipcodePlaceholder
	 */
	public String getZipcodePlaceholder() {
		return zipcodePlaceholder;
	}

	/**
	 * @return the zipcodeErrorMsg
	 */
	public String getZipcodeErrorMsg() {
		return zipcodeErrorMsg;
	}

	/**
	 * @return the phoneNumRequired
	 */
	public String getPhoneNumRequired() {
		return phoneNumRequired;
	}

	/**
	 * @return the phoneNumPlaceholder
	 */
	public String getPhoneNumPlaceholder() {
		return phoneNumPlaceholder;
	}

	

	/**
	 * @return the countyPlaceholder
	 */
	public String getCountyPlaceholder() {
		return countyPlaceholder;
	}

	/**
	 * @return the stageOneCta
	 */
	public String getStageOneCta() {
		return stageOneCta;
	}

	/**
	 * @return the checkboxLabel
	 */
	public String getCheckboxLabel() {
		return checkboxLabel;
	}

	

	/**
	 * @return the systemError
	 */
	public String getSystemError() {
		return systemError;
	}

	/**
	 * @return the confirmationMsg
	 */
	public String getConfirmationMsg() {
		return PlatformUtils.getAnthemFormattedPhoneNumber(confirmationMsg);
	}

	/**
	 * @return the confirmation Message AriaText
	 */
	public String getConfirmationMsgAriaText() {
		return confirmationMsg.replaceAll("\\<.*?\\>", "");
	}

	/**
	 * @return the legalDisclaimer
	 */
	public String getLegalDisclaimer() {
		return PlatformUtils.getAnthemFormattedPhoneNumber(legalDisclaimer);
	}

	/**
	 * @return the overlayAriaText
	 */
	public String getLegalDisclaimerAriaText() {
		return legalDisclaimer.replaceAll("\\<.*?\\>", "");
	}

	/**
	 * @return the countyRequired
	 */
	public String getCountyRequired() {
		return countyRequired;
	}

	/**
	 * @return the olsDefaultLink
	 */
	public String getOlsDefaultLink() {
		return olsDefaultLink;
	}

	/**
	 * @return the olsDeepLink
	 */
	public String getOlsDeepLink() {
		return olsDeepLink;
	}

	/**
	 * @return the requiredFieldAdvisory
	 */
	public String getRequiredFieldAdvisory() {
		return requiredFieldAdvisory;
	}

}
