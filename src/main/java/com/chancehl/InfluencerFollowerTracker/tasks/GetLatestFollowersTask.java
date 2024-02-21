package com.chancehl.InfluencerFollowerTracker.tasks;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

@NoArgsConstructor
public class GetLatestFollowersTask extends TimerTask {
    @Override
    public void run() {
        Logger logger = LoggerFactory.getLogger(GetLatestFollowersTask.class);

        logger.info("Running on an interval...");
    }
}
