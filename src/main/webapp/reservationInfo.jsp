<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Iaroslav
  Date: 12/1/2016
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reservation Info</title>
    <style type="text/css">
        #loggedUser {
            position: static;
            top: 0px;
            right: 0px;
            background: gainsboro;
        }
    </style>
</head>
<body>
<div id="loggedUser">${loggedUser.name} <a href="/signin/logout">logout</a></div>
<a href="/">/ (request reservation)</a> <br>
<a href="/reservations/confirmed">/confirmed</a> <br>
<a href="/login">/login</a> <br>
<a href="/reservations/requested">/requests</a> <br> <br> <br>
<jsp:useBean id="reservation" scope="request"
             type="com.vaka.domain.Reservation"/>
<c:choose>
    <c:when test="${reservation.status.name() == 'REQUESTED'}"><h3>Reservation is submitted and waiting for process</h3></c:when>
    <c:when test="${reservation.status.name()== 'CONFIRMED'}"><h3>Reservation is processed and waiting for apply</h3></c:when>
</c:choose>
<table id="reservationRequestTable" class="display" cellpadding="0">
    <tr><th colspan="2"><h2>Reservation Info</h2></th></tr>
    <tr>
        <th>Request Id:</th>
        <td>${reservation.id}</td>
    </tr>
    <tr>
        <th>Client Name</th>
        <td>${reservation.user.name}</td>
    </tr>

    <tr>
        <th>Phone Number</th>
        <td>${reservation.user.phoneNumber}</td>
    </tr>

    <tr>
        <th>Guests</th>
        <td>${reservation.guests}</td>
    </tr>

    <tr>
        <th>Room Class</th>
        <td>${reservation.requestedRoomClass}</td>
    </tr>

    <tr>
        <th>Arrival Date</th>
        <td>${reservation.arrivalDate}</td>
    </tr>

    <tr>
        <th>Departure Date</th>
        <td>${reservation.departureDate}</td>
    </tr>

</table>
</body>
</html>
