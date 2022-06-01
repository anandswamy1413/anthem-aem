package com.anthem.platform.core.schedulers.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Apigee API scheduler Config")
public @interface SchedulerConfigs {
    /**
     * schedulerName
     * @return String name
     */
    @AttributeDefinition(name = "Scheduler name", description = "Scheduler name", type = AttributeType.STRING)
    String schedulerName() default "Create Access Token Scheduler";

    /**
     * schedulerConcurrent
     * @return schedulerConcurrent
     */
    @AttributeDefinition(name = "Concurrent", description = "Schedule task concurrently", type = AttributeType.BOOLEAN)
    boolean schedulerConcurrent() default true;

    /**
     * serviceEnabled
     * @return serviceEnabled
     */
    @AttributeDefinition(name = "Enabled", description = "Enable Scheduler", type = AttributeType.BOOLEAN)
    boolean serviceEnabled() default true;

    /**
     * schedulerExpression
     * @return schedulerExpression
     */
    @AttributeDefinition(name = "Expression", description = "Scheduler cron-job expression. Default: run every 15 min.", type = AttributeType.STRING)
    String schedulerExpression() default "0 */12 * ? * *";
}