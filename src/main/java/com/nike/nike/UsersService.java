package com.nike.nike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nike.nike.Models.Users;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Users> findAll() {
        return usersRepository.findAll(); // Use JpaRepository's findAll() method
    }
    public Users findById(Integer id) {
        return usersRepository.findById(id).orElse(null);
    }
    public void delete(Integer id) {
        usersRepository.deleteById(id);
    }

    // User registration logic
    public String registerUser(Users user) {
        if (usersRepository.existsByEmail(user.getEmail())) { // Check if email exists
            return "Email is already registered!";
        }
        // Encode the password and save the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        return "User registered successfully!";
    }

    // User login logic
    public String loginUser(String email, String password, HttpSession session) {
        Optional<Users> optionalUser = usersRepository.findByEmail(email); // Fetch user by email
        if (optionalUser.isEmpty()) {
            return "User not found"; // User does not exist
        }

        Users user = optionalUser.get();

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "login";
        }

        // Redirect based on user role
        if (user.getRole() == 1) {
            return "addproduct"; // Normal user -> Product page
        } else if (user.getRole() == 2) {
            return "redirect:/products/management"; // Admin -> Product management page
        } else {
            return "Invalid role";
        }
    }
    public void save(Users user) {
        usersRepository.save(user);
    }
}
