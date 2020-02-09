package com.gmail.pashasimonpure.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gmail.pashasimonpure.repository.ConnectionRepository;
import com.gmail.pashasimonpure.repository.RoleRepository;
import com.gmail.pashasimonpure.repository.impl.ConnectionRepositoryImpl;
import com.gmail.pashasimonpure.repository.impl.RoleRepositoryImpl;
import com.gmail.pashasimonpure.repository.model.Role;
import com.gmail.pashasimonpure.service.RoleService;
import com.gmail.pashasimonpure.service.model.RoleDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private ConnectionRepository connectionRepository;
    private RoleRepository roleRepository;

    private static RoleService instance;

    private RoleServiceImpl(
            ConnectionRepository connectionRepository,
            RoleRepository roleRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.roleRepository = roleRepository;
    }

    public static RoleService getInstance() {
        if (instance == null) {
            instance = new RoleServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    RoleRepositoryImpl.getInstance());

        }
        return instance;
    }

    @Override
    public boolean init() {

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {
                roleRepository.createTable(connection);

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
                roleRepository.dropTable(connection);

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
    public boolean deleteRole(Long id) {

        Boolean success = false;

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {

                roleRepository.delete(connection, id);

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
    public boolean add(RoleDTO roleDTO) {

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {

                Role role = new Role();
                role.setName(roleDTO.getName());
                role.setDescription(roleDTO.getDescription());
                //returns role object with id field:
                role = roleRepository.add(connection, role);

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
    public List<RoleDTO> findAll() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);

            try {
                List<RoleDTO> rolesDTO = new ArrayList<>();

                List<Role> roles = roleRepository.findAll(connection);

                for (Role role : roles) {

                    RoleDTO roleDTO = new RoleDTO();

                    roleDTO.setId(role.getId());
                    roleDTO.setName(role.getName());
                    roleDTO.setDescription(role.getDescription());

                    rolesDTO.add(roleDTO);
                }

                connection.commit();
                return rolesDTO;

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