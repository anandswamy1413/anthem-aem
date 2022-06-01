package com.anthem.ems.core.service.impl;

import com.anthem.ems.core.configs.GroupProfileConfig;
import com.anthem.ems.core.service.GroupProfileFetcherService;
import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.HttpMethodType;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.RestClient;
import com.google.common.net.HttpHeaders;
import org.apache.http.MethodNotSupportedException;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = GroupProfileFetcherService.class)
@Designate(ocd = GroupProfileConfig.class)
public class GroupProfileFetcherServiceImpl implements GroupProfileFetcherService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupProfileFetcherServiceImpl.class);
    protected static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36";
    private String groupProfileEndPoint;

    @Reference
    private RestClient restClient;

    @Reference
    private transient XSSAPI xssAPI;

    @Activate
    public void activate(GroupProfileConfig config) {
        this.groupProfileEndPoint = config.getGroupProfileEndPoint();
    }

    @Override
    public String fetchGroupProfile(String groupNumber) {
        String resultJson = PlatformConstants.EMPTY_JSON_OBJECT;
        try {
            if (null != groupNumber && !groupNumber.isEmpty()) {
                final String API_ENDPOINT = groupProfileEndPoint + groupNumber;
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
}
