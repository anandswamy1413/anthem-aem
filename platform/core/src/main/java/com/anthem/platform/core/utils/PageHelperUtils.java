package com.anthem.platform.core.utils;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.constants.PlatformConstants;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;

@Component(immediate = true, service = PageHelperUtils.class)
public class PageHelperUtils {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	QueryBuilder builder;

	public String getPageProperty(String pagePath, String property) {
		Session session = null;
		try (ResourceResolver resourceResolver = resourceResolverFactory
				.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
			session = resourceResolver.adaptTo(Session.class);
			Node varNode = session.getNode(pagePath);
			if (varNode.hasProperty(property))
				return varNode.getProperty(property).getString();
			else
				return StringUtils.EMPTY;
		} catch (LoginException | RepositoryException e) {
			log.error("Exception while getting page property {}", e);
		} finally {
			if (null != session && session.isLive())
				session.logout();
		}
		return StringUtils.EMPTY;
	}

	public long getChildNodesOfType(String path, String type) {
		Session session = null;
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put(PlatformConstants.PATH, path);
		queryMap.put(PlatformConstants.PATH_FLAT, PlatformConstants.TRUE);
		queryMap.put(PlatformConstants.TYPE, type);
		queryMap.put(PlatformConstants.P_LIMIT, PlatformConstants.MINUS_ONE);
		try (ResourceResolver resourceResolver = resourceResolverFactory
				.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
			session = resourceResolver.adaptTo(Session.class);
			Query query = builder.createQuery(PredicateGroup.create(queryMap), session);
			return query.getResult().getHits().size();
		} catch (LoginException e) {
			log.error("Exception while getting child node types {}", e);
		} finally {
			if (null != session && session.isLive())
				session.logout();
		}
		return 0;
	}

	public String getJcrPrimaryType(String path) {
		try (ResourceResolver resourceResolver = resourceResolverFactory
				.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
			Resource resource = resourceResolver.getResource(path);
			return resource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class);
		} catch (LoginException e) {
			log.error("Exception while jcr primary typr {}", e);}
		return StringUtils.EMPTY;
	}

}
