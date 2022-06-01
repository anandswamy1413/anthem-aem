package com.anthem.ems.core.validation;

import org.apache.sling.api.SlingHttpServletRequest;

public interface ValidatorService {

    boolean validateData(String[] requestSelectors, SlingHttpServletRequest httpRequest);
}
