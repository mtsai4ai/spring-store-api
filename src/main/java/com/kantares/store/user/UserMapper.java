package com.kantares.store.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);

    void update(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
