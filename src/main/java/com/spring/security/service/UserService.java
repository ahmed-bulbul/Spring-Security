package com.spring.security.service;

import com.spring.security.dto.UserRegistrationDto;
import com.spring.security.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);
    List<User> getAll();
}