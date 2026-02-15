package com.test.CompanyDriverPortal.service.impl;

import com.test.CompanyDriverPortal.dto.user.UserResponseDto;
import com.test.CompanyDriverPortal.model.User;
import com.test.CompanyDriverPortal.repository.UserRepository;
import com.test.CompanyDriverPortal.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDto> getAllUsers(String currentUsername) {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name(),
                        user.getUsername().equals(currentUsername) ? false : true // canDelete flag
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id, String currentUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent Super Admin from deleting themselves
        if (user.getUsername().equals(currentUsername)) {
            throw new RuntimeException("Super Admin cannot delete themselves");
        }

        // Hard delete
        userRepository.delete(user);
    }

    @Override
    public void updateUserRole(Long id, String role, String currentUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent Super Admin from changing their own role
        if (user.getUsername().equals(currentUsername)) {
            throw new RuntimeException("Super Admin cannot change their own role");
        }

        // Update role
        user.setRole(User.Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
    }
}
