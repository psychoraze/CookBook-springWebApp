package com.raze.springExam.services;

import com.raze.springExam.models.User;
import com.raze.springExam.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsersExcludingCurrent(Long userId) {
        List<User> allUsers = (List<User>) userRepository.findAll();
        return allUsers.stream()
                .filter(user -> !user.getId().equals(userId)) // Исключаем текущего пользователя
                .collect(Collectors.toList());
    }
}