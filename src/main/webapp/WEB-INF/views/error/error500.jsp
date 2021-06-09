<%@page isErrorPage="true"%>
<%@include file="../lib.jsp"%>
<html>
<head>
    <title>500</title>
    <c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>
    <fmt:message key="error.500" var="error—ode"/>
    <fmt:message key="error.500.description" var="error_description"/>
</head>
<body>
    <%@include file="../nav/navbar.jsp"%>

    <h1>${error—ode}</h1>
    <h2>${error_description}</h2>
</body>
</html>
