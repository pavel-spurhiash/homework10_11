package com.gmail.pashasimonpure.controller.servlet;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gmail.pashasimonpure.service.UserService;
import com.gmail.pashasimonpure.service.impl.UserServiceImpl;
import com.gmail.pashasimonpure.service.model.UserDTO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gmail.pashasimonpure.controller.constant.PageConstant.*;
import static com.gmail.pashasimonpure.controller.constant.RegexConstant.*;
import static com.gmail.pashasimonpure.controller.constant.RoleConstant.*;

public class LoginServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserService userService = UserServiceImpl.getInstance();

    private boolean validate(HttpServletRequest req) {

        String userName = req.getParameter("name");
        String password = req.getParameter("password");

        if (!Pattern.compile(USER_NAME_REGEX).matcher(userName).matches()) {
            return false;
        } else if (!Pattern.compile(PASSWORD_REGEX).matcher(password).matches()) {
            return false;
        }

        return true;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (validate(request)) {

            String userName = request.getParameter("name");
            String password = request.getParameter("password");

            try {
                //check if user exists, else null pointer exception:
                UserDTO user = userService.find(userName);

                if (user.getPassword().equals(password)) {

                    session.setAttribute("logged", true);
                    session.setAttribute("role", user.getRoleName());

                    if (user.getRoleName().equals(ROLE_TYPE_ADMIN)) {
                        resp.sendRedirect(request.getContextPath()+"/roles");
                    } else if (user.getRoleName().equals(ROLE_TYPE_USER)) {
                        resp.sendRedirect(request.getContextPath()+"/users");
                    }

                } else {

                    logger.error("Incorrect password.");
                    resp.sendError(400, "WRONG PASSWORD."); //400 - bad request

                }

            } catch (NullPointerException e) {

                logger.error("User not found.");
                resp.sendError(404, "USER NOT FOUND."); //404 - not found

            }

        } else {

            logger.error("Incorrect parameter format.");
            resp.sendError(400, "WRONG FORMAT."); //400 - bad request

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        getServletContext().getRequestDispatcher(PAGE_LOGIN).forward(req, resp);

    }
}