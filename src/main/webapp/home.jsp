<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Iaroslav
  Date: 11/29/2016
  Time: 4:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Make Request</title>
    <script type="text/javascript">
        function compareTime(arrival, departure) {
            return new Date(arrival) < new Date(departure); // true if time1 is later
        }
    </script>
    <style type="text/css">

        th {
            /*text-align: left;*/
            text-align: right;
        }

        .ftitle {
            text-align: center;
            border-bottom: 1px solid #ccc
        }

        #loggedUser {
            position: absolute;
            right: 0px;
            top: 0px;
            background: gainsboro;
        }
    </style>
</head>
<body>
<%--<jsp:useBean id="loggedUser" scope="request"--%>
<%--beanName="com.vaka.domain.User"/>--%>
<span id="loggedUser">${loggedUser.name} <a href="/signin/logout">logout</a></span>
<a href="/">/ (request reservation)</a> <br>
<a href="/reservations/confirmed">/confirmed</a> <br>
<a href="/login">/login</a> <br>
<a href="/reservations/requested">/requests</a> <br>
<div id="addForm">
    <form action="/reservations" method="post" id="createForm">
        <table>
            <tr>
                <th class="ftitle" colspan="2">Customer contact info</th>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="name">Name:</label>
                </th>
                <td>
                    <input id="name" name="name"
                    <c:if test="${!empty loggedUser.name}"> value="${loggedUser.name}"</c:if>
                           required>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="email">Email:</label>
                </th>
                <td>
                    <input id="email" name="email" type="email" value="${loggedUser.email}"
                           required>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="phoneNumber">Phone Number:</label>
                </th>
                <td>
                    <input id="phoneNumber" type="tel" pattern="[\+][0-9]{10, 20}"
                    <c:choose>
                           <c:when test="${!empty loggedUser.email}">value="${loggedUser.phoneNumber}"</c:when>
                            <c:otherwise>value = "+380"</c:otherwise>
                    </c:choose>
                           title="Phone number should be in format '+38044567890'"
                           name="phoneNumber" required>
                </td>
            </tr>
            <tr>
                <th class="ftitle" colspan="2">Request Information</th>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="guests">Guests:</label>
                </th>
                <td>
                    <input id="guests" name="guests" type="number" min="1">
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="roomClazz">Room Class</label>
                </th>
                <td>
                    <select id="roomClazz" name="roomClazz">
                        <c:forEach items="${roomClazzez}" var="clazz">
                            <jsp:useBean id="clazz" scope="page"
                                         type="com.vaka.domain.RoomClass"/>
                            <option selected>${clazz.name()}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="arrivalDate">Arrival Date</label>
                </th>
                <td>
                    <input id="arrivalDate" name="arrivalDate" type="date">
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="departureDate">Departure Date</label>
                </th>
                <td>
                    <input id="departureDate" name="departureDate" type="date">
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
</div>
</body>
</html>
