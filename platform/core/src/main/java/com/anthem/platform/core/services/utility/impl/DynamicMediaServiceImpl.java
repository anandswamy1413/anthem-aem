package com.anthem.platform.core.services.utility.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.services.utility.DynamicMediaService;
import com.anthem.platform.core.services.utility.configs.DynamicMediaServiceConfig;

@Designate(ocd = DynamicMediaServiceConfig.class)
@Component(service = DynamicMediaService.class, immediate = true)
@ServiceDescription("Dynamic Media Service")
public class DynamicMediaServiceImpl implements DynamicMediaService {

	private static final Logger LOG = LoggerFactory.getLogger(DynamicMediaServiceImpl.class);
	private static final String DAM_PREFIX = "/content/dam";
	private static final String IS_IMAGE = "is/image";
	private static final String SLASH = "/";
	private static final String COLON = ":";
	private static final String DAM_METADATA = "jcr:content/metadata";
	private static final String SCENE7_DOMAIN = "dam:scene7Domain";
	private static final String SCENE7_FILE_NAME = "dam:scene7File";
	
	private String previewServerUrl;
	private String defaultImagePreset;

	@Reference
	private SlingSettingsService slingSettingsService;

	private boolean isAuthor() {
	     return this.slingSettingsService.getRunModes().contains("author");
	}
		
	@Activate
	public void activate(DynamicMediaServiceConfig config) {
		this.previewServerUrl = config.previewServerUrl();
		this.defaultImagePreset = config.defaultImagepreset();
	}

	public ValueMap getAssetMetadataMap(String damImagePath, ResourceResolver resolver) {
		ValueMap vm = null;
		if (null != resolver && isDamPath(damImagePath)) {
			Resource damRes = resolver.getResource(damImagePath);
			if (damRes!= null && damRes.getChild(DAM_METADATA) != null) {
				Resource metaRes = damRes.getChild(DAM_METADATA);
				vm = metaRes.getValueMap();
			}
		}
		return vm;
	}
	
	private String getScene7Url(ValueMap map, boolean isTargetPreview) {	
		String scene7Domain = map.get(SCENE7_DOMAIN, String.class);
		if(StringUtils.isBlank(scene7Domain)) {
			return null;
		}		
		if(isAuthor() && !isTargetPreview) {
			return this.previewServerUrl;
		}
		return scene7Domain;
	}
	
	private String getScene7File(ValueMap map) {		
		return map.get(SCENE7_FILE_NAME, String.class);
	}

	public boolean isDamPath(String path) {
		if (null != path && StringUtils.startsWith(path, DAM_PREFIX)) {
			return StringUtils.startsWith(path, DAM_PREFIX);
		} else {
			return false;
		}
	}

	
	
	@Override
	public String getDMAssetUrl(String assetPath, String imagePreset, String cropProfile, String queryParams, ResourceResolver resourceResolver, boolean isTargetPreview) {		
		ValueMap map = getAssetMetadataMap(assetPath, resourceResolver);
		if(null == map ) {
			return assetPath;
		}
		String scene7Url = getScene7Url(map,isTargetPreview);
		if(StringUtils.isBlank(scene7Url)) {
			return assetPath;
		}
		String finalImagePreset = defaultImagePreset;
		StringBuffer sb = new StringBuffer();
		scene7Url = scene7Url.substring(scene7Url.indexOf(":")+1);
		sb.append(scene7Url);
		sb.append(IS_IMAGE);
		sb.append(SLASH);
		sb.append(getScene7File(map));
		
		// Add crop profile
		if(StringUtils.isNotBlank(cropProfile)) {
			sb.append(COLON);
			sb.append(cropProfile);
		}	
		
		//Add image preset
		if(StringUtils.isNotBlank(imagePreset)) {
			finalImagePreset = imagePreset;
		}
		
		sb.append("?$");
		sb.append(finalImagePreset);
		sb.append("$");
		
		if(StringUtils.isNotBlank(queryParams)) {
			sb.append(StringUtils.startsWith(queryParams, "&")?"":"&");
			sb.append(queryParams);
		}
		
		return sb.toString();
		
	}

}
