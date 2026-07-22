package com.facundo.assistentia.infrastructure.persistence.jpa.adapter;

import com.facundo.assistentia.domain.user.model.User;
import com.facundo.assistentia.domain.user.repository.UserRepository;
import com.facundo.assistentia.infrastructure.persistence.jpa.entity.WorkspaceUserEntity;
import com.facundo.assistentia.infrastructure.persistence.jpa.repository.WorkspaceUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("!test")
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final WorkspaceUserJpaRepository userJpaRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsernameIgnoreCase(username).map(this::toDomain);
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public User save(User user) {
        return toDomain(userJpaRepository.save(toEntity(user)));
    }

    private WorkspaceUserEntity toEntity(User user) {
        return WorkspaceUserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .build();
    }

    private User toDomain(WorkspaceUserEntity user) {
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .build();
    }
}