<%@include file="../lib.jsp"%>

<html>
<head>
    <c:set var="status_enum" value="<%=OrderStatus.values()%>" scope="session"/>
<%--    <script src="script/user_home.js" type="text/javascript"></script>--%>
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>
    <link rel="stylesheet" href="style/user_home.css" type="text/css">
    <title>Home</title>
</head>

<body>

<%-- Include joint header --%>
<%--<%@ include file="../header.jsp"%>--%>
<%@include file="../nav/navbar.jsp"%>
<%-- Switch between which content should be displayed by user role --%>
<c:choose>
    <c:when test="${sessionScope.roleId == Role.CLIENT.ordinal()}">
        <%@include file="user_home.jsp"%>
    </c:when>

    <c:when test="${sessionScope.roleId == Role.MANAGER.ordinal()}">
        <%@include file="manager_home.jsp"%>
    </c:when>

    <c:when test="${sessionScope.roleId == Role.REPAIRER.ordinal()}">
        <%@include file="repairer_home.jsp"%>
    </c:when>
</c:choose>
</body>
</html>
