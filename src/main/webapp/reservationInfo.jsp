<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jspf" %>

    <title><fmt:message key="ReservationInfo" bundle="${bundle}"/></title>

</head>
<body>
<jsp:useBean id="reservation" scope="request"
             type="com.vaka.hotel_manager.domain.Reservation"/>
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
    <tr><th colspan="2"><h2>Reservation Info</h2></th></tr>
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
        <td><fmt:message key="${reservation.requestedRoomClass.name()}" bundle="${bundle}"/></td>
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

<td><form action="/reservations/${reservation.id}/reject" method="post">
            <input type="submit" name="submit" value="<fmt:message key="Reject" bundle="${bundle}"/>" required/>
        </form></td>
</c:if>
        <c:if test="${reservation.status.name().equals('CONFIRMED')}">
            <td><a href="/bills/byReservation?id=${reservation.id}">Bill</a></td>
    </c:if>
    </tr>

</table>

</body>
</html>
