<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<a href="/users/signin">/signin</a> <br>
<a href="/users/signup">/signup</a> <br>
<a href="/">/ (make reservation request)</a> <br>
<a href="/reservations/confirmed">/confirmed</a> <br>
<a href="/reservations/requested">/requests</a> <br>
<p>For Bill go to the RequestInfo page</p>
<a href="/users/${loggedUser.id}">/user info</a> <br> <br> <br>
<c:if test="${!empty message}">
    <h3>${message}</h3>
</c:if>

<form action="/users/signup" method="post" id="createForm">
    <table id="signup">
        <tr>
            <th class="ftitle" colspan="2">Creating User</th>
        </tr>
        <tr class="fitem">
            <th>
                <label for="name">Name:</label>
            </th>
            <td>
                <input id="name" name="name"
                       required min="3" max="20">
            </td>
        </tr>
        <tr class="fitem">
            <th>
                <label for="phoneNumber">Phone Number:</label>
            </th>
            <td>
                <input id="phoneNumber" type="tel" pattern='[\+]\d{2}[\(]?\s?\d{3}[\)]?\s?\d{7}'
                       value="+38 044 "
                       title="Phone number should be in format '+38 044 1234567'"
                       name="phoneNumber" required>
            </td>
        </tr>
        <tr>
            <td></td>
            <td><span class="ftitle"></span></td>
        </tr>
        <tr class="fitem">
            <th>
                <label for="email">Email:</label>
            </th>
            <td>
                <input id="email" name="email" type="email"
                       required>
            </td>
        </tr>
        <tr>
            <td>
                <label for="password">Password:</label><br>
            </td>
            <td>
                <input name="password" type="password" id="password" required maxlength="32">
            </td>
        </tr>
        <tr>
            <td>
                <label for="passwordCheck">Password check:</label><br>
            </td>
            <td>
                <input name="passwordCheck" type="password" id="passwordCheck" required=
                       maxlength="32">
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <c:if test="${!empty exception}">
                    <p id ="exception">${exception}</p>
                </c:if>
            </td>
        </tr>
        <tr>
            <th></th>
            <td>
                <input type="submit" name="submit" value="submit"/>
                <a href="/">Cancel</a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
