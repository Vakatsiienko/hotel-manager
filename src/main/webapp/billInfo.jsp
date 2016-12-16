<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bill Information</title>
    <style type="text/css">
        #billTable {
            position: absolute;
            left: 30%;
            top: 20%
        }
    </style>
</head>
<body>
<%@include file="header.jspf" %>

<jsp:useBean id="bill" scope="request"
             type="com.vaka.hotel_manager.domain.Bill"/>


<table id="billTable" class="display" cellpadding="0">
    <tr>
        <th colspan="2"><h2><fmt:message key="Bill" bundle="${bundle}"/></h2></th>
    </tr>
    <tr>
        <th><fmt:message key="BillId" bundle="${bundle}"/>:</th>
        <td>${bill.id}</td>
    </tr>
    <tr>
        <th><fmt:message key="ClientName" bundle="${bundle}"/>:</th>
        <td>${bill.reservation.user.name}</td>
    </tr>

    <tr>
        <th><fmt:message key="Guests" bundle="${bundle}"/>:</th>
        <td>${bill.reservation.guests}</td>
    </tr>

    <tr>
        <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
        <td><fmt:message key="${bill.reservation.requestedRoomClass}" bundle="${bundle}"/></td>
    </tr>

    <tr>
        <th><fmt:message key="ArrivalDate" bundle="${bundle}"/></th>
        <td>${fn:formatDate(bill.reservation.arrivalDate)}</td>
    </tr>

    <tr>
        <th><fmt:message key="DepartureDate" bundle="${bundle}"/></th>
        <td>${fn:formatDate(bill.reservation.departureDate)}</td>
    </tr>

    <tr>
        <th><fmt:message key="CostPerDay" bundle="${bundle}"/></th>
        <c:set var="costPerDay" value="${bill.reservation.room.costPerDay / 100}"/>
        <td>${costPerDay}$</td>
    </tr>

    <tr>
        <th><fmt:message key="TotalCost" bundle="${bundle}"/></th>
        <c:set var="totalCost" value="${bill.totalCost / 100}"/>
        <td>${totalCost}$</td>
    </tr>

</table>
</body>
</html>
