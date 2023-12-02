package com.dbdb.dbdb.domain.user.repository;

import com.dbdb.dbdb.domain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean insertUser(UserDto userDto) {
        // INSERT
        String sql = "INSERT INTO user (password, username, user_type, email, phone_number, " +
                "weight, age, last_accessed_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                userDto.getPassword(),
                userDto.getUsername(),
                userDto.getUser_type(),
                userDto.getEmail(),
                userDto.getPhone_number(),
                userDto.getWeight(),
                userDto.getAge(),
                userDto.getLast_accessed_at());
        return true;
    }

    public void updateLastAccessedAt(UserDto userDto) {
        String sql = "UPDATE user SET last_accessed_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, userDto.getLast_accessed_at(), userDto.getId());
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
            userdto.setId(findUserIdByEmail(userdto.getEmail()));
            updateLastAccessedAt(userdto);
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
            return null;
        }
    }

    public String findUserEmailById(int userId) {
        String sql = "SELECT email FROM user WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, String.class);
        } catch (EmptyResultDataAccessException e) {
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
        // UPDATE
        String sql = "UPDATE user SET password = ? WHERE email = ?";
        jdbcTemplate.update(sql, newPassword, email);
    }

    public void deleteUserById(int id) {
        // DELETE
        String sql = "DELETE FROM user WHERE id = ?";
            jdbcTemplate.update(sql, id);
    }

    public List<UserDto> returnAllUsers() {
        // SOME
        String sql = "SELECT * FROM user WHERE id = SOME (SELECT id FROM user WHERE id > 0)";
        return jdbcTemplate.query(
                sql,
                BeanPropertyRowMapper.newInstance(UserDto.class)
        );
    }

    public void modifyUser(UserDto userDto) {
        String sql = "UPDATE user SET email = ?, username = ?, password = ?, phone_number = ?, weight = ?, age = ?, total_money = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                userDto.getEmail(),
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getPhone_number(),
                userDto.getWeight(),
                userDto.getAge(),
                userDto.getTotal_money(),
                userDto.getId());
    }

    public UserDto findUserByEmail(String email) {
        // INTERSECT
        String sql = "SELECT * FROM user " +
                "INTERSECT " +
                "SELECT * FROM user WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UserDto findUserById(int id) {
        // EXCEPT
        String sql = "SELECT * FROM user " +
                "EXCEPT " +
                "SELECT * FROM user WHERE id <> ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

//     return jdbcTemplate.query(
//    sql,
//            BeanPropertyRowMapper.newInstance(UserDto.class)
//            );

    // RowMapper ����
    private static class UserRowMapper implements RowMapper<UserDto> {
        @Override
        public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserDto user = new UserDto();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setUser_type(rs.getInt("user_type"));
            user.setEmail(rs.getString("email"));
            user.setPhone_number(rs.getString("phone_number"));
            user.setWeight(rs.getDouble("weight"));
            user.setAge(rs.getInt("age"));
            user.setTotal_money(rs.getInt("total_money"));
            return user;
        }
    }
}
