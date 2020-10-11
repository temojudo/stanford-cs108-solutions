<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>Creating New Account</h1>

<h3>
    <c:if test="${error != null}">
        Register failed: ${error}
    </c:if>
</h3>

<h4>please enter proposed name and password</h4>

<form method="POST">
    <label for="username">Username</label>
    <input type="text" name="username" value="${username}"/><br/>
    <label for="password">Password</label>
    <input type="password" name="password"/>
    <button type="submit">Register</button>
</form>

</body>
</html>