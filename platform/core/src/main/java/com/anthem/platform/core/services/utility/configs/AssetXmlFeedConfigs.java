package com.anthem.platform.core.services.utility.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Anthem Utility -  Asset XML Feed Config", description = "Anthem Utility - Asset XML Feed Config")
public @interface AssetXmlFeedConfigs {

	@AttributeDefinition(name = "Unique Id for this service", description = "Unique Id for this service", type = AttributeType.STRING)
	String serviceId();
	
	@AttributeDefinition(name = "Asset Types", description = "Add dc:format of asset to generate xml feed [e.g. pdf, msword etc]", type = AttributeType.STRING)
	String[] assetTypes() default "pdf";
	
	@AttributeDefinition(name = "Page paths", description = "Add list of root page path to be searched to get pages for crawling", type = AttributeType.STRING)
	String[] pagePaths();
	
	@AttributeDefinition(name = "Brightcove videos path", description = "Add list of brightcove video paths to be searched to get videos for crawling", type = AttributeType.STRING)
	String[] brightcoveVideoPaths();
	
	@AttributeDefinition(name = "Is Extensionless page urls", description = "Is Extensionless page urls", type = AttributeType.BOOLEAN)
	boolean extensionlessUrl() default false;
	
	@AttributeDefinition(name = "Thumbnail List", description = "Thumbnail used for Assets", type = AttributeType.STRING)
	String thumbnailListPath() default "";
	
	@AttributeDefinition(name = "Application Tag ID", description = "Provide Tag Id to be checked during search", type = AttributeType.STRING)
	String applicationTagId() default "";
	
}
