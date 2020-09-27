package ua.com.taxi.service;

import ua.com.taxi.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findByUser(Integer userId);

    List<Role> findAll();
}
