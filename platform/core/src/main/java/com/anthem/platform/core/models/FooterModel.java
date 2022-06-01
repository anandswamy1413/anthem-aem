
package com.anthem.platform.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;


/**
 * A FooterModel Sling Model Class.
 * 
 */
@Model(adaptables = {
    Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
    @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
    @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")
})
public class FooterModel {

    @Inject
    private String title;
    @Inject
    private String zipcode;

    /**
     * Returns the title
     * 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the zipcode
     * 
     */
    public String getZipcode() {
        return zipcode;
    }

    @PostConstruct
    protected void init() {
    	//servlet is initialized by calling the init() method.
    }

}
