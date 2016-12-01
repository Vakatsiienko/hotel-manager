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
<body>
<a href="/">/ (request reservation)</a> <br>
<a href="/reservations/confirmed">/confirmed</a> <br>
<a href="/login">/login</a> <br>
<a href="/reservations/requested">/requests</a> <br>
<jsp:useBean id="user" scope="request" beanName="com.vaka.domain.User"/>
<c:choose>
    <c:when test="${empty user.name}">
        <form action="/signin" method="post">
            <table>
                <tr>
                    <th><label for="login">Login:</label></th>
                    <td><input id="login" type="text" required="true" maxlength="16" name="login"></td>
                </tr>
                <tr>
                    <th><label for="password">Password:</label></th>
                    <td><input id="password" type="password" required="true" maxlength="16"
                               name="password"></td>
                </tr>
                <tr>
                    <td><br><input type="submit" value="Sign In"></td>
                </tr>
            </table>
        </form>
    </c:when>
    <c:when test="${!empty user}">
        <h2>Welcome ${user.name}</h2>
    </c:when>
</c:choose>



</body>
</html>
