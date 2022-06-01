package com.anthem.platform.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.genericlists.GenericList;
import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.PostBackResponse;
import com.day.cq.dam.api.Asset;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(immediate=true, service = PlatformHelper.class)
public class PlatformHelper {	
	
	private static final Logger LOG = LoggerFactory.getLogger(PlatformHelper.class);
	
	public PostBackResponse getDefaultSuccessPostBackResponse() {
		return new PostBackResponse(PostBackResponse.Status.SUCCESS, 200);
	}
	
	public PostBackResponse getSuccessPostBackResponse(int status, Map<String,Object> responseMap) {
		PostBackResponse pbResponse = new PostBackResponse(PostBackResponse.Status.SUCCESS, status);
		pbResponse.setSuccessData(responseMap);
		return pbResponse;
	}
	
	public PostBackResponse getErrorResponseFromApiException(int status, ApiException apiException) {
		PostBackResponse pbResponse =  new PostBackResponse(PostBackResponse.Status.ERROR, status);
		pbResponse.addErrorCode(apiException.getErrorCode()).addErrorMsg(apiException.getErrorMsg());
		return pbResponse;
	}
	
		
	public InputStream getFileInputStream(String filePath, ResourceResolver resourceResolver) {
		Resource fileResource = resourceResolver.getResource(filePath);
		if (fileResource != null) {
			LOG.info("fileResource {}", fileResource.getName());
			Asset asset = fileResource.adaptTo(Asset.class);
			Resource original = asset.getOriginal();
			return original.adaptTo(InputStream.class);
		}
		return null;
	}
	
	public void setSoapLogger(BindingProvider bindingProvider) {
		Binding binding = bindingProvider.getBinding();
		@SuppressWarnings("squid:S3740")
		List<Handler> handlerChain = binding.getHandlerChain();
		handlerChain.add(new SoapLogHandler());
		binding.setHandlerChain(handlerChain);
	}
	
	 /**
     * Log method time.
     *
     * @param lStartTime the l start time
     * @return the long
     */
    public long logMethodTime(long lStartTime) {
        return (System.nanoTime() - lStartTime) / 1000000;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public long getStartTime() {
        return System.nanoTime();
    }
    	
	public void replicateContent(String path, Replicator replicator, ResourceResolver resourceResolver) {
		try {
			LOG.info("In replicateConent");
			ReplicationOptions options = new ReplicationOptions();
			options.setSuppressVersions(true);
			options.setSynchronous(true);
			options.setSuppressStatusUpdate(false);
			LOG.info("[replicateContent] About to replicate: {}", path);
			Session session = resourceResolver.adaptTo(Session.class);
			replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
			LOG.info("[replicateContent] Replicated: {}", path);
		} catch (ReplicationException e) {
			LOG.error("Repository Exception ", e);
		}
	}
	
	public void deleteOldNodes(ResourceResolver resourceResolver, String jsonPath, String nodeType) {
		try{
			Session session = resourceResolver.adaptTo(Session.class);
			if (session.itemExists(jsonPath)) {
				session.removeItem(jsonPath);
				session.save();
				LOG.info("removing {}", jsonPath);
			}
			String[] pathArray = jsonPath.split("/");
			StringBuilder newPath = new StringBuilder();
			for (int i = 1; i < pathArray.length - 1; i++) {
				newPath.append("/");
				newPath.append(pathArray[i]);
			}
			Node jcrNode = session.getNode(newPath.toString());
			Node jsonNode = jcrNode.addNode(pathArray[pathArray.length - 1], nodeType);
			jsonNode.getSession().save();
		} catch (RepositoryException e) {
			LOG.error("Repository Exception ", e);
		}
	}

	/**
	 *
	 * @param resourceResolver
	 * @param genericlistPagePath
	 * @param payloadPath
	 * @return csvFileType
	 */
	public String getCSVFileType(ResourceResolver resourceResolver, String genericlistPagePath, String payloadPath) {
		String csvFileType ;

		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page listPage = pageManager.getPage(genericlistPagePath);
		GenericList list = listPage.adaptTo(GenericList.class);
		csvFileType = list.lookupTitle(payloadPath);

		return csvFileType;
	}
	
	
	
	public String getLeadID() {
		SecureRandom random = new SecureRandom();
		return String.format("AEM%09d", random.nextInt(1000000000));	
	}
	
	public final APIResponse getResponse(HttpResponse httpResponse) throws IOException {
        APIResponse response = new APIResponse();
        String responseData = StringUtils.EMPTY;
        if (httpResponse != null) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            LOG.info("Response status code is {}", statusCode);
            response.setStatusCode(statusCode);
            if (statusCode != HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    && statusCode != HttpServletResponse.SC_NOT_FOUND && null != httpResponse.getEntity()) {
                responseData = EntityUtils.toString(httpResponse.getEntity());
            }
        }
        response.setResponse(responseData);
        return response;
    }
	
	public void setResponseFromAPI(HttpServletResponse response, APIResponse apiResponse, boolean disableCache) throws IOException {
		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		response.setHeader("Strict-Transport-Security", "max-age=31622400; includeSubDomains");
		if(disableCache) {
			response.setHeader("Dispatcher", "no-cache");
		}
		response.setStatus(apiResponse.getStatusCode());
		response.getWriter().write(apiResponse.getResponse());
	}

}
