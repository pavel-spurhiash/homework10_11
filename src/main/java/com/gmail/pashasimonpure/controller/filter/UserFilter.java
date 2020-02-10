package com.gmail.pashasimonpure.controller.filter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gmail.pashasimonpure.controller.constant.RoleConstant.*;

public class UserFilter extends HttpFilter {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpSession session = request.getSession();

        String role = (String) session.getAttribute("role");

        if (role != null && role.equals(ROLE_TYPE_USER)) {

            chain.doFilter(request, response);

        } else {

            logger.error("Access denied.");
            response.sendError(403, "ACCESS DENIED."); //403 - access denied

        }

    }

}