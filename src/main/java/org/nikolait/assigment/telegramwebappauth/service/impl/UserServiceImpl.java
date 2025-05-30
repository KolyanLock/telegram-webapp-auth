package org.nikolait.assigment.telegramwebappauth.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.nikolait.assigment.telegramwebappauth.entity.User;
import org.nikolait.assigment.telegramwebappauth.repository.UserRepository;
import org.nikolait.assigment.telegramwebappauth.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void syncWithDatabase(User user) {
        userRepository.findById(user.getId()).ifPresentOrElse(
                existingUser -> modelMapper.map(user, existingUser),
                () -> userRepository.save(user)
        );
    }

}
