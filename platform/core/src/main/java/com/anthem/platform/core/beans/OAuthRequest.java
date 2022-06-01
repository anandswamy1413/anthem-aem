package com.anthem.platform.core.beans;

import org.apache.commons.lang3.StringUtils;

import com.anthem.platform.core.constants.PlatformConstants;
import com.google.gson.annotations.SerializedName;

public class OAuthRequest {

    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("client_secret")
    private String clientSecret;

    @SerializedName("account_id")
    private String accountId;

    public OAuthRequest(String clientId, String clientSecret, String grantType, String accountId) throws ApiException {

        super();
        
        if(StringUtils.isBlank(grantType)) {
        	grantType = PlatformConstants.CLIENT_CREDS;
        }
        
        if(StringUtils.isNoneBlank(clientId,clientSecret,grantType,accountId)){
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.grantType = grantType;           
            this.accountId = accountId;
        } else {
            throw new ApiException("SFMC:INVALID_INPUT", "Required fields can't be null or empty.");
        }
    }

    public String getGrantType() {
        return grantType;
    }

   
    public String getClientId() {
        return clientId;
    }

    
    public String getClientSecret() {
        return clientSecret;
    }

   

    public String getAccountId() {
        return accountId;
    }

}
