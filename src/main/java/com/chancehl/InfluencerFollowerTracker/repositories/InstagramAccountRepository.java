package com.chancehl.InfluencerFollowerTracker.repositories;

import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstagramAccountRepository extends JpaRepository<InstagramAccount, String> {}
