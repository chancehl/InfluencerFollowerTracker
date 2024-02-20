package com.chancehl.InfluencerFollowerTracker.repositories;

import com.chancehl.InfluencerFollowerTracker.models.FollowerSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerSnapshotRepository extends JpaRepository<FollowerSnapshot, Long> {
    List<FollowerSnapshot> findByInstagramAccountHandleOrderByCreatedOnDesc(String instagramAccountHandle);
}
