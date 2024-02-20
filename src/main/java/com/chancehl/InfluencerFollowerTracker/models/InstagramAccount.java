package com.chancehl.InfluencerFollowerTracker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InstagramAccount {
    @Id
    public String handle;

    public String fullName;

    public int followers;
}
