package com.example.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserRequestMapper;
import com.example.demo.mapper.UserResponseMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final RedisTemplate<String, User> redisTemplate;
    private final String USER_KEY_PREFIX = "user:";

    public UserService(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %s", id)));
        return UserResponseMapper.INSTANCE.toDto(user);
    }

    // @Cacheable
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with username: %s", username)));
        User redisUser = redisTemplate.opsForValue().get(USER_KEY_PREFIX + username);
        System.out.println("User found in Redis with value: " + redisUser);
        if (redisUser != null) {
            return UserResponseMapper.INSTANCE.toDto(redisUser);
        }
        return UserResponseMapper.INSTANCE.toDto(user);
    }

    public UserResponse createUser(UserRequest userDto) {
        User user = UserRequestMapper.INSTANCE.toEntity(userDto);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + savedUser.getUsername(), savedUser);
        return UserResponseMapper.INSTANCE.toDto(savedUser);
    }

    public UserResponse updateUser(UUID id, UserRequest updatedUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %s", id)));

        existingUser.setName(updatedUserDto.getName());
        existingUser.setEmail(updatedUserDto.getEmail());
        existingUser.setUsername(updatedUserDto.getUsername());
        existingUser.setPassword(updatedUserDto.getPassword());
        existingUser.setRole(updatedUserDto.getRole());

        User updatedUser = userRepository.save(existingUser);
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + updatedUser.getUsername(), updatedUser);
        return UserResponseMapper.INSTANCE.toDto(updatedUser);

    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
        redisTemplate.delete(USER_KEY_PREFIX + id);
    }
}
