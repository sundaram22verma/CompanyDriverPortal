package com.test.CompanyDriverPortal.service;

import com.test.CompanyDriverPortal.dto.user.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers(String currentUsername);
    void deleteUser(Long id, String currentUsername);
    void updateUserRole(Long id, String role, String currentUsername);
}
