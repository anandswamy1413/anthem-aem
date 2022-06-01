package com.anthem.ems.core.service;

import org.apache.sling.api.resource.ResourceResolver;

public interface XFFetchPathService {
	String fetchXFPathFor(ResourceResolver resolver, String lang, String state, String brand, String groupNumber, String segment, String pageName) ;
}
