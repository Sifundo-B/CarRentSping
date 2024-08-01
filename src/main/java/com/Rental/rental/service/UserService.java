package com.Rental.rental.service;

import com.Rental.rental.entity.User;

import java.util.Optional;

public interface UserService {
    User findByEmail(String email);
    Optional<User> findById(Long id);
    User registerUser(User user);

}
