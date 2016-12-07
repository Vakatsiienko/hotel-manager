<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reservation Info</title>
    <style type="text/css">
        #loggedUser {
            position: absolute;
            right: 0px;
            top:0px;
            background: gainsboro;
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
<div id="loggedUser">${loggedUser.name} <a href="/users/signout">logout</a></div>
<a href="/users/${loggedUser.id}">/user info</a> <br> <br> <br>
<c:if test="${!empty message}">
    <h3>${message}</h3>
</c:if>

<jsp:useBean id="reservation" scope="request"
             type="com.vaka.hotel_manager.domain.Reservation"/>
<c:choose>
    <c:when test="${reservation.status.name() == 'REQUESTED'}"><h3>Reservation is submitted and waiting for process</h3></c:when>
    <c:when test="${reservation.status.name()== 'CONFIRMED'}"><h3>Reservation is
        applied</h3></c:when>
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
        <td>${reservation.requestedRoomClass.toString()}</td>
    </tr>

    <tr>
        <th>Arrival Date</th>
        <td>${fn:formatDate(reservation.arrivalDate)}</td>
    </tr>

    <tr>
        <th>Departure Date</th>
        <td>${fn:formatDate(reservation.departureDate)}</td>
    </tr>
    <c:if test="${reservation.status.name().equals('CONFIRMED')}">
        <tr>
            <td></td>
            <td><a href="/bills/byReservation?id=${reservation.id}">Bill</a></td>
        </tr>
    </c:if>
</table>

</body>
</html>
