<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            requestTable = $("#reservationRequestTable").dataTable({
                "dom": "<lftip>"
            })
        })

    </script>
    <jsp:useBean id="requestRes" scope="page"
                 type="com.vaka.domain.ReservationRequest"/>
    <jsp:useBean id="rooms" scope="page"
                 type="java.util.List"/>
</head>
<body>
<div>
    <table id="reservationRequestTable" class="display" cellpadding="0" width="100%">
        <jsp:useBean id="requestRes.customer" scope="page"
                     type="com.vaka.domain.Customer"/>
        <thead>
        <tr>
            <th>Request Id:</th>
            <th>${requestRes.id}</th>
        </tr>
        <tr>
            <th>Client Name</th>
            <th>Phone Number</th>
            <th>Beds</th>
            <th>Room Class</th>
            <th>Cost</th>
            <th>Arrival Date</th>
            <th>Departure Date</th>
            <th>Commentary</th>
            <th>Action</th>
        </tr>
        </thead>
        <form action="/requests/process?id=${requestRes.id}" method="post">
            <tbody>
            <tr>
                <td>${requestRes.customer.name}</td>
                <td>${requestRes.customer.phoneNumber}</td>
                <td>${requestRes.numOfBeds}</td>
                <td>${requestRes.roomClass}</td>
                <td>${requestRes.totalCost}</td>
                <td>${requestRes.arrivalDate}</td>
                <td>${requestRes.departureDate}</td>
                <td>${requestRes.commentary}</td>
                <td></td>
            </tr>
            <tr>
                <td>
                    <%--customer name--%>
                </td>
                <td>
                    <%--customer phone number--%>
                </td>
                <td>
                    <input id="numOfBeds" name="numOfBeds" type="number" min="0">
                </td>
                <td>
                    <%--TODO add room class option--%>
                </td>
                <td>
                    <input id="totalCost" name="totalCost" type="number" min="0">
                </td>
                <td>
                    <input id="arrivalDate" name="arrivalDate" type="date">
                </td>
                <td>
                    <input id="departureDate" name="departureDate" type="date">
                </td>
                <td>
                </td>
                <td>
                    <input id="submit" name="submit" type="submit">
                </td>
            </tr>
            </tbody>
        </form>
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
                         type="com.vaka.domain.Room"/>
            <tr>
                <td>${room.number}</td>
                <td>${room.clazz}</td>
                <td>${room.numOfBeds}</td>
                <td>${room.costPerDay}</td>
                <td><a href="/rooms/applyForRequest?roomId=${room.id}&reqId=${requestRes.id}">Apply</a><%--TODO add apply process--%>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
