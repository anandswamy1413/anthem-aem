package com.anthem.platform.core.services.api.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem API - Apigee County Generation Service", description = "Anthem API - Apigee County Generation Service")
public @interface CountyGenerateServiceConfig {
	
	@AttributeDefinition(name = "Endpoint Id", description = "Endpoint Id", type = AttributeType.STRING)
	String endpointId() default "api.apigee";
	
	@AttributeDefinition(name = "County population Endpoint", description = "County population Endpoint", type = AttributeType.STRING)
	String countyPopulationEndpoint() default "/v1/coreutility/zipcode/details";
}
