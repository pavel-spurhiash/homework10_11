package com.gmail.pashasimonpure.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gmail.pashasimonpure.repository.RoleRepository;
import com.gmail.pashasimonpure.repository.model.Role;

public class RoleRepositoryImpl implements RoleRepository {

    private static RoleRepository instance;

    private RoleRepositoryImpl() {
    }

    public static RoleRepository getInstance() {
        if (instance == null) {
            instance = new RoleRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void delete(Connection connection, Long id) throws SQLException {

        String sql = "DELETE FROM role WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("RoleRepository: delete role failed, no rows affected.");
            }

        }
    }

    @Override
    public Role add(Connection connection, Role role) throws SQLException {

        String sql = "INSERT INTO role(role_name, description) VALUES (?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, role.getName());
            statement.setString(2, role.getDescription());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("RoleRepository: add role failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    role.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("RoleRepository: add role failed, no ID obtained.");
                }
            }

            return role;
        }

    }

    @Override
    public List<Role> findAll(Connection connection) throws SQLException {

        String sql = "SELECT id, role_name, description  FROM role";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            List<Role> roles = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getLong("id"));
                    role.setName(rs.getString("role_name"));
                    role.setDescription(rs.getString("description"));
                    roles.add(role);
                }

                return roles;
            }
        }

    }

    @Override
    public Role get(Connection connection, String roleName) throws SQLException {

        String sql = "SELECT id, role_name, description  FROM role WHERE role_name =?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, roleName);

            try (ResultSet rs = statement.executeQuery()) {

                Role role = new Role();
                if (rs.next()) {
                    role.setId(rs.getLong("id"));
                    role.setName(rs.getString("role_name"));
                    role.setDescription(rs.getString("description"));
                }

                return role;
            }
        }
    }

    @Override
    public void dropTable(Connection connection) throws SQLException {

        String sql = "DROP TABLE IF EXISTS role";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

    }

    @Override
    public void createTable(Connection connection) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS role (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "role_name VARCHAR(40) NOT NULL UNIQUE," +
                "description VARCHAR(40) NOT NULL)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

    }

}