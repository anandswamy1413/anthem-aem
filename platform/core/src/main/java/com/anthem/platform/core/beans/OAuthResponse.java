package com.anthem.platform.core.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type O auth response.
 *
 * @author aswaroop
 */
public class OAuthResponse extends APIResponse {

	@JsonProperty("access_token")
    private String accessToken;

	@JsonProperty("token_type")
    private String tokenType;

	@JsonProperty("expires_in")
    private int expiresIn;

	@JsonProperty("scope")
    private String scope;

	@JsonProperty("soap_instance_url")
    private String soapInstanceUrl;

	@JsonProperty("rest_instance_url")
    private String restInstanceUrl;

	@JsonProperty("issued_at")
    private String issuedAt;

	@JsonProperty("application_name")
    private String applicationName;

	@JsonProperty("status")
    private String status;

	@JsonProperty("client_id")
    private String clientId;

    /**
     * Gets access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets access token.
     *
     * @param access_token the access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Gets token type.
     *
     * @return the token type
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Sets token type.
     *
     * @param token_type the token type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Gets expires in.
     *
     * @return the expires in
     */
    public int getExpiresIn() {
        return expiresIn;
    }

    /**
     * Sets expires in.
     *
     * @param expires_in the expires in
     */
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Gets scope.
     *
     * @return the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets scope.
     *
     * @param scope the scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Gets soap instance url.
     *
     * @return the soap instance url
     */
    public String getSoapInstanceUrl() {
        return soapInstanceUrl;
    }

    /**
     * Sets soap instance url.
     *
     * @param soap_instance_url the soap instance url
     */
    public void setSoapInstanceUrl(String soapInstanceUrl) {
        this.soapInstanceUrl = soapInstanceUrl;
    }

    /**
     * Gets rest instance url.
     *
     * @return the rest instance url
     */
    public String getRestInstanceUrl() {
        return restInstanceUrl;
    }

    /**
     * Sets rest instance url.
     *
     * @param rest_instance_url the rest instance url
     */
    public void setRestInstanceUrl(String restInstanceUrl) {
        this.restInstanceUrl = restInstanceUrl;
    }

    /**
     * Gets issued at.
     *
     * @return the issued at
     */
    public String getIssuedAt() {
        return issuedAt;
    }

    /**
     * Sets issued at.
     *
     * @param issued_at the issued at
     */
    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    /**
     * Gets application name.
     *
     * @return the application name
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets application name.
     *
     * @param application_name the application name
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets client id.
     *
     * @param client_id the client id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
