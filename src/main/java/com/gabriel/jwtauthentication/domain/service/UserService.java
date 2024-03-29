package com.gabriel.jwtauthentication.domain.service;

import com.gabriel.jwtauthentication.domain.model.User;
import com.gabriel.jwtauthentication.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new RuntimeException(String.format("User not found for email: %s", email)));
    }

    @Transactional
    public User save(User user) {
        if (user.getId() == null) {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        return repository.save(user);
    }





}
