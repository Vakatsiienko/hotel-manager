<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="com.hotel_manager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Information</title>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <style type="text/css">
        #loggedUser {
            position: absolute;
            right: 0px;
            top: 0px;
            background: gainsboro;
        }

        #userTable {
            position: absolute;
            left: 15%;
            top: 1%;
        }

    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#reservationTable").dataTable({
                "dom": "<t>"
            });
        })
    </script>
</head>
<body>
<div id="loggedUser">${loggedUser.name} <a href="/users/signout">logout</a></div>
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
<jsp:useBean id="user" scope="request"
             type="com.vaka.domain.User"/>
<table id="userTable" class="display" cellpadding="0">
    <tr>
        <th colspan="2"><h2>Your Info</h2></th>
    </tr>
    <tr>
        <th>Name</th>
        <td>${user.name}</td>
    </tr>

    <tr>
        <th>Email</th>
        <td>${user.name}</td>
    </tr>

    <tr>
        <th>Phone Number</th>
        <td>${user.phoneNumber}</td>
    </tr>

</table>
<br>
<br>
<br>
<h3>Active reservations:</h3>
<table id="reservationTable" class="display" cellspacing="0" width="100%">
    <thead>
    <tr>
        <th>Request Id</th>
        <th>Guests</th>
        <th>Room Class</th>
        <th>Arrival Date</th>
        <th>Departure Date</th>
        <th>Status</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${reservationList}" var="reservation">
        <jsp:useBean id="reservation" scope="page"
                     type="com.vaka.domain.Reservation"/>
        <tr>
            <th>${reservation.id}</th>
            <th>${reservation.guests}</th>
            <th>${reservation.requestedRoomClass}</th>
            <th>${reservation.arrivalDate}</th>
            <th>${reservation.departureDate}</th>
            <th>${reservation.status}</th>
            <th><a href="/reservations/${reservation.id}">Details</a>
            </th>
        </tr>
    </c:forEach>

    </tbody>
</table>
</body>
</html>
