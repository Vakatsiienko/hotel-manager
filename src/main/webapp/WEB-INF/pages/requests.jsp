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
    <div><button id = "addRequestButton">Add Request</button><p id = "manual">To edit or delete request just click on his row.</p></div>

    <table id="myTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>Request Id</th>
            <th>Client Name</th>
            <th>Beds</th>
            <th>Class Type</th>
            <th>Bathroom Type</th>
            <th>Created Date</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
<c:forEach items="${requestList}" var="request">
    <jsp:useBean id="request" scope="page"
                 type="com.vaka.domain.ReservationRequest"/>
    <jsp:useBean id="request.customer" scope="page"
                 type="com.vaka.domain.User"/>
        <tr>
            <th>${request.id}</th>
            <th>${request.customer.name}</th>
            <th>${request.numOfBeds}</th>
            <th>${request.roomClass}</th>
            <th>${request.bathroomType}</th>
            <th>${request.createdDate}</th>
            <th>${request.status}</th>
            <th> <a href="rooms?forRequest&id=${request.id}">Process</a><%--TODO add process request button--%></th>
        </tr>
</c:forEach>

        </tbody>
    </table></div>
</body>
</html>
