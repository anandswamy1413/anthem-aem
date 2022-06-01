package com.anthem.platform.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.models.SeoPair;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, resourceType = AnthemPageModel.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })

public class SeoPairModel {

	protected static final String RESOURCE_TYPE = "anthem/platform/components/structure/page";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@ChildResource
	private List<Resource> seoPairs;
	
	@ScriptVariable
	private Style currentStyle;
	
	private String[] dnsLinkNames;
	
	private String disableFontPreload;
	
	private List<SeoPair> seoPairList = new ArrayList<>();

	@PostConstruct
	protected void init() {
		logger.info("seo pair model invoked");

		if (seoPairs != null && !seoPairs.isEmpty()) {
			for (Resource resources : seoPairs) {
				SeoPair seo = resources.adaptTo(SeoPair.class);
				seoPairList.add(seo);
			}
		}
		
		if(currentStyle != null) {
			dnsLinkNames = currentStyle.get("dnsLinkNames", String[].class);
		}
		
		if(currentStyle != null) {
			disableFontPreload = currentStyle.get("disableFontPreload", String.class);
		}
		
	}

	public List<Resource> getSeoPairs() {
		return new ArrayList<> (seoPairs);
	}

	public List<SeoPair> getSeoPairList() {
		return new ArrayList<> (seoPairList);
	}

	public String[] getDnsLinkNames() {
		if(null == dnsLinkNames) {
			return null;
		}
		return Arrays.copyOf(dnsLinkNames, dnsLinkNames.length);
	}

	public String getDisableFontPreload() {
		return disableFontPreload;
	}
		
}
