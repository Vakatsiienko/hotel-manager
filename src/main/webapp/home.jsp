<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jspf" %>

    <title><fmt:message key="MakeReservation" bundle="${bundle}"/></title>

    <script
            src="https://code.jquery.com/jquery-3.1.1.js"
            integrity="sha256-16cdPddA6VdVInumRGo6IbivbERE8p7CQR3HzTBuELA="
            crossorigin="anonymous"></script>
    <script type="text/javascript">

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
            var departureValue= document.getElementById("departureDate").value;
            if (arrivalDate >= new Date(departureValue) || departureDate.toString() == "") {
                document.getElementById("departureDate").setAttribute("value", nextDate);
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

            today = y + '-' + m + '-' + d;
            document.getElementById("arrivalDate").setAttribute("min", today);
            document.getElementById("arrivalDate").setAttribute("value", today);

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

            tomorrow = yyyy + '-' + mm + '-' + dd;
            document.getElementById("departureDate").setAttribute("min", tomorrow);
            document.getElementById("departureDate").setAttribute("value", tomorrow);
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
        #AddReservationBlock{
            position: absolute;
            left:30%;
        }

    </style>
</head>
<body>
<div id="AddReservationBlock">
<c:if test="${loggedUser.role == 'ANONYMOUS'}">
<p><fmt:message key="SubmitFormMessage" bundle="${bundle}"/></p>
</c:if>

    <form action="/reservations" method="post" id="createForm">
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
                            <input id="nameReg" name="name"
                                   value="${loggedUser.name}"
                                   required readonly>
                        </td>
                    </tr>
                    <tr class="fitem">
                        <th>
                            <label for="emailReg"><fmt:message key="Email" bundle="${bundle}"/>:</label>
                        </th>
                        <td>
                            <input id="emailReg" name="email" type="email"
                                   value="${loggedUser.email}"
                                   required readonly>
                        </td>
                    </tr>
                    <tr class="fitem">
                        <th>
                            <label for="phoneNumberReg"><fmt:message key="PhoneNumber"
                                                                     bundle="${bundle}"/>:</label>
                        </th>
                        <td>
                            <input id="phoneNumberReg" type="tel"
                                   pattern="[\+]\d{2}[\(]\d{3}[\)]\d{7}"
                                   value="${loggedUser.phoneNumber}"
                                   title="Phone number should be in format '+38(044)1234567'"
                                   name="phoneNumber" readonly required>
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
                                   required>
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
                    <input id="guests" name="guests" type="number" min="1" required>
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
                            <option value="${clazz.name()}" selected><fmt:message key="${clazz.name()}" bundle="${bundle}"/></option>
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
                    <input type="submit" name="submit" value="<fmt:message key="Submit" bundle="${bundle}"/>" required/>
                    <a href="/"><fmt:message key="Cancel" bundle="${bundle}"/></a>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
