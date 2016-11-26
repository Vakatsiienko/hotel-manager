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
    <jsp:useBean id="request" scope="page"
                 type="com.vaka.domain.ReservationRequest"/>
    <jsp:useBean id="rooms" scope="page"
                 type="java.util.List"/>
</head>
<body>
<div>
    <table id="reservationRequestTable" class="display" cellpadding="0" width="100%">
        <jsp:useBean id="request.customer" scope="page"
                     type="com.vaka.domain.User"/>
        <thead>
        <tr>
            <th>Request Id:</th>
            <th>${request.id}</th>
        </tr>
        <tr>
            <th>Client Name</th>
            <th>Beds</th>
            <th>Room Class</th>
            <th>Bathroom Type</th>
            <th>Cost</th>
            <th>Commentary</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${request.customer.name}</td>
            <td>${request.numOfBeds}</td>
            <td>${request.roomClass}</td>
            <td>${request.bathroomType}</td>
            <td>${request.totalCost}</td>
            <td>${request.commentary}</td>
            <td></td>
        </tr>
        <%--<form>--%>
            <%--<tr>--%>
                <%--<td>--%>
                <%--</td>--%>
            <%--</tr>--%>
        <%--</form>--%>
        </tbody>
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
            <th>Bathroom Type</th>
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
                <td>${room.bathroomType}</td>
                <td>${room.numOfBeds}</td>
                <td>${room.costPerDay}</td>
                <td><a href="requests/apply?reqId=${request.id}&roomId=${room.id}">Apply</a><%--TODO add apply process--%>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
