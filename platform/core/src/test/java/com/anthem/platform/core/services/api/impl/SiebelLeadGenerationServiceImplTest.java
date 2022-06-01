package com.anthem.platform.core.services.api.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.services.api.ApiService;
import com.anthem.platform.core.services.api.configs.SiebelLeadGenerationSerivceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.utils.PlatformHelper;
import com.anthem.ws.siebel.leadgen.ListOfWlpLmLeadImportIo;
import com.anthem.ws.siebel.leadgen.WLPMessageBasedLeadInput;
import com.anthem.ws.siebel.leadgen.WLPMessageBasedLeadOutput;
import com.anthem.ws.siebel.leadgen.WLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF;
import com.anthem.ws.siebel.leadgen.WLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF_Service;

class SiebelLeadGenerationServiceImplTest {

	@InjectMocks
	private SiebelLeadGenerationServiceImpl siebelleadgenerationserviceimpl;

	@Mock
	private ApiService<ListOfWlpLmLeadImportIo, WLPMessageBasedLeadOutput> apiservice;

	@Mock

	private WLPMessageBasedLeadOutput wlpmessagebasedleadoutput;

	@Mock
	private WLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF_Service wlpspc_service;

	@Mock

	private WLPMessageBasedLeadInput wlpmessagebasedleadinput;

	@Mock
	private WLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF wlpspclmspcmessagespcbasedspcleadspcwf;

	@Mock
	private BindingProvider bindingprovider;

	@Mock
	private ListOfWlpLmLeadImportIo list;

	@Mock
	private Map<String, Object> map;

	@Mock
	private SiebelLeadGenerationSerivceConfig config;

	@Mock
	private PlatformHelper platformHelper;

	@Mock
	private ApiEndpointFactory apiEndpointFactory;

	@Mock
	private ApiEndpointService apiEndpointService;

	@InjectMocks
	private ApiException apiException = new ApiException("test", "testcode");

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(apiEndpointService.get("test")).thenReturn("test_Value");
		when(config.endpointId()).thenReturn("test_Id");
		when(apiEndpointFactory.getEndpoint("test_Id")).thenReturn(apiEndpointService);
		when(config.leadGenerationEndpoint())
				.thenReturn("/eaicons_anon/start.swe?SWEExtSource=WLPAnonWebService&SWEExtCmd=Execute");
		when(apiEndpointService.get(EndpointKeys.SiebelEndpointKey.SIEBEL_HOST_URL.name())).thenReturn("test_Key");
		when(apiEndpointService.get(EndpointKeys.SiebelEndpointKey.SOAP_DEBUG_ENABLED.name())).thenReturn("true");

		when(wlpspc_service.getWLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF())
				.thenReturn(wlpspclmspcmessagespcbasedspcleadspcwf);

		when(wlpspclmspcmessagespcbasedspcleadspcwf.wlpMessageBasedLead(wlpmessagebasedleadinput))
				.thenReturn(wlpmessagebasedleadoutput);
		siebelleadgenerationserviceimpl.activate(config);

	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.SiebelLeadGenerationServiceImpl#getApiId()}.
	 */
	@Test
	void testGetApiId() {
		String expected = siebelleadgenerationserviceimpl.API_ID;
		String actual = siebelleadgenerationserviceimpl.getApiId();
		assertEquals(expected, actual);

	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.SiebelLeadGenerationServiceImpl#getDesc()}.
	 */
	@Test
	void testGetDesc() {
		String expected = siebelleadgenerationserviceimpl.API_DESC;
		String actual = siebelleadgenerationserviceimpl.getDesc();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.SiebelLeadGenerationServiceImpl#getResults()}.
	 * 
	 * @throws ApiException
	 */
	@Test
	void testGetResults() throws ApiException {
		WLPMessageBasedLeadOutput expected = null;
		WLPMessageBasedLeadOutput actual = siebelleadgenerationserviceimpl.getResults(list, map);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.SiebelLeadGenerationServiceImpl#getEndpointData()}.
	 * 
	 */
	@Test
	void testGetEndpointData() {
		final String expected = "test_Value";
		String actual = siebelleadgenerationserviceimpl.getEndpointData("test");
		assertEquals(expected, actual);
	}

	/* *//**
			 ** Test method for
			 * {@link com.anthem.platform.core.services.impl.SiebelLeadGenerationServiceImpl#updateData()}.
			 * 
			 * @throws ApiException
			 *//*
				 * @Test() void testUpdateData() throws ApiException {
				 * siebelleadgenerationserviceimpl.updateData(list,map);
				 * assertNotNull(siebelleadgenerationserviceimpl); }
				 */
}
