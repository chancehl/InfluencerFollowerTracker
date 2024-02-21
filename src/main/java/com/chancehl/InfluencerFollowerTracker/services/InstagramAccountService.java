package com.chancehl.InfluencerFollowerTracker.services;

import com.chancehl.InfluencerFollowerTracker.models.FollowerSnapshot;
import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import com.chancehl.InfluencerFollowerTracker.models.InstagramPrivateApiUserResponse;
import com.chancehl.InfluencerFollowerTracker.repositories.InstagramAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class InstagramAccountService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InstagramAccountRepository instagramAccountRepository;

    // Don't ask me why, but this is absolutely required in order to call their API.
    // We can omit this by swapping to the Instagram Graph API, but that only works for
    // fetching the followers of creator, business, and verified accounts. I'm assuming
    // that some of the people on these shows won't have verified accounts at first.
    // So, for now, I'm going to keep using the "private" API that Meta exposes to their
    // mobile clients. Those APIs require a mobile User-Agent header.
    private static final String HACKY_ASS_USER_AGENT_FROM_INTERNET = "Instagram 76.0.0.15.395 Android (24/7.0; 640dpi; 1440x2560; samsung; SM-G930F; herolte; samsungexynos8890; en_US; 138226743)";

    /**
     * Gets the follower count for a given Instagram account name
     *
     * @param handle The Instagram account name
     * @return Instagram "private" API response object
     * @throws IOException When communication cannot be established with the "private" Instagram API
     * @throws InterruptedException When communication is severed when communicating with the "private" Instagram API
     */
    public InstagramPrivateApiUserResponse getInstagramAccountData(String handle) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://i.instagram.com/api/v1/users/web_profile_info/?username=" + handle))
                .header("user-agent", HACKY_ASS_USER_AGENT_FROM_INTERNET)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return this.objectMapper.readValue(response.body(), InstagramPrivateApiUserResponse.class);
    }

    /**
     * Returns the follower account for a given user
     *
     * @param handle The Instagram account handle
     * @return An int representing the follower count
     * @throws IOException When communication cannot be established with the "private" Instagram API
     * @throws InterruptedException When communication is severed when communicating with the "private" Instagram API
     */
    public int getFollowerCount(String handle) throws IOException, InterruptedException {
        InstagramPrivateApiUserResponse response = this.getInstagramAccountData(handle);

        return response.data.user.edgeFollowedBy.count;
    }

    /**
     * Gets an Instagram account from the database
     *
     * @param handle The Instagram account handle
     * @return The Instagram account from the database
     */
    public Optional<InstagramAccount> getAccount(String handle) {
        return this.instagramAccountRepository.findById(handle);
    }

    /**
     * Gets a list of all saved Instagram accounts
     *
     * @return A list of the saved Instagram accounts
     */
    public List<InstagramAccount> getAllAccounts() {
        return this.instagramAccountRepository.findAll();
    }

    /**
     * Saves an Instagram account in the database
     *
     * @param account The account to save
     * @return The account that was saved
     */
    public InstagramAccount saveAccount(InstagramAccount account) {
        return this.instagramAccountRepository.save(account);
    }

    /**
     * Saves multiple Instagram accounts to the database
     *
     * @param accounts The account(s) to save
     */
    public void saveAccounts(List<InstagramAccount> accounts) {
        this.instagramAccountRepository.saveAll(accounts);
    }


    /**
     * Updates an account after associating a snapshot with it
     *
     * @param account The account to update
     * @param snapshot The snapshot to associate with the account
     */
    public void updateAccount(InstagramAccount account, FollowerSnapshot snapshot) {
        // Associate back reference
        account.snapshots.add(snapshot);

        // Update account
        this.instagramAccountRepository.save(account);
    }
}
