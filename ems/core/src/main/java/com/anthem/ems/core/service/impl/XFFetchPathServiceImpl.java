package com.anthem.ems.core.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.ems.core.service.XFFetchPathService;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = XFFetchPathService.class, property = {
        Constants.SERVICE_ID + "= XF Fetch Path Service",
        Constants.SERVICE_DESCRIPTION + "= This service fetches the XF path given the group profile data"
})
public class XFFetchPathServiceImpl implements XFFetchPathService {
	
	private static final Logger LOG = LoggerFactory.getLogger(XFFetchPathServiceImpl.class);
	
	private List<String> specifics = Collections.unmodifiableList(Arrays.asList("group-number", "segment", "state", "brand"));

	private static final String VARIATIONPATH = "/content/experience-fragments/ems/%s/%s/%s/%s";
	
	@Reference
    private QueryBuilder queryBuilder;
	
	public String fetchXFPathFor(ResourceResolver resolver, String lang, String state, String brand, String groupNumber, String segment, String pageName) {
		Session session = resolver.adaptTo(Session.class);
		String path = "";
		String xfPath = "";
		for(int i = 0;i < specifics.size(); i++) {
			switch(specifics.get(i)) {
			case "group-number":  
				path = String.format(VARIATIONPATH, lang, specifics.get(i), pageName, JcrUtil.createValidName(groupNumber)); 
				xfPath = getXFResourcePath(resolver, session, lang, path, specifics.get(i), pageName, groupNumber);
				if(StringUtils.isNotEmpty(xfPath))
					return xfPath;
				break;
			case "segment": 
				path = String.format(VARIATIONPATH, lang, specifics.get(i), pageName,  JcrUtil.createValidName(segment)); 
				xfPath = getXFResourcePath(resolver, session, lang, path, specifics.get(i), pageName, segment);
				if(StringUtils.isNotEmpty(xfPath))
					return xfPath;
				break;
			case "state": 
				path = String.format(VARIATIONPATH, lang, specifics.get(i), pageName,  JcrUtil.createValidName(state)); 
				xfPath = getXFResourcePath(resolver, session, lang, path, specifics.get(i), pageName, state);
				if(StringUtils.isNotEmpty(xfPath))
					return xfPath;
				break;
			case "brand": 
				path = String.format(VARIATIONPATH, lang, specifics.get(i), pageName, JcrUtil.createValidName(brand)); 
				xfPath = getXFResourcePath(resolver, session, lang, path, specifics.get(i), pageName, brand);
				if(StringUtils.isNotEmpty(xfPath))
					return xfPath;
				break;
			default: return "";
			}
		}
		return "";
	}

	private String getXFResourcePath(ResourceResolver resolver, Session session, String lang, String path, String specific, String pageName, String xftitle) {
		String resourcePath = "";
		if(resourceExists(resolver, path)) {
			resourcePath = path;
		} else {
			String pagePath = String.format("/content/experience-fragments/ems/%s/%s/%s", lang, specific, pageName);
			String tempResourcePath = getResourceWithTitle(session, pagePath, xftitle);
			if(StringUtils.isNotEmpty(tempResourcePath)) {
				resourcePath = tempResourcePath;
			}
		}
		return resourcePath;
	}

	private String getResourceWithTitle(Session session, String pagePath, String jcrTitle) {
		String path = "";
		Map<String, String> predicateMap = new HashMap<>();
		predicateMap.put("type", "cq:Page");
		predicateMap.put("path", pagePath);
		predicateMap.put("1_property", "jcr:content/sling:resourceType");
		predicateMap.put("1_property.value", "ems/components/content/xfpage");
		predicateMap.put("2_property", "jcr:content/jcr:title");
		predicateMap.put("2_property.value", jcrTitle);
		
		Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
		 
		//Get search results
		SearchResult result = query.getResult();
		List<Hit> hits = result.getHits();
		if(!hits.isEmpty()) {
			Hit hit = hits.get(0);
			try {
				 path = hit.getPath();
			} catch (RepositoryException e) {
				LOG.error("Repository exception while querying the xf with title:{}", e.getMessage());
			}
		}
		return path;
	}

	private boolean resourceExists(ResourceResolver resolver, String path) {
		return (StringUtils.isNotEmpty(path) && resolver.getResource(path) != null);
	}

}
