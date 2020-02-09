package com.gmail.pashasimonpure.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GeneralRepository<T> {

    T add(Connection connection, T t) throws SQLException;

    T get(Connection connection, String name) throws SQLException;

    void delete(Connection connection, Long id) throws SQLException;

    void dropTable(Connection connection) throws SQLException;

    void createTable(Connection connection) throws SQLException;

    List<T> findAll(Connection connection) throws SQLException;

}