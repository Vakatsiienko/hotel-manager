<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="com.hotel_manager" %>

<%--
  Created by IntelliJ IDEA.
  User: Iaroslav
  Date: 12/6/2016
  Time: 4:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bill Information</title>
    <style type="text/css">
        #loggedUser {
            position: absolute;
            right: 0px;
            top: 0px;
            background: gainsboro;
        }

        #billTable {
            position: absolute;
            left: 30%;
            top: 20%
        }
    </style>
</head>
<body>
<div id="loggedUser">${loggedUser.name} <a href="/users/signout">logout</a></div>
<a href="/users/signin">/signin</a> <br>
<a href="/users/signup">/signup</a> <br>
<a href="/">/ (make reservation request)</a> <br>
<a href="/reservations/confirmed">/confirmed</a> <br>
<a href="/reservations/requested">/requests</a> <br>
<a href="/users/${loggedUser.id}">/user info</a> <br> <br> <br>
<c:if test="${!empty message}">
    <h3>${message}</h3>
</c:if>

<jsp:useBean id="bill" scope="request"
             type="com.vaka.domain.Bill"/>


<table id="billTable" class="display" cellpadding="0">
    <tr>
        <th colspan="2"><h2>Reservation Info</h2></th>
    </tr>
    <tr>
        <th>Bill Id:</th>
        <td>${bill.id}</td>
    </tr>
    <tr>
        <th>Client Name</th>
        <td>${bill.reservation.user.name}</td>
    </tr>

    <tr>
        <th>Guests</th>
        <td>${bill.reservation.guests}</td>
    </tr>

    <tr>
        <th>Room Class</th>
        <td>${bill.reservation.requestedRoomClass}</td>
    </tr>

    <tr>
        <th>Arrival Date</th>
        <td>${fn:formatDate(bill.reservation.arrivalDate)}</td>
    </tr>

    <tr>
        <th>Departure Date</th>
        <td>${fn:formatDate(bill.reservation.departureDate)}</td>
    </tr>

    <tr>
        <th>Room cost per day</th>
        <c:set var="costPerDay" value="${bill.reservation.room.costPerDay / 100}"/>
        <td>${costPerDay}$</td>
    </tr>

    <tr>
        <th>Total cost</th>
        <c:set var="totalCost" value="${bill.totalCost / 100}"/>
        <td>${totalCost}$</td>
    </tr>

</table>
</body>
</html>
