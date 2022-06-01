package com.anthem.platform.core.services.utility.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.xf.ExperienceFragmentVariation;
import com.adobe.cq.xf.ExperienceFragmentsServiceFactory;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.XFUtilityService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

@Component(service = XFUtilityService.class, immediate = true)
@ServiceDescription("Experience fragments utility Service")
public class XFUtilityServiceImpl implements XFUtilityService {

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	private ExperienceFragmentsServiceFactory experienceFragmentsServiceFactory;

	private static final String XF_RESOURCE_TYPE = "cq/experience-fragments/components/experiencefragment";
	private final Logger log = LoggerFactory.getLogger(getClass());
	

	@Override
	public List<String> getAllXFVariationsPaths(String xfPath) {
		List<String> xfList = new ArrayList<>();
		try (ResourceResolver resourceResolver = resourceResolverFactory
				.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
			Resource xfResource = resourceResolver.getResource(xfPath);
			Page page = xfResource.adaptTo(Page.class);
			if (page.getContentResource().isResourceType(XF_RESOURCE_TYPE)) { 
				Iterator<Page> fragmentVariation = page.listChildren(new PageFilter(), false);
				while (fragmentVariation.hasNext()) {
					Page fragmentPage = fragmentVariation.next();
					if (null != fragmentPage) {
						ExperienceFragmentVariation var = fragmentPage.adaptTo(ExperienceFragmentVariation.class);
						if (null != var)
							xfList.add(var.getPath());
					}
				} return xfList;
			}
		} catch (LoginException e) {
			log.error("Exception while getting parent nodes {}", e);
		} 
		return xfList;
	}

}
