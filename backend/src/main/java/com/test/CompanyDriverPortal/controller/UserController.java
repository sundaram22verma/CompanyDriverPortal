package com.test.CompanyDriverPortal.controller;

import com.test.CompanyDriverPortal.dto.user.UserResponseDto;
import com.test.CompanyDriverPortal.service.UserService;
import com.test.CompanyDriverPortal.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        String currentUsername = SecurityUtil.getCurrentUsername(); // Get the current username from the security context
        List<UserResponseDto> users = userService.getAllUsers(currentUsername); // Pass the username to the service layer to filter results based on role
        return ResponseEntity.ok(users);
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        userService.deleteUser(id, currentUsername);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Long id,
            @RequestParam String role
    ) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        userService.updateUserRole(id, role, currentUsername);
        return ResponseEntity.ok("User role updated successfully");
    }

}
