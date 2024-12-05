package com.nike.nike.service;

import java.util.List;

import com.nike.nike.model.Role;

public interface RoleService {
    Role findByName(String name);
    Role saveRole(Role role);
    List<Role> getAllRoles();
}
