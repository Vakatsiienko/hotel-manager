<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jspf" %>
    <title><fmt:message key="RoomInfo" bundle="${bundle}"/></title>
    <script type="text/javascript">
        function submitRoomForm() {
            var costPerDayInput = document.getElementById("costPerDay");
            costPerDayInput.value *= 100;
            document.getElementById("editRoomForm").submit();
        }
    </script>

</head>
<body>

<jsp:useBean id="room" scope="request"
             type="com.vaka.hotel_manager.domain.Room" beanName="room"/>
<form name="editRoomForm" action="/rooms/${room.id}" method="post">

    <table id="roomTable" class="display" cellpadding="0">
        <tr>
            <th colspan="2"><h2><fmt:message key="RoomInfo" bundle="${bundle}"/></h2></th>
        </tr>
        <tr>
            <th><fmt:message key="ID" bundle="${bundle}"/></th>
            <td>${room.id}</td>
            <td><input type="number" name="id" value="${room.id}" readonly></td>

        </tr>
        <tr>
            <th><fmt:message key="Number" bundle="${bundle}"/></th>
            <td>${room.number}</td>
            <td><input type="number" name="number" value="${room.number}" min="0"></td>

        </tr>

        <tr>
            <th><fmt:message key="Capacity" bundle="${bundle}"/></th>
            <td>${room.capacity}</td>
            <td><input type="number" name="capacity" value="${room.capacity}" min="0"></td>
        </tr>

        <tr>
            <th><fmt:message key="CostPerDay" bundle="${bundle}"/></th>
            <td>${room.costPerDay / 100}</td>
            <td><input id="costPerDay" type="number" name="costPerDay"
                       value="${room.costPerDay / 100}" min="0"></td>
        </tr>

        <tr>
            <th><fmt:message key="RoomClass" bundle="${bundle}"/></th>
            <td><fmt:message key="${room.roomClazz.name()}" bundle="${bundle}"/></td>
            <td>
                <select name="roomClass">
                    <c:forEach items="${roomClasses}" var="roomClass">
                        <jsp:useBean id="roomClass" scope="page"
                                     type="com.vaka.hotel_manager.domain.RoomClass"/>
                        <c:choose>
                            <c:when test="${roomClass == room.roomClazz}">
                                <option value="${roomClass.name()}" selected><fmt:message
                                        key="${roomClass.name()}" bundle="${bundle}"/></option>
                            </c:when>
                            <c:otherwise>
                                <option value="${roomClass.name()}"><fmt:message
                                        key="${roomClass.name()}" bundle="${bundle}"/></option>
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
                <button onclick="submitRoomForm()"><fmt:message key="Submit"
                                                                bundle="${bundle}"/></button>
                <a href="/rooms/${room.id}">
                    <fmt:message key="Cancel" bundle="${bundle}"/></a>
            </td>
        </tr>
    </table>
</form>


</body>
</html>
