package com.chancehl.InfluencerFollowerTracker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstagramPrivateApiUser {
    public EdgeFollowedBy edge_followed_by;
}
