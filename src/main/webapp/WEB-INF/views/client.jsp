<%@include file="lib.jsp"%>
<link rel="stylesheet" href="style/order.css" type="text/css"/>

<fmt:message key="person.full_name" var="full_name"/>
<fmt:message key="person.birth_date" var="birth_date"/>
<fmt:message key="email" var="email"/>
<fmt:message key="cash" var="cash"/>
<fmt:message key="person.personal_data" var="personal_data"/>
<fmt:message key="refill_user_account" var="refill_user_account"/>
<fmt:message key="enter_amount" var="enter_amount"/>
<fmt:message key="currency.uah" var="currency"/>

<html>
<head>
    <title>Client</title>
</head>
<body>
<%@include file="nav/navbar.jsp"%>

    <div class="d-flex w-100 justify-content-center align-self-center">
        <table class="table table-bordered w-25" style="margin-top: 100px; margin-left: 0">
            <thead class="thead-dark text-center">
                <tr>
                    <th scope="col" colspan="2">${personal_data}</th>
                </tr>
            </thead>

            <tr>
                <th scope="row">${email}</th>
                <td><c:out value="${userEmail}"/></td>
            </tr>

            <tr>
                <th scope="row">${full_name}</th>
                <td><c:out value="${personInfo.lastName} ${personInfo.firstName} ${personInfo.middleName}"/></td>
            </tr>

            <tr>
                <th scope="row">${birth_date}</th>
                <td><fmt:formatDate value="${personInfo.birthDate}" dateStyle="LONG"/></td>
            </tr>

            <tr>
                <th scope="row">${cash}</th>
                <td>${clientInfo.cash} ${currency}</td>
            </tr>
    </table>
    </div>

    <div class="d-flex w-100 justify-content-center align-self-center">
        <form class="d-flex flex-column" method="post" action="controller">
            <input type="hidden" name="command" value="setClientCash">
            <input type="hidden" name="clientId" value="${clientInfo.id}">
            <input type="number" placeholder="${enter_amount}" name="cashValue">
            <input class="btn btn-light border-dark mt-2" type="submit" value="${refill_user_account}">
        </form>
    </div>
</body>
</html>
