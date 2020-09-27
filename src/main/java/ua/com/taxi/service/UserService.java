package ua.com.taxi.service;

import ua.com.taxi.model.User;
import ua.com.taxi.model.dto.UserRegistrationDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByPhone(String phone);
    Optional<User> findById(Integer id);
    List<User> findAllUsers();
    void registrate(UserRegistrationDto user);

    void update(Integer id, String name, List<Integer> selectedRoles);
}
