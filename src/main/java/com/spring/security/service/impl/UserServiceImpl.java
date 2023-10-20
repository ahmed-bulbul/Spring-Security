package com.spring.security.service.impl;


import com.spring.security.dto.UserRegistrationDto;
import com.spring.security.entity.Role;
import com.spring.security.entity.User;
import com.spring.security.repository.RoleRepository;
import com.spring.security.repository.UserRepository;
import com.spring.security.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public User save(UserRegistrationDto registrationDto) {

        var user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto
                        .getPassword()),
                List.of(new Role("ROLE_ADMIN")));

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        var user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException
                    ("Invalid username or password.");
        }
        return new org.springframework.security
                .core.userdetails.User(user.getFirstName(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority>
    mapRolesToAuthorities(Collection<Role> roles) {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority
                        (role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {

        return userRepository.findAll();
    }
}