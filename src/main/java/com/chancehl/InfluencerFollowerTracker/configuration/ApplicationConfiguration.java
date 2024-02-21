package com.chancehl.InfluencerFollowerTracker.configuration;

import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import com.chancehl.InfluencerFollowerTracker.models.InstagramPrivateApiUserResponse;
import com.chancehl.InfluencerFollowerTracker.models.Program;
import com.chancehl.InfluencerFollowerTracker.services.InstagramAccountService;
import com.chancehl.InfluencerFollowerTracker.tasks.GetLatestFollowersTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    private InstagramAccountService instagramAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final long DELAY = 3_600_000; // 1 hr

    private static final long INTERVAL = 3_600_000; // 1 hr

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() throws IOException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

        List<InstagramAccount> savedAccounts = this.instagramAccountService.getAllAccounts();

        if (savedAccounts.isEmpty()) {
            logger.info("Seeding Love Is Blind (S6)");

            // seed Love Is Blind S6
            List<InstagramAccount> accounts = List.of(this.objectMapper.readValue(new File("src/main/resources/static/influencers/LoveIsBlind/Season6/influencers.json"), InstagramAccount[].class));

            for (InstagramAccount account : accounts) {
                InstagramPrivateApiUserResponse instagramAccountData = this.instagramAccountService.getInstagramAccountData(account.handle);

                account.program = Program.LOVE_IS_BLIND_S6;
                account.followers = instagramAccountData.data.user.edgeFollowedBy.count;
                account.fullName = instagramAccountData.data.user.fullName;
                account.profilePicUrl = instagramAccountData.data.user.profilePicUrl;
                account.createdOn = new Date().getTime();
                account.lastUpdated = new Date().getTime();

                logger.info("Success fully fetched data for seed account (handle={}, followers={}, fullName={})", account.handle, account.followers, account.fullName);
            }

            this.instagramAccountService.saveAccounts(accounts);
        } else {
            logger.warn("Accounts already exist. Skipping initialization.");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleTasks() {
        Timer timer = new Timer();

        timer.schedule(new GetLatestFollowersTask(), DELAY, INTERVAL);
    }
}
