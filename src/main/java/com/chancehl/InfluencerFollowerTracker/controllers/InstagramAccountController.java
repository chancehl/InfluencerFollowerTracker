package com.chancehl.InfluencerFollowerTracker.controllers;

import com.chancehl.InfluencerFollowerTracker.models.*;
import com.chancehl.InfluencerFollowerTracker.services.InstagramAccountService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
public class InstagramAccountController {
    @Autowired
    private InstagramAccountService instagramAccountService;

    /**
     * Looks up an Instagram account by handle and saves it in the database
     *
     * @param handle The Instagram account handle
     * @return The saved Instagram account
     * @throws IOException When communication cannot be established with the "private" Instagram API
     * @throws InterruptedException When communication is severed when communicating with the "private" Instagram API
     * @throws AccessDeniedException When the client API key does not match what's stored on the server
     */
    @PostMapping("/accounts/{handle}")
    public InstagramAccount saveInstagramAccount(@RequestHeader("X-Api-Key") String apiKey, @PathVariable String handle) throws IOException, InterruptedException, AccessDeniedException {
        Dotenv dotenv = Dotenv.load();

        String serverKey = dotenv.get("API_KEY");

        if (!apiKey.equals(serverKey)) {
            throw new AccessDeniedException("Invalid API key: " + apiKey);
        }

        InstagramPrivateApiUserResponse response = this.instagramAccountService.getInstagramAccountData(handle);

        InstagramAccount account = InstagramAccount.builder()
                .handle(handle)
                .followers(response.data.user.edgeFollowedBy.count)
                .fullName(response.data.user.fullName)
                .createdOn(new Date().getTime())
                .lastUpdated(new Date().getTime())
                .snapshots(new ArrayList<>())
                .build();

        return this.instagramAccountService.saveAccount(account);
    }

    /**
     * Returns a saved Instagram account
     *
     * @param handle The Instagram account handle
     * @return The saved Instagram account
     * @throws MissingEntityException When the account does not exist
     */
    @GetMapping("/accounts/{handle}")
    public InstagramAccount getInstagramAccount(@PathVariable String handle) throws MissingEntityException {
        Optional<InstagramAccount> account = this.instagramAccountService.getAccount(handle);

        if (account.isEmpty()) {
            throw new MissingEntityException("Invalid account: " + handle);
        }

        return account.get();
    }

    /**
     * Creates a snapshot for a given Instagram handle
     *
     * @param handle The Instagram account handle
     * @return The new snapshot
     * @throws IOException When communication cannot be established with the "private" Instagram API
     * @throws InterruptedException When communication is severed when communicating with the "private" Instagram API
     * @throws MissingEntityException When the account does not exist
     */
    @PostMapping("/accounts/{handle}/snapshots")
    public FollowerSnapshot createSnapshot(@PathVariable String handle) throws IOException, InterruptedException, MissingEntityException {
        Optional<InstagramAccount> account = this.instagramAccountService.getAccount(handle);

        if (account.isEmpty()) {
            throw new MissingEntityException("Invalid account: " + handle);
        }

        InstagramPrivateApiUserResponse response = this.instagramAccountService.getInstagramAccountData(handle);

        FollowerSnapshot snapshot = FollowerSnapshot.builder()
                .instagramAccount(account.get())
                .createdOn(new Date().getTime())
                .followers(response.data.user.edgeFollowedBy.count)
                .build();

        this.instagramAccountService.updateAccount(account.get(), snapshot);

        return snapshot;
    }
}
