package com.manajemen.asset.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manajemen.asset.dto.ChangePasswordRequest;
import com.manajemen.asset.dto.UserDto;
import com.manajemen.asset.entity.User;
import com.manajemen.asset.mapper.UserMapper;
import com.manajemen.asset.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(UserDto userDto) {
        // Encode password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // Save user
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return UserMapper.mapToUserDto(user);
        }
        return null;
    }

    public UserDto getUserProfile(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserDto(user);
    }

    @Transactional
    public User updateUserProfile(long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return userRepository.save(user);
    }

    public boolean changePassword(long id, ChangePasswordRequest passwordRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
