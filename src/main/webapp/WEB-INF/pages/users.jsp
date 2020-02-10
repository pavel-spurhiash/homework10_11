<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

    <head>
        <title>User List</title>
        <link rel='stylesheet' href='css/table.css'>
        <link rel='stylesheet' href='css/style.css'>
    </head>

    <body>

        <h2>User List</h2>
        <table >
            <tr>
                <td>User ID</td>
                <td>User Name</td>
                <td>Password</td>
                <td>Created</td>
                <td>Role</td>
                <td>Role Description</td>
            </tr>

            <c:forEach var="user" items="${users}">
                <tr>
                    <td><c:out value="${user.id}"/></td>
                    <td><c:out value="${user.name}"/></td>
                    <td><c:out value="${user.password}"/></td>
                    <td><c:out value="${user.date}"/></td>
                    <td><c:out value="${user.roleName}"/></td>
                    <td><c:out value="${user.roleDescription}"/></td>
                </tr>
            </c:forEach>

        </table>

    </body>
</html>