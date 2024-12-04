package com.nike.nike;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nike.nike.Models.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    boolean existsByEmail(String email); // To check if a user exists
    Optional<Users> findByEmail(String email); // To fetch the user
}
