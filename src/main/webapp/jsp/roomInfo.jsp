<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jspf" %>
    <title><fmt:message key="RoomInfo" bundle="${bundle}"/></title>
    <script type="text/javascript">
        //        function submitRoomForm() {
        //            if ($("#editRoomForm").checkValidity()) {
        //                var costPerDayInput = document.getElementById("costPerDay");
        //                costPerDayInput.value *= 100;
        //                document.getElementById("editRoomForm").submit();
        //            }
        //        }
        $(document).ready(function () {
            $('#editButton').click(function () {
                var costPerDayInput = document.getElementById("costPerDay");
                costPerDayInput.value *= 100;
                document.getElementById("editRoomForm").submit();
            })
        });
    </script>

</head>
<body>

<jsp:useBean id="room" scope="request"
             type="com.vaka.hotel_manager.domain.entity.Room" beanName="room"/>
<form name="editRoomForm" action="/rooms/${room.id}" method="post">

    <table id="roomTable" class="display" cellpadding="0">
        <tr>
            <th colspan="2"><h2><fmt:message key="RoomInfo" bundle="${bundle}"/></h2></th>
        </tr>
        <tr>
            <th><fmt:message key="ID" bundle="${bundle}"/></th>
            <td>${room.id}</td>
            <td><input type="number" name="id" value="${room.id}" hidden></td>

        </tr>
        <tr>
            <th><fmt:message key="Number" bundle="${bundle}"/></th>
            <td>${room.number}</td>
            <td><input type="number" name="number" value="${room.number}" min="0" step="1">
            </td>

        </tr>

        <tr>
            <th><fmt:message key="Capacity" bundle="${bundle}"/></th>
            <td>${room.capacity}</td>
            <td><input type="number" name="capacity" value="${room.capacity}" min="0" step="1">
            </td>
        </tr>

        <tr>
            <th><fmt:message key="CostPerDay" bundle="${bundle}"/> $</th>
            <td><fmt:formatNumber value="${room.costPerDay / 100}"/></td>
            <td><input id="costPerDay" type="number" name="costPerDay"
                       value="${room.costPerDay / 100}" min="0" step="1"></td>
        </tr>

        <tr>
            <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
            <td>${room.roomClass.name}</td>
            <td>
                <select name="roomClassId">
                    <c:forEach items="${roomClasses}" var="roomClass">
                        <jsp:useBean id="roomClass" scope="page"
                                     type="com.vaka.hotel_manager.domain.entity.RoomClass"/>
                        <c:choose>
                            <c:when test="${roomClass.name == room.roomClass.name}">
                                <option value="${roomClass.id}"
                                        selected>${roomClass.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${roomClass.id}">${roomClass.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </td>
        </tr>

        <tr>
            <td></td>
            <td><input hidden name="method" value="PUT"></td>
            <td>
                <button id="editButton"><fmt:message key="Submit"
                                                     bundle="${bundle}"/></button>
                <a href="/rooms/${room.id}">
                    <fmt:message key="Cancel" bundle="${bundle}"/></a>
            </td>
        </tr>
    </table>
</form>



</body>
</html>
