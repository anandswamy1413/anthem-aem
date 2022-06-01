package com.anthem.platform.core.services.api.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem API - Apigee Access Token Generation Service", description = "Anthem API - Apigee Access Token Generation Service")
public @interface  GenerateAccessTokenServiceConfig {
	
	@AttributeDefinition(name = "Endpoint Id", description = "Endpoint Id", type = AttributeType.STRING)
	String endpointId() default "api.apigee";
	
	@AttributeDefinition(name = "Apigee Access Token Endpoint", description = "Apigee Access Token Endpoint", type = AttributeType.STRING)
	String apigeeAccessTokenEndpoint() default "/v1/oauth/accesstoken";
	
}
