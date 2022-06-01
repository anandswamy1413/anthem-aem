package com.anthem.platform.core.services.api;

import java.io.IOException;

/**
 * The interface Generate access token service.
 *
 * @author aswaroop
 */
public interface GenerateAccessTokenService {
    /**
     * Generate new token string.
     *
     * @return the string
     * @throws IOException the io exception
     */
    String generateNewToken() throws IOException;

    /**
     * Gets token.
     *
     * @return the token
     */
    String getToken();
}
