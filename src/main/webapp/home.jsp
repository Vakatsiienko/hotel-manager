<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jspf" %>

    <title><fmt:message key="MakeReservation" bundle="${bundle}"/></title>

    <script
            src="https://code.jquery.com/jquery-3.1.1.js"
            integrity="sha256-16cdPddA6VdVInumRGo6IbivbERE8p7CQR3HzTBuELA="
            crossorigin="anonymous"></script>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript">


        function findAvailable() {
            document.reservationForm.action = '/';
            document.getElementById('reservationForm').submit();
        }
        function changeDeparture(arrival) {
            var nextDate = new Date();
            var arrivalDate = new Date(arrival);
            nextDate.setDate(arrivalDate.getDate() + 1);
            var departureDate = nextDate;
            var dd = nextDate.getDate();
            var mm = nextDate.getMonth() + 1;
            var yyyy = nextDate.getFullYear();
            yyyy = "" + yyyy;
            while (yyyy.length < 4) {
                yyyy = '0' + yyyy;
            }

            if (dd < 10) {
                dd = '0' + dd
            }
            if (mm < 10) {
                mm = '0' + mm
            }

            nextDate = yyyy + '-' + mm + '-' + dd;
            document.getElementById("departureDate").setAttribute("min", nextDate);
            var departureValue = document.getElementById("departureDate").value;
            if (arrivalDate >= new Date(departureValue) || departureDate.toString() == "") {
                document.getElementById("departureDate").setAttribute("value", nextDate);
            }
        }
        function getParameter(parameterName) {
            var result = null,
                    tmp = [];
            var items = location.search.substr(1).split("&");
            for (var index = 0; index < items.length; index++) {
                tmp = items[index].split("=");
                if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
            }
            return result;
        }
        //setting default and min value for date imput
        $(function () {
            var today = new Date();
            var d = today.getDate();
            var m = today.getMonth() + 1;
            var y = today.getFullYear();
            if (d < 10) {
                d = '0' + d
            }
            if (m < 10) {
                m = '0' + m
            }

            var todayStr = y + '-' + m + '-' + d;
            document.getElementById("arrivalDate").min = todayStr;

            var arrivalDateParam = getParameter('arrivalDate');
            if (arrivalDateParam != null)
                if (new Date(arrivalDateParam) >= today) {
                    debugger;
                    todayStr = arrivalDateParam;
                }
            debugger;
            document.getElementById("arrivalDate").value = todayStr;
        });

        $(function () {
            var tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            var dd = tomorrow.getDate();
            var mm = tomorrow.getMonth() + 1;
            var yyyy = tomorrow.getFullYear();
            if (dd < 10) {
                dd = '0' + dd
            }
            if (mm < 10) {
                mm = '0' + mm
            }

            var tomorrowStr = yyyy + '-' + mm + '-' + dd;
            document.getElementById("departureDate").min = tomorrowStr;
            var departureDateParam = getParameter('departureDate');

            if (departureDateParam != null)
                if (new Date(departureDateParam) >= tomorrow) {
                    debugger;
                    tomorrowStr = departureDateParam;
                }
            debugger;
            document.getElementById("departureDate").value = tomorrowStr;
        });
        $(function () {
            var roomClass = getParameter('roomClass');
            debugger;
            if (roomClass != null) {
                document.getElementById('roomClass').value = roomClass;
            }
            else document.getElementById('roomClass').value = 'STANDARD';
        });
        $(document).ready(function () {
            $("#availableRooms").dataTable({
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
        });
    </script>
    <style type="text/css">
        th {
            text-align: right;
        }

        .ftitle {
            text-align: center;
            border-bottom: 1px solid #ccc
        }

        #AddReservationBlock {
            position: relative;
            left: 30%;
        }

    </style>
</head>
<body>
<div id="AddReservationBlock">
    <c:if test="${loggedUser.role == 'ANONYMOUS'}">
        <p><fmt:message key="SubmitFormMessage" bundle="${bundle}"/></p>
    </c:if>

    <form action="/reservations" name="reservationForm" method="post" id="reservationForm">
        <table>
            <tr>
                <th class="ftitle" colspan="2"><fmt:message key="ContactInfo" bundle="${bundle}"/></th>
            </tr>
            <c:choose>
                <c:when test="${!empty loggedUser.name}">
                    <tr class="fitem">
                        <th>
                            <label for="nameReg"><fmt:message key="Name" bundle="${bundle}"/>:</label>
                        </th>
                        <td>
                            <p id="nameReg"> ${loggedUser.name}
                            </p>
                        </td>
                    </tr>
                    <tr class="fitem">
                        <th>
                            <label for="emailReg"><fmt:message key="Email" bundle="${bundle}"/>:</label>
                        </th>
                        <td>
                            <p id="emailReg">${loggedUser.email}</p>
                        </td>
                    </tr>
                    <tr class="fitem">
                        <th>
                            <label for="phoneNumberReg"><fmt:message key="PhoneNumber"
                                                                     bundle="${bundle}"/>:</label>
                        </th>
                        <td>
                            <p id="phoneNumberReg">${loggedUser.phoneNumber}</p>
                        </td>
                    </tr>
                    <tr>
                </c:when>
                <c:otherwise>
                    <tr class="fitem">
                        <th>
                            <label for="name"><fmt:message key="Name" bundle="${bundle}"/>:</label>
                        </th>
                        <td>
                            <input id="name" name="name"
                                   value="${loggedUser.name}"
                                   required>
                        </td>
                    </tr>
                    <tr class="fitem">
                        <th>
                            <label for="email"><fmt:message key="Email" bundle="${bundle}"/>:</label>
                        </th>
                        <td>
                            <input id="email" name="email" type="email"
                                   pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$"
                                   required title="<fmt:message key="emailTitle" bundle="${bundle}"/>">
                        </td>
                    </tr>
                    <tr class="fitem">
                        <th>
                            <label for="phoneNumber"><fmt:message key="PhoneNumber"
                                                                  bundle="${bundle}"/>:</label>
                        </th>
                        <td>
                            <input id="phoneNumber" type="tel"
                                   pattern="[\+]\d{2}[\(]?\s?\d{3}[\)]?[\s]?\d{7}"
                                   value="+38 0"
                                   title="Phone number should be in format '+38 044 1234567'"
                                   name="phoneNumber" required>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>

            <tr>
                <th class="ftitle" colspan="2"><fmt:message key="ReservationInfo" bundle="${bundle}"/></th>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="guests"><fmt:message key="Guests" bundle="${bundle}"/>:</label>
                </th>
                <td>
                    <input id="guests" name="guests" type="number" min="1"
                           value="${param.guests}" required>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="roomClass"><fmt:message key="RoomClass" bundle="${bundle}"/></label>
                </th>
                <td>
                    <select id="roomClass" name="roomClass">
                        <c:forEach items="${roomClasses}" var="clazz">
                            <jsp:useBean id="clazz" scope="page"
                                         type="com.vaka.hotel_manager.domain.RoomClass"/>
                            <option value="${clazz.name()}"><fmt:message key="${clazz.name()}"
                                                                         bundle="${bundle}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="arrivalDate"><fmt:message key="ArrivalDate" bundle="${bundle}"/></label>
                </th>
                <td>
                    <input id="arrivalDate" name="arrivalDate" type="date"
                           onchange="changeDeparture(document.getElementById('arrivalDate').value)"
                           required>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="departureDate"><fmt:message key="DepartureDate" bundle="${bundle}"/></label>
                </th>
                <td>
                    <input id="departureDate" name="departureDate" type="date"
                           title="Departure date cant be lower than arrival">
                </td>
            </tr>
            <tr>
                <th></th>
                <td>
                    <input type="submit" name="submitReservation" value="<fmt:message key="Submit" bundle="${bundle}"/>" required/>
                    <a href="/"><fmt:message key="Cancel" bundle="${bundle}"/></a>
                </td>
            </tr>
        </table>
    </form>
    <button id="findAvailable" type="submit" onclick="findAvailable()"><fmt:message key="FindAvailable" bundle="${bundle}"/></button>

</div>

<br>
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
                         type="com.vaka.hotel_manager.domain.Room"/>
            <tr>
                <th>${room.number}</th>
                <th>${room.capacity}</th>
                <th>${room.costPerDay / 100}</th>
                <th><fmt:message key="${room.roomClazz.name()}" bundle="${bundle}"/></th>
            </tr>
        </c:forEach>

        </tbody>
    </c:if>
</body>
</html>
