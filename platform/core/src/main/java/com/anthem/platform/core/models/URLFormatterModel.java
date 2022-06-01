package com.anthem.platform.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.utils.PlatformUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The model formats the url by resolving the url and appending the suffix if it is an internal url.
 * @author imrankhan
 *
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class URLFormatterModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(URLFormatterModel.class);
	private static final String ANTHEM_REL_ATTRIBUTE = "noreferrer noopener";

	/** The url passed as parameter to the model and needs to be formatted. */
	@RequestAttribute
	private String url;

	@Inject
	@JsonIgnore
	private GenericListService genericListService;


	@Inject
	private String linkTarget;
	
	@Inject
	private String disableRelTag;

	/** The ResourceResolver. */
	@SlingObject
	ResourceResolver resourceResolver;
	
	@Inject
	@JsonIgnore
	protected SlingHttpServletRequest slingHttpServletRequest;

	@PostConstruct
	protected void init() {
		LOGGER.debug("Inside URLFormatterModel");
	}

	/**
	 *
	 * @return the formatted url
	 */
	public String getFormattedURL() {
		return StringUtils.isNotBlank(url) ? PlatformUtils.formatURL(url, resourceResolver,slingHttpServletRequest,genericListService.getGenericListAsMap(PlatformConstants.INTERNAL_DOMAINS)) : StringUtils.EMPTY;
	}
	
	/**
	 *
	 * @return the Rel Tag
	 */
	public String getRelTag() {
		return "_blank".equals(linkTarget) && !"true".equals(disableRelTag) ? ANTHEM_REL_ATTRIBUTE : "";
	}
}