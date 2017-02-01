<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="com.vaka.hotel_manager" %>
<html>
<head>

    <script
            src="https://code.jquery.com/jquery-3.1.1.js"
            integrity="sha256-16cdPddA6VdVInumRGo6IbivbERE8p7CQR3HzTBuELA="
            crossorigin="anonymous"></script>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript"
            src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <%@include file="header.jspf" %>

    <title><fmt:message key="MakeReservation" bundle="${bundle}"/></title>

    <script type="text/javascript">


        function changeDeparture() {
            var arrivalDate = new Date(document.getElementById('arrivalDate').value);
            var nextDate = new Date(document.getElementById('arrivalDate').value);
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
            document.getElementById("departureDate").min = nextDate;
            var departureValue = document.getElementById("departureDate").value;
            if (arrivalDate >= new Date(departureValue) || departureDate.toString() == "") {
                document.getElementById("departureDate").value = nextDate;
            }
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
            var arrivalDateValue = document.getElementById("arrivalDate").value;
            if (arrivalDateValue != null)
                if (new Date(arrivalDateValue) >= today) {
                    todayStr = arrivalDateValue;
                }
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

            var departureDateValue = document.getElementById("departureDate").value;
            if (departureDateValue != null)
                if (new Date(departureDateValue) >= tomorrow) {
                    tomorrowStr = departureDateValue;
                }
            document.getElementById("departureDate").value = tomorrowStr;
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
                                   pattern="[\+]\d{12}"
                                   value="+380"
                                   title="Phone number should be in format '+380441234567'"
                                   name="phoneNumber" required>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            <c:if test="${!empty reservation}">
                <jsp:useBean id="reservation" scope="session"
                             type="com.vaka.hotel_manager.domain.Reservation"
                             beanName="reservation"/>
            </c:if>
            <tr>
                <th class="ftitle" colspan="2"><fmt:message key="ReservationInfo" bundle="${bundle}"/></th>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="guests"><fmt:message key="Guests" bundle="${bundle}"/>:</label>
                </th>
                <td>
                    <input id="guests" name="guests" type="number" min="1" step="1" pattern="\d*"
                           value="${reservation.guests}" required>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="roomClassName"><fmt:message key="RoomClass" bundle="${bundle}"/></label>
                </th>
                <td>
                    <select id="roomClassName" name="roomClassName">
                        <c:forEach items="${roomClasses}" var="clazz">
                            <jsp:useBean id="clazz" scope="page"
                                         type="com.vaka.hotel_manager.domain.RoomClass"/>
                            <option value="${clazz.name}"
                                    <c:if test="${reservation.requestedRoomClass == clazz.name}">
                                        selected
                                    </c:if>
                            >${clazz.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="arrivalDate"><fmt:message key="ArrivalDate" bundle="${bundle}"/></label>
                </th>
                <td>
                    <input id="arrivalDate" name="arrivalDate" type="date" value="${fn:htmlFormatDate(reservation.arrivalDate)}"
                           onchange="changeDeparture()"
                           required title="<fmt:message key="arrivalDate.title" bundle="${bundle}"/>">
                </td>
            </tr>
            <tr class="fitem">
                <th>
                    <label for="departureDate"><fmt:message key="DepartureDate" bundle="${bundle}"/></label>
                </th>
                <td>
                    <input id="departureDate" name="departureDate" type="date" value="${fn:htmlFormatDate(reservation.departureDate)}"
                           title="Departure date cant be lower than arrival" required>
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

</div>
<br>
</body>
</html>
