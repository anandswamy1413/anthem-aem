package com.anthem.platform.core.services.endpoints;

public class EndpointKeys {

	public enum SiebelEndpointKey {
		SIEBEL_HOST_URL,
		SOAP_DEBUG_ENABLED,
		ROUTING_CODE,
		VENDOR_SOURCE
	}
	
	public enum SFMCEndpointKey {
		CLIENT_ID,
		CLIENT_SECRET,
		AUTH_URL,
		GRANT_TYPE,
		ACCOUNT_ID,
		TRIGGER_MAIL_API_PATH,
		SUBSCRIBE_API_PATH;
	}
	
	public enum ApigeeEndpointKey {
		APIGEE_API_KEY, 
		APIGEE_HOST_URL,
		API_CLIENT_ID,
		API_CLIENT_SECRET;
	}
	
	public enum AkamaiEndpointKey {
		AKAMAI_ACCESS_TOKEN, 
		AKAMAI_HOST_URL,
		AKAMAI_CLIENT_TOKEN,
		AKAMAI_CLIENT_SECRET,
		AKAMAI_CONNECTION_TIMEOUT,
		AKAMAI_REQUEST_TIMEOUT,
		AKAMAI_SOCKET_TIMEOUT;
	}
}
