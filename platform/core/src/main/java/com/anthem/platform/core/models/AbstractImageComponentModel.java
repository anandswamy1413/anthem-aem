package com.anthem.platform.core.models;

import java.util.HashMap;
import java.util.Map;
import java.net.URI;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.services.utility.DynamicMediaService;
import com.anthem.platform.core.utils.PlatformFileHelper;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;

public abstract class AbstractImageComponentModel {

	@Inject
	@Source("sling-object")
	private ResourceResolver resourceResolver;
	
	@SlingObject
	protected Resource resource;
	
	@Self
	protected SlingHttpServletRequest request;
	
	@ValueMapValue
	protected String isTransparentImage;
	
	@ValueMapValue
	protected String imgPath;

	@ValueMapValue
	protected String mobileImgPath;

	@ValueMapValue
	protected String s7CropProfile;
	
	@ValueMapValue
	protected String s7MobileCropProfile;
	
	@ValueMapValue
	protected String altText;
	
	@ValueMapValue
	protected String s7ImagePreset;
	
	@ValueMapValue
	protected String s7MobileImagePreset;
	
	@ValueMapValue
	protected String s7QueryParams;
	
	@Inject
	protected DynamicMediaService mediaService;
	
	@ScriptVariable
	protected Style currentStyle;
	
	@Inject
	protected PlatformFileHelper platformFileHelper;
	
	protected Map<String,String> imageMap = new HashMap<>();
	
	// Override this method to provide custom impl for breakpoint definitions
	public Map<String,String> getImageMap(){
		if("true".equalsIgnoreCase(getDmDisabledFlag())) {
			return getDefaultImageMap();
		}
		 		
		String queryParms = s7QueryParams;
		
		if ("true".equalsIgnoreCase(isTransparentImage)) {
			if (null != s7QueryParams) {
				queryParms = queryParms + "&fmt=png-alpha";
			} else {
				queryParms = "&fmt=png-alpha";
			}
		}
		
		String desktopDmUrl = mediaService.getDMAssetUrl(imgPath, s7ImagePreset, s7CropProfile, queryParms, resourceResolver,isTargetPreview()).replaceAll(" ", "%20");		
		imageMap.put("md", desktopDmUrl);
		imageMap.put("lg", desktopDmUrl);
		imageMap.put("xl",desktopDmUrl);
		if(StringUtils.isNotBlank(mobileImgPath)) {
			String mobileDmUrl =  mediaService.getDMAssetUrl(mobileImgPath, s7MobileImagePreset, s7MobileCropProfile, queryParms, resourceResolver,isTargetPreview());
			mobileDmUrl = mobileDmUrl.replaceAll(" ", "%20");
			imageMap.put("sm", mobileDmUrl);
		} else {
			String mobileDmUrl =  mediaService.getDMAssetUrl(imgPath, s7MobileImagePreset, s7MobileCropProfile, queryParms, resourceResolver,isTargetPreview());
			mobileDmUrl = mobileDmUrl.replaceAll(" ", "%20");
			imageMap.put("sm", mobileDmUrl);		
		}
		return imageMap;
	}
	
	public Map<String,String> getImageMapFromVm(ValueMap vm){
		if("true".equalsIgnoreCase(getDmDisabledFlag())) {
			return getDefaultImageMap();
		}
		Map<String,String> imageMap = new HashMap<>();
		String desktopDmUrl = mediaService.getDMAssetUrl(vm.get("imgPath",String.class), vm.get("s7ImagePreset",String.class), vm.get("s7CropProfile",String.class), vm.get("s7QueryParams",String.class), resourceResolver,isTargetPreview());		
		imageMap.put("md", desktopDmUrl);
		imageMap.put("lg", desktopDmUrl);
		imageMap.put("xl",desktopDmUrl);
		if(StringUtils.isNotBlank(vm.get("mobileImgPath",String.class))) {
			imageMap.put("sm", mediaService.getDMAssetUrl(vm.get("mobileImgPath",String.class), vm.get("s7ImagePreset",String.class), vm.get("s7CropProfile",String.class), vm.get("s7QueryParams",String.class), resourceResolver,isTargetPreview()));
		} else {
			imageMap.put("sm", mediaService.getDMAssetUrl(vm.get("imgPath",String.class), vm.get("s7ImagePreset",String.class), vm.get("s7CropProfile",String.class), vm.get("s7QueryParams",String.class), resourceResolver,isTargetPreview()));		
		}
		return imageMap;
	}
	
	public boolean isTargetPreview() {
		RequestPathInfo requestPathInfo  = request.getRequestPathInfo();
		String[] selectors = requestPathInfo.getSelectors();
		return ArrayUtils.contains(selectors, "nocloudconfigs");
	}
	
	public Map<String, String> getDefaultImageMap() {
		String escapedPath = imgPath.replaceAll(" ", "%20");
		imageMap.put("md", escapedPath);
		imageMap.put("lg", escapedPath);
		imageMap.put("xl",escapedPath);
		if(StringUtils.isNotBlank(mobileImgPath)) {
			imageMap.put("sm", mobileImgPath.replaceAll(" ", "%20"));
		} else {
			imageMap.put("sm", escapedPath);
		}		
		return imageMap;
	}

	public String getAltText() {
		return altText;
	}
	
	public String getDmDisabledFlag() {
		ContentPolicyManager policyManager = resourceResolver.adaptTo(ContentPolicyManager.class);
        if (policyManager != null) {
            ContentPolicy contentPolicy = policyManager.getPolicy(resource);
            if (contentPolicy != null) {
                ValueMap properties = contentPolicy.getProperties();
                return properties.get("dmDisabledFlag",String.class);
            }
        }
        return "false";
	}
		
}
