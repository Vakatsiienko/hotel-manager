<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Iaroslav
  Date: 11/26/2016
  Time: 3:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Authentication</title>
</head>
<style type="text/css">
    #welcome {
        position: absolute;
        right: 45%;
    }

    #exception{
        color : red;
    }
</style>
<body>
<a href="/users/signin">/signin</a> <br>
<a href="/users/signup">/signup</a> <br>
<a href="/">/ (make reservation request)</a> <br>
<a href="/reservations/confirmed">/confirmed</a> <br>
<a href="/reservations/requested">/requests</a> <br>
<a href="/users/${loggedUser.id}">/user info</a> <br> <br> <br>
<c:if test="${!empty message}">
    <h3>${message}</h3>
</c:if>

<form action="${pageContext.request.contextPath}" id="welcome" method="post">
    <table >
        <tr>
            <th><label for="email">Email:</label></th>
            <td><input id="email" type="email" required maxlength="20" min="5" name="email">
            </td>
        </tr>
        <tr>
            <th><label for="password">Password:</label></th>
            <td><input id="password" type="password" required="true" maxlength="32" min="3"
                       name="password"></td>
        </tr>
        <tr>
            <c:if test="${!empty exception}">
                <td colspan="2" id = "exception">${exception}</td>
            </c:if>
        </tr>
        <tr>
            <td><br><input type="submit" value="Sign In"></td>
        </tr>
    </table>
</form>


</body>
</html>