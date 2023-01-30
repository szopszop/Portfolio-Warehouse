package com.example.springbootaws.datastore;

import com.example.springbootaws.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();


    static  {
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "SzymonTracz", null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "JanetJackson", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }

}
