package org.nikolait.assigment.telegramwebappauth.service;

import org.nikolait.assigment.telegramwebappauth.entity.User;

import java.util.Optional;

public interface UserService {

    void createOrUpdate(User user);

    Optional<User> getUserById(Long id);

}
