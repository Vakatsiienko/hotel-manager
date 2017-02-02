<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <%@include file="header.jspf" %>
    <title>User Information</title>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>
    <style type="text/css">

        #userTable {
            position: absolute;
            left: 20%;
            top: 1%;
        }

    </style>
    <script type="text/javascript">
        $(function () {
            $("#reservationTable").dataTable({
                "dom": "<tp>",
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

<jsp:useBean id="user" scope="request"
             type="com.vaka.hotel_manager.domain.entity.User"/>
<table id="userTable" class="display" cellpadding="0">
    <tr>
        <th colspan="2"><h2><fmt:message key="ContactInfo" bundle="${bundle}"/></h2></th>
    </tr>
    <tr>
        <th><fmt:message key="Name" bundle="${bundle}"/></th>
        <td><c:out value="${user.name}"/></td>
    </tr>

    <tr>
        <th><fmt:message key="Email" bundle="${bundle}"/></th>
        <td><c:out value="${user.email}"/> </td>
    </tr>

    <tr>
        <th><fmt:message key="PhoneNumber" bundle="${bundle}"/></th>
        <td>${user.phoneNumber}</td>
    </tr>

</table>
<br>
<br>
<br>
<h3><fmt:message key="Reservations" bundle="${bundle}"/>:</h3>
<table id="reservationTable" class="display" cellspacing="0" width="100%">
    <thead>
    <tr>
        <th><fmt:message key="ReservationId" bundle="${bundle}"/></th>
        <th><fmt:message key="Guests" bundle="${bundle}"/></th>
        <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
        <th><fmt:message key="ArrivalDate" bundle="${bundle}"/></th>
        <th><fmt:message key="DepartureDate" bundle="${bundle}"/></th>
        <th><fmt:message key="Status" bundle="${bundle}"/></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${reservationList}" var="reservation">
        <tr>
            <th>${reservation.id}</th>
            <th>${reservation.guests}</th>
            <th>${reservation.requestedRoomClass.name}</th>
            <th>${reservation.arrivalDate}</th>
            <th>${reservation.departureDate}</th>
            <th>${reservation.status}</th>
            <th><a href="/reservations/${reservation.id}"><fmt:message key="Details"
                                                                       bundle="${bundle}"/></a>
            </th>
        </tr>
    </c:forEach>

    </tbody>
</table>
</body>
</html>
