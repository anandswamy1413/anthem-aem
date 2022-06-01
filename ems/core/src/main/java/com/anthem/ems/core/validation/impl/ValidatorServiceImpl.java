package com.anthem.ems.core.validation.impl;

import com.anthem.ems.core.beans.ProfileApiResponse;
import com.anthem.ems.core.service.CookieDataFetcher;
import com.anthem.ems.core.validation.ValidatorService;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ValidatorService.class)
public class ValidatorServiceImpl implements ValidatorService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorServiceImpl.class);
    protected static final String GROUP_PROFILE_COOKIE_NAME = "group-profile";

    @Reference
    private JacksonMapperService jacksonMapperService;

    @Reference
    private CookieDataFetcher cookieDataFetcher;

    @Override
    public boolean validateData(String[] requestSelectors, SlingHttpServletRequest httpRequest) {

        boolean skipGroupProfileApiCallFlag = false;
        String groupInfoParameter = requestSelectors[1];
        String cookieData = cookieDataFetcher.fetchCookieDataFrom(httpRequest, GROUP_PROFILE_COOKIE_NAME);
        if (null != cookieData) {
            ProfileApiResponse profileApiResponse = new ProfileApiResponse();
            profileApiResponse = jacksonMapperService.convertJsonToObject(cookieData, ProfileApiResponse.class);
            if (null != profileApiResponse) {
                String groupId = profileApiResponse.groupProfile.get(0).groupNumber;
                String micrositeName = profileApiResponse.groupProfile.get(0).micrositeName;
                if (groupId.equalsIgnoreCase(groupInfoParameter) || micrositeName.equalsIgnoreCase(groupInfoParameter)) {
                    skipGroupProfileApiCallFlag = true;
                }
            }
        }
        return skipGroupProfileApiCallFlag;
    }
}
