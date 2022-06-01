package com.anthem.platform.core.utils;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.jcr.RepositoryException;
import javax.script.Bindings;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RteHtmlFilterTest {

	@Mock
	Document doc;

	@Mock
	Resource resource;


	@InjectMocks
	RteHtmlFilter rteHtmlFilter;

	private static final String RICHTEXT = "richtext";
	
	@Mock
	Elements links;
	
	@Mock
	Element link;

	@BeforeEach
	void setUp() throws LoginException, RepositoryException {
		MockitoAnnotations.initMocks(this);
		when(doc.select("a")).thenReturn(links);
	}

	@Test
	void testGetAccessAPI() throws Exception {
		Class[] parameterTypes = new Class[1];
		parameterTypes[0] = Document.class;
		Object[] parameters = new Object[1];
		parameters[0] = doc;
		Method method = RteHtmlFilter.class.getDeclaredMethod("linkFilter", parameterTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(rteHtmlFilter, parameters);
		assertNotNull(rteHtmlFilter);
	}

	@Test
	void testGetPageProperty() throws Exception {
		Bindings bindings = mock(Bindings.class);
		when(bindings.get("richtext")).thenReturn("hello");
		RteHtmlFilter rteHtmlFilter = new RteHtmlFilter();
		rteHtmlFilter.init(bindings);
	}

}
