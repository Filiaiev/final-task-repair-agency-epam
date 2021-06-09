<%@include file="../lib.jsp"%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">
<html>
<head>
    <title>Profile</title>
    <fmt:message key="person.personal_data" var="personalData"/>
    <fmt:message key="person.full_name" var="fullName"/>
    <fmt:message key="email" var="email"/>
    <fmt:message key="cash" var="cash"/>
    <fmt:message key="login" var="login"/>
    <fmt:message key="currency.uah" var="currency"/>

</head>
<body>
<%@include file="../nav/navbar.jsp"%>

<div class="d-flex w-100 justify-content-center align-self-center">
    <table class="table table-bordered w-25" style="margin-top: 100px; margin-left: 0">
        <thead class="thead-dark text-center">
            <tr>
                <th scope="col" colspan="2">${personalData}</th>
            </tr>
        </thead>

        <tbody>
            <tr>
                <th scope="col">${login}</th>
                <td><c:out value="${user.login}"/></td>
            </tr>

            <tr>
                <th scope="col">${email}</th>
                <td><c:out value="${user.email}"/></td>
            </tr>

            <tr>
                 <th scope="col">${fullName}</th>
                 <td><c:out value="${person.lastName} ${person.firstName} ${person.middleName}"/></td>
            </tr>

            <tr>
                <th scope="col">${cash}</th>
                <td>${client.cash} ${currency}</td>
            </tr>

        </tbody>
    </table>
</div>
</body>
</html>
