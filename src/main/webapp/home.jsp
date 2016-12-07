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
<%--beanName="com.vaka.hotel_manager.domain.User"/>--%>
<c:if test="${!empty loggedUser.name}">
    <span id="loggedUser">${loggedUser.name} <a href="/users/signout">logout</a></span>
</c:if>
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

<div id="addForm">
    <form action="/reservations" method="post" id="createForm">
        <table>
            <tr>
                <th class="ftitle" colspan="2">Customer contact info</th>
            </tr>
            <c:choose>
            <c:when test="${!empty loggedUser.name}">
            <tr class="fitem">
                <th>
                    <label for="nameReg">Name:</label>
                </th>
                <td>
                    <input id="nameReg" name="name"
                           value="${loggedUser.name}"
                           required readonly>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="emailReg">Email:</label>
                </th>
                <td>
                    <input id="emailReg" name="email" type="email" value="${loggedUser.email}"
                           required readonly>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="phoneNumberReg">Phone Number:</label>
                </th>
                <td>
                    <input id="phoneNumberReg" type="tel" pattern="[\+]\d{2}[\(]\d{3}[\)]\d{7}"
                           value="${loggedUser.phoneNumber}"
                           title="Phone number should be in format '+38(044)1234567'"
                           name="phoneNumber" readonly required>
                </td>
            </tr>
            <tr>
                </c:when>
                <c:otherwise>
                    <tr class="fitem">
                        <th>
                            <label for="name">Name:</label>
                        </th>
                        <td>
                            <input id="name" name="name"
                                   value="${loggedUser.name}"
                                   required>
                        </td>
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
                    <tr class="fitem">
                        <th>
                            <label for="phoneNumber">Phone Number:</label>
                        </th>
                        <td>
                            <input id="phoneNumber" type="tel" pattern="[\+]\d{2}[\(]?\s?\d{3}[\)]?\s?\d{7}"
                                   value="+380"
                                   title="Phone number should be in format '+38 044 1234567'"
                                   name="phoneNumber" required>
                        </td>
                    </tr>
                </c:otherwise>
                </c:choose>

            <tr>
                <th class="ftitle" colspan="2">Request Information</th>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="guests">Guests:</label>
                </th>
                <td>
                    <input id="guests" name="guests" type="number" min="1" required>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="roomClass">Room Class</label>
                </th>
                <td>
                    <select id="roomClass" name="roomClass">
                        <c:forEach items="${roomClasses}" var="clazz">
                            <jsp:useBean id="clazz" scope="page"
                                         type="com.vaka.hotel_manager.domain.RoomClass"/>
                            <option selected>${clazz.toString()}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="arrivalDate">Arrival Date</label>
                </th>
                <td>
                    <input id="arrivalDate" name="arrivalDate" type="date" required>
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
                    <input type="submit" name="submit" value="submit" required/>
                    <a href="/">Cancel</a>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
