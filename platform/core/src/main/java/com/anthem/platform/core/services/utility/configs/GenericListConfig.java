package com.anthem.platform.core.services.utility.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Utility -  Generic List Configs", description = "Anthem Utility -  Generic List Configs")
public @interface GenericListConfig {

	@AttributeDefinition(name = "GenericList Root", description = "GenericList Root", type = AttributeType.STRING)
	String genericListRoot() default "/etc/acs-commons/lists/anthem/";
	
}
