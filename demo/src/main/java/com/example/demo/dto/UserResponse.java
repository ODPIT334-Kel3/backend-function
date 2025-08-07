package com.example.demo.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String role;
    // private String password;
}
