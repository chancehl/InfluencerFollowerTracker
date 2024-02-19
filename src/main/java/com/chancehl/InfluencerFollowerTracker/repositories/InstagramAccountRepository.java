package com.chancehl.InfluencerFollowerTracker.repositories;

import com.chancehl.InfluencerFollowerTracker.models.InstagramAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstagramAccountRepository extends JpaRepository<InstagramAccount, UUID> {}
