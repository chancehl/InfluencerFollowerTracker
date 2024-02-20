package com.chancehl.InfluencerFollowerTracker.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FollowerSnapshot {
    @Id
    @GeneratedValue
    public long id;

    public long createdOn;

    public int followers;

    @JsonBackReference
    @ManyToOne
    public InstagramAccount instagramAccount;
}
