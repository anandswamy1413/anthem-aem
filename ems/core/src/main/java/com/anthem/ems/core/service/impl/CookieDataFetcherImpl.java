package com.anthem.ems.core.service.impl;

import com.anthem.ems.core.service.CookieDataFetcher;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

import javax.servlet.http.Cookie;
import java.util.stream.Stream;

@Component(service = CookieDataFetcher.class)
public class CookieDataFetcherImpl implements CookieDataFetcher {

    @Override
    public String fetchCookieDataFrom(SlingHttpServletRequest httpRequest, String cookieName) {
        Cookie[] cookies = httpRequest.getCookies();
        return Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
