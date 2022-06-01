package com.anthem.platform.core.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.constants.PlatformConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

class PageHelperUtilsTest {

	@Mock
	private ResourceResolverFactory resourceResolverFactory;

	@Mock
	QueryBuilder builder;

	@Mock
	Session session;
	
	@Mock
	ResourceResolver resourceResolver;

	@Mock
	Query query;
	
	@Mock
	SearchResult searchRes;

	@Mock
	Hit hit;
	
	@Mock
	Node varNode;
	
	@Mock
	Property varProp;
	
	
	@InjectMocks
	PageHelperUtils pageHelper;
	
	List<Hit> resHits = new ArrayList<>();

	@BeforeEach
	void setUp() throws LoginException, RepositoryException {
		MockitoAnnotations.initMocks(this);
		when(resourceResolverFactory.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO))
				.thenReturn(resourceResolver);
		when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
		when(builder.createQuery(any(PredicateGroup.class), any(Session.class))).thenReturn(query);
		when(query.getResult()).thenReturn(searchRes);
		resHits.add(hit);
		when(searchRes.getHits()).thenReturn(resHits);
		when(resHits.get(0).getPath()).thenReturn("resultPath");
	}

	@Test
	void testGetPageProperty() throws RepositoryException {
		when(varNode.hasProperty("property")).thenReturn(true);
	//	when(varNode.getProperty("property")).thenReturn()
		when(session.isLive()).thenReturn(true);
		//pageHelper.getPageProperty("path", "type");
	}

}
