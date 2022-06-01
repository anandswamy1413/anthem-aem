package com.anthem.platform.core.servlets;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.services.api.ApiService;
import com.anthem.platform.core.services.api.impl.AkamaiPurgeServiceImpl;
import com.anthem.platform.core.services.utility.JacksonMapperService;

@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = "services/anthem/akamaipurge", methods = HttpConstants.METHOD_POST, extensions = "json")
@ServiceDescription("Akamai cache purge servlet")

public class AkamaiPurgeServlet extends SlingAllMethodsServlet implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(AkamaiPurgeServlet.class);

	@Reference(target = "(apiId=" + AkamaiPurgeServiceImpl.API_ID + ")")
	private transient ApiService<String, String> akamaiPurgeApi;

	@Reference
	private transient JacksonMapperService jacksonMapper;

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws IOException {
		resp.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		String resultJson = StringUtils.EMPTY;
		try {
			String[] contentPaths = req.getParameterValues("obj");
			Set<String> objects = new HashSet<>();
			Arrays.stream(contentPaths).filter(item -> !item.isEmpty())
					.forEach(e -> objects.add(jacksonMapper.sanitizedHtmlString(e)));
			String purgeType = jacksonMapper.sanitizedHtmlString(req.getParameter("objType"));
			Map<String, Object> urls = new HashMap<>();
			urls.put("objects", objects);
			resultJson = akamaiPurgeApi.getResults(purgeType, urls);
			resp.getWriter().write(resultJson);
		} catch (ApiException e) {
			LOG.error("Exception purging akamai flush ", e);
		}
	}
}