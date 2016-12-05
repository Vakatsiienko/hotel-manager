<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
<div id="loggedUser">${loggedUser.name} <a href="/signin/logout">logout</a></div>
<a href="/">/ (request reservation)</a> <br>
<a href="/reservations/confirmed">/confirmed</a> <br>
<a href="/login">/login</a> <br>
<a href="/reservations/requested">/requests</a> <br> <br>


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
                     type="com.vaka.domain.Reservation"/>
        <tr>
            <th>${reservation.id}</th>
            <th>${reservation.user.name}</th>
            <th>${reservation.guests}</th>
            <th>${reservation.requestedRoomClass}</th>
            <th>${reservation.createdDatetime}</th>
            <th>${reservation.status}</th>
            <th><a href="/reservations/process/${reservation.id}">Process</a><%--TODO add process request button--%>
            </th>
        </tr>
    </c:forEach>

    </tbody>
</table>
</body>
</html>