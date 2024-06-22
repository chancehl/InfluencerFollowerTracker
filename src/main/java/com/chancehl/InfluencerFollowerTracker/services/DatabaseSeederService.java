package com.chancehl.InfluencerFollowerTracker.services;

import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import com.chancehl.InfluencerFollowerTracker.models.InstagramPrivateApiUserResponse;
import com.chancehl.InfluencerFollowerTracker.models.Program;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class DatabaseSeederService {
    @Autowired
    private InstagramAccountService instagramAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederService.class);

    public void seedInstagramAccounts() throws IOException, InterruptedException {
        List<InstagramAccount> savedAccounts = this.instagramAccountService.getAllAccounts();

        if (savedAccounts.isEmpty()) {
            logger.info("Seeding Love Is Blind (S6)");
            this.seedInstagramAccountsFromStaticData("src/main/resources/static/influencers/LoveIsBlind/Season6/influencers.json");
        } else {
            logger.warn("Accounts already exist. Skipping initialization.");
        }
    }

    private void seedInstagramAccountsFromStaticData(String location) throws IOException, InterruptedException {
        // seed Love Is Blind S6
        List<InstagramAccount> accounts = List.of(this.objectMapper.readValue(new File(location), InstagramAccount[].class));

        for (InstagramAccount account : accounts) {
            InstagramPrivateApiUserResponse instagramAccountData = this.instagramAccountService.getInstagramAccountData(account.handle);

            account.program = Program.LOVE_IS_BLIND_S6;
            account.followers = instagramAccountData.data.user.edgeFollowedBy.count;
            account.fullName = instagramAccountData.data.user.fullName;
            account.profilePicUrl = instagramAccountData.data.user.profilePicUrl;
            account.createdOn = new Date().getTime();
            account.lastUpdated = new Date().getTime();

            logger.info("Successfully fetched data for seed account (handle={}, followers={}, fullName={})", account.handle, account.followers, account.fullName);
        }

        this.instagramAccountService.saveAccounts(accounts);
    }
}
