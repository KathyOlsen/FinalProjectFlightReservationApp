package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
    ArrayList<Role> findAll();
}
