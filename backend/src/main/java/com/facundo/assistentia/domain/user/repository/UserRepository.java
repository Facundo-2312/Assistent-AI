package com.facundo.assistentia.domain.user.repository;

import com.facundo.assistentia.domain.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository {
    boolean existsByEmail(String email);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    long count();
    List<User> findAll();
    User save(User user);
}
