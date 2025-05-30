package org.nikolait.assigment.telegramwebappauth.service;

import org.nikolait.assigment.telegramwebappauth.entity.User;

public interface UserService {
    void createOrUpdate(User user);
}
