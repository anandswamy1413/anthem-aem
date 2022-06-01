package com.anthem.platform.core.services.api.impl;

import com.anthem.platform.core.services.api.AnthemConnectorConfigService;
import com.anthem.platform.core.services.endpoints.configs.ApigeeEndpointConfig;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * service for getting the Anthem Connector Config.
 *
 * @author : aswaroop.
 */
@Component(immediate = true, service = AnthemConnectorConfigService.class)
public class AnthemConnectorConfigServiceImpl implements AnthemConnectorConfigService {

	private int connectionTimeOut;

    private int socketTimeOut;

    private int requestTimeOut;

    /**
     * Activate.
     *
     * @param config the config
     */
    @Activate
    @Modified
    protected final void activate(ApigeeEndpointConfig config){

        this.connectionTimeOut = config.connectionTimeOut();
        this.socketTimeOut = config.socketTimeOut();
        this.requestTimeOut = config.requestTimeOut();
    }

    @Override
    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    @Override
    public int getSocketTimeOut() {
        return socketTimeOut;
    }

    @Override
    public int getRequestTimeOut() {
        return requestTimeOut;
    }

}
