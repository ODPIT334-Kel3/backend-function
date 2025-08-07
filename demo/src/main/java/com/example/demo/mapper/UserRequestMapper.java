package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.UserRequest;
import com.example.demo.models.User;

@Mapper
public interface UserRequestMapper {
    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);

    UserRequest toDto(User user);

    User toEntity(UserRequest userDto);
    
}
