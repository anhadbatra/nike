package com.nike.nike.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nike.nike.model.Role;
import com.nike.nike.model.User;
import com.nike.nike.repository.RoleRepository;
import com.nike.nike.repository.UserRepository;
import com.nike.nike.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        if (isUsernameTaken(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (isEmailTaken(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Save the user
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        // If it's a new user (ID is null), encode the password
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID must not be null for update operation.");
        }
        User existingUser = getUserById(user.getId());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        // Update password if it's not empty
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Update roles if provided
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }

        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

@Override
@Transactional
public void assignRolesToUser(User user, List<Long> roleIds) {
    if (roleIds == null || roleIds.isEmpty()) {
        throw new IllegalArgumentException("Role IDs cannot be null or empty.");
    }

    try {
        // Fetch roles by IDs
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("One or more Role IDs are invalid: " + roleIds);
        }

        // Assign roles to user
        user.setRoles(roles);
        user.setConfirmPassword("defaultPassword");
        userRepository.save(user);
        userRepository.flush();

        System.out.println("Roles successfully assigned to user: " + user.getUsername());
    } catch (DataAccessException dae) {
        System.err.println("Database error: " + dae.getMessage());
        dae.printStackTrace();
        throw dae; // Rethrow to ensure transaction rollback
    } catch (Exception e) {
        System.err.println("Error assigning roles to user: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}





    @Override
    public List<User> searchUsers(String keyword) {
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }
}
