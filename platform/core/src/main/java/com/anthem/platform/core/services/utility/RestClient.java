package com.anthem.platform.core.services.utility;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.BaseRequest;
import org.apache.http.MethodNotSupportedException;

import java.io.IOException;
import java.util.Map;

/**
 * The Interface RestClient.
 */
public interface RestClient {

    /**
     * Send request with body string.
     *
     * @param request   the request
     * @param headerMap the header map
     * @return the string
     * @throws IOException
     * @throws MethodNotSupportedException
     */
    APIResponse sendRequest(final BaseRequest request, final Map<String, String> headerMap) throws IOException, MethodNotSupportedException;

}
