package com.anthem.platform.core.services.api;

/**
 * The interface Anthem connector config service.
 *
 * @author aswaroop
 */
public interface AnthemConnectorConfigService {

    /**
     * Gets connection time out.
     *
     * @return the connection time out
     */
    public int getConnectionTimeOut();

    /**
     * Gets socket time out.
     *
     * @return the socket time out
     */
    public int getSocketTimeOut();

    /**
     * Gets request time out.
     *
     * @return the request time out
     */
    public int getRequestTimeOut();
}
