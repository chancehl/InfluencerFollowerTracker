package com.chancehl.InfluencerFollowerTracker.configuration;

import com.chancehl.InfluencerFollowerTracker.services.DatabaseSeederService;
import com.chancehl.InfluencerFollowerTracker.tasks.GetLatestFollowersTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.Timer;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    private DatabaseSeederService seeder;

    @Autowired
    private GetLatestFollowersTask task;

    private static final long DELAY = 0; // 1 hr

    private static final long INTERVAL = 60_000; // 1 hr

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() throws IOException, InterruptedException {
        // seed database
        this.seeder.seedInstagramAccounts();

        // schedule timers
        Timer timer = new Timer();
        timer.schedule(this.task, DELAY, INTERVAL);
    }
}
