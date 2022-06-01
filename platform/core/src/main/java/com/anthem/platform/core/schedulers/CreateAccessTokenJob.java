package com.anthem.platform.core.schedulers;

import com.anthem.platform.core.schedulers.configs.SchedulerConfigs;
import com.anthem.platform.core.services.api.GenerateAccessTokenService;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Class Create Access Token runs every 15 mins.
 * @author aswaroop.
 */


@Designate(ocd=SchedulerConfigs.class)
@Component(immediate = true, service = CreateAccessTokenJob.class)
public class CreateAccessTokenJob implements Runnable {

    @Reference
    private Scheduler scheduler;

    @Reference
    private GenerateAccessTokenService generateAccessTokenService;

    private final Logger logger = LoggerFactory.getLogger(CreateAccessTokenJob.class);
 
    private int schedulerID;

    @Activate
    public void activate(SchedulerConfigs config) {
        schedulerID = config.schedulerName().hashCode();
        addScheduler(config);
    }

    @Modified
    protected void modified(SchedulerConfigs config) {
        removeScheduler();
        schedulerID = config.schedulerName().hashCode(); // update schedulerID
        addScheduler(config);
    }

    @Deactivate
    protected void deactivate(SchedulerConfigs config) {
        removeScheduler();
    }

    /**
     * Remove a scheduler based on the scheduler ID
     */
    private void removeScheduler() {
        logger.debug("Removing Scheduler Job '{}'", schedulerID);
        scheduler.unschedule(String.valueOf(schedulerID));
    }

    /**
     * Add a scheduler based on the scheduler ID
     *
     * @param config
     */
    private void addScheduler(SchedulerConfigs config) {
        if (config.serviceEnabled()) {
            ScheduleOptions opts = scheduler.EXPR(config.schedulerExpression());
            opts.name(String.valueOf(schedulerID));
            opts.canRunConcurrently(false);
            scheduler.schedule(this, opts);
            logger.debug("Scheduler added successfully");
        } else {
            logger.debug("Create Access Token Scheduler is Disabled.");
        }
    }

    @Override
    public void run() {
        try {
            generateAccessTokenService.generateNewToken();
        } catch (IOException e) {
            logger.error("IO Exception while generating Token{}", e.getLocalizedMessage());
        }
    }

}
