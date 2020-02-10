package com.gmail.pashasimonpure.service;

import java.util.List;

import com.gmail.pashasimonpure.service.model.RoleDTO;

public interface RoleService {

    boolean add(RoleDTO roleDTO);

    boolean deleteRole(Long id);

    List<RoleDTO> findAll();

    boolean init();

    boolean drop();

}