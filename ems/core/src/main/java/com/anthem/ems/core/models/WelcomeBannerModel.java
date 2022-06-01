package com.anthem.ems.core.models;

import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.anthem.platform.core.models.AbstractImageComponentModel;

/**
* The model class for Hero Banner Component.
*
* Only requirement to use this for dynamic image.
*
* Need to Optimize: Need to find a better way to utilize dynamic image feature.
*
*
*/
@Model(adaptables = { Resource.class,
       SlingHttpServletRequest.class }, resourceType = WelcomeBannerModel.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class WelcomeBannerModel extends AbstractImageComponentModel {

   protected static final String RESOURCE_TYPE = "/apps/ems/components/content/welcomeBanner";

   public Map<String, String> getImageMap() {
       return super.getImageMap();
   }
}

