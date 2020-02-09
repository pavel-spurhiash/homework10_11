package com.gmail.pashasimonpure.controller.servlet;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.pashasimonpure.service.RoleService;
import com.gmail.pashasimonpure.service.impl.RoleServiceImpl;
import com.gmail.pashasimonpure.service.model.RoleDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gmail.pashasimonpure.controller.constant.PageConstant.*;

public class RoleListServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private RoleService roleService = RoleServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<RoleDTO> roles = roleService.findAll();

        if (!roles.isEmpty()) {

            req.setAttribute("roles", roles);
            getServletContext().getRequestDispatcher(PAGE_ROLE_LIST).forward(req, resp);

        } else {

            logger.error("No roles in database.");
            resp.sendError(500, "EMPTY."); //500 - Internal Server Error

        }

    }

}