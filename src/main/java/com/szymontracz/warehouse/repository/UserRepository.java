package com.szymontracz.warehouse.repository;

import com.szymontracz.warehouse.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByEmail(String email);
    Optional<UserEntity> findUserByUserId(UUID uuid);
    Optional<UserEntity> findByUsername(String username);
}
