package com.opsmind_auth_service.infrastructure.persistence.mapper;

import com.opsmind_auth_service.domain.entity.User;
import com.opsmind_auth_service.infrastructure.persistence.entity.UserJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    User toDomain(UserJpaEntity entity);

    UserJpaEntity toEntity(User domain);
}
