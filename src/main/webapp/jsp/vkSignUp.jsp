<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Sign Up</title>
    <style type="text/css">
        .ftitle {
            text-align: center;
            border-bottom: 1px solid #ccc
        }

        #signup {
            position: absolute;
            right: 45%;
        }

        #exception {
            color: red;
        }
    </style>
</head>
<body>
<%@include file="header.jspf" %>


<form action="/signup-vk" method="post" id="createForm">
    <table id="signup">
        <tr>
            <th class="ftitle" colspan="2"><fmt:message key="SignUp" bundle="${bundle}"/></th>
        </tr>
        <jsp:useBean id="vkUser" class="com.vaka.hotel_manager.domain.entities.User" scope="session"/>
        <tr class="fitem">
            <th>
                <label for="name"><fmt:message key="Name" bundle="${bundle}"/>:</label>
            </th>
            <td>
                <input id="name" name="name"
                       required min="3" max="32" value="${vkUser.name}">
            </td>
        </tr>
        <tr class="fitem">
            <th>
                <label for="phoneNumber"><fmt:message key="PhoneNumber"
                                                      bundle="${bundle}"/>:</label>
            </th>
            <td>
                <input id="phoneNumber" type="tel" pattern='[\+]\d{12}'
                       value="+380"
                       title="Phone number should be in format '+380441234567'"
                       name="phoneNumber" required>
            </td>
        </tr>
        <tr class="fitem">
            <th>
                <label for="email"><fmt:message key="Email" bundle="${bundle}"/>:</label>
            </th>
            <td>
                <c:choose>
                    <c:when test="${empty vkUser.email}">
                        <input id="email" name="email" type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$"
                               required>
                    </c:when>
                    <c:otherwise>
                        <span id="email">${vkUser.email}</span>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <c:if test="${!empty exception}">
                    <p id="exception"><fmt:message key="${exception}" bundle="${bundle}"/></p>
                </c:if>
            </td>
        </tr>
        <tr>
            <th></th>
            <td>
                <input type="submit" name="submit"
                       value="<fmt:message key="Submit" bundle="${bundle}"/>"/>
                <a href="/"><fmt:message key="Cancel" bundle="${bundle}"/></a>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
