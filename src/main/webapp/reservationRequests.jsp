<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<html>
<head>
    <title>RequestsList</title>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript">
        var table;
        $(document).ready(function () {
            table = $("#myTable").dataTable({
                "dom": "<lftip>"
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
<p>For Bill go to the RequestInfo page</p>
<a href="/users/${loggedUser.id}">/user info</a> <br> <br> <br>
<c:if test="${!empty message}">
    <h3>${message}</h3>
</c:if>

<table id="myTable" class="display" cellspacing="0" width="100%">
    <thead>
    <tr>
        <th>Request Id</th>
        <th>Client Name</th>
        <th>Guests</th>
        <th>Class Type</th>
        <th>Created Date</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${reservationList}" var="reservation">
        <jsp:useBean id="reservation" scope="page"
                     type="com.vaka.hotel_manager.domain.Reservation"/>
        <tr>
            <th>${reservation.id}</th>
            <th>${reservation.user.name}</th>
            <th>${reservation.guests}</th>
            <th>${reservation.requestedRoomClass}</th>
            <th>${fn:formatDateTime(reservation.createdDatetime)}</th>
            <th>${reservation.status}</th>
            <th><a href="/reservations/process/${reservation.id}">Process</a><%--TODO add process request button--%>
            </th>
        </tr>
    </c:forEach>

    </tbody>
</table>
</body>
</html>
