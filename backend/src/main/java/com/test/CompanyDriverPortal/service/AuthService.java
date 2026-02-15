package com.test.CompanyDriverPortal.service;

import com.test.CompanyDriverPortal.dto.auth.LoginRequest;
import com.test.CompanyDriverPortal.dto.auth.LoginResponse;
import com.test.CompanyDriverPortal.dto.auth.RegisterRequest;

public interface AuthService {
    LoginResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}