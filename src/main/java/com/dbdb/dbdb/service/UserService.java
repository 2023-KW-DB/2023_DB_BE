package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.repository.UserRepository;
import com.dbdb.dbdb.table.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원 가입
    public Boolean signUp(UserDto userDto) {
        try {
            return userRepository.insertUser(userDto);
        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    // 이메일 중복확인
    public Boolean checkEmailDuplicate(UserDto userDto) {
        try {
            return userRepository.checkEmailDuplicate(userDto);
        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    // 로그인
    public Boolean signIn(UserDto userdto) {
        try {
            return userRepository.validateUser(userdto);
        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public int findUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }

    public void withdrawal(int id) {
        userRepository.deleteUserById(id);
    }
}
