<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<html>
<head>
    <title>Processing</title>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript">
        var roomTable;
        var requestTable;
        $(document).ready(function () {
            roomTable = $("#roomsTable").dataTable({
                "dom": "<lftip>",
            });
        })

    </script>
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

<div>
    <jsp:useBean beanName="reservation" id="reservation" scope="request"
                 type="com.vaka.hotel_manager.domain.Reservation"/>
    <table id="reservationRequestTable" class="display" cellpadding="0">
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
            <td>${fn:formatDate(reservation.arrivalDate)}</td>
        </tr>

        <tr>
            <th>Departure Date</th>
            <td>${fn:formatDate(reservation.departureDate)}</td>
        </tr>

    </table>
</div>
<br>
<br>
<br>
<h3>Matching rooms:</h3>
<div id="rooms">
    <table id="roomsTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>Room Number</th>
            <th>Room Class</th>
            <th>Beds</th>
            <th>Cost Per Day</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rooms}" var="room">
            <jsp:useBean id="room" scope="page"
                         type="com.vaka.hotel_manager.domain.Room"/>
            <tr>
                <td>${room.number}</td>
                <td>${room.roomClazz}</td>
                <td>${room.capacity}</td>
                <c:set var="cost" value="${room.costPerDay /100}"/>
                <td>${cost}</td>
                <td><a href="/reservations/applyRoom?roomId=${room.id}&reservationId=${reservation.id}">Apply</a><%--TODO add apply process--%>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
