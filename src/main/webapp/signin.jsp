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
</style>
<body>
<div id="hrefs">

    <form action="${pageContext.request.contextPath}" id="signinForm" method="post">
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
                <input id="email" type="email" value="${param.email}" required maxlength="20" min="5" name="email">
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
            </c:if>
            <%--TODO make exception param, --%>
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