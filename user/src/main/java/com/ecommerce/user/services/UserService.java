package com.ecommerce.user.services;


import com.ecommerce.user.dto.UserDTO;
import com.ecommerce.user.models.User;
import com.ecommerce.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper ;

    public List<UserDTO> fetchAllUsers() {

        return userRepository.findAll().stream().map(user -> modelMapper.map(user,UserDTO.class)).collect(Collectors.toList());
    }
    public void addUser(UserDTO user) {

        userRepository.save(modelMapper.map(user, User.class));
    }

    public Optional<UserDTO> fetchUserById(Long id) {

        return userRepository.findById(id).map(user -> modelMapper.map(user, UserDTO.class));
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(UserDTO user, Long id) {

        return userRepository.findById(id).map(u -> { u.setFirstName(user.getFirstName());u.setLastName(user.getLastName()); userRepository.save(u);return u;}).orElseGet(null);
    }
}
