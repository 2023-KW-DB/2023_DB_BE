package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원 가입
    public Boolean signUp(UserDto userDto) {
        return userRepository.insertUser(userDto);
    }

    // 이메일 중복확인
    public void checkEmailDuplicate(UserDto userDto) {
    }
}
