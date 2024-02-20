package com.chancehl.InfluencerFollowerTracker.configuration;

import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import com.chancehl.InfluencerFollowerTracker.models.InstagramPrivateApiUserResponse;
import com.chancehl.InfluencerFollowerTracker.services.InstagramAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    private InstagramAccountService instagramAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() throws IOException, InterruptedException {
        // seed Love Is Blind S6
        List<InstagramAccount> accounts = List.of(this.objectMapper.readValue(new File("src/main/resources/static/influencers/LoveIsBlind/Season6/influencers.json"), InstagramAccount[].class));

        for (InstagramAccount account : accounts) {
            InstagramPrivateApiUserResponse instagramAccountData = this.instagramAccountService.getInstagramAccountData(account.handle);

            account.followers = instagramAccountData.data.user.edgeFollowedBy.count;
            account.fullName = instagramAccountData.data.user.fullName;
        }

        this.instagramAccountService.saveAccounts(accounts);
    }
}
