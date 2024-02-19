package com.chancehl.InfluencerFollowerTracker.services;

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

    public InstagramPrivateApiUserResponse getFollowerCount(String name) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://i.instagram.com/api/v1/users/web_profile_info/?username=" + name))
                .header("user-agent", HACKY_ASS_USER_AGENT_FROM_INTERNET)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return this.objectMapper.readValue(response.body(), InstagramPrivateApiUserResponse.class);
    }

    public InstagramAccount saveAccount(InstagramAccount account) {
        return this.instagramAccountRepository.save(account);
    }
}
