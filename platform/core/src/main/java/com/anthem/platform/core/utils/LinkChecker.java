package com.anthem.platform.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.GenericListService;
import com.day.cq.commons.jcr.JcrConstants;

public class LinkChecker extends WCMUsePojo {
	private static final Logger LOG = LoggerFactory.getLogger(LinkChecker.class);
	String link = null;
	String mappedLink = null;
	String disableSearchFlag = null;

	private static final String AUTHORED_LINK = "link";
	public static final String SLASH_CONTENT = "/content";
	public static final String SLASH_CONTENT_SLASH_DAM = "/content/dam";
	public static final String SUFFIX_HTML = ".html";
	public static final String HTTPS_PROTOCOL = "https://";


	@Override
	public void activate() throws Exception {
		//ResourceResolver resolver = getResourceResolver();
		String authoredLink = get(AUTHORED_LINK, String.class);
		if (StringUtils.isNotBlank(authoredLink)) {
			link = linkProvider(authoredLink);
			// commenting this code. This shortening will be done by AEM
			/*
			 * if (link.startsWith("/content")) { link = resolver.map(getRequest(),link); }
			 */
		}
	}
	
	public String linkProvider(String authoredLink) {
		String link = authoredLink;
		if (null != link) {
			link = link.trim();
			if (link.startsWith(SLASH_CONTENT)
					&& (!link.startsWith(SLASH_CONTENT_SLASH_DAM) && !link
							.contains(SUFFIX_HTML))) {
				link = link + SUFFIX_HTML;
			} else if(link.startsWith("www.")) {
				link = HTTPS_PROTOCOL + link;
			}

		} 
		return link;

	}

	public String getLink() {
		return link;
	}
	
	public String getMappedLink() {
		ResourceResolver resolver = getResourceResolver();
		mappedLink = link;
		if (StringUtils.startsWith(link, SLASH_CONTENT)) { 
			mappedLink = resolver.map(getRequest(),link); 
		}
		return mappedLink;
	}
	
	public String getDisableSearchFlag() {
		ResourceResolver resolver = getResourceResolver();
		String link = get(AUTHORED_LINK, String.class);
		if (StringUtils.startsWith(link, SLASH_CONTENT)) { 
			Resource resource = resolver.getResource(link);
			if(null != resource) {
				Resource jcrContent = resource.getChild(JcrConstants.JCR_CONTENT);
				if(null != jcrContent) {
					ValueMap vm = jcrContent.getValueMap();
					disableSearchFlag = vm.get("disableESearch", String.class);
				}
			}
		}
		return disableSearchFlag;
	}

	public String getExternalUrl() {
		GenericListService genericListService = getSlingScriptHelper().getService(GenericListService.class);
		return PlatformUtils.externalizeUri(link, getResourceResolver(), getRequest(),genericListService.getGenericListAsMap(PlatformConstants.INTERNAL_DOMAINS),null);
	}
	
}