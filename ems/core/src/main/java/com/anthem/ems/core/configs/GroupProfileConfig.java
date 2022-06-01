package com.anthem.ems.core.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Group Profile Config")
public @interface GroupProfileConfig {

    @AttributeDefinition(name = "Group profile endpoint url",
            type = AttributeType.STRING)
    String getGroupProfileEndPoint() default "https://www.joinanthembenefits.com/ems/public/api/ems/group/groupprofiles/";
}
