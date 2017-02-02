<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jspf" %>

    <title><fmt:message key="ReservationInfo" bundle="${bundle}"/></title>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $("#availableRooms").dataTable({
                "dom": "<t>",
                "ordering": false,
                "language": {
                    "lengthMenu": '<fmt:message key="dataTable.lengthMenu" bundle="${bundle}"/>',
                    "zeroRecords": '<fmt:message key="dataTable.zeroRecords" bundle="${bundle}"/>',
                    "info": '<fmt:message key="dataTable.info" bundle="${bundle}"/>',
                    "infoEmpty": '<fmt:message key="dataTable.infoEmpty" bundle="${bundle}"/>',
                    "infoFiltered": '<fmt:message key="dataTable.infoFiltered" bundle="${bundle}"/>',
                    "search": '<fmt:message key="dataTable.search" bundle="${bundle}"/>',
                    "paginate": {
                        "first": '<fmt:message key="dataTable.first" bundle="${bundle}"/>',
                        "last": '<fmt:message key="dataTable.last" bundle="${bundle}"/>',
                        "next": '<fmt:message key="dataTable.next" bundle="${bundle}"/>',
                        "previous": '<fmt:message key="dataTable.previous" bundle="${bundle}"/>'
                    }
                }
            });
        });
    </script>

</head>
<body>
<jsp:useBean id="reservation" scope="request"
             type="com.vaka.hotel_manager.domain.entities.Reservation"/>
<c:choose>
    <c:when test="${reservation.status.name() == 'REQUESTED'}"><h3><fmt:message
            key="ReservationRequestedMessage"
            bundle="${bundle}"/></h3></c:when>
    <c:when test="${reservation.status.name()== 'CONFIRMED'}"><h3><fmt:message
            key="ReservationConfirmedMessage"
            bundle="${bundle}"/></h3></c:when>
    <c:when test="${reservation.status.name()== 'REJECTED'}"><h3><fmt:message
            key="ReservationRejectedMessage"
            bundle="${bundle}"/></h3></c:when>
</c:choose>
<table id="reservationRequestTable" class="display" cellpadding="0">
    <tr><th colspan="2"><h2><fmt:message key="ReservationInfo" bundle="${bundle}"/></h2></th></tr>
    <tr>
        <th><fmt:message key="ReservationId" bundle="${bundle}"/>:</th>
        <td>${reservation.id}</td>
    </tr>
    <tr>
        <th><fmt:message key="ClientName" bundle="${bundle}"/>:</th>
        <td>${reservation.user.name}</td>
    </tr>

    <tr>
        <th><fmt:message key="PhoneNumber" bundle="${bundle}"/>:</th>
        <td>${reservation.user.phoneNumber}</td>
    </tr>

    <tr>
        <th><fmt:message key="Guests" bundle="${bundle}"/>:</th>
        <td>${reservation.guests}</td>
    </tr>

    <tr>
        <th><fmt:message key="RoomClass" bundle="${bundle}"/>:</th>
        <td>${reservation.requestedRoomClass.name}</td>
    </tr>

    <tr>
        <th><fmt:message key="ArrivalDate" bundle="${bundle}"/>:</th>
        <td>${fn:formatDate(reservation.arrivalDate)}</td>
    </tr>

    <tr>
        <th><fmt:message key="DepartureDate" bundle="${bundle}"/>:</th>
        <td>${fn:formatDate(reservation.departureDate)}</td>
    </tr>
    <tr>
        <c:if test="${!reservation.status.name().equals('REJECTED')}">

            <td>
                <form action="/reservations/${reservation.id}/reject" method="post">
            <input type="submit" name="submit" value="<fmt:message key="Reject" bundle="${bundle}"/>" required/>
                </form>
            </td>
        </c:if>
            <td>
                <c:if test="${reservation.status.name().equals('CONFIRMED')}">
                <a href="/bills/byReservation?id=${reservation.id}"><fmt:message key="Bill"
                                                                                 bundle="${bundle}"/></a>
                </c:if>
            </td>
    </tr>

</table>

<c:if test="${!empty availableRooms}">
<h3 id="tableTitle"><fmt:message key="MatchingRooms" bundle="${bundle}"/></h3>
<table id="availableRooms" class="display" cellspacing="0" width="100%">
    <thead>
    <tr>
        <th><fmt:message key="Number" bundle="${bundle}"/></th>
        <th><fmt:message key="Capacity" bundle="${bundle}"/></th>
        <th><fmt:message key="CostPerDay" bundle="${bundle}"/> ($)</th>
        <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${availableRooms}" var="room">
        <jsp:useBean id="room" scope="page"
                     type="com.vaka.hotel_manager.domain.entities.Room"/>
        <tr>
            <th>${room.number}</th>
            <th>${room.capacity}</th>
            <th>${room.costPerDay / 100}</th>
            <th>${room.roomClass.name}</th>
        </tr>
    </c:forEach>

    </tbody>
    </c:if>

</body>
</html>
