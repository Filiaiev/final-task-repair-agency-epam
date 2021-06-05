<%@include file="../lib.jsp"%>
<html>
<head>
    <title>Error</title>
    <fmt:message key="error.404" var="error_code"/>
    <fmt:message key="login.form.log_in" var="log_in"/>
</head>
<body>
    <%@include file="../nav/navbar.jsp"%>
    
    <h1><c:out value="${error_code}"/></h1>

    <c:if test="${errorKey != null}">
        <fmt:message key="error.${errorKey}" var="errorMessage"/>
        <h2>${errorMessage}</h2>
    </c:if>

</body>
</html>
