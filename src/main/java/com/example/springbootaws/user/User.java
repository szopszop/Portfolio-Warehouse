package com.example.springbootaws.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    private String username;
    private String email;
    private String userProfileImageLink;   // S3 key
    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String email, String password) {
    }

    public enum Role {
        USER("ROLE_USER"),
        ADMIN("ROLE_ADMIN");
        private final String name;

        Role(String name) {
            this.name = name;
        }
    }

    public enum Provider {
        LOCAL, GOOGLE
    }

    public String getEmail() {
        return email;
    }

    public UUID getUserId() {
        return userId;
    }

    public Optional<String> getUserProfileImageLink() {
        return Optional.ofNullable(userProfileImageLink);
    }

    public void setUserProfileImageLink(String userProfileImageLink) {
        this.userProfileImageLink = userProfileImageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(userProfileImageLink, that.userProfileImageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, userProfileImageLink);
    }
}


