package com.szymontracz.warehouse.repository;

import com.szymontracz.warehouse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
//    Optional<User> findUserById(Integer id);
//    Optional<User> findByUsername(String username);
}
