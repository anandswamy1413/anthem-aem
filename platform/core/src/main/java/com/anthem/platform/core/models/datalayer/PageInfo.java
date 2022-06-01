package com.anthem.platform.core.models.datalayer;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.utils.PlatformUtils;
import com.day.cq.commons.Externalizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;

import com.adobe.acs.commons.models.injectors.annotation.HierarchicalPageProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class },
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class PageInfo extends AbstractDigitalData {
	
	private static final Logger LOG = LoggerFactory.getLogger(PageInfo.class);
		
	private String pageId;
	
	private String pageName;
	
	private String pageTitle;
	
	private String analyticsTitle;
	
	private String analyticsName;
	
	private String destinationURL;
	
	private String referringURL;
	
	private String language;
	
	private String geoRegion;
	
	private String seoTitle;

	private String hostName;

	private String absolutePageUrl;

	@HierarchicalPageProperty("brandName")
	private String brandName;

	@SlingObject
	private ResourceResolver resourceResolver;

	@PostConstruct
	@JsonIgnore
	private void init() {
		this.pageName = pageInfoService.getAnalyticsPageName(currentPage, 2);
		this.pageTitle = pageInfoService.getPageTitle(currentPage);
		this.analyticsTitle = pageInfoService.getAnalyticsTitle(currentPage);
		this.seoTitle = pageInfoService.getSeoTitle(currentPage);
		this.pageId = currentPage.getName();
		this.destinationURL = currentPage.getPath();
		if(null != this.slingHttpServletRequest) {
			this.destinationURL = this.slingHttpServletRequest.getRequestURL().toString();
			this.hostName = PlatformUtils.extractDomainNameFromRequest(this.slingHttpServletRequest);
			String[] hostArray = hostName.split("//");
			this.hostName = hostArray[1];
		}
		Locale pageLocale = pageInfoService.getPageLocale(currentPage, resourceResolver);
		this.language = pageLocale.toLanguageTag();
		this.geoRegion = pageInfoService.getPageStateCode(currentPage);
		this.analyticsName = pageInfoService.getAnalyticsName(currentPage);
		this.absolutePageUrl = PlatformUtils.externalizeUri(currentPage.getPath(), this.resourceResolver, this.slingHttpServletRequest, genericListService.getGenericListAsMap(PlatformConstants.INTERNAL_DOMAINS), StringUtils.EMPTY);
	}

	public String getPageId() {
		return pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public String getDestinationURL() {
		return destinationURL;
	}

	public String getReferringURL() {
		return referringURL;
	}

	public String getLanguage() {
		return language;
	}

	public String getGeoRegion() {
		return geoRegion;
	}

	public String getAnalyticsTitle() {
		return analyticsTitle;
	}
	
	public String getAnalyticsName() {
		return analyticsName;
	}
	
	public String getBrandName() {
		return brandName;
	}

	public String getHostName() {
		return hostName;
	}

	public String getAbsolutePageUrl() {
		return absolutePageUrl;
	}

	public String getSeoTitle() {
		return seoTitle;
	}
	
}
