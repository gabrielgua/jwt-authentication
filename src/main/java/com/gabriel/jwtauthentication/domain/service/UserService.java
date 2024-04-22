package com.gabriel.jwtauthentication.domain.service;

import com.gabriel.jwtauthentication.domain.exception.DuplicateEntryException;
import com.gabriel.jwtauthentication.domain.exception.GenericException;
import com.gabriel.jwtauthentication.domain.exception.UserNotFoundException;
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
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Transactional
    public User save(User user) {

        checkEmailAvailable(user.getEmail());

        if (checkUserIsNew(user.getId())) {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        return repository.save(user);
    }

    private void checkEmailAvailable(String email) {
        var existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new DuplicateEntryException(email);
        }
    }

    private boolean checkUserIsNew(Long userId) {
        return userId == null;
    }





}
