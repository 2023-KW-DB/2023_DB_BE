package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    // 모든 유저 정보 확인
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userRepository.returnAllUsers();
        return userRepository.returnAllUsers();
    }

    public void modifyUser(UserDto userDto) {
        userRepository.modifyUser(userDto);
    }

    public void deleteUser(int id) {
        userRepository.deleteUserById(id);
    }
}
