package com.chancehl.InfluencerFollowerTracker.controllers;

import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import com.chancehl.InfluencerFollowerTracker.models.InstagramPrivateApiUserResponse;
import com.chancehl.InfluencerFollowerTracker.services.InstagramAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class InstagramAccountController {
    @Autowired
    private InstagramAccountService instagramAccountService;

    /**
     * Looks up an Instagram account by name and saves it in the database
     *
     * @param name The Instagram account name
     * @return The saved Instagram account
     * @throws IOException When communication cannot be established with the "private" Instagram API
     * @throws InterruptedException When communication is severed when communicating with the "private" Instagram API
     */
    @PostMapping("/account/{name}")
    public InstagramAccount saveInstagramAccount(@PathVariable String name) throws IOException, InterruptedException {
        InstagramPrivateApiUserResponse response = this.instagramAccountService.getFollowerCount(name);

        InstagramAccount account = InstagramAccount.builder()
                .name(name)
                .followers(response.data.user.edgeFollowedBy.count)
                .build();

        return this.instagramAccountService.saveAccount(account);
    }
}
