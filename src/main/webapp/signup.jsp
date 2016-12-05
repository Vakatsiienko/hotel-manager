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
    </style>
</head>
<body>
<%--<form action="/users/signup" method="POST">--%>
<%--<table>--%>
<%--<tr>--%>
<%--<td>--%>
<%--<label for="email">Email:</label><br>--%>
<%--</td>--%>
<%--<td>--%>
<%--<input name="email" type="email" id="email" required maxlength="32">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td>--%>
<%--<label for="name">Name:</label><br>--%>
<%--</td>--%>
<%--<td>--%>
<%--<input name="name" type="test" id="name" required maxlength="20">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td>--%>
<%--<label for="phoneNumber">Phone Number:</label><br>--%>
<%--</td>--%>
<%--<td>--%>
<%--<input name="phoneNumber" type="tel" id="phoneNumber" required pattern="[\+][0-9]{10, 20}" maxlength="20">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td>--%>
<%--<label for="password">Password:</label>Password:<br>--%>
<%--</td>--%>
<%--<td>--%>
<%--<input name="password" type="password" id="password" required maxlength="32">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td>--%>
<%--<label for="passwordCheck">Password check:</label><br>--%>
<%--</td>--%>
<%--<td>--%>
<%--<input name="passwordCheck" type="password" id="passwordCheck" required="true" maxlength="32">--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td><br><input type="submit" value="Sign Up"></td>--%>
<%--</tr>--%>
<c:if test="${!empty exception}">
    <p style="color:red;">${exception}</p>
</c:if>
<%--</table>--%>
<%--</form>--%>


<form action="/users/signup" method="post" id="createForm">
    <table>
        <tr>
            <th class="ftitle" colspan="2">Creating User</th>
        </tr>
        <tr class="fitem">
            <th>
                <label for="name">Name:</label>
            </th>
            <td>
                <input id="name" name="name"
                       required>
            </td>
        </tr>
        <tr class="fitem">
            <th>
                <label for="phoneNumber">Phone Number:</label>
            </th>
            <td>
                <input id="phoneNumber" type="tel" pattern='[\+][0-9]{10, 20}'
                       value="+380"
                       title="Phone number should be in format '+38044567890'"
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
                    <p style="color:red;">${exception}</p>
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
