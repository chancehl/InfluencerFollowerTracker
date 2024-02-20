package com.chancehl.InfluencerFollowerTracker.repositories;

import com.chancehl.InfluencerFollowerTracker.models.FollowerSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerSnapshotRepository extends JpaRepository<FollowerSnapshot, Long> {}
