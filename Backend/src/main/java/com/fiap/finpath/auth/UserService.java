package com.fiap.finpath.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String name, String email, String plainTextPassword) {
        // Verificar se usuário já existe
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists with email: " + email);
        }

        User newUser = authService.registerUser(name, email, plainTextPassword);
        System.out.println("UserService: Registering new user: " + newUser.getEmail() + " with ID: " + newUser.getId());
        return userRepository.save(newUser);
    }

    public Optional<User> getUserById(String userId) {
        System.out.println("UserService: Retrieving user with ID: " + userId);
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        System.out.println("UserService: Retrieving user with email: " + email);
        return userRepository.findByEmail(email);
    }

    public boolean loginUser(String email, String plainTextPassword) {
        System.out.println("UserService: Attempting login for user: " + email);
        return authService.loginUser(email, plainTextPassword);
    }

    public User updateUser(String userId, String newName, String newPassword) {
        System.out.println("UserService: Updating user with ID: " + userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (newName != null) {
                user.setName(newName);
            }
            if (newPassword != null) {
                String hashedPassword = passwordEncoder.encode(newPassword);
                user.setPasswordHash(hashedPassword);
            }
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(String userId) {
        System.out.println("UserService: Deleting user with ID: " + userId);
        userRepository.deleteById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
