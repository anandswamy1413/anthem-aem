package com.anthem.platform.core.models;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, resourceType = AnthemPageModel.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class PlanZipcodeWidgetPairModel {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected static final String EMPTY_JSON_OBJECT = "[]";

	protected static final String CONTENT_FRAGMENT_URL = "/_jcr_content/data/master.json";

	@ChildResource
	private List<Resource> planZipcode;

	@ChildResource
	private List<Resource> medicareZipcodeApi;

	private List<PlanZipcodeWidgetModel> planZipcodeWidgetList = new ArrayList<>();

	private List<DeepLinks> medicareZipcodeApiParamList = new ArrayList<>();

	private String medicareZipcodeApiJSON;

	@Inject @Required
	private ResourceResolver resourceResolver;

	@Inject
	Page currentPage;

	@Inject
	JacksonMapperService jacksonMapper;

	@PostConstruct
	protected void init() {
		logger.info("Plan Zipcode model invoked");

		if (planZipcode != null && !planZipcode.isEmpty()) {
			for (Resource resources : planZipcode) {
				PlanZipcodeWidgetModel planZipcodeWidgetModel = resources.adaptTo(PlanZipcodeWidgetModel.class);

				if (planZipcodeWidgetModel != null &&
					StringUtils.isNoneEmpty(planZipcodeWidgetModel.getBrandCFUrl())) {
					planZipcodeWidgetModel.setBrandCFUrl(planZipcodeWidgetModel.getBrandCFUrl() +
							CONTENT_FRAGMENT_URL);
				}
				
				if (planZipcodeWidgetModel != null  && planZipcodeWidgetModel.getLobType().equalsIgnoreCase("hbla")) {
					planZipcodeWidgetModel.setLobType("medicareormedicaid");
				}
				
				planZipcodeWidgetList.add(planZipcodeWidgetModel);
			}
		}

		if (CollectionUtils.isEmpty(planZipcodeWidgetList) && currentPage != null) {
			getPlanZipcodeCFPath(currentPage.getPath());
		}

		if (medicareZipcodeApi != null && !medicareZipcodeApi.isEmpty()) {
			for (Resource resources : medicareZipcodeApi) {
				DeepLinks deepLinksModel = resources.adaptTo(DeepLinks.class);
				medicareZipcodeApiParamList.add(deepLinksModel);
			}
			if (CollectionUtils.isNotEmpty(medicareZipcodeApiParamList)) {
				this.medicareZipcodeApiJSON = jacksonMapper.convertObjectToJson(this.medicareZipcodeApiParamList, true);
			}
		}
	}

	public List<PlanZipcodeWidgetModel> getPlanZipcodeCFPath(String pagePath) {
		List<PlanZipcodeWidgetModel> fragPathList = new ArrayList<>();
		long count = pagePath.chars().filter(ch -> ch == '/').count();
		if (count > 2) {
			String path = pagePath  + PlatformConstants.FORWARD_SLASH + JcrConstants.JCR_CONTENT;
			Resource resource = resourceResolver.getResource(path);
			if (resource != null) {
				Resource planZipcodeResource = resource.getChild("planZipcode");
				if (planZipcodeResource != null) {
					for (Resource resources : planZipcodeResource.getChildren()) {
						PlanZipcodeWidgetModel planZipcodeWidgetModel = resources.adaptTo(PlanZipcodeWidgetModel.class);

						if (planZipcodeWidgetModel != null &&
								StringUtils.isNoneEmpty(planZipcodeWidgetModel.getBrandCFUrl())) {
							planZipcodeWidgetModel.setBrandCFUrl(planZipcodeWidgetModel.getBrandCFUrl() +
									CONTENT_FRAGMENT_URL);
						}

						fragPathList.add(planZipcodeWidgetModel);
					}
				}
				if(CollectionUtils.isEmpty(planZipcodeWidgetList)) {
					getPlanZipcodeCFPath(pagePath.substring(0,
							pagePath.lastIndexOf(PlatformConstants.FORWARD_SLASH)));
				}
			}
		}

		if (this.planZipcodeWidgetList.size() == 0) {
			this.planZipcodeWidgetList = fragPathList;
			return new ArrayList<>(this.planZipcodeWidgetList);
		} else {
			return new ArrayList<>(this.planZipcodeWidgetList);
		}
	}

	@JsonIgnore
	public String getJson() {
		if (CollectionUtils.isNotEmpty(this.planZipcodeWidgetList)) {
			return jacksonMapper.convertObjectToJson(this.planZipcodeWidgetList, true);
		}
		return EMPTY_JSON_OBJECT;
	}

	public List<Resource> getPlanZipcode() {
		return new  ArrayList<> (planZipcode);
	}

	public List<PlanZipcodeWidgetModel> getPlanZipcodeWidgetList() {
		return new ArrayList<> (planZipcodeWidgetList);
	}

	public String getMedicareZipcodeApiJSON() {
		return medicareZipcodeApiJSON;
	}

	public List<Resource> getMedicareZipcodeApi() {
		return new  ArrayList<> (medicareZipcodeApi);
	}

	public List<DeepLinks> getMedicareZipcodeApiParamList() {
		return new  ArrayList<> (medicareZipcodeApiParamList);
	}

}
