/**
 * 
 */
package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * @author nitishsaxena8
 *
 */

@ExtendWith(AemContextExtension.class)
class LeadGenFormModelTest {
	
	
	private final AemContext ctx = new AemContext();
	private Resource resource;
	private LeadGenFormModel leadGenForm;

	@BeforeEach
	void setUp() throws Exception {

		ctx.addModelsForClasses(LeadGenFormModel.class);
		ctx.load().json("/com/anthem/platform/core/models/LeadGenFormModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/lead_gen_form");
		leadGenForm = resource.adaptTo(LeadGenFormModel.class);

	}


	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getFormHeadline()}.
	 */
	@Test
	void testGetFormHeadline() {
		final String expected = "Test Headline";

		String actual = leadGenForm.getFormHeadline();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getRequiredFieldAdvisory()}.
	 */
	@Test
	void testGetRequiredFieldAdvisory() {
		final String expected = "Required Field";

		String actual = leadGenForm.getRequiredFieldAdvisory();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getFirstNameRequired()}.
	 */
	@Test
	void testGetFirstNameRequired() {
		final String expected = "true";

		String actual = leadGenForm.getFirstNameRequired();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getFirstNamePlaceholder()}.
	 */
	@Test
	void testGetFirstNamePlaceholder() {
		final String expected = "Test Fisrt Name Placeholder";

		String actual = leadGenForm.getFirstNamePlaceholder();
		assertEquals(expected, actual);
	}

	

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getLastNameRequired()}.
	 */
	@Test
	void testGetLastNameRequired() {
		final String expected = "Test last Name";

		String actual = leadGenForm.getLastNameRequired();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getLastNamePlaceholder()}.
	 */
	@Test
	void testGetLastNamePlaceholder() {
		final String expected = "Test Last Name Placeholder";

		String actual = leadGenForm.getLastNamePlaceholder();
		assertEquals(expected, actual);
	}

	

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getEmailRequired()}.
	 */
	@Test
	void testGetEmailRequired() {
		final String expected = "true";

		String actual = leadGenForm.getEmailRequired();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getEmailPlaceholder()}.
	 */
	@Test
	void testGetEmailPlaceholder() {
		final String expected = "Test email Placeholder";

		String actual = leadGenForm.getEmailPlaceholder();
		assertEquals(expected, actual);
	}

	

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getZipcodeRequired()}.
	 */
	@Test
	void testGetZipcodeRequired() {
		final String expected = "true";

		String actual = leadGenForm.getZipcodeRequired();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getZipcodePlaceholder()}.
	 */
	@Test
	void testGetZipcodePlaceholder() {
		final String expected = "Test zipcode Placeholder";

		String actual = leadGenForm.getZipcodePlaceholder();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getZipcodeErrorMsg()}.
	 */
	@Test
	void testGetZipcodeErrorMsg() {
		final String expected = "Test zipcode error";

		String actual = leadGenForm.getZipcodeErrorMsg();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getPhoneNumRequired()}.
	 */
	@Test
	void testGetPhoneNumRequired() {
		final String expected = "true";

		String actual = leadGenForm.getPhoneNumRequired();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getPhoneNumPlaceholder()}.
	 */
	@Test
	void testGetPhoneNumPlaceholder() {
		final String expected = "Test phone number Placeholder";

		String actual = leadGenForm.getPhoneNumPlaceholder();
		assertEquals(expected, actual);
	}

	

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getCountyPlaceholder()}.
	 */
	@Test
	void testGetCountyPlaceholder() {
		final String expected = "Test county Placeholder";

		String actual = leadGenForm.getCountyPlaceholder();
		assertEquals(expected, actual);
	}
	
	
	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getCountyRequired()}.
	 */
	@Test
	void testGetCountyRequired() {
		final String expected = "true";

		String actual = leadGenForm.getCountyRequired();
		assertEquals(expected, actual);
	}
	
	
	

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getStageOneCta()}.
	 */
	@Test
	void testGetStageOneCta() {
		final String expected = "Test stage 1 cta";

		String actual = leadGenForm.getStageOneCta();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#checkboxLabel()}.
	 */
	@Test
	void testGetCheckboxLabel() {
		final String expected = "Test checkbox label";

		String actual = leadGenForm.getCheckboxLabel();
		assertEquals(expected, actual);
	}

	
	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getSystemError()}.
	 */
	@Test
	void testGetSystemError() {
		final String expected = "Test system error";

		String actual = leadGenForm.getSystemError();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getConfirmationMsg()}.
	 */
	@Test
	void testGetConfirmationMsg() {
		final String expected = "Test confirmation msg";

		String actual = leadGenForm.getConfirmationMsg();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getConfirmationMsgAriaText()}.
	 */
	@Test
	void testGetConfirmationMsgAriaText() {
		final String expected = "Test confirmation msg";

		String actual = leadGenForm.getConfirmationMsgAriaText();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getLegalDisclaimer()}.
	 */
	@Test
	void testGetLegalDisclaimer() {
		final String expected = "Test legal disclaimer";

		String actual = leadGenForm.getLegalDisclaimer();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getLegalDisclaimerAriaText()}.
	 */
	@Test
	void testGetLegalDisclaimerAriaText() {
		final String expected = "Test legal disclaimer";

		String actual = leadGenForm.getLegalDisclaimerAriaText();
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getOlsDeepLink()}.
	 */
	@Test
	void testGetOlsDeepLink() {
		final String expected = "Test Link";

		String actual = leadGenForm.getOlsDeepLink();
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link com.anthem.platform.core.models.LeadGenFormModel#getOlsDefaultLink()}.
	 */
	@Test
	void testGetOlsDefaultLink() {
		final String expected = "Default Link";

		String actual = leadGenForm.getOlsDefaultLink();
		assertEquals(expected, actual);
	}

	


}
