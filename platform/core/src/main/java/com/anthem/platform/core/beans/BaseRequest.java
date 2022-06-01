package com.anthem.platform.core.beans;

import java.util.UUID;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Base request.
 *
 * @author aswaroop
 */
public class BaseRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRequest.class);

    private String url;
    private HttpMethodType methodtype;
    private String data;
    private String requestId;

    

	/**
     * Instantiates a new Base request.
     *
     * @param url    the url
     * @param method the method
     */
    public BaseRequest(final String url, final HttpMethodType method) {
        this.url = url;
        this.methodtype = method;
        this.requestId = UUID.randomUUID().toString();
    }

    /**
     * Instantiates a new Base request.
     *
     * @param url    the url
     * @param method the method
     * @param data   the data
     */
    public BaseRequest(final String url, final HttpMethodType method, final String data) {
        this.url = url;
        this.methodtype = method;
        this.data = data;
        this.requestId = UUID.randomUUID().toString();
    }
    
    
    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Gets method type.
     *
     * @return the method type
     */
    public HttpMethodType gethttpMethodType() {
        return methodtype;
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public HttpMethod getMethod() {
        switch (methodtype) {
            case GET:
                return caseGet();
            case PUT:
                return new PutMethod(url);
            case POST:
                return new PostMethod(url);
            case DELETE:
                return new DeleteMethod(url);
            default:
                return new GetMethod(url);
        }
    }
    
	private HttpMethod caseGet() {
		try {
			String finalUrl = url+"?"+data;
			return new GetMethod(new URI(finalUrl, false).toString());
		} catch (URIException e) {
			LOGGER.error("URIException", e);
		}
		return null;
	}

	public String getRequestId() {
		return requestId;
	}
	
}
