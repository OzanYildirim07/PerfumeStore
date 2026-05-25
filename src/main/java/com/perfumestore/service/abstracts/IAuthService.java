package com.perfumestore.service.abstracts;

import com.perfumestore.dto.request.LoginRequest;
import com.perfumestore.dto.request.RegisterRequest;
import com.perfumestore.dto.response.AuthResponse;

public interface IAuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);
}
