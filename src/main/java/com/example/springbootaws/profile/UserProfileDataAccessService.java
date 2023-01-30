package com.example.springbootaws.profile;

import com.example.springbootaws.datastore.FakeUserProfileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserProfileDataAccessService {

    private final FakeUserProfileDataStore fakeUserProfileDataStore;

    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

    List<UserProfile> getAllUserProfiles() {
        return fakeUserProfileDataStore.getUserProfiles();
    }

    Optional<UserProfile> getUserProfile(UUID userProfileId) {
        return fakeUserProfileDataStore.getUserProfile(userProfileId);
    }
}
