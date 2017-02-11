<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%@include file="header.jspf" %>

    <title><fmt:message key="SignIn" bundle="${bundle}"/></title>
</head>
<style type="text/css">
    #signinForm {
        position: absolute;
        right: 45%;
    }

    #exception{
        color : red;
    }
    .message{
        right: 45%;
    }
</style>
<body>
<h3 class="message">Resetting DB every commit</h3>

<form
      <c:choose>
              <c:when test="${empty param.redirectUri}">action="/signin"</c:when>
        <c:otherwise>action="/signin?redirectUri=${param.redirectUri}"</c:otherwise>
</c:choose>
      id="signinForm" method="post">
    <table>
        <tr>
            <th colspan="2"><fmt:message key="Manager" bundle="${bundle}"/></th>
        </tr>
        <tr>
            <td colspan="2">admin@gmail.com / admin</td>
        </tr>
        <tr>
            <th colspan="2"><fmt:message key="Customer" bundle="${bundle}"/></th>
        </tr>
        <tr>
            <td colspan="2">customer@gmail.com / customer</td>
        </tr>
        <tr>
            <th>
                <label for="email">
                    <fmt:message key="Email" bundle="${bundle}"/>:
                </label>
            </th>
            <td>
                <input id="email" type="text" pattern="[^@\s]+@[^@\s]+\.[^@\s]+"
                <c:choose>
                       <c:when test="${!empty email}">
                       value="${email}"
                    <c:remove var="email" scope="session"/>
                </c:when>
                       <c:otherwise>value="${param.email}"</c:otherwise>
                </c:choose>
                       required maxlength="50"
                       min="5" name="email">
            </td>
        </tr>
        <tr>
            <th>
                <label for="password">
                    <fmt:message key="Password" bundle="${bundle}"/>:</label>
            </th>
            <td>
                <input id="password" type="password" required="true" maxlength="32" min="3"
                       name="password"></td>
        </tr>
        <tr>
            <c:if test="${!empty exception}">
                <td colspan="2" id = "exception"><fmt:message key="${exception}" bundle="${bundle}"/></td>
                <c:remove var="exception"/>
            </c:if>
        </tr>
        <tr>
            <td><br><input type="submit"
                           value="<fmt:message key="SignIn" bundle="${bundle}"/>">
            </td>
        </tr>
    </table>
</form>

</body>
</html>