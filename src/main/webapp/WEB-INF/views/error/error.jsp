<%@include file="../lib.jsp"%>
<html>
<head>
    <title>Error</title>
    <fmt:message key="error.error" var="error"/>
    <fmt:message key="login.form.log_in" var="logIn"/>
    <fmt:message key="error.description" var="description"/>
</head>
<body>
    <%@include file="../nav/navbar.jsp"%>
    
    <h1><c:out value="${error}"/></h1>

    <c:choose>
        <c:when test="${errorKey != null}">
            <fmt:message key="error.${errorKey}" var="errorMessage"/>
            <h2>${errorMessage}</h2>
        </c:when>
        <c:otherwise>
            <fmt:message key="error.bad_request" var="errorMessage"/>
            <h2>${errorMessage}</h2>
        </c:otherwise>
    </c:choose>

    <h2><c:out value="${description}"/></h2>

</body>
</html>
