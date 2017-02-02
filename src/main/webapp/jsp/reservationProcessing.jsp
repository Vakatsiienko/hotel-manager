<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <%@include file="header.jspf" %>

    <title><fmt:message key="Processing" bundle="${bundle}"/></title>
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
                "dom": "<t>",
                "ordering": false,
                "language": {
                    "lengthMenu": '<fmt:message key="dataTable.lengthMenu" bundle="${bundle}"/>' ,
                    "zeroRecords": '<fmt:message key="dataTable.zeroRecords" bundle="${bundle}"/>' ,
                    "info": '<fmt:message key="dataTable.info" bundle="${bundle}"/>',
                    "infoEmpty": '<fmt:message key="dataTable.infoEmpty" bundle="${bundle}"/>',
                    "infoFiltered": '<fmt:message key="dataTable.infoFiltered" bundle="${bundle}"/>',
                    "search":         '<fmt:message key="dataTable.search" bundle="${bundle}"/>',
                    "paginate": {
                        "first":      '<fmt:message key="dataTable.first" bundle="${bundle}"/>',
                        "last":       '<fmt:message key="dataTable.last" bundle="${bundle}"/>',
                        "next":       '<fmt:message key="dataTable.next" bundle="${bundle}"/>',
                        "previous":   '<fmt:message key="dataTable.previous" bundle="${bundle}"/>'
                    }
                }
            });
        })

    </script>
</head>
<body>

<div>
    <jsp:useBean beanName="reservation" id="reservation" scope="request"
                 type="com.vaka.hotel_manager.domain.entities.Reservation"/>
    <table id="reservationRequestTable" class="display" cellpadding="0">
        <tr>
            <th><fmt:message key="ReservationId" bundle="${bundle}"/>:</th>
            <td>${reservation.id}</td>
        </tr>

        <tr>
            <th><fmt:message key="ClientName" bundle="${bundle}"/></th>
            <td>${reservation.user.name}</td>
        </tr>

        <tr>
            <th><fmt:message key="PhoneNumber" bundle="${bundle}"/></th>
            <td>${reservation.user.phoneNumber}</td>
        </tr>

        <tr>
            <th><fmt:message key="Guests" bundle="${bundle}"/></th>
            <td>${reservation.guests}</td>
        </tr>

        <tr>
            <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
            <td>${reservation.requestedRoomClass.name}</td>
        </tr>

        <tr>
            <th><fmt:message key="ArrivalDate" bundle="${bundle}"/></th>
            <td>${fn:formatDate(reservation.arrivalDate)}</td>
        </tr>

        <tr>
            <th><fmt:message key="DepartureDate" bundle="${bundle}"/></th>
            <td>${fn:formatDate(reservation.departureDate)}</td>
        </tr>
        <tr>
            <th></th>
            <th><form action="/reservations/${reservation.id}/reject" method="post">
                <input type="submit" name="submit" value="<fmt:message key="Reject" bundle="${bundle}"/>" required/>
            </form></th>
        </tr>

    </table>
</div>
<br>
<br>
<br>
<h3><fmt:message key="MatchingRooms" bundle="${bundle}"/>:</h3>
<div id="rooms">
    <table id="roomsTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th><fmt:message key="RoomNumber" bundle="${bundle}"/></th>
            <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
            <th><fmt:message key="Capacity" bundle="${bundle}"/></th>
            <th><fmt:message key="CostPerDay" bundle="${bundle}"/> ($)</th>
            <th><fmt:message key="Action" bundle="${bundle}"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rooms}" var="room">
            <jsp:useBean id="room" scope="page"
                         type="com.vaka.hotel_manager.domain.entities.Room"/>
            <tr>
                <td>${room.number}</td>
                <td>${room.roomClass.name}</td>
                <td>${room.capacity}</td>
                <c:set var="cost" value="${room.costPerDay /100}"/>
                <td>${cost}</td>
                <td><a href="/reservations/applyRoom?roomId=${room.id}&reservationId=${reservation.id}"><fmt:message
                        key="Apply" bundle="${bundle}"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
