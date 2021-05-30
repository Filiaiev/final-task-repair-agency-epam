<%@include file="lib.jsp"%>
<link rel="stylesheet" href="style/order.css" type="text/css"/>

<fmt:setBundle basename="l18n/app_uk"/>
<fmt:message key="full_name" var="full_name"/>
<fmt:message key="birth_date" var="birth_date"/>
<fmt:message key="email" var="email"/>
<fmt:message key="cash" var="cash"/>
<fmt:message key="personal_data" var="personal_data"/>
<fmt:message key="refill_user_account" var="refill_user_account"/>
<fmt:message key="enter_amount" var="enter_amount"/>

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
                <th scope="row"><c:out value="${email}"/></th>
                <td>${userEmail}</td>
            </tr>

            <tr>
                <th scope="row"><c:out value="${full_name}"/></th>
                <td>${personInfo.lastName} ${personInfo.firstName} ${personInfo.middleName}</td>
            </tr>

            <tr>
                <th scope="row"><c:out value="${birth_date}"/></th>
                <td>${personInfo.birthDate}</td>
            </tr>

            <tr>
                <th scope="row"><c:out value="${cash}"/></th>
                <td>${clientInfo.cash}</td>
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
