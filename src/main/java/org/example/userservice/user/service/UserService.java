package org.example.userservice.user.service;

import org.example.userservice.common.exception.ResourceNotFoundException;
import org.example.userservice.entity.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User service.
 */
@Service("userService")
public class UserService {

    /**
     * User repository.
     */
    private UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Fetch list of all users.
     * @return user list
     */
    @Transactional
    public List<User> fetchAll() {
        final List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(user -> userList.add(user));

        return userList;
    }

}
