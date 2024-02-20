package com.chancehl.InfluencerFollowerTracker.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InstagramAccount {
    @Id
    public String handle;

    public String fullName;

    public int followers;

    public long createdOn;

    public long lastUpdated;

    @JsonManagedReference
    @OneToMany(mappedBy = "instagramAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<FollowerSnapshot> snapshots;
}
