package com.dbdb.dbdb.repository;

import com.dbdb.dbdb.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean insertUser(UserDto userDto) {
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

    public Boolean checkEmailDuplicate(UserDto userDto) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        try {
            // queryForObject�� ����Ͽ� ����� 1 �̻��̸� �����ϴ� ������ �Ǵ�
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userDto.getEmail());
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public Boolean validateUser(UserDto userdto) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ? AND password = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userdto.getEmail(), userdto.getPassword());
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            // �̸��ϰ� ��й�ȣ�� ��ġ�ϴ� ���ڵ尡 ������ false ��ȯ
            return false;
        }
    }

    public Integer findUserIdByEmail(String email) {
        String sql = "SELECT id FROM user WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            // �̸��Ͽ� �ش��ϴ� user_id�� ������ null ��ȯ
            return null;
        }
    }

    public UserDto.UserNameTypeDto findNameTypeNameById ( int user_id){
        var userMapper = BeanPropertyRowMapper.newInstance(UserDto.UserNameTypeDto.class);
        UserDto.UserNameTypeDto userNameTypeDto = jdbcTemplate.queryForObject(
                "SELECT username, user_type FROM `user` WHERE id=?",
                userMapper, user_id
        );

        return userNameTypeDto;
    }

    public void updatePassword(String email, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE email = ?";
        jdbcTemplate.update(sql, newPassword, email);
    }

    public void deleteUserById(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
            jdbcTemplate.update(sql, id);
    }
}
