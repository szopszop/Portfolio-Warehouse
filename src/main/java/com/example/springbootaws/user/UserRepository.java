package com.example.springbootaws.user;

import com.example.springbootaws.security.payload.request.RegisterRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmail(String email);


    Optional<User> findUserByUserId(UUID uuid);

    boolean existsByEmail(String email);



}
