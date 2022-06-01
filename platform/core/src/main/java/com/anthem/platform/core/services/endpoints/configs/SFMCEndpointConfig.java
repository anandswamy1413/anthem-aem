package com.anthem.platform.core.services.endpoints.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Endpoint - SFMC Factory Configs", description = "Anthem Endpoint - SFMC Factory Configs")
public @interface SFMCEndpointConfig {

	@AttributeDefinition(name = "Endpoint Id", description = "Endpoint Id", type = AttributeType.STRING)
	String endpointId() default "api.sfmc";

	@AttributeDefinition(name = "Client Id", description = "SFMC client ID", type = AttributeType.STRING)
	public String clientId() default "fin3boxoanows5uertn9visw";
	
	@AttributeDefinition(name = "Client Secret", description = "SFMC client secret", type = AttributeType.STRING)
	public String clientSecret() default "60Cvh1BzrRw6kb1KmCGyu283";
	
	@AttributeDefinition(name = "Authentication URL", description = "SFMC Authentication URL", type = AttributeType.STRING)
	public String authUrl() default "https://mc5s8k4mxpmd-2bwfxrn7s0nlg14.auth.marketingcloudapis.com/v2/token";
	
	@AttributeDefinition(name = "Grant Type", description = "Request Grant Type", type = AttributeType.STRING)
	public String grantType() default "client_credentials";
	
	@AttributeDefinition(name = "Account Id", description = "SFMC Account ID", type = AttributeType.STRING)
	public String accountId() default "10944798";

    @AttributeDefinition(name = "Connection Timeout", description = "Connection timeout value in ms.", type = AttributeType.INTEGER)
    public int connectionTimeOut() default 0;

    @AttributeDefinition(name = "Socket Timeout", description = "Socket timeout value in ms.", type = AttributeType.INTEGER)
    public int socketTimeOut() default 0;

    @AttributeDefinition(name = "Request Timeout", description = "Request timeout value in ms.", type = AttributeType.INTEGER)
    public int requestTimeOut() default 0;
    
    @AttributeDefinition(name = "Trigger Mail API Path", description = "REST API Path for triggering email", type = AttributeType.STRING)
    public String triggerMailAPIPath() default "messaging/v1/messageDefinitionSends/key:2694/send";
    
    @AttributeDefinition(name = "Subscribe API Path", description = "REST API Path for Subscribe", type = AttributeType.STRING)
    public String subscribeAPIPath() default "hub/v1/dataeventsasync/key:ACE_API_InsertDE/rowset";

}
