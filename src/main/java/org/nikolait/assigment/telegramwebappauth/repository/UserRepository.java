package org.nikolait.assigment.telegramwebappauth.repository;

import org.nikolait.assigment.telegramwebappauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
