package com.chancehl.InfluencerFollowerTracker.controllers;

import com.chancehl.InfluencerFollowerTracker.models.AccessDeniedException;
import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import com.chancehl.InfluencerFollowerTracker.models.InstagramPrivateApiUserResponse;
import com.chancehl.InfluencerFollowerTracker.services.InstagramAccountService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @throws AccessDeniedException When the client API key does not match what's stored on the server
     */
    @PostMapping("/account/{name}")
    public InstagramAccount saveInstagramAccount(@RequestHeader("X-Api-Key") String apiKey, @PathVariable String name) throws IOException, InterruptedException, AccessDeniedException {
        Dotenv dotenv = Dotenv.load();

        String serverKey = dotenv.get("API_KEY");

        if (!apiKey.equals(serverKey)) {
            throw new AccessDeniedException("Invalid API key: " + apiKey);
        }

        InstagramPrivateApiUserResponse response = this.instagramAccountService.getFollowerCount(name);

        InstagramAccount account = InstagramAccount.builder()
                .name(name)
                .followers(response.data.user.edgeFollowedBy.count)
                .build();

        return this.instagramAccountService.saveAccount(account);
    }
}
