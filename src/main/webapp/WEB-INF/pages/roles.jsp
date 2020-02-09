<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

    <head>
        <title>Role List</title>
        <link rel='stylesheet' href='css/table.css'>
        <link rel='stylesheet' href='css/style.css'>
    </head>

    <body>

        <h2>Role List</h2>
        <table >
            <tr>
                <td>Role ID</td>
                <td>Role Name</td>
                <td>Role Description</td>
            </tr>

            <c:forEach var="role" items="${roles}">
                <tr>
                    <td><c:out value="${role.id}"/></td>
                    <td><c:out value="${role.name}"/></td>
                    <td><c:out value="${role.description}"/></td>
                </tr>
            </c:forEach>

        </table>

    </body>
</html>