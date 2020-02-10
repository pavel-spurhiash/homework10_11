package com.gmail.pashasimonpure.service.impl;

import com.gmail.pashasimonpure.repository.ConnectionRepository;
import com.gmail.pashasimonpure.repository.RoleRepository;
import com.gmail.pashasimonpure.repository.UserRepository;
import com.gmail.pashasimonpure.repository.impl.ConnectionRepositoryImpl;
import com.gmail.pashasimonpure.repository.impl.RoleRepositoryImpl;
import com.gmail.pashasimonpure.repository.impl.UserRepositoryImpl;
import com.gmail.pashasimonpure.repository.model.Role;
import com.gmail.pashasimonpure.repository.model.User;
import com.gmail.pashasimonpure.service.UserService;
import com.gmail.pashasimonpure.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private ConnectionRepository connectionRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private static UserService instance;

    private UserServiceImpl(
            ConnectionRepository connectionRepository,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    UserRepositoryImpl.getInstance(),
                    RoleRepositoryImpl.getInstance());

        }
        return instance;
    }

    @Override
    public boolean init() {

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {
                userRepository.createTable(connection);

                connection.commit();
                return true;

            } catch (SQLException e) {

                connection.rollback();
                logger.error(e.getMessage(), e);
                return false;

            }
        } catch (SQLException e) {

            logger.error(e.getMessage(), e);
            return false;

        }

    }

    @Override
    public boolean drop() {

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {
                userRepository.dropTable(connection);

                connection.commit();
                return true;

            } catch (SQLException e) {

                connection.rollback();
                logger.error(e.getMessage(), e);
                return false;

            }
        } catch (SQLException e) {

            logger.error(e.getMessage(), e);
            return false;

        }

    }

    @Override
    public boolean deleteUser(Long id) {

        Boolean success = false;

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {

                userRepository.delete(connection, id);

                connection.commit();
                success = true;

            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return success;
    }

    @Override
    public boolean add(UserDTO userDTO) {

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {
                //check if exists role, else exception:
                Role role = roleRepository.get(connection, userDTO.getRoleName());

                User user = new User();
                user.setName(userDTO.getName());
                user.setPassword(userDTO.getPassword());
                user.setRole(role);
                user.setDate(userDTO.getDate());
                //returns user object with id field:
                user = userRepository.add(connection, user);

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                return false;
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public UserDTO find(String userName) {

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {
                User user = userRepository.get(connection, userName);

                UserDTO userDTO = new UserDTO();

                userDTO.setId(user.getId());
                userDTO.setName(user.getName());
                userDTO.setPassword(user.getPassword());
                userDTO.setDate(user.getDate());
                userDTO.setRoleName(user.getRole().getName());
                userDTO.setRoleDescription(user.getRole().getDescription());

                connection.commit();
                return userDTO;

            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return null;

    }

    @Override
    public List<UserDTO> findAll() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {
                List<UserDTO> usersDTO = new ArrayList<>();

                List<User> users = userRepository.findAll(connection);

                for (User user : users) {

                    UserDTO userDTO = new UserDTO();

                    userDTO.setId(user.getId());
                    userDTO.setName(user.getName());
                    userDTO.setPassword(user.getPassword());
                    userDTO.setDate(user.getDate());
                    userDTO.setRoleName(user.getRole().getName());
                    userDTO.setRoleDescription(user.getRole().getDescription());

                    usersDTO.add(userDTO);
                }

                connection.commit();
                return usersDTO;

            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return Collections.emptyList();
    }

}