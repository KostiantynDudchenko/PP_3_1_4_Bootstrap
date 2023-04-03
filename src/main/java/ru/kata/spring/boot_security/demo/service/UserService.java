package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    void save(User user);

    User getUserById(Long id);

    void update(User user);

    void delete(Long id);

    User findByName(String name);
}
