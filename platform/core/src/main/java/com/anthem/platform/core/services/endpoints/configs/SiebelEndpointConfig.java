package com.anthem.platform.core.services.endpoints.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Endpoint - Siebel Factory Configs", description = "Anthem Endpoint - Siebel Factory Configs")
public @interface SiebelEndpointConfig {
	
	@AttributeDefinition(name = "Endpoint Id", description = "Endpoint Id", type = AttributeType.STRING)
	String endpointId() default "api.siebel";

	@AttributeDefinition(name = "Siebel Lead Generation Endpoint URL", description = "Siebel Lead Generation Endpoint URL", type = AttributeType.STRING)
	String siebelHostUrl() default "https://162.95.230.68";
	
	@AttributeDefinition(name = "Routing Code", description = "Routing Code", type = AttributeType.STRING)
	String routingCode() default "21";
	
	@AttributeDefinition(name = "Vendor Source", description = "Vendor Source", type = AttributeType.STRING)
	String vendorSource() default "AEM";
	
	@AttributeDefinition(name = "Enable debug logs for soap apis", description = "Enable debug logs for soap apis", type = AttributeType.BOOLEAN)
	boolean enableDebugLogsForSoap() default true;
	
}

