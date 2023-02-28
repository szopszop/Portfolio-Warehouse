package com.szymontracz.warehouse.repository;

import com.szymontracz.warehouse.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<EmailToken, Integer> {

    Optional<EmailToken> findByToken(String token);
}
