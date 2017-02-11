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
        var totalRows = ${roomPage.totalLength};
        function paginationButtonsLoad() {
            var currentPage = parseInt(getParam("page", 1));
            var currentSize = parseInt(getParam("size", 10));
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
        function getParam(sParam, defaultValue) {
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
        function changeSize() {
            var selected = $("#tableSize").find("option:selected").text();
            var redirectUri = $(location).attr('pathname') + '?size=' + selected + '&page=' + getParam("page", 1);
            window.location.replace(redirectUri);
        }

        function roomFormSubmit() {
            if ($("#addForm").checkValidity()) {
                var costPerDayInput = document.getElementById("costPerDay");
                costPerDayInput.value *= 100;
                document.getElementById("addForm").submit();
            }
        }
        var roomTable;
        var requestTable;
        $(document).ready(function () {
            roomTable = $("#myTable").dataTable({
                "dom": "<t>",
                "ordering": false,
                "deferLoading": ${roomPage.totalLength},
                "processing": true,
                "serverSide": true,
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
            $('#ud_modal_close, #overlay, #add_modal_close').click(function () { // лoвим клик пo крестику или пoдлoжке
                $('#udWindow, #addWindow')
                        .animate({opacity: 0, top: '45%'}, 200,  // плaвнo меняем прoзрaчнoсть нa 0 и oднoвременнo двигaем oкнo вверх
                                function () { // пoсле aнимaции
                                    $(this).css('display', 'none'); // делaем ему display: none;
                                    $('#overlay').fadeOut(400); // скрывaем пoдлoжку
                                }
                        );
            });
            $('#addButton').click(function () {
                event.preventDefault(); // выключaем стaндaртную рoль элементa
                $('#overlay').fadeIn(400, // снaчaлa плaвнo пoкaзывaем темную пoдлoжку
                        function () { // пoсле выпoлнения предъидущей aнимaции
                            $('#addWindow')
                                    .css('display', 'block') // убирaем у мoдaльнoгo oкнa display: none;
                                    .animate({opacity: 1, top: '50%'}, 200); // плaвнo прибaвляем прoзрaчнoсть oднoвременнo сo съезжaнием вниз
                        });
            });
            paginationButtonsLoad();
            var val = getParam('size', 10);
            var sel = document.getElementById('tableSize');
            var opts = sel.options;
            for (var opt, j = 0; opt = opts[j]; j++) {
                if (opt.value == val) {
                    sel.selectedIndex = j;
                    break;
                }
            }

        });

    </script>
    <style type="text/css">
        #tableTitle {
            position: relative;
            left: 40%;
        }

        #paginationButtons {
            position: absolute;
            right: 0px;
        }

        #addWindow {
            width: 300px;
            height: 300px; /* Рaзмеры дoлжны быть фиксирoвaны */
            border-radius: 5px;
            border: 3px #000 solid;
            background: #fff;
            position: fixed; /* чтoбы oкнo былo в видимoй зoне в любoм месте */
            top: 45%; /* oтступaем сверху 45%, oстaльные 5% пoдвинет скрипт */
            left: 50%; /* пoлoвинa экрaнa слевa */
            margin-top: -150px;
            margin-left: -150px; /* тут вся мaгия центрoвки css, oтступaем влевo и вверх минус пoлoвину ширины и высoты сooтветственнo =) */
            display: none; /* в oбычнoм сoстoянии oкнa не дoлжнo быть */
            opacity: 0; /* пoлнoстью прoзрaчнo для aнимирoвaния */
            z-index: 5; /* oкнo дoлжнo быть нaибoлее бoльшем слoе */
            padding: 20px 10px;
        }

        #overlay {
            z-index: 3; /* пoдлoжкa дoлжнa быть выше слoев элементoв сaйтa, нo ниже слoя мoдaльнoгo oкнa */
            position: fixed; /* всегдa перекрывaет весь сaйт */
            background-color: #000; /* чернaя */
            opacity: 0.8; /* нo немнoгo прoзрaчнa */
            width: 100%;
            height: 100%; /* рaзмерoм вo весь экрaн */
            top: 0;
            left: 0; /* сверху и слевa 0, oбязaтельные свoйствa! */
            cursor: pointer;
            display: none; /* в oбычнoм сoстoянии её нет) */
        }
        caption {
            font-weight: bold;
        }
    </style>
    <title><fmt:message key="RoomsPage" bundle="${bundle}"/></title>
