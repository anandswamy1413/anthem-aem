package com.anthem.ems.core.filters;

import static org.apache.sling.engine.EngineConstants.FILTER_SCOPE_REQUEST;
import static org.apache.sling.engine.EngineConstants.SLING_FILTER_EXTENSIONS;
import static org.apache.sling.engine.EngineConstants.SLING_FILTER_PATTERN;
import static org.apache.sling.engine.EngineConstants.SLING_FILTER_SCOPE;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Filter.class, name = "ExperienceFragment Fetcher Filter", property = {
		SLING_FILTER_SCOPE + "=" + FILTER_SCOPE_REQUEST, Constants.SERVICE_RANKING + ":Integer=1",
		SLING_FILTER_PATTERN + "=/ems/.*",
		SLING_FILTER_EXTENSIONS + "=html",
		})
public class AnthemMemberPlanSelectorFilter implements Filter {

	private static final String REQ_PATTERN = "/ems/([A-Za-z]{2})/(.*)/(.*).html";
	
	private static final Logger LOG = LoggerFactory.getLogger(AnthemMemberPlanSelectorFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof SlingHttpServletRequest && response instanceof SlingHttpServletResponse) {

			final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
			
			String path = slingRequest.getResource().getPath();
			LOG.debug("In filter, Request resource path : {}", path);
			Pattern pattern = Pattern.compile(REQ_PATTERN);
			Matcher matcher = pattern.matcher(path);
			
			StringBuilder builder = new StringBuilder("/content/ems/api/pages.");
			while (matcher.find()) {
				builder.append(matcher.group(1))
				.append('.')
				.append(matcher.group(2))
				.append('.')
				.append(matcher.group(3))
				.append(".html");
			}
			slingRequest.getRequestDispatcher(builder.toString()).forward(request, response);
	        return;

		} else {
			chain.doFilter(request, response);
			return;
		}
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//nothing to implement
		
	}

	@Override
	public void destroy() {
		//nothing to implement
		
	}


}
