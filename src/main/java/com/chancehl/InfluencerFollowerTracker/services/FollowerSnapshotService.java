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

    public FollowerSnapshot getLatestSnapshot(String handle) {
        List<FollowerSnapshot> sortedSnapshots = this.followerSnapshotRepository.findByInstagramAccountHandleOrderByCreatedOnDesc(handle);

        if (sortedSnapshots.isEmpty()) {
            throw new MissingEntityException("No snapshots exist");
        }

        return sortedSnapshots.get(0);
    }
}
