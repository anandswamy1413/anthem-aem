package com.anthem.ems.core.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.MethodNotSupportedException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.ems.core.beans.GroupProfile;
import com.anthem.ems.core.beans.ProfileApiResponse;
import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.HttpMethodType;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.services.utility.RestClient;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.common.net.HttpHeaders;

@Component(service = Servlet.class, immediate = true, property = { "sling.servlet.paths=" + "/ems/pages",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=" + "html" })
@ServiceDescription("Service to search for the relavant XF based on request URL")
public class ExperienceFragmentFetcher extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ExperienceFragmentFetcher.class);

	@Reference
	private RestClient restClient;

	@Reference
	private JacksonMapperService jacksonMapperService;

	@Reference
    private QueryBuilder queryBuilder;
	
	@Reference
	private transient XSSAPI xssAPI;

	protected static final String FETCH_GROUP_PROFILE_ENDPOINT = "https://www.joinanthembenefits.com/ems/public/api/ems/group/groupprofiles/";
	protected static final String ERROR_PAGE_PATH = "/content/www/us/en/home/errors.html";
	protected static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36";
	protected static final String SUCCESS = "SUCCESS";
	protected static final String FAILURE = "No Records Found";

	protected static final String VARIATIONPATH = "/content/experience-fragments/ems/%s/%s/%s";
	protected static final String GROUP_PROFILE_COOKIE_NAME = "group-profile";

	private List<String> specifics = Collections.unmodifiableList(Arrays.asList("group-number", "brand", "segment", "state"));

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException, ServletException {
		LOG.trace("-------- ExperienceFragmentFetcher Starts ---------- ");
		final SlingHttpServletRequest syntheticRequest = new SlingHttpServletRequestWrapper(request);
		final RequestDispatcherOptions options = new RequestDispatcherOptions();
		options.setReplaceSelectors("");
		options.setForceResourceType("cq/Page");

		String[] requestSelectors = validateAndFetchRequestSelectors(request);
		if (null != requestSelectors) {
			String groupProfileData =null;
			boolean skipGroupProfileApiCall = validateCookieData(requestSelectors,request);
			if(skipGroupProfileApiCall) {
				LOG.trace("Skipping group profile API Invocation .....");
				groupProfileData = fetchCookieDataFrom(request, GROUP_PROFILE_COOKIE_NAME);
			}else {
				LOG.trace("Client requested for a different groupnumber/microstiename ,hence invoking group profile API  .....");
				groupProfileData = fetchGroupProfile(requestSelectors[0]);
			}
	
			String pageName = requestSelectors[1];
			LOG.trace("Group Profile REST API Response  --> {}", groupProfileData);

			ProfileApiResponse profileApiResponse = new ProfileApiResponse();
			profileApiResponse = jacksonMapperService.convertJsonToObject(groupProfileData, ProfileApiResponse.class);

			LOG.trace("\n\nConverted Object API Response {}", profileApiResponse.toString());

			String apiResponseStatus = profileApiResponse.status;
			if (apiResponseStatus.equals(SUCCESS)) {
				if (!skipGroupProfileApiCall) {
					StringBuilder cookieString = new StringBuilder(GROUP_PROFILE_COOKIE_NAME + "=" + groupProfileData);
					response.addHeader("Set-Cookie", cookieString.toString());
					LOG.trace("Setting a session cookie  --> {}", cookieString.toString());
				}

				GroupProfile groupProfile = profileApiResponse.groupProfile.get(0);

				String brand = groupProfile.brand;
				String segment = groupProfile.marketSegment;
				String stateCode = groupProfile.stateCode;
				String groupId = groupProfile.groupNumber;

				LOG.trace("Market Segment {}", segment);
				LOG.trace("Brand {}", brand);
				LOG.trace(" State Code {}", stateCode);
				LOG.trace("Group Id {}", groupId);
				LOG.trace("Page Requested {}", pageName);

				String xfPath = fetchXFPathFor(request.getResourceResolver(), stateCode, brand, groupId, segment,pageName);
				if (null != xfPath && !xfPath.isEmpty()) {
					request.getRequestDispatcher(xfPath, options).forward(syntheticRequest, response);
				} else {
					request.getRequestDispatcher(ERROR_PAGE_PATH, options).forward(syntheticRequest, response);
				}
			} else {
				request.getRequestDispatcher(ERROR_PAGE_PATH, options).forward(syntheticRequest, response);
			}
		} else {
			request.getRequestDispatcher(ERROR_PAGE_PATH, options).forward(syntheticRequest, response);
		}
		LOG.trace("-------- ExperienceFragmentFetcher Ends ---------- ");
	}

	private String[] validateAndFetchRequestSelectors(SlingHttpServletRequest request) {

		String[] selectorArr = request.getRequestPathInfo().getSelectors();
		if (selectorArr.length > 1 && selectorArr.length <= 2) {
			LOG.trace("Obtained groupId as {}", selectorArr[0]);
			LOG.trace("Obtained page as {}", selectorArr[1]);
			return selectorArr;
		}
		return null;
	}

	private boolean validateCookieData(String[] requestSelectors, SlingHttpServletRequest httpRequest) {
		boolean skipGroupProfileApiCallFlag = false;
		String groupInfoParameter = requestSelectors[0];

		String cookieData = fetchCookieDataFrom(httpRequest, GROUP_PROFILE_COOKIE_NAME);
		if (null != cookieData) {
			ProfileApiResponse profileApiResponse = new ProfileApiResponse();
			profileApiResponse = jacksonMapperService.convertJsonToObject(cookieData, ProfileApiResponse.class);
			if (null != profileApiResponse) {
				String groupId = profileApiResponse.groupProfile.get(0).groupNumber;
				String micrositeName = profileApiResponse.groupProfile.get(0).micrositeName;
				if (groupId.equalsIgnoreCase(groupInfoParameter) || micrositeName.equalsIgnoreCase(groupInfoParameter)) {
					skipGroupProfileApiCallFlag = true;
				}
			}
		}
		return skipGroupProfileApiCallFlag;
	}
	
	private String fetchCookieDataFrom(SlingHttpServletRequest httpRequest, String cookieName) {

		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					LOG.trace("Obtained cookie for {} ",cookieName);
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	private String fetchGroupProfile(String groupNumber) {
		String resultJson = PlatformConstants.EMPTY_JSON_OBJECT;
		try {
			if (null != groupNumber && !groupNumber.isEmpty()) {
				final String API_ENDPOINT = FETCH_GROUP_PROFILE_ENDPOINT + groupNumber;
				LOG.trace("Invoking {}", API_ENDPOINT);
				BaseRequest request = new BaseRequest(API_ENDPOINT, HttpMethodType.GET);
				Map<String, String> headerMap = new HashMap<>();
				headerMap.put(HttpHeaders.USER_AGENT, USER_AGENT);
				APIResponse response = restClient.sendRequest(request, headerMap);
				int status = response.getStatusCode();
				if (status == 200) {
					resultJson = xssAPI.getValidJSON(response.getResponse(), PlatformConstants.EMPTY_JSON_OBJECT);
				}
			}
		} catch (IOException e) {
			LOG.error("IOException found in while fetching group profile", e);
		} catch (MethodNotSupportedException e) {
			LOG.error("MethodNotSupportedException found while fetching group profile", e);
		}
		return resultJson;
	}

	private String fetchXFPathFor(ResourceResolver resolver, String state, String brand, String groupNumber, String segment, String pageName) {
		Session session = resolver.adaptTo(Session.class);
		String path = "";
		String xfPath = "";
		for(int i = 0;i < specifics.size(); i++) {
			switch(specifics.get(i)) {
			case "group-number":  
				path = String.format(VARIATIONPATH, specifics.get(i), pageName, JcrUtil.createValidName(groupNumber)); 
				xfPath = getXFResourcePath(resolver, session, path, specifics.get(i), pageName, groupNumber);
				if(StringUtils.isNotEmpty(xfPath))
					return xfPath;
				break;
			case "brand": 
				path = String.format(VARIATIONPATH, specifics.get(i), pageName, JcrUtil.createValidName(brand)); 
				xfPath = getXFResourcePath(resolver, session, path, specifics.get(i), pageName, brand);
				if(StringUtils.isNotEmpty(xfPath))
					return xfPath;
				break;
			case "segment": 
				path = String.format(VARIATIONPATH, specifics.get(i), pageName,  JcrUtil.createValidName(segment)); 
				xfPath = getXFResourcePath(resolver, session, path, specifics.get(i), pageName, segment);
				if(StringUtils.isNotEmpty(xfPath))
					return xfPath;
				break;
			case "state": 
				path = String.format(VARIATIONPATH, specifics.get(i), pageName,  JcrUtil.createValidName(state)); 
				xfPath = getXFResourcePath(resolver, session, path, specifics.get(i), pageName, state);
				if(StringUtils.isNotEmpty(xfPath))
					return xfPath;
				break;
			default: return "";
			}
		}
		
		
		return "";
	}

	private String getXFResourcePath(ResourceResolver resolver, Session session, String path, String specific, String pageName, String xftitle) {
		String resourcePath = "";
		if(resourceExists(resolver, path)) {
			resourcePath = path;
		} else {
			String pagePath = String.format("/content/experience-fragments/ems/%s/%s", specific, pageName);
			String tempResourcePath = getResourceWithTitle(session, pagePath, xftitle);
			if(StringUtils.isNotEmpty(tempResourcePath)) {
				resourcePath = tempResourcePath;
			}
		}
		return resourcePath;
		
	}

	private String getResourceWithTitle(Session session, String pagePath, String jcrTitle) {
		String path = "";
		Map<String, String> predicateMap = new HashMap<>();
		predicateMap.put("type", "cq:Page");
		predicateMap.put("path", pagePath);
		predicateMap.put("1_property", "jcr:content/sling:resourceType");
		predicateMap.put("1_property.value", "ems/components/content/xfpage");
		predicateMap.put("2_property", "jcr:content/jcr:title");
		predicateMap.put("2_property.value", jcrTitle);
		
		Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
		 
		//Get search results
		SearchResult result = query.getResult();
		List<Hit> hits = result.getHits();
		if(!hits.isEmpty()) {
			Hit hit = hits.get(0);
			try {
				 path = hit.getPath();
			} catch (RepositoryException e) {
				LOG.error("Repository exception while querying the xf with title:{}", e.getMessage());
			}
		}
		return path;
	}

	private boolean resourceExists(ResourceResolver resolver, String path) {
		return (StringUtils.isNotEmpty(path) && resolver.getResource(path) != null);
	}
}