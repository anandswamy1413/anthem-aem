package com.anthem.platform.core.services.utility;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public interface ContentFragmentJsonService {

	public String contentFragmentToJson(Resource resource, String contentFragmentName,ResourceResolver resolver);
	
}
