<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Login</title>
    </head>
    <body>
        <h1>
            <c:if test="${message != null}">${message}</c:if>
        </h1>

        <h2>
            <c:if test="${error != null}">
                Authentication failed: ${error}
            </c:if>
        </h2>

        <form method="POST">
            <label for="username">User Name: </label>
            <input type="text" name="username" /><br/>
            <label for="password">Password: </label>
            <input type="password" name="password" />
            <button type="submit">Login</button>
        </form>

        <a href="/register">Create New Account</a>
    </body>
</html>
