package com.anthem.ems.core.service;

import org.apache.sling.api.SlingHttpServletRequest;

public interface CookieDataFetcher {
    String fetchCookieDataFrom(SlingHttpServletRequest httpRequest, String cookieName);
}
