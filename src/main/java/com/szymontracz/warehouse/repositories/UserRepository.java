package com.szymontracz.warehouse.repositories;

import com.szymontracz.warehouse.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
//    Optional<User> findUserById(Integer id);
//    Optional<User> findByUsername(String username);
}
