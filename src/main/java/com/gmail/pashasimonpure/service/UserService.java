package com.gmail.pashasimonpure.service;

import com.gmail.pashasimonpure.service.model.UserDTO;

import java.util.List;

public interface UserService {

    boolean add(UserDTO userDTO);

    boolean deleteUser(Long id);

    List<UserDTO> findAll();

    UserDTO find(String userName);

    boolean init();

    boolean drop();

}