<%@include file="../lib.jsp"%>

<html>
<head>
    <c:set var="statuses" value="<%=OrderStatus.values()%>" scope="session"/>
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>
    <link rel="stylesheet" href="style/core.css" type="text/css">
    <title>Home</title>
</head>

<body>

<%-- Include joint header --%>
<%--<%@ include file="../header.jsp"%>--%>
<%@include file="../nav/navbar.jsp"%>

<%-- Switch between which content should be displayed by user role --%>
<div class="d-inline-flex w-100 justify-content-center" style="margin-top: 200px">
    <c:choose>
        <c:when test="${sessionScope.role == Role.CLIENT}">
            <fmt:message key="client.help.message.create" var="createMsg"/>
            <fmt:message key="client.help.message.view_orders" var="viewOrdersMsg"/>
            <div class="d-flex flex-column">
                <h4>${createMsg}</h4>
                <h4>${viewOrdersMsg}</h4>
            </div>
        </c:when>

        <c:when test="${sessionScope.role == Role.MANAGER}">
            <fmt:message key="home.message.need_to_be_processed" var="needToBeProcessed"/>
            <fmt:message key="home.message.todays_orders" var="todaysOrders"/>
            <div class="d-flex flex-column">
                <h4>${todaysOrders}: ${requestScope.todaysOrders}</h4>
                <h4>${needToBeProcessed}: ${requestScope.toWorkCount}</h4>
            </div>
        </c:when>

        <c:when test="${sessionScope.role == Role.REPAIRER}">
            <fmt:message key="home.message.need_to_be_processed" var="needToBeProcessed"/>
            <h4>${needToBeProcessed}: ${requestScope.toWorkCount}</h4>
        </c:when>
    </c:choose>
</div>
</body>
</html>
