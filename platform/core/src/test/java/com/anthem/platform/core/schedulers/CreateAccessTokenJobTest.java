package com.anthem.platform.core.schedulers;

import com.anthem.platform.core.schedulers.configs.SchedulerConfigs;
import com.anthem.platform.core.services.api.GenerateAccessTokenService;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccessTokenJobTest {

    @InjectMocks
    private CreateAccessTokenJob createAccessTokenJob;

    @Mock
    private Scheduler scheduler;

    @Mock
    private GenerateAccessTokenService generateAccessTokenService;

    @Mock
    private Logger logger;

    @Mock
    private SchedulerConfigs schedulerConfigs;

    @Mock
    private ScheduleOptions opts;

    @BeforeEach
    void setup() {
        when(schedulerConfigs.schedulerName()).thenReturn("Create Access Token Scheduler");


    }

    @Test
    void TestCreateAccessTokenJob() throws IOException {
        //set-up
        when(schedulerConfigs.serviceEnabled()).thenReturn(true);
        generateAccessTokenService.generateNewToken();

        when(schedulerConfigs.schedulerExpression()).thenReturn("0 */14 * ? * *");
        when(scheduler.EXPR("0 */14 * ? * *")).thenReturn(opts);
        when(opts.name(anyString())).thenReturn(opts);

        //execute
        createAccessTokenJob.activate(schedulerConfigs);
        createAccessTokenJob.modified(schedulerConfigs);
        createAccessTokenJob.deactivate(schedulerConfigs);
        createAccessTokenJob.run();

        //verify
        verify(logger, never()).error("IO Exception while generating Token");
    }

    @Test
    void TestSchedulerDisabled() {
        //set-up
        when(schedulerConfigs.serviceEnabled()).thenReturn(false);

        //execute
        createAccessTokenJob.activate(schedulerConfigs);
        createAccessTokenJob.modified(schedulerConfigs);
        createAccessTokenJob.deactivate(schedulerConfigs);
        createAccessTokenJob.run();

        //verify
        verify(logger, never()).error("IO Exception while generating Token");
    }
}
