package com.chancehl.InfluencerFollowerTracker.models;

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

    public String gender;

    @Column(length = 1024)
    public String profilePicUrl;

    @Enumerated(EnumType.STRING)
    public Program program;

    @JsonManagedReference
    @OneToMany(mappedBy = "instagramAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<FollowerSnapshot> snapshots;
}
