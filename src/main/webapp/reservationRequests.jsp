<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <%@include file="header.jspf" %>

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
                "dom": "<lftip>",
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
<%--TODO Add Title of the table--%>
<table id="myTable" class="display" cellspacing="0" width="100%">
    <thead>
    <tr>
        <th><fmt:message key="ReservationId" bundle="${bundle}"/></th>
        <th><fmt:message key="ClientId" bundle="${bundle}"/></th>
        <th><fmt:message key="Guests" bundle="${bundle}"/></th>
        <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
        <th><fmt:message key="CreatedDatetime" bundle="${bundle}"/></th>
        <th><fmt:message key="Action" bundle="${bundle}"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${reservationList}" var="reservation">
        <jsp:useBean id="reservation" scope="page"
                     type="com.vaka.hotel_manager.domain.DTO.ReservationDTO"/>
        <tr>
            <th>${reservation.id}</th>
            <th>${reservation.userId}</th>
            <th>${reservation.guests}</th>
            <th><fmt:message key="${reservation.requestedRoomClass.name()}" bundle="${bundle}"/></th>
            <th>${fn:formatDateTime(reservation.createdDatetime)}</th>
            <th><a href="/reservations/process/${reservation.id}"><fmt:message key="toProcess"
                                                                               bundle="${bundle}"/></a>
            </th>
        </tr>
    </c:forEach>

    </tbody>
</table>
</body>
</html>
