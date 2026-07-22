package com.facundo.assistentia.infrastructure.persistence.inmemory;

import com.facundo.assistentia.domain.user.model.User;
import com.facundo.assistentia.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("test")
public class UserInMemoryRepository implements UserRepository {

    private final Map<UUID, User> storage = new ConcurrentHashMap<>();

    @Override
    public boolean existsByEmail(String email) {
        return storage.values().stream().anyMatch(user -> email.equalsIgnoreCase(user.getEmail()));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage.values().stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        storage.put(user.getId(), user);
        return user;
    }
}
