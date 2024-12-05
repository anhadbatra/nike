package com.nike.nike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nike.nike.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
