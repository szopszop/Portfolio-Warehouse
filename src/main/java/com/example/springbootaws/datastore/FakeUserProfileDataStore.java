package com.example.springbootaws.datastore;

import com.example.springbootaws.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();


    static {
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "SzymonTracz", null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "JanetJackson", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }

    public Optional<UserProfile> getSingleUserProfile(UUID uuid) {
        return USER_PROFILES
                .stream()
                .filter(profileId -> profileId.getUserProfileId().equals(uuid))
                .findAny();
    }

}
