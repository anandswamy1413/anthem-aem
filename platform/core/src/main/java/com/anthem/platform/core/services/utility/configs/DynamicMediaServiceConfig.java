package com.anthem.platform.core.services.utility.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Utility -  Dynamic Media Config", description = "Anthem Utility -  Dynamic Media Config")
public @interface DynamicMediaServiceConfig {

	@AttributeDefinition(name = "Preview Server URL", description = "Preview Server URL", type = AttributeType.STRING)
	String previewServerUrl() default "https://s7test1.scene7.com/";
	
	@AttributeDefinition(name = "Default Image preset", description = "Default Image preset", type = AttributeType.STRING)
	String defaultImagepreset() default "defaultResponsive";
}
