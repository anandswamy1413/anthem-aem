package com.anthem.platform.core.servlets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.services.api.ApiService;
import com.anthem.platform.core.services.utility.JacksonMapperService;

@ExtendWith(MockitoExtension.class)
class AkamaiPurgeServletTest {

	@Mock
	private SlingHttpServletRequest request;

	@Mock
	private SlingHttpServletResponse response;

	@InjectMocks
	AkamaiPurgeServlet replicationServlet;

	StringWriter stringWriter = new StringWriter();
	PrintWriter writer = new PrintWriter(stringWriter, false);

	@Mock
	private Logger logger;

	@Mock
	private ApiService<String, String> akamaiPurgeApi;

	String[] contentPaths = new String[3];

	Set<String> objects = new HashSet<>();

	Map<String, Object> urls = new HashMap<>();
	
	@Mock
	JacksonMapperService jacksonMapper;

	@BeforeEach
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		contentPaths[0] = "obj";
		contentPaths[1] = "obj1";
		contentPaths[2] = "obj2";
		for (String contentPath : contentPaths) {
			when(jacksonMapper.sanitizedHtmlString(contentPath)).thenReturn("obj");
			objects.add("obj");
		}
		urls.put("objects", objects);
	}

	@Test
	void akamaiFlush() throws IOException, ApiException {
		assertNotNull(replicationServlet);
		when(request.getParameterValues("obj")).thenReturn(contentPaths);
		when(jacksonMapper.sanitizedHtmlString(request.getParameter("objType"))).thenReturn("purgeType");
		when(response.getWriter()).thenReturn(writer);
		when(akamaiPurgeApi.getResults("purgeType", urls)).thenReturn("resultJson");
		replicationServlet.doPost(request, response);
		verify(response).setContentType(ContentType.APPLICATION_JSON.getMimeType());
	}

}
