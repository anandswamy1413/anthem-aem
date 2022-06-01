package com.anthem.platform.core.services.endpoints.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Endpoint - Apigee Factory Configs", description = "Anthem Endpoint - Apigee Factory Configs")
public @interface ApigeeEndpointConfig {
	
	@AttributeDefinition(name = "Endpoint Id", description = "Endpoint Id", type = AttributeType.STRING)
	String endpointId() default "api.apigee";

	@AttributeDefinition(name = "Apigee Api Key", description = "Apigee Api Key", type = AttributeType.STRING)
	String apigeeApiKey() default "H4mPESl6mEqk0M8iZnC5oF4IoUmVGFds";

	@AttributeDefinition(name = "Apigee Host URL", description = "Apigee Host URL", type = AttributeType.STRING)
	String apigeeHostUrl() default "https://api-np.anthem.com";

	@AttributeDefinition(name = "Apigee Client ID", description = "pigee Client ID", type = AttributeType.STRING)
	String apigeeClientID() default "05413c95e336496598a59c45aa3e3da5";

	@AttributeDefinition(name = "Apigee Client Secret Key", description = "Apigee Client Secret Key", type = AttributeType.STRING)
	String apigeeClientSecretKey() default "c3b63ea28a69467f5db0da165e44e02d680d721164ae0209b807fa621e8e2366";

	@AttributeDefinition(name = "Apigee Connection Timeout", description = "Connection timeout value in ms.", type = AttributeType.INTEGER)
	public int connectionTimeOut() default 0;

	@AttributeDefinition(name = "Apigee Socket Timeout", description = "Socket timeout value in ms.", type = AttributeType.INTEGER)
	public int socketTimeOut() default 0;

	@AttributeDefinition(name = "Apigee Request Timeout", description = "Request timeout value in ms.", type = AttributeType.INTEGER)
	public int requestTimeOut() default 0;
	
}
