package com.anthem.platform.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import com.day.cq.dam.api.DamConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.crx.JcrConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Anthem Smart Crop Validation",
		"sling.servlet.resourceTypes=" + SmartCropValidationServlet.RESOURCE_TYPE, "sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class SmartCropValidationServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(SmartCropValidationServlet.class);
	private static final String SMART_CROP_VALUE = "crop_smart";
	private static final String SMART_CROP_PROP = "crop_type";
	private static final String BANNER = "banner";
	static final String RESOURCE_TYPE = "services/anthem/touchui/datasources/dm/smartcropvalidation";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		try {

			ResourceResolver resourceResolver = request.getResourceResolver();
			Map<String, Boolean> validations = new HashMap<String, Boolean>();
			String damPath = request.getParameter("imgPath");
			String mobileDamPath = request.getParameter("mobileImgPath");
			String cropProfile = request.getParameter("cropProfile");
			String mobileCropProfile = request.getParameter("mobileCropProfile");

			ValueMap map = getProfileMap(resourceResolver, damPath);
			Boolean validCropProfile = null != damPath && !damPath.isEmpty() ? false : true;
			String smartCrop = null;
			if (null != map) {
				String banner = map.get(BANNER, String.class);
				smartCrop = map.get(SMART_CROP_PROP, String.class);
				String[] profiles = banner.split("\\|");
				for (String crop : profiles) {
					String profile = crop.substring(0, crop.indexOf(","));
					if (cropProfile.equals(profile)) {
						validCropProfile = true;
					}
				}
			}
			validations.put("cropProfile", validCropProfile);
			validations.put("crop", (SMART_CROP_VALUE).equals(smartCrop) || null == damPath ? true : false);

			ValueMap mobileMap = getProfileMap(resourceResolver, mobileDamPath);
			String mobileSmartCrop = null;
			Boolean validMobileCropProfile = null != mobileDamPath && !mobileDamPath.isEmpty() ? false : true;
			if (null != mobileMap) {
				String mobileBanner = mobileMap.get(BANNER, String.class);
				mobileSmartCrop = mobileMap.get(SMART_CROP_PROP, String.class);
				String[] mobileProfiles = mobileBanner.split("\\|");
				for (String mobileCrop : mobileProfiles) {
					String mobProfile = mobileCrop.substring(0, mobileCrop.indexOf(","));
					if (mobileCropProfile.equals(mobProfile)) {
						validMobileCropProfile = true;
					}
				}
			}
			validations.put("mobileCropProfile", validMobileCropProfile);
			validations.put("mobileCrop",
					(SMART_CROP_VALUE).equals(mobileSmartCrop) || null == mobileDamPath ? true : false);
			Gson gson = new GsonBuilder().create();
			String responseStr = gson.toJson(validations);

			response.getWriter().write(responseStr);

		} catch (IOException e) {
			LOG.error("Error in Getting Validations", e);
		}
	}

	private ValueMap getProfileMap(ResourceResolver resourceResolver, String damPath) {
		Resource assetRes = resourceResolver.getResource(damPath);
		ValueMap map = null;
		if (assetRes != null) {
			Resource folderRes = assetRes.getParent();
			if (folderRes != null) {
				Resource jcrResource = folderRes.getChild(JcrConstants.JCR_CONTENT);
				if (jcrResource != null) {
					String imgProfile = jcrResource.getValueMap().get(DamConstants.IMAGE_PROFILE, String.class);
					if (StringUtils.isNoneEmpty(imgProfile)) {
						Resource profileRes = resourceResolver.getResource(imgProfile);
						map = null != profileRes ? profileRes.getValueMap() : null;
					}
				}
			}
		}
		return map;
	}
}