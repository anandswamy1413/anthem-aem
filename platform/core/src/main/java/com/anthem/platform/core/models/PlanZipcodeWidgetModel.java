package com.anthem.platform.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;

@Model(adaptables = Resource.class)
public class PlanZipcodeWidgetModel {

    @Inject
    @Optional
    private String lobType;

    @Inject
    @Optional
    private String brandCFUrl;

    public String getLobType() {
        return lobType;
    }

    public String getBrandCFUrl() {
        return brandCFUrl;
    }

    public void setBrandCFUrl(String brandCFUrl) {
        this.brandCFUrl = brandCFUrl;
    }
    
    public void setLobType(String lobType) {
        this.lobType = lobType;
    }
}
