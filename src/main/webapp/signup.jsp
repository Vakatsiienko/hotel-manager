<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Sign Up Page</title>
    <style type="text/css">
        .ftitle {
            text-align: center;
            border-bottom: 1px solid #ccc
        }
        #signup {
            position: absolute;
            right: 45%;
        }

        #exception{
            color : red;
        }
    </style>

</head>
<body>
<div id="hrefs">
    <%@include file="header.jspf" %>

<form action="${pageContext.request.contextPath}" method="post" id="createForm">
    <table id="signup">
        <tr>
            <th class="ftitle" colspan="2"><fmt:message key="SignUp" bundle="${bundle}"/></th>
        </tr>
        <tr class="fitem">
            <th>
                <label for="name"><fmt:message key="Name" bundle="${bundle}"/>:</label>
            </th>
            <td>
                <input id="name" name="name"
                       required min="3" max="20">
            </td>
        </tr>
        <tr class="fitem">
            <th>
                <label for="phoneNumber"><fmt:message key="PhoneNumber" bundle="${bundle}"/>:</label>
            </th>
            <td>
                <input id="phoneNumber" type="tel" pattern='[\+]\d{2}[\(]?\s?\d{3}[\)]?\s?\d{7}'
                       value="+3 80"
                       title="Phone number should be in format '+38 044 1234567'"
                       name="phoneNumber" required>
            </td>
        </tr>
        <tr class="fitem">
            <th>
                <label for="email"><fmt:message key="Email" bundle="${bundle}"/>:</label>
            </th>
            <td>
                <input id="email" name="email" type="email"
                       required>
            </td>
        </tr>
        <tr class="fitem">
            <td>
                <label for="password"><fmt:message key="Password" bundle="${bundle}"/>:</label><br>
            </td>
            <td>
                <input name="password" type="password" id="password" required maxlength="32">
            </td>
        </tr>
        <tr class="fitem">
            <td>
                <label for="passwordCheck"><fmt:message key="PasswordCheck" bundle="${bundle}"/>:</label><br>
            </td>
            <td>
                <input name="passwordCheck" type="password" id="passwordCheck" required=
                       maxlength="32">
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <c:if test="${!empty exception}">
                    <p id ="exception"><fmt:message key="${exception}" bundle="${bundle}"/></p><%--TODO set exception message to PasswordCheckException--%>
                </c:if>
            </td>
        </tr>
        <tr>
            <th></th>
            <td>
                <input type="submit" name="submit" value="<fmt:message key="Submit" bundle="${bundle}"/>"/>
                <a href="/"><fmt:message key="Cancel" bundle="${bundle}"/></a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
