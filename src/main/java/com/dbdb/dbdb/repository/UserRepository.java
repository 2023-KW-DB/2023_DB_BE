package com.dbdb.dbdb.repository;

import com.dbdb.dbdb.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean insertUser(UserDto userDto){
        String sql = "INSERT INTO user (password, username, user_type, email, phone_number, weight, age) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                userDto.getPassword(),
                userDto.getUsername(),
                1,
                userDto.getEmail(),
                userDto.getPhone_number(),
                userDto.getWeight(),
                userDto.getAge());
        return true;
    }

    public UserDto.UserNameTypeDto findNameTypeNameById(int user_id) {
        var userMapper = BeanPropertyRowMapper.newInstance(UserDto.UserNameTypeDto.class);
        UserDto.UserNameTypeDto userNameTypeDto = jdbcTemplate.queryForObject(
                "SELECT username, user_type FROM `user` WHERE id=?",
                userMapper, user_id
        );

        return userNameTypeDto;
    }
}
