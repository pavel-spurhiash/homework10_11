package com.gmail.pashasimonpure.repository.impl;

import com.gmail.pashasimonpure.repository.UserRepository;
import com.gmail.pashasimonpure.repository.model.Role;
import com.gmail.pashasimonpure.repository.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private static UserRepository instance;

    private UserRepositoryImpl() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void delete(Connection connection, Long id) throws SQLException {

        String sql = "DELETE FROM user WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("UserRepository: delete user failed, no rows affected.");
            }

        }
    }

    @Override
    public User add(Connection connection, User user) throws SQLException {

        String sql = "INSERT INTO user(username, password, created_by, role_id) VALUES (?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setDate(3, user.getDate());
            statement.setLong(4, user.getRole().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("UserRepository: add user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("UserRepository: add user failed, no ID obtained.");
                }
            }

            return user;
        }

    }

    @Override
    public User get(Connection connection, String userName) throws SQLException {

        String sql = "SELECT u.id, u.username, u.password, u.created_by, r.role_name, r.description " +
                "FROM user u JOIN role r ON ( u.username = ? and u.role_id = r.id )";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userName);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    Role role = new Role();

                    role.setName(rs.getString("role_name"));
                    role.setDescription(rs.getString("description"));

                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setDate(rs.getDate("created_by"));
                    user.setRole(role);

                    return user;
                }

                return null;
            }
        }
    }

    @Override
    public List<User> findAll(Connection connection) throws SQLException {

        String sql = "SELECT u.id, u.username, u.password, u.created_by, r.role_name, r.description " +
                "FROM user u JOIN role r ON ( u.role_id = r.id )";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            List<User> users = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    User user = new User();
                    Role role = new Role();

                    role.setName(rs.getString("role_name"));
                    role.setDescription(rs.getString("description"));

                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setDate(rs.getDate("created_by"));
                    user.setRole(role);

                    users.add(user);

                }

                return users;
            }
        }

    }

    @Override
    public void dropTable(Connection connection) throws SQLException {

        String sql = "DROP TABLE IF EXISTS user";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

    }

    @Override
    public void createTable(Connection connection) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS user (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(40) NOT NULL UNIQUE," +
                "password VARCHAR(40) NOT NULL, " +
                "created_by DATE NOT NULL, " +
                "role_id BIGINT NOT NULL, " +
                "FOREIGN KEY(role_id) REFERENCES role(id))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

    }

}