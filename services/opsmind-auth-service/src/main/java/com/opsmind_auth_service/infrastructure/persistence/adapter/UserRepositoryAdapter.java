package com.opsmind_auth_service.infrastructure.persistence.adapter;

import com.opsmind_auth_service.domain.entity.User;
import com.opsmind_auth_service.domain.repository.UserRepository;
import com.opsmind_auth_service.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.opsmind_auth_service.infrastructure.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {

        var entity = mapper.toEntity(user);

        var saved = jpaUserRepository.save(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return jpaUserRepository
                .findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {

        return jpaUserRepository.existsByEmail(email);
    }
}
