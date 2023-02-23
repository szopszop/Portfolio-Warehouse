package com.szymontracz.portfoliowarehouse.repository;

import com.szymontracz.portfoliowarehouse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findUserById(UUID uuid);

    Optional<User> findByUsername(String username);
}
