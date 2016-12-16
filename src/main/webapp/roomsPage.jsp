<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jspf" %>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript">
        var roomTable;
        var requestTable;
        $(document).ready(function () {
            roomTable = $("#myTable").dataTable({
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
    <style type="text/css">
        #tableTitle {
            position: relative;
            left: 40%;
        }
    </style>
    <title><fmt:message key="RoomsPage" bundle="${bundle}"/></title>
</head>
<body>
<br> <br> <br>

<h3 id="tableTitle"><fmt:message key="RoomList" bundle="${bundle}"/></h3>
<table id="myTable" class="display" cellspacing="0" width="100%" >
    <thead>
    <tr>
        <th><fmt:message key="Number" bundle="${bundle}"/></th>
        <th><fmt:message key="Capacity" bundle="${bundle}"/></th>
        <th><fmt:message key="CostPerDay" bundle="${bundle}"/> ($)</th>
        <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
        <c:if test="${loggedUser.role.name().equals('MANAGER')}">
            <th><fmt:message key="Action" bundle="${bundle}"/></th>
        </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${roomList}" var="room">
        <jsp:useBean id="room" scope="page"
                     type="com.vaka.hotel_manager.domain.Room"/>
        <tr>
            <th>${room.number}</th>
            <th>${room.capacity}</th>
            <th>${room.costPerDay / 100}</th>
            <th><fmt:message key="${room.roomClazz.name()}" bundle="${bundle}"/></th>
            <c:if test="${loggedUser.role.name().equals('MANAGER')}">
                <th>
                    <button onclick="location.href='/rooms/${room.id}'">Edit</button>
                    <form action="/rooms/${room.id}" method="POST">
                        <input type="text" name="method" value="DELETE" hidden>
                        <input type="submit" name="submit"
                               value="<fmt:message key="Delete" bundle="${bundle}"/>"
                               required/>
                    </form>
                </th>
            </c:if>
        </tr>
    </c:forEach>

    </tbody>
</table>
</body>
</html>