</head>
<body>
<br>

<h3 id="tableTitle"></h3>
<c:if test="${loggedUser.role.name().equals('MANAGER')}">
    <button id="addButton"><fmt:message key="Create" bundle="${bundle}"/></button>
</c:if>
<div id="tableWrapper">
    <select id="tableSize" onchange="changeSize()">
        <option value="5">5</option>
        <option value="10">10</option>
        <option value="20">20</option>
    </select>
<table id="myTable" class="display" cellspacing="0" width="100%" >
    <caption><fmt:message key="RoomList" bundle="${bundle}"/></caption>
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
    <jsp:useBean id="roomPage" type="com.vaka.hotel_manager.domain.Page" scope="request"/>
    <c:forEach items="${roomPage.content}" var="room">
        <jsp:useBean id="room" scope="page"
                     type="com.vaka.hotel_manager.domain.entity.Room"/>
        <tr>
            <th>${room.number}</th>
            <th>${room.capacity}</th>
            <th><fmt:formatNumber value="${room.costPerDay / 100}"/></th>
            <th>${room.roomClass.name}</th>
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
    <span id="totalRows"><fmt:message key="TotalRows" bundle="${bundle}"/>: ${roomPage.totalLength}</span>
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
<div id="addWindow" style="width:450px;height:200px;padding:10px 20px">
    <div class="ftitle">Room Information</div>
    <form id="addForm" action="/rooms" method="POST">
        <c:if test="${!empty sessionRoom}">
            <jsp:useBean id="sessionRoom" class="com.vaka.hotel_manager.domain.entity.Room"
                         scope="session"/>
        </c:if>
        <div class="fitem">
            <label for="number">Number:</label>
            <input id="number" name="number" type="number" min="0" step="1" pattern="\d"
                   required
            <c:if test="${!empty sessionRoom}">
                   value="${sessionRoom.number}"
            </c:if>
            >
        </div>
        <div class="fitem">
            <label for="capacity">Capacity:</label>
            <input id="capacity" name="capacity" type="number" min="0" step="1" pattern="\d"
                   required
            <c:if test="${!empty sessionRoom}">
                   value="${sessionRoom.capacity}"
            </c:if>
            >
        </div>
        <div class="fitem">
            <label for="costPerDay">Cost Per Day:</label>
            <input id="costPerDay" name="costPerDay" type="number" min="0" step="1"
                   pattern="\d" required
            <c:if test="${!empty sessionRoom}">
                   value="${sessionRoom.costPerDay}"
            </c:if>
            >
        </div>
        <div class="fitem">
            <label for="roomClass">Room Class:</label>
            <select id="roomClass" name="roomClassId" required>
                <c:forEach items="${roomClasses}" var="clazz">
                    <jsp:useBean id="clazz" scope="page"
                                 type="com.vaka.hotel_manager.domain.entity.RoomClass"/>
                    <option value="${clazz.id}"
                            <c:if test="${!empty sessionRoom}">
                                <c:if test="${sessionRoom.roomClass.id == clazz.id}">
                                    selected
                                </c:if>
                            </c:if>
                    >${clazz.name}</option>
                </c:forEach>
            </select>
        </div>
        <br>
        <div>
            <button id="addFormSubmit" onclick="roomFormSubmit()">
                <fmt:message
                        key="Create"
                        bundle="${bundle}"/>
            </button>
            <button id="add_modal_close">
                <fmt:message key="Cancel"
                             bundle="${bundle}"/>
            </button>
        </div>
    </form>

</div>

<div id="overlay"></div>
</body>
</html>
