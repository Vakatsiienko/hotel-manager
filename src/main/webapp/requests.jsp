<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>RequestsList</title>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.8/css/jquery.dataTables.min.css"/>
    <script type="text/javascript" src="//cdn.datatables.net/1.10.8/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript">
    var table;
    $(document).ready(function () {
        table = $("#myTable").on('xhr.dt', function (e, settings, json, xhr) {

        }).dataTable({
            "dom": "<lftip>",
//                "columns": [
//                    {data: "id"},
//                    {data: "userName"},
//                    {data: "beds"},
//                    {data: "classType"},
//                    {data: "createdDate"},
//                    {data: "status"},
//                    {data: "action"},
//                ]//,
//                "ajax": {
//                    url: "users",
//                    dataSrc: "rows"
//                } get data from attributes
        });
    })
</script>
</head>
<body>
<div id="test">
    <div><button id = "addRequestButton">Add Request</button></div>

    <table id="myTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>Request Id</th>
            <th>Client Name</th>
            <th>Beds</th>
            <th>Class Type</th>
            <th>Created Date</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
<c:forEach items="${requestList}" var="requestRes">
    <jsp:useBean id="requestRes" scope="page"
                 type="com.vaka.domain.ReservationRequest"/>
        <tr>
            <th>${requestRes.id}</th>
            <th>${requestRes.customer.name}</th>
            <th>${requestRes.numOfBeds}</th>
            <th>${requestRes.roomClass}</th>
            <th>${requestRes.createdDate}</th>
            <th>${requestRes.status}</th>
            <th> <a href="/requests/process?id=${requestRes.id}">Process</a><%--TODO add process request button--%></th>
        </tr>
</c:forEach>

        </tbody>
    </table></div>
</body>
</html>
