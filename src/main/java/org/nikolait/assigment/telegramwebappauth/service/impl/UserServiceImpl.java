package org.nikolait.assigment.telegramwebappauth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.nikolait.assigment.telegramwebappauth.entity.User;
import org.nikolait.assigment.telegramwebappauth.repository.UserRepository;
import org.nikolait.assigment.telegramwebappauth.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createOrUpdate(User user) {
        userRepository.findById(user.getId()).ifPresentOrElse(
                existingUser -> modelMapper.map(user, existingUser),
                () -> userRepository.save(user)
        );
        log.info("{} accepted!}", user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

}
