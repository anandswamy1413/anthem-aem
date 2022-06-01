package com.anthem.platform.core.services.api.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem API - Siebel Lead Generation Service", description = "Anthem API - Siebel Lead Generation Service")
public @interface SiebelLeadGenerationSerivceConfig {
	
	@AttributeDefinition(name = "Endpoint Id", description = "Endpoint Id", type = AttributeType.STRING)
	String endpointId() default "api.siebel";
	
	@AttributeDefinition(name = "Siebel Lead Generation Endpoint URL", description = "Siebel Lead Generation Endpoint URL", type = AttributeType.STRING)
	String leadGenerationEndpoint() default "/eaicons_anon/start.swe?SWEExtSource=WLPAnonWebService&SWEExtCmd=Execute";
}


