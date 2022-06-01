package com.anthem.platform.core.services.api.impl;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


@Component(service = ApiService.class, immediate = true, property = { "apiId="
		+ SiebelLeadGenerationServiceImpl.API_ID }, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = SiebelLeadGenerationSerivceConfig.class)
public class SiebelLeadGenerationServiceImpl implements ApiService<ListOfWlpLmLeadImportIo, WLPMessageBasedLeadOutput> {

	public static final String API_ID = "SIEBEL_LEAD_GENERATION";

	public static final String API_DESC = "Service to get/post lead data to siebel";

	public static final String ERROR_CODE_UPDATE_DATA = "API:SOAP:LEAD_UPDATE_FAILED";

	private static final Logger LOG = LoggerFactory.getLogger(SiebelLeadGenerationServiceImpl.class);
	
	private String leadgenerationEndpoint;
	
	private boolean isDebugEnabled;

	private ApiEndpointService apiEndpointService;

	@Reference
	private PlatformHelper platformHelper;

	@Reference
	private ApiEndpointFactory apiEndpointFactory;

	@Activate
	@Modified
	public void activate(SiebelLeadGenerationSerivceConfig config) {
		String endpointId = config.endpointId();
		LOG.info("Reading endpoint details from {} ", endpointId);
		this.apiEndpointService = apiEndpointFactory.getEndpoint(endpointId);
		this.leadgenerationEndpoint = this.apiEndpointService.get(EndpointKeys.SiebelEndpointKey.SIEBEL_HOST_URL.name()) + config.leadGenerationEndpoint();
		this.isDebugEnabled = Boolean.valueOf(apiEndpointService.get(EndpointKeys.SiebelEndpointKey.SOAP_DEBUG_ENABLED.name()));

	}

	@Override
	public String getApiId() {
		return API_ID;
	}

	@Override
	public String getDesc() {
		return API_DESC;
	}

	@Override
	public WLPMessageBasedLeadOutput getResults(ListOfWlpLmLeadImportIo apiData, Map<String, Object> metadata)
			throws ApiException {
		return null;
	}

	@Override
	public WLPMessageBasedLeadOutput updateData(ListOfWlpLmLeadImportIo apiData, Map<String, Object> metadata)
			throws ApiException {
		try {
			WLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF_Service wfService = new WLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF_Service();
			WLPMessageBasedLeadInput leadInput = new WLPMessageBasedLeadInput();
			leadInput.setListOfWlpLmLeadImportIo(apiData);
			WLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF service = wfService.getWLPSpcLMSpcMessageSpcbasedSpcLeadSpcWF();
			BindingProvider bindingProvider = (BindingProvider) service;
			bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,leadgenerationEndpoint);
			if (isDebugEnabled) {
				platformHelper.setSoapLogger(bindingProvider);
			}
			return service.wlpMessageBasedLead(leadInput);
		} catch (Exception e) {
			throw new ApiException(ERROR_CODE_UPDATE_DATA, "Error during Lead update",e);
		}

	}

	@Override
	public String getEndpointData(String key) {
		return this.apiEndpointService.get(key);
	}

}
