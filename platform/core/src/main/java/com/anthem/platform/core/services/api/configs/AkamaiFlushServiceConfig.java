package com.anthem.platform.core.services.api.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Akamai API - Akamai cache purge Service", description = "Anthem Akamai API - Akamai cache purge Service")
public @interface AkamaiFlushServiceConfig {
	
	@AttributeDefinition(name = "Endpoint Id", description = "Endpoint Id", type = AttributeType.STRING)
	String endpointId() default "api.akamai";
	
	@AttributeDefinition(name = "Akamai cache purge Endpoint", description = "cache purge Endpoint", type = AttributeType.STRING)
	String cacheInvalidatePath();
	
	@AttributeDefinition(name = "Akamai cache purge Endpoint", description = "cache purge Endpoint", type = AttributeType.STRING)
	String cacheDeletePath();
}
