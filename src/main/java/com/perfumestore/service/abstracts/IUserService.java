package com.perfumestore.service.abstracts;

import com.perfumestore.dto.response.UserResponse;
import com.perfumestore.entity.User;

import java.util.List;

public interface IUserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    User getCurrentUser();

    void deleteUser(Long id);
}
