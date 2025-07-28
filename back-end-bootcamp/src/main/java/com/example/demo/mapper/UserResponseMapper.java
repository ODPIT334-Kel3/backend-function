package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;

@Mapper
public interface UserResponseMapper {

    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    // Map Entity to DTO
    UserResponse toDto(User user);

    // Map DTO to Entity
    User toEntity(UserResponse userDto);
}
