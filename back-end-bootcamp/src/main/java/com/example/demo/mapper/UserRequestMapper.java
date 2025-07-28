package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.UserRequest;
import com.example.demo.model.User;

@Mapper
public interface UserRequestMapper {
    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);

    // Map Entity to DTO
    UserRequest toDto(User user);

    // Map DTO to Entity
    User toEntity(UserRequest userDto);
}
