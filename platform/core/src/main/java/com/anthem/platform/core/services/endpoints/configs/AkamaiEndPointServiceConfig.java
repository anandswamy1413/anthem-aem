package com.anthem.platform.core.services.endpoints.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Akamai API - Akamai cache factory end points", description = "Anthem Akamai API -Akamai Factory Configs")
public @interface AkamaiEndPointServiceConfig {

	@AttributeDefinition(name = "Endpoint Id", description = "Endpoint Id", type = AttributeType.STRING)
	String endpointId() default "api.akamai";

	@AttributeDefinition(name = "Akamai Access Token", description = "Akamai Access Token", type = AttributeType.STRING)
	String akamaiAccessToken();

	@AttributeDefinition(name = "Akamai Host URL", description = "Akamai Host URL", type = AttributeType.STRING)
	String akamaiHostUrl();

	@AttributeDefinition(name = "Akamai Client Token", description = "Akamai Client Token", type = AttributeType.STRING)
	String akamaiClientToken();

	@AttributeDefinition(name = "Akamai Client Secret Key", description = "Akamai Client Secret Key", type = AttributeType.STRING)
	String akamaiClientSecretKey();
	
	@AttributeDefinition(name = "Akamai Connection Timeout", description = "Connection timeout value in ms.", type = AttributeType.INTEGER)
	String connectionTimeOut();

	@AttributeDefinition(name = "Akamai Socket Timeout", description = "Socket timeout value in ms.", type = AttributeType.INTEGER)
	String socketTimeOut();

	@AttributeDefinition(name = "Akamai Request Timeout", description = "Request timeout value in ms.", type = AttributeType.INTEGER)
	String requestTimeOut();

}
