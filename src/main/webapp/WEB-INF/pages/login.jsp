<%@ page import="static com.gmail.pashasimonpure.controller.constant.RegexConstant.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Log in</title>
        <link rel='stylesheet' href='css/style.css'>
    </head>

    <body>
        <div class="block">
            <h2>Log in</h2>

            <form action="login" method="post">
                <lable class="form_label">User Name:</lable>
                <input class="form_input" type="text" name="name" placeholder="required" required pattern="<%= USER_NAME_REGEX %>">
                <br>
                <lable class="form_label">Password:</lable>
                <input class="form_input" type="password" name="password" placeholder="required" required pattern="<%= PASSWORD_REGEX %>">
                <br><br>
                <input class="myButton" type="submit" value="Log in">
            </form>

            <p>If you click the "Submit" button, the form-data will be sent to a page called "/login".</p>

        </div>
    </body>
</html>