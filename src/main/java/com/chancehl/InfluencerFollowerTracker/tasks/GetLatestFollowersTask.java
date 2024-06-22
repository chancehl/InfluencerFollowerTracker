package com.chancehl.InfluencerFollowerTracker.tasks;

import com.chancehl.InfluencerFollowerTracker.models.FollowerSnapshot;
import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import com.chancehl.InfluencerFollowerTracker.models.InstagramPrivateApiUserResponse;
import com.chancehl.InfluencerFollowerTracker.services.InstagramAccountService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

@Service
@NoArgsConstructor
public class GetLatestFollowersTask extends TimerTask {
    @Autowired
    private InstagramAccountService instagramAccountService;

    @Override
    public void run() {
        Logger logger = LoggerFactory.getLogger(GetLatestFollowersTask.class);

        List<InstagramAccount> allAccounts = this.instagramAccountService.getAllAccounts();

        logger.info("Refreshing {} accounts", allAccounts.size());

        for (InstagramAccount account : allAccounts) {
            logger.info("Refreshing Instagram followers for account {}", account.handle);

            try {
                // fetch data
                InstagramPrivateApiUserResponse response = this.instagramAccountService.getInstagramAccountData(account.handle);

                // create snapshot
                FollowerSnapshot snapshot = FollowerSnapshot.builder()
                        .instagramAccount(account)
                        .createdOn(new Date().getTime())
                        .followers(response.data.user.edgeFollowedBy.count)
                        .build();

                // update account
                this.instagramAccountService.updateAccount(account, snapshot);

                // sleep so we don't get rate limited lol
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                logger.error("Failed to refresh account {}: {}", account.handle, e.getMessage());
            }
        }
    }
}
