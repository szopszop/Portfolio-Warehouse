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
        USER_PROFILES.add(new UserProfile(UUID.fromString("ab1378cb-e8ea-4d9b-b97e-604acdde3f14")
                , "Szymon Tracz", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("911e32a4-659f-457c-9445-704d6fc8c92a")
                , "Janet Jackson", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }

    public UserProfile getUserProfile(UUID userProfileId) {
        return USER_PROFILES
                .stream()
                .filter(profile -> profile.getUserProfileId().equals(userProfileId))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(
                        String.format("User profile %s not found!", userProfileId)));
    }

}
