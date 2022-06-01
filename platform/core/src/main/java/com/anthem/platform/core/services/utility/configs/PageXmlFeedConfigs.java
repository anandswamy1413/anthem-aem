package com.anthem.platform.core.services.utility.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Utility -  Page XML Feed Config", description = "Anthem Utility - Page XML Feed Config")
public @interface PageXmlFeedConfigs {

	@AttributeDefinition(name = "Allowed Templates", description = "Allowed Template List")
	String[] allowedTemplates();
	
	
	
	
	
}
