package com.gmail.pashasimonpure.controller.listener;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

import com.gmail.pashasimonpure.controller.util.RandomUtil;
import com.gmail.pashasimonpure.service.RoleService;
import com.gmail.pashasimonpure.service.UserService;
import com.gmail.pashasimonpure.service.impl.RoleServiceImpl;
import com.gmail.pashasimonpure.service.impl.UserServiceImpl;
import com.gmail.pashasimonpure.service.model.RoleDTO;
import com.gmail.pashasimonpure.service.model.UserDTO;

import static com.gmail.pashasimonpure.controller.constant.RoleConstant.*;

public class AppListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    private UserService userService = UserServiceImpl.getInstance();
    private RoleService roleService = RoleServiceImpl.getInstance();

    // Public constructor is required by servlet spec
    public AppListener() {}

    private RoleDTO generateRole(String roleName) {

        RoleDTO role = new RoleDTO();
        role.setName(roleName);
        role.setDescription("Role description"+RandomUtil.getRandomIntAbs());
        return role;

    }

    private UserDTO generateUser(String role) {

        UserDTO user = new UserDTO();
        user.setName("User"+RandomUtil.getRandomIntAbs());
        user.setPassword(String.valueOf(RandomUtil.getRandomIntAbs()));
        user.setDate(new Date(Calendar.getInstance().getTimeInMillis()));
        user.setRoleName(role);
        return user;

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        userService.drop();
        roleService.drop();

        roleService.init();
        userService.init();

        roleService.add(generateRole(ROLE_TYPE_USER));
        roleService.add(generateRole(ROLE_TYPE_ADMIN));

        userService.add(generateUser(ROLE_TYPE_USER));
        userService.add(generateUser(ROLE_TYPE_ADMIN));
    }

}