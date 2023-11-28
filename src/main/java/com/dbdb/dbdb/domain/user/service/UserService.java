package com.dbdb.dbdb.domain.user.service;

import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원 가입
    public void signUp(UserDto userDto) {
        try {
            userDto.setUser_type(1);
            userRepository.insertUser(userDto);
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
            userdto.setLast_accessed_at(LocalDateTime.now());
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

    public String findUserEmailById(int userId) {
        return userRepository.findUserEmailById(userId);
    }

    public UserDto findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public UserDto findUserByid(int id){
        return userRepository.findUserById(id);
    }

    public void withdrawal(int id) {
        userRepository.deleteUserById(id);
    }
}
