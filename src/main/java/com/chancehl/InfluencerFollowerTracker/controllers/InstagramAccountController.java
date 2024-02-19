package com.chancehl.InfluencerFollowerTracker.controllers;

import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import com.chancehl.InfluencerFollowerTracker.models.InstagramPrivateApiUserResponse;
import com.chancehl.InfluencerFollowerTracker.services.InstagramAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class InstagramAccountController {
    @Autowired
    private InstagramAccountService instagramAccountService;

    @GetMapping("/account/{name}")
    public InstagramAccount getInstagramAccount(@PathVariable String name) throws IOException, InterruptedException {
        InstagramPrivateApiUserResponse response = this.instagramAccountService.getFollowerCount(name);

        return InstagramAccount.builder()
                .followers(response.data.user.edge_followed_by.count)
                .build();
    }
}