package com.dbdb.dbdb.domain.user.service;

import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    // ��� ���� ���� Ȯ��
    public List<UserDto> getAllUsers() {
        return userRepository.returnAllUsers();
    }

    public void modifyUser(UserDto userDto) {
        userRepository.modifyUser(userDto);
    }

    public void deleteUser(int id) {
        userRepository.deleteUserById(id);
    }
}
