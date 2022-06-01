package com.anthem.platform.core.models.datalayer;

import javax.annotation.PostConstruct;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.services.utility.PageInfoService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Model(adaptables = { SlingHttpServletRequest.class, Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AbstractDigitalData {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractDigitalData.class);

	@Inject
	@JsonIgnore
	protected Page currentPage;
	
	@Inject
	@JsonIgnore
	protected Resource resource;
	
	@Inject
	@JsonIgnore
	protected PageInfoService pageInfoService;
	
	@Inject
	@JsonIgnore
	protected GenericListService genericListService;
	
	@Inject
	@JsonIgnore
	protected SlingHttpServletRequest slingHttpServletRequest;
	
	@Inject
	@JsonIgnore
	protected ResourceResolver resourceResolver;
	
	@Inject
	@JsonIgnore
	private JacksonMapperService jacksonMapper;
	
	@PostConstruct
	@JsonIgnore
	protected void initBase() {
		if(null == currentPage) {
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			this.currentPage = pageManager.getContainingPage(resource);
		}
	}
	
	@JsonIgnore
	public String getJson() {
		return jacksonMapper.convertObjectToJson(this, true);
	}
}
