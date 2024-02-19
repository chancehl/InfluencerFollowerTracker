package com.chancehl.InfluencerFollowerTracker.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstagramPrivateApiUser {
    @JsonProperty("edge_followed_by")
    public EdgeFollowedBy edgeFollowedBy;
}
