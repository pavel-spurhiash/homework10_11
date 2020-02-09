package com.gmail.pashasimonpure.controller.filter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gmail.pashasimonpure.controller.constant.RoleConstant.*;

public class LoginFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();

        String role = (String) session.getAttribute("role");

        if (role != null ) {

            if(role.equals(ROLE_TYPE_USER)) {
                response.sendRedirect(request.getContextPath()+"/users");
            }else if(role.equals(ROLE_TYPE_ADMIN)){
                response.sendRedirect(request.getContextPath()+"/roles");
            }else{
                logger.error("Unknown role.");
                response.sendError(500, "UNKNOWN ROLE."); //500 - Internal Server Error
            }

        } else {

            chain.doFilter(req, resp);

        }

    }

}