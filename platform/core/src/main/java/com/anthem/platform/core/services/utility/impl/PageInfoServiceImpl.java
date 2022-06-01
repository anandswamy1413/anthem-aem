package com.anthem.platform.core.services.utility.impl;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.services.utility.PageInfoService;
import com.day.cq.commons.Language;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.LanguageManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.text.Text;

@Component(immediate = true, service = PageInfoService.class)
public class PageInfoServiceImpl implements PageInfoService {

	@Reference
	private GenericListService genericListService;

	@Reference
	private SlingSettingsService slingSettingsService;

	@Reference
	private LanguageManager languageManager;

	private static final Pattern STATE_CODE = Pattern.compile("/([a-zA-Z]{2})(/|$)");
	
	private static final String STATE_CODES_PATH = "all_state_codes";
	
	public static final String ENVIRONMENTS = "environments";
	
	public static final String BRAND = "brand";

	@Override
	public Locale getPageLocale(Page page, ResourceResolver resourceResolver) {
		Language language = languageManager.getCqLanguage(page.getContentResource(), true);
		Page languageRoot = languageManager.getLanguageRoot(page.getContentResource(), true);
		if(null == language) {
			return Locale.ENGLISH;
		}
		
		if(null == languageRoot) {
			return language.getLocale();
		}
		
		if (languageRoot.getName().length() > 2) {
			return language.getLocale();
		}

		if (languageRoot.getName().equalsIgnoreCase(getPageStateCode(page))) {
			String statePagePath = getStatePagePath(page.getPath(), getPageStateCode(page));

			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			Page statePage = pageManager.getPage(statePagePath);
			Page parentPage = statePage.getParent();
			Language parentPageLanguage = languageManager.getCqLanguage(parentPage.getContentResource(), true);
			Page parentLanguageRoot = languageManager.getLanguageRoot(parentPage.getContentResource(), true);
			String countryPagePath = Text.getRelativeParent(parentLanguageRoot.getPath(), 1);
			String countryName = Text.getName(countryPagePath);
			return new Locale(parentPageLanguage.getLanguageCode(), countryName);
		}

		String countryPagePath = Text.getRelativeParent(languageRoot.getPath(), 1);
		String countryName = Text.getName(countryPagePath);
		return new Locale(language.getLanguageCode(), countryName);
	}

	private String getStatePagePath(String pagePath, String stateCode) {
		String[] pathArray = pagePath.split("/");
		StringBuilder str  = new StringBuilder();
		str.append("/");
		for(int i = 1; i < pathArray.length; i++) {
			str.append(pathArray[i]);
			if(pathArray[i].equalsIgnoreCase(stateCode)) {
				break;
			}else {
				str.append("/");
			}
		}
		return str.toString();
	}

	@Override
	public String getPageStateCode(Page page) {
		Map<String, String> stateMap = genericListService.getGenericListAsMap(STATE_CODES_PATH);
		Matcher sm = STATE_CODE.matcher(page.getPath());
		String stateCode = null;
		while (sm.find()) {
			String code = sm.group(1);
			if (stateMap.containsKey(code.toUpperCase())) {
				stateCode = code;
				break;
			}
			sm.region(sm.end(1), page.getPath().length());
		}
		return stateCode;
	}
	
	@Override
	public String getPageStateName(Page page) {
		
		String pagePath = page.getPath();
		String stateCode = getPageStateCode(page);
		
		if(stateCode == null){
			return StringUtils.EMPTY;
		}
		String statePagePath = pagePath.substring(0,(pagePath.indexOf(stateCode) + stateCode.length()) );
		Page statePage = page.getPageManager().getPage(statePagePath);
		
		
		return getPageTitle(statePage);
	}

	@Override
	public String getPageTitle(Page page) {
		String title = page.getPageTitle();
		if (StringUtils.isEmpty(title)) {
			title = page.getTitle();
		}
		if (StringUtils.isEmpty(title)) {
			title = page.getNavigationTitle();
		}
		if (StringUtils.isEmpty(title)) {
			title = page.getName();
		}
		return title;
	}

	@Override
	public String getAnalyticsPageName(Page page, int pageNameLevel) {
		String[] pathArray = page.getPath().split("/");
		StringBuilder str  = new StringBuilder();
		for(int i = pageNameLevel; i < pathArray.length; i++) {
			str.append(pathArray[i]);
			str.append(":");
		}
		str.setLength(str.length() - 1);
		return str.toString();
	}

	@Override
	public Set<String> getRunModes() {
		 Set<String> runModeset = Collections.emptySet();
		 if (slingSettingsService != null) {
             runModeset = slingSettingsService.getRunModes();             
         }
		return runModeset;
	}

	@Override
	public String getAnalyticsTitle(Page page) {
		ValueMap vm = page.getContentResource().getValueMap();
		String analyticsTitle = vm.get("analyticsTitle", String.class);
		if(StringUtils.isNotBlank(analyticsTitle)) {
			return analyticsTitle;
		} 
		return this.getPageTitle(page);
	}

	@Override
	public String getAnalyticsName(Page page) {
		String title = StringUtils.EMPTY;
		if(null != getAnalyticsTitle(page) && StringUtils.isNotEmpty(getAnalyticsTitle(page))) {
			title = getAnalyticsTitle(page).toLowerCase().replaceAll("-", " | ").replaceAll("\\s+", " ");
		}
		if (StringUtils.isNotEmpty(title)) {
			InheritanceValueMap ivm = new HierarchyNodeInheritanceValueMap(page.getContentResource());
			if (ivm != null) {
				String analyticsName = ivm.getInherited("analyticsName", String.class);
				if (StringUtils.isNotEmpty(analyticsName)) {
					title = analyticsName + title;
				}
			}
		}
		return title;
	}


	@Override
	public String getRunMode() {
		Map<String,String> environments = genericListService.getGenericListAsMap(ENVIRONMENTS);
		Set<String> runModes = getRunModes();
		for(Map.Entry<String, String> entry : environments.entrySet()) {
			if(runModes.contains(entry.getValue())) {
				return entry.getValue();
			}
		}		
		return environments.get("dev");
	}
	
	/*
	 * @Override public ValueMap getInheritedContentValueMap(Page page) { if(null !=
	 * page && null != page.getContentResource()) { return new
	 * HierarchyNodeInheritanceValueMap(page.getContentResource()); }
	 * 
	 * return null; }
	 * 
	 * @Override public String getBrand(Page page) { ValueMap vm =
	 * getInheritedContentValueMap(page); if(null != vm) { return vm.get(BRAND,
	 * String.class); } return null; }
	 */
	
	@Override
	public String getContextData(Map<String,String> dataSource, String key, String brandName) {
		if(StringUtils.isNotBlank(key)) {
			String runMode = getRunMode();
			String contextKey = runMode;
			if (StringUtils.isNotEmpty(brandName)) {
				contextKey = contextKey + ":" + brandName;
			}
			contextKey = contextKey + ":" + key;
			String value = dataSource.get(contextKey);
			if(StringUtils.isNotBlank(value)) {
				return value;
			} else {
				return dataSource.get(key);
			}
		}		
		return null;
	}
	
	@Override
	public String getSeoTitle(Page page) {
		ValueMap vm = page.getContentResource().getValueMap();
		String seoTitle = vm.get("seoTitle", String.class);
		if(StringUtils.isNotBlank(seoTitle)) {
			return seoTitle;
		} 
		return this.getPageTitle(page);
	}
}
