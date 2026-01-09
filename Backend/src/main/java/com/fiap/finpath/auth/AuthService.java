package com.fiap.finpath.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthService() {
        // Construtor padrão
    }

    public User registerUser(String name, String email, String plainTextPassword) {
        String hashedPassword = passwordEncoder.encode(plainTextPassword);

        User newUser = new User(name, email, hashedPassword);
        System.out.println("AuthService: Registering new user: " + newUser.getEmail() + " with ID: " + newUser.getId());
        return userRepository.save(newUser);
    }

    public boolean loginUser(String email, String plainTextPassword) {
        System.out.println("AuthService: Attempting login for user: " + email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(plainTextPassword, user.getPasswordHash());
        }
        return false;
    }

    public VerificationCode requestVerificationCode(String userEmail) {
        VerificationCode code = new VerificationCode(userEmail);
        System.out.println("AuthService: Generating and sending verification code for " + userEmail + ". Code: " + code.getCodeText());
        return code;
    }

    public boolean verifyUser(String userEmail, String codeText) {
        System.out.println("AuthService: Verifying user " + userEmail + " with code: " + codeText);
        // Em produção, verificar código no banco de dados
        return true;
    }

    public void updateUserName(String userId, String newName) {
        System.out.println("AuthService: Updating name for user " + userId + " to " + newName);
        // Implementação real seria feita no UserService
    }
}