package com.anthem.platform.core.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.anthem.platform.core.utils.PlatformFileHelper;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;

@SuppressWarnings("squid:S2221")
public class SvgWcmUse extends WCMUsePojo {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected final static String ASSET_TYPE = "assetType";
	protected final static String ASSET_DATA = "assetData";
	
	@Override
	public void activate() throws Exception {
		Validate.notNull(getResourceResolver());
	}

	public String getSvgDataPath() {
		String svgDataPath = null;
       Document svgDataDoc;
		String svgPath = get("svgPath", String.class);
		String eleName = StringUtils.defaultString(get("eleName", String.class), "");
		Object[] keyValueEleObj = get("keyValueEle", Object[].class);
		String[] keyValueEle;
		logger.info("inside getSvgDataPath{}", svgDataPath);
		if (svgPath == null) {
			return null;
		}

		try {
			PlatformFileHelper platformFileHelper = getSlingScriptHelper().getService(PlatformFileHelper.class);
			String fileExtension = StringUtils.substringAfterLast(svgPath, ".");
			if (!"svg".equalsIgnoreCase(fileExtension)) {
				return null;
			}

			String absPath = getResolvedPath(svgPath,platformFileHelper);
			logger.info("inside absPath{}", absPath);
			svgDataDoc = platformFileHelper.readjcrDataAsXML(absPath, getResourceResolver());
			if (svgDataDoc == null) {
				return null;
			}
			svgDataPath = svgDataDoc.toString();
			Element pathEle = svgDataDoc.select(eleName).first();
			if (pathEle != null && keyValueEleObj != null) {
				
			
			
			keyValueEle = Arrays.copyOf(keyValueEleObj, keyValueEleObj.length, String[].class);
			
			for (String keyValue : keyValueEle) {
				int keyvalue=keyValue.indexOf("=");
				if (keyvalue > 1) {
					String[] spiltByEqual = keyValue.split("=");
					pathEle.attr(spiltByEqual[0], spiltByEqual[1]);
				}
			}
			}
			svgDataPath = svgDataDoc.toString();

		} catch (Exception e) {
			logger.error("Exception converting data into svg", e);
		}
		return svgDataPath;
	}

	private String getResolvedPath(String svgPath,PlatformFileHelper platformFileHelper) {
		ResourceResolver resourceResolver = getResourceResolver();
		String absPath = svgPath;

		if (null != resourceResolver) {
				Resource resolvedResource = platformFileHelper.getRenditionResource(svgPath, "original", resourceResolver);
			if (null != resolvedResource) {
				absPath = resolvedResource.getPath();
			}
		}
		return absPath;
	}

	public boolean isSvg() {
		String svgPath = get("svgPath", String.class);
		String fileExtension = StringUtils.substringAfterLast(svgPath, ".");
		return ("svg".equalsIgnoreCase(fileExtension));
	}
	
	public Map<String,String> getBase64ImageMap() {
		String imgPath = get("imgPath", String.class);
		PlatformFileHelper platformFileHelper = getSlingScriptHelper().getService(PlatformFileHelper.class);
		

		if(!"true".equalsIgnoreCase(getbase64EncodeDisabled())) {
			Asset asset = platformFileHelper.getAsset(imgPath, getResourceResolver());
			if(null == asset) {
				return null;
			}
			try (InputStream inputStream = platformFileHelper.getOriginalInputStream(asset)){
				String encodedString = Base64
				          .getEncoder()
				          .encodeToString(IOUtils.toByteArray(inputStream));
				Map<String,String> dataMap = new HashMap<>();
				dataMap.put(ASSET_TYPE, asset.getMimeType());
				dataMap.put(ASSET_DATA, encodedString);
				return dataMap;
			} catch (IOException e) {				
				logger.error("Exception encoding image",e);
			}
		}
		return null;
	}

	public String getbase64EncodeDisabled() {
		ResourceResolver resourceResolver = getResourceResolver();
		ContentPolicyManager policyManager = resourceResolver.adaptTo(ContentPolicyManager.class);
        if (policyManager != null) {
            ContentPolicy contentPolicy = policyManager.getPolicy(getResource());
            if (contentPolicy != null) {
                ValueMap properties = contentPolicy.getProperties();
                return properties.get("base64EncodeDisabled",String.class);
            }
        }
        return "false";
	}

}
