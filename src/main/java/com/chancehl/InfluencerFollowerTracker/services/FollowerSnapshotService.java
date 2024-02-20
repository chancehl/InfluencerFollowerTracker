package com.chancehl.InfluencerFollowerTracker.services;

import com.chancehl.InfluencerFollowerTracker.models.FollowerSnapshot;
import com.chancehl.InfluencerFollowerTracker.models.MissingEntityException;
import com.chancehl.InfluencerFollowerTracker.repositories.FollowerSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerSnapshotService {
    @Autowired
    private FollowerSnapshotRepository followerSnapshotRepository;

    /**
     * Gets the latest snapshot for an Instagram account
     *
     * @param handle The Instagram account handle
     * @return The latest snapshot
     * @throws MissingEntityException When account does not exist or when the account has no snapshots
     */
    public FollowerSnapshot getLatestSnapshot(String handle) throws MissingEntityException {
        List<FollowerSnapshot> sortedSnapshots = this.followerSnapshotRepository.findByInstagramAccountHandleOrderByCreatedOnDesc(handle);

        if (sortedSnapshots.isEmpty()) {
            throw new MissingEntityException("Account/snapshots do not exist");
        }

        return sortedSnapshots.get(0);
    }
}
