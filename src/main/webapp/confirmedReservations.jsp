<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Iaroslav
  Date: 12/1/2016
  Time: 6:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Confirmed Reservations</title>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript">
        var roomTable;
        var requestTable;
        $(document).ready(function () {
            roomTable = $("#myTable").dataTable({
                "dom": "<lftip>",
            });
        })

    </script>
</head>
<body>
<a href="/">/ (request reservation)</a> <br>
<a href="/reservations/confirmed">/confirmed</a> <br>
<a href="/login">/login</a> <br>
<a href="/reservations/requested">/requests</a> <br> <br>

    <table id="myTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>Reservation Id</th>
            <th>Client Name</th>
            <th>Guests</th>
            <th>Class Type</th>
            <th>Room Number</th>
            <th>Arrival Date</th>
            <th>Departure Date</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reservationList}" var="reservation">
            <jsp:useBean id="reservation" scope="page"
                         type="com.vaka.domain.Reservation"/>
            <tr>
                <th>${reservation.id}</th>
                <th>${reservation.user.name}</th>
                <th>${reservation.guests}</th>
                <th>${reservation.requestedRoomClass}</th>
                <th>${reservation.room.number}</th>
                <th>${reservation.arrivalDate}</th>
                <th>${reservation.departureDate}</th>
                <th> <a href="/reservations/process/${reservation.id}">Process</a><%--TODO add process request button--%></th>
            </tr>
        </c:forEach>

        </tbody>
        </table>
</body>
</html>
