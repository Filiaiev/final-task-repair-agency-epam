<%@include file="lib.jsp"%>
<link rel="stylesheet" href="style/order.css" type="text/css"/>

<fmt:setBundle basename="l18n/app_uk"/>
<fmt:message key="full_name" var="full_name"/>
<fmt:message key="birth_date" var="birth_date"/>
<fmt:message key="email" var="email"/>
<fmt:message key="cash" var="cash"/>

<html>
<head>
    <title>Client</title>
</head>
<body>
<%@include file="header.jsp"%>
    <table>
        <tr>
            <td><c:out value="${email}"/></td>
            <td>${userEmail}</td>
        </tr>

        <tr>
            <td><c:out value="${full_name}"/></td>
            <td>${personInfo.lastName} ${personInfo.firstName} ${personInfo.middleName}</td>
        </tr>
        
        <tr>
            <td><c:out value="${birth_date}"/></td>
            <td>${personInfo.birthDate}</td>
        </tr>

        <tr>
            <td><c:out value="${cash}"/></td>
            <td>${clientInfo.cash}</td>
        </tr>
    </table>

    <form method="post" action="controller">
        <input type="hidden" name="command" value="setClientCash">
        <input type="hidden" name="clientId" value="${clientInfo.id}">
        <input type="number" name="cashValue">
        <input type="submit" value="Process">
    </form>
</body>
</html>
