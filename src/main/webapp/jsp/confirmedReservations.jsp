<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jspf" %>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>

    <title><fmt:message key="ConfirmedReservations" bundle="${bundle}"/></title>
    <link rel="stylesheet" type="text/css"
    href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
    src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript">
        function changeSize() {
            var selected = $("#tableSize").find("option:selected").text();
            var redirectUri = $(location).attr('pathname') + '?size=' + selected + '&page=' + getURLParameter("page", 1);
            window.location.replace(redirectUri);
        }

        var totalRows = ${reservationPage.totalLength};
        function paginationButtonsLoad() {
            var currentPage = parseInt(getURLParameter("page", 1));
            var currentSize = parseInt(getURLParameter("size", 10));
            document.getElementById("paginationFirst").href = $(location).attr('pathname') + '?page=1&size=' + currentSize;
            var previousPage;
            if (currentPage > 1)
                previousPage = currentPage - 1;
            else previousPage = 1;
            document.getElementById("paginationPrevious").href =
                    $(location).attr('pathname') + '?' + 'page=' + previousPage + '&size=' + currentSize;
            document.getElementById("paginationCurrent").href =
                    $(location).attr('pathname') + '?' + 'page=' + currentPage + '&size=' + currentSize;
            document.getElementById("paginationCurrent").textContent = currentPage.toString();
            var nextPage = currentPage;
            if (totalRows > (currentSize * currentPage)) {
                nextPage += 1;
            }
            document.getElementById("paginationNext").href =
                    $(location).attr('pathname') + '?' + 'page=' + nextPage + '&size=' + currentSize;
            var lastPage = Math.ceil(totalRows / currentSize);
            document.getElementById("paginationLast").href =
                    $(location).attr('pathname') + '?' + 'page=' + lastPage + '&size=' + currentSize;
        }

        function getURLParameter(sParam, defaultValue) {
            var sPageURL = window.location.search.substring(1);
            var sURLVariables = sPageURL.split('&');
            for (var i = 0; i < sURLVariables.length; i++) {
                var sParameterName = sURLVariables[i].split('=');
                if (sParameterName[0] == sParam) {
                    return sParameterName[1];
                }
            }
            return defaultValue;
        }

        var reservationTable;
        var requestTable;
        $(document).ready(function () {
            reservationTable = $("#myTable").dataTable({
                "dom": "<t>",
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
            paginationButtonsLoad();
            var val = getURLParameter('size', 10);
            var sel = document.getElementById('tableSize');
            var opts = sel.options;
            for (var opt, j = 0; opt = opts[j]; j++) {
                if (opt.value == val) {
                    sel.selectedIndex = j;
                    break;
                }
            }
        })
    </script>

    <style type="text/css">
        caption {
            font-weight: bold;
        }

        #paginationButtons {
            position: relative;
            left: 80%;
        }
    </style>
</head>
<body>
<div id="tableWrapper">

<select id="tableSize" onchange="changeSize()">
    <option value="5">5</option>
    <option value="10">10</option>
    <option value="20">20</option>
</select>
    <table id="myTable" class="display" cellspacing="0" width="100%">
        <caption><fmt:message key="ConfirmedReservations" bundle="${bundle}"/></caption>
        <thead>
        <tr>
            <th><fmt:message key="ReservationId" bundle="${bundle}"/></th>
            <th><fmt:message key="ClientId" bundle="${bundle}"/></th>
            <th><fmt:message key="Guests" bundle="${bundle}"/></th>
            <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
            <th><fmt:message key="RoomId" bundle="${bundle}"/></th>
            <th><fmt:message key="ArrivalDate" bundle="${bundle}"/></th>
            <th><fmt:message key="DepartureDate" bundle="${bundle}"/></th>
            <th><fmt:message key="Action" bundle="${bundle}"/></th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="reservationPage" type="com.vaka.hotel_manager.domain.Page" scope="request"/>
        <c:forEach items="${reservationPage.content}" var="reservation">
            <jsp:useBean id="reservation" scope="page"
                         type="com.vaka.hotel_manager.domain.DTO.ReservationDTO"/>
            <tr>
                <th><a href="/reservations/${reservation.id}">${reservation.id}</a></th>
                <th><a href="/users/${reservation.userId}">${reservation.userId}</a></th>
                <th>${reservation.guests}</th>
                <th>${reservation.requestedRoomClass.name}</th>
                <th>${reservation.roomId}</th>
                <th>${fn:formatDate(reservation.arrivalDate)}</th>
                <th>${fn:formatDate(reservation.departureDate)}</th>
                <th>
                    <form action="/reservations/${reservation.id}/reject" method="post">
                        <input type="submit" name="submit" value="<fmt:message key="Reject" bundle="${bundle}"/>" required/>
                    </form>
                </th>
            </tr>
        </c:forEach>

        </tbody>
        </table>
    <span id="totalRows"><fmt:message key="TotalRows"
                                      bundle="${bundle}"/>: ${reservationPage.totalLength}</span>
    <span id="paginationButtons">
        <a id="paginationFirst">First</a>
        <a id="paginationPrevious">Previous</a>
        <%--TODO add ?sort=abc&direction=asc--%>
        <%--TODO replace current with currentNumber--%>
        <a id="paginationCurrent"></a>
        <a id="paginationNext">Next</a>
        <a id="paginationLast">Last</a>
    </span>
</div>

</body>
</html>
