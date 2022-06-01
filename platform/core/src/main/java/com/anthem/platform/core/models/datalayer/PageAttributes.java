package com.anthem.platform.core.models.datalayer;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.utils.PlatformUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class },
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class PageAttributes extends AbstractDigitalData{
		
	

	private String language;
	
	private String country;
	
	private String environment;
	
	private String errorType;

	@SlingObject
	private ResourceResolver resourceResolver;
	
	@Self
	private SlingHttpServletRequest request;
	
	@Inject
	private GenericListService genericListService;
		
	@PostConstruct
	@JsonIgnore
	private void init() {
		Locale pageLocale = pageInfoService.getPageLocale(currentPage, resourceResolver);
		this.language = pageLocale.toLanguageTag();
		this.country = pageLocale.getCountry();
		this.environment = pageInfoService.getRunMode();
		this.errorType = StringUtils.EMPTY;
	}
	
	public String getLanguage() {
		return language;
	}

	public String getCountry() {
		return country;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getErrorType() {
		return errorType;
	}
	
	public String getPageStateCode() {
		return pageInfoService.getPageStateCode(currentPage);
	}
	
	public String getStateRedirectionUrl() {
		String stateCode = pageInfoService.getPageStateCode(currentPage);
		String currentPagePath = currentPage.getPath();
		String redirectString = currentPagePath.replaceAll("/home/", "/{2}/").replaceAll("/"+stateCode+"/", "/{3}/");
		String[] pathArray = redirectString.split("/");
		pathArray[2] = "{1}";
		return StringUtils.join(pathArray, "/");
	}
	
	public String getPageDomainType() {
		String type = "internal";
		String runMode = pageInfoService.getRunMode();
		if(StringUtils.isNotEmpty(runMode)) {
			String domainName = PlatformUtils.extractDomainNameFromRequest(request);
			if(!PlatformUtils.isInternalDomain(genericListService.getGenericListAsMap(PlatformConstants.INTERNAL_DOMAINS),domainName)) {
				type = "external";
			}
		} 
		return type;
	}
}
