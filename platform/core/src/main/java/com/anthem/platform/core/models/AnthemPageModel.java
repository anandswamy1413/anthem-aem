package com.anthem.platform.core.models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.anthem.platform.core.services.utility.GenericListService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import com.adobe.acs.commons.models.injectors.annotation.HierarchicalPageProperty;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.utils.PlatformFileHelper;
import com.anthem.platform.core.utils.PlatformUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, 
resourceType = AnthemPageModel.RESOURCE_TYPE,
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class AnthemPageModel{
	
    protected static final String RESOURCE_TYPE = "anthem/platform/components/structure/page";
    
    protected static final String SCHEMA_PATH = "/content/dam/anthem/jsonld.json";
        
    @Inject @Required
    private ResourceResolver resourceResolver;
    
    @Inject
    private SlingHttpServletRequest request;
    
    @HierarchicalPageProperty("headerFragmentPath")
    private String headerFragmentPath;
    
    @HierarchicalPageProperty("footerFragmentPath")
    private String footerFragmentPath;
    
    @HierarchicalPageProperty("tfn")
	private String tfn;
	
	@ChildResource
	private List<Resource> deepLinkParams;

	@Inject
	private GenericListService genericListService;
	
	@Inject
	private JacksonMapperService jacksonMapper;
	
	@Inject
	private PlatformFileHelper platformFileHelper;

	private List<DeepLinks> deepLinksList = new ArrayList<>();
	private String deepLinks;
	private String[] mailerStateList;
	private List<String> legalDisclaimerCFPaths = new ArrayList<>();
	private Map<String,Object> jsonSchemaMap = new HashMap<>();
	private String jsonSchemaMapJson = "{}";
	
	@Inject
	Resource resource;
	
	@Inject
	Page currentPage;
	
	@ScriptVariable
	Style currentStyle;
	
	@PostConstruct
	protected void init() {

		String headerMasterVariationPath = this.getMasterVariationPath(headerFragmentPath);
		if (StringUtils.isNotBlank(headerMasterVariationPath)) {
			this.headerFragmentPath = headerMasterVariationPath;
		}

		String footerMasterVariationPath = this.getMasterVariationPath(footerFragmentPath);
		if (StringUtils.isNotBlank(footerMasterVariationPath)) {
			this.footerFragmentPath = footerMasterVariationPath;
		}
 

		if (deepLinkParams != null && !deepLinkParams.isEmpty()) {
			for (Resource resources : deepLinkParams) {
				DeepLinks deepLink = resources.adaptTo(DeepLinks.class);
				deepLinksList.add(deepLink);
			}
			deepLinks = jacksonMapper.convertObjectToJson(deepLinksList, false);
			
			
		}
		
		if(currentStyle != null) {
			mailerStateList = currentStyle.get("stateList", String[].class);
		}

		getLegalDisclaimerCFPath(currentPage.getPath());
		
		getJsonldSchema(currentPage);
	}


	public void getJsonldSchema(Page currentPage) {
		InputStream is = platformFileHelper.getFileInputStream(SCHEMA_PATH, resourceResolver);
		Map<String,Object> schemaMap = jacksonMapper.convertJsonToObject(is, Map.class);
		Resource contentResource = currentPage.getContentResource();
		ValueMap vm = contentResource.getValueMap();
		populateJsonMap(schemaMap, jsonSchemaMap, vm,currentPage);
	}
	
	private void populateJsonMap(Map<String,Object> schemaMap, Map<String,Object> jsonSchemaMap , ValueMap vm,Page currentPage) {
		if(MapUtils.isNotEmpty(schemaMap)) {
			for(Map.Entry<String, Object> item : schemaMap.entrySet()) {
				if(item.getValue() instanceof String) {
					String schemaValue = String.valueOf(item.getValue());
					if("url".equalsIgnoreCase(item.getKey())) {
						jsonSchemaMap.put(item.getKey(), PlatformUtils.externalizeUri(currentPage.getPath(), resourceResolver, request,genericListService.getGenericListAsMap(PlatformConstants.INTERNAL_DOMAINS),null));
					}else if(schemaValue.startsWith("_cq(")) {
						String aemKey = StringUtils.substringBetween(String.valueOf(item.getValue()), "_cq(", ")");
						aemKey = "cq:" + aemKey;
						String value = vm.get(aemKey, String.class);
						if(StringUtils.isNotBlank(value)) {
							jsonSchemaMap.put(item.getKey(), value);
						}
					} else if(schemaValue.startsWith("_jcr(")) {
						String aemKey = StringUtils.substringBetween(String.valueOf(item.getValue()), "_jcr(", ")");
						aemKey = "jcr:" + aemKey;
						String value = vm.get(aemKey, String.class);
						if(StringUtils.isNotBlank(value)) {
							jsonSchemaMap.put(item.getKey(), value);
						}						
					} else if(schemaValue.startsWith("(")){
						String aemKey = StringUtils.substringBetween(String.valueOf(item.getValue()), "(", ")");
						String value = vm.get(aemKey, String.class);					
						if(StringUtils.isNotBlank(value)) {
							jsonSchemaMap.put(item.getKey(), value);
						}
					} else {
						jsonSchemaMap.put(item.getKey(), schemaValue);
					}					
				} else if(item.getValue() instanceof Map) {
					populateJsonMap((Map<String,Object>)item.getValue(), jsonSchemaMap, vm,currentPage);
				}
			}
		}
	}


	public List<String> getLegalDisclaimerCFPath(String pagePath) {
		List<String> fragPathList = new ArrayList<>();
		boolean legalDisclaimerResourceFound = false;
		long count = pagePath.chars().filter(ch -> ch == '/').count();
		if (count > 2) {
			String path = pagePath  + PlatformConstants.FORWARD_SLASH + JcrConstants.JCR_CONTENT;
			Resource resource = resourceResolver.getResource(path);
			if (resource != null) {
				Resource legalDisclaimerResource = resource.getChild("legalDisclaimerPaths");
				if (legalDisclaimerResource != null) {
					Iterable<Resource> resourceIterable = legalDisclaimerResource.getChildren();
					Iterator<Resource> resItr = resourceIterable.iterator();
					while(resItr.hasNext()) {
						Resource resItem = resItr.next();
						ValueMap vm = resItem.getValueMap();
						if (vm != null && vm.containsKey("legalDisclaimerFragmentPath")) {
							String expFragPath = vm.get("legalDisclaimerFragmentPath", String.class);
							if (StringUtils.isNotEmpty(expFragPath) && resourceResolver.getResource(expFragPath) != null) {
								fragPathList.add(expFragPath);
							}
						}
					}
					legalDisclaimerResourceFound = true;
				}
				if(legalDisclaimerResource != null && !legalDisclaimerResourceFound) {
					getLegalDisclaimerCFPath(pagePath.substring(0,
						pagePath.lastIndexOf(PlatformConstants.FORWARD_SLASH)));
				}
			}
		}
		if (this.legalDisclaimerCFPaths.size() == 0) {
			this.legalDisclaimerCFPaths = fragPathList;
			return new ArrayList<>(this.legalDisclaimerCFPaths);
		} else {
			return new ArrayList<>(this.legalDisclaimerCFPaths);
		}
	}
	
	private String getMasterVariationPath(String fragmentPath) {
    	if(null != fragmentPath) {
    		Resource fragment = resourceResolver.getResource(fragmentPath);
        	if(null != fragment) {
        		Resource fragmentContent = fragment.getChild(JcrConstants.JCR_CONTENT);
        		if(fragmentContent.isResourceType(com.adobe.cq.xf.ExperienceFragmentsConstants.RT_EXPERIENCE_FRAGMENT_MASTER)) {
        			return getMasterFragmentPath(fragmentContent);
        		}
        	}
    	} 
    	return null;
    }
    
    
    private String getMasterFragmentPath(Resource fragmentContent) {
		Iterable<Resource> children = fragmentContent.getChildren();
		Iterator<Resource> childrenIterator = children.iterator();
		while(childrenIterator.hasNext()) {
			Resource res = childrenIterator.next();
			Resource jcrContent = res.getChild(JcrConstants.JCR_CONTENT);
			if(null != jcrContent) {
				ValueMap valueMap = jcrContent.getValueMap();
				boolean isMasterVariation = valueMap.get(com.adobe.cq.xf.ExperienceFragmentsConstants.PN_XF_MASTER_VARIATION, Boolean.class);
				if(isMasterVariation) {
					return jcrContent.getPath();
				}
			}
		}
		return null;
    }

	public String getHeaderFragmentPath() {
		return headerFragmentPath;
	}

	public String getFooterFragmentPath() {
		return footerFragmentPath;
	}

	public List<DeepLinks> getDeepLinksList() {
		return Collections.unmodifiableList(deepLinksList);
		
	}

	public String getDeepLinks() {
		return deepLinks;
	}
	
	/**
	 * @return the mailerStateList
	 */
	public String[] getMailerStateList() {
		if(null != mailerStateList && mailerStateList.length > 0) {
			return mailerStateList.clone();
		}
		return null;
	}

	public List<String> getLegalDisclaimerCFPaths() {
		return new ArrayList<>(legalDisclaimerCFPaths);
	}


	public Map<String, Object> getJsonSchemaMap() {
		return jsonSchemaMap;
	}


	public String getJsonSchemaMapJson() {
		return jacksonMapper.mapToJson(jsonSchemaMap);
	}
	
	public String getTfnJsonFromRequest() {
		Map<String,Object> outputMap = new HashMap<>();
		if(null != request.getAttribute(PlatformConstants.TFN_SET)) {
			Set<String> tfnSet = (Set<String>)request.getAttribute(PlatformConstants.TFN_SET);
			for(String variationType : tfnSet) {
				Resource resourceContent = resourceResolver.getResource(tfn + PlatformConstants.CF_PATH + variationType);
				if(null != resourceContent) {
					outputMap.put(variationType, jacksonMapper.valueMapToMap(resourceContent.getValueMap(), null));
				}
			}
		}
		
		return jacksonMapper.mapToJson(outputMap);
	}
	
}
