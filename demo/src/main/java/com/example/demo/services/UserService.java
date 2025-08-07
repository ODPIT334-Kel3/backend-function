package com.example.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.mapper.UserRequestMapper;
import com.example.demo.mapper.UserResponseMapper;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.exceptions.ResourceNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // get all users
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(UserResponseMapper.INSTANCE::toDto)
                    .collect(Collectors.toList());
    }

    // get user by id
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id %s", id)));
        return UserResponseMapper.INSTANCE.toDto(user);
    }

    // get user by username
    public UserResponse getUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with username %s", username)));
        return UserResponseMapper.INSTANCE.toDto(user);
    }

    // create new user (register)
    public UserResponse createUser(UserRequest userDto){
        User user = UserRequestMapper.INSTANCE.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return UserResponseMapper.INSTANCE.toDto(savedUser);
    }

    // update user
    public UserResponse updateUser(UUID id, UserRequest updatedUserDto){
        User existingUser = userRepository.findById(id)
                                          .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with format %s", id)));
        
        existingUser.setName(updatedUserDto.getName());
        existingUser.setEmail(updatedUserDto.getEmail());
        existingUser.setUsername(updatedUserDto.getUsername());
        existingUser.setRole(updatedUserDto.getRole());
        existingUser.setPassword(updatedUserDto.getPassword());

        User updatedUser = userRepository.save(existingUser);
        return UserResponseMapper.INSTANCE.toDto(updatedUser); 
    }

    public void deleteUser(UUID id){
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id %s", id)));
        userRepository.delete(user);
    }
}
