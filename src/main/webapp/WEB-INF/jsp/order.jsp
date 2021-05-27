<%@include file="lib.jsp"%>
<html>
<head>
    <link rel="stylesheet" href="style/order.css" type="text/css"/>
    <script src="script/script.js" type="text/javascript"></script>
    <title>Order</title>
    <fmt:setBundle basename="l18n.app_uk"/>

    <fmt:message key="order_no" var="order_no"/>
    <fmt:message key="order_date" var="order_date"/>
    <fmt:message key="complete_date" var="complete_date"/>
    <fmt:message key="order_cost" var="order_cost"/>
    <fmt:message key="order_comment" var="order_comment"/>
    <fmt:message key="order_description" var="order_description"/>
    <fmt:message key="workers_full_name" var="workers_full_name"/>
    <fmt:message key="client_full_name" var="clients_full_name"/>
    <fmt:message key="order_status" var="order_status"/>
    <fmt:message key="comment" var="comment"/>
    <fmt:message key="pay" var="pay"/>

    <%-- Order statuses --%>
    <fmt:parseNumber var="status_id" type="number" value="${order_info.get('status_id')}"/>
    <%-- TODO: Why request scope doesnt work? When variable has req. scope --%>
    <fmt:message key="${OrderStatus.values()[status_id].toString()}" var="status_value"/>

    <%--  Payment statuses  --%>

    <c:set var="order_id" value="${order_info.get('id')}"/>
    <c:set var="statuses" value="${OrderStatus.values()}"/>
</head>
<body onload="setStatusColor('status-holder', ${status_id});">
    <%@ include file="header.jsp"%>

    <table>
        <%-- Order number --%>
        <tr>
            <td><c:out value="${order_no}"/></td>
            <td><c:out value="${order_id}"/></td>
        </tr>

        <%--    Customer data
                If role is manager
        --%>
        <c:if test="${roleId == 0}">
            <tr>
                <td><c:out value="${clients_full_name}"/></td>
                <td>
                    <form action="controller" method="post" id="client-info-form">
                        <a href="#" onclick="document.getElementById('client-info-form').submit();">
                                ${order_info.get('client_last_name')}<br>
                                ${order_info.get('client_first_name')}<br>
                                ${order_info.get('client_middle_name')}<br>
                        </a>
                        <input type="hidden" name="command" value="goToClientInfo">
                        <input type="hidden" name="clientId" value="${order_info.get('client_id')}">
                    </form>
                </td>
            </tr>
        </c:if>

        <%-- Order date --%>
        <tr>
            <td><c:out value="${order_date}"/></td>
            <td><c:out value="${order_info.get('order_date')}"/></td>
        </tr>

        <%-- Complete date --%>
        <c:if test="${statuses[status_id] == 'COMPLETED'}">
            <tr>
                <td><c:out value="${complete_date}"/></td>
                <td><c:out value="${order_info.get('complete date')}"/></td>
            </tr>
        </c:if>

        <%-- Cost --%>
        <c:if test="${status_id > 0 or roleId == 0}">
            <tr>
                <td><c:out value="${order_cost}"/></td>
                <td><c:out value="${order_info.get('cost')}"/></td>
            </tr>
        </c:if>

        <%-- Order text --%>
        <tr>
            <td><c:out value="${order_description}"/></td>
            <td><c:out value="${order_info.get('description')}"/></td>
        </tr>

        <%-- Worker`s data --%>
        <c:if test="${order_info.get('worker_last_name') != null && roleId != 1}">
            <tr>
                <td><c:out value="${workers_full_name}"/></td>
                <td>
                    <c:out value="${order_info.get('worker_last_name')}"/><br>
                    <c:out value="${order_info.get('worker_first_name')}"/><br>
                    <c:out value="${order_info.get('worker_middle_name')}"/><br>
                </td>
            </tr>
        </c:if>

        <%-- Status value --%>
        <tr>
            <td><c:out value="${order_status}"/></td>
            <td id="status-holder"><c:out value="${status_value}"/></td>
        </tr>

    </table>

    <%-- Controllers --%>
    <c:choose>
        <%-- Client role elements --%>
        <c:when test="${roleId == 2}">
            <c:choose>
                <%--
                    Client create comment option
                --%>
                <c:when test="${statuses[status_id] == 'COMPLETED'}">
                    <form action="controller" method="post">
                        <input type="hidden" name="command" value="createComment"/>
                        <input type="hidden" name="order_id" value="${order_id}"/>
                        <textarea name="commentText" placeholder="${comment}"></textarea>
                        <input type="submit" value="ok"/>
                    </form>
                </c:when>

                <%--  Client pay option  --%>
                <c:when test="${statuses[status_id] == 'WAITING_FOR_PAYMENT'}">
                    <form action="controller" method="post">
                        <input type="hidden" name="command" value="clientPay">
                        <input type="hidden" name="orderId" value="${order_id}">
                            <%--
                                    TODO: SEND ORDER_INFO TO PAY COMMAND
                                    WHEN IT HAS REQUEST SCOPE
                            --%>
                        <input type="hidden" name="order_info" value="">
                        <input type="submit" value="${pay}">
                    </form>
                    <c:if test="${payment_message_key != null}">
                        <fmt:message key="${payment_message_key}" var="payment_status"/>
                        <c:out value="${payment_status}"/>
                    </c:if>
                </c:when>
            </c:choose>
        </c:when>

        <%-- Manager role elements --%>
        <c:when test="${roleId == 0}">
            <c:choose>
                <%-- Set clien`t payment --%>
                <c:when test="${statuses[status_id] == 'CREATED'}">
                    <form action="controller" method="post">
                        <input type="hidden" name="order_id" value="${order_id}">
                        <input type="hidden" name="command" value="setPayment"/>
                        <input type="number" name="cost_value"/>
                        <input type="submit" value="Встановити ціну"/>
                    </form>
                </c:when>

                <%-- Set repairer --%>
                <c:when test="${statuses[status_id] == 'PAID'}">
                    <form action="controller" method="post">
                    <select name="repairer">
                        <c:forEach items="${repairers}" var="item" varStatus="loop_status">
                            <option value="${item.id}">${item.lastName} ${item.firstName} ${item.middleName}</option>
                        </c:forEach>
                    </select>
                        <input type="submit" name="command" value="setRepairer"/>
                    </form>
                </c:when>
            </c:choose>
        </c:when>

        <%-- Repairer role elements --%>
        <c:when test="${roleId == 1}">
            <form method="post" action="controller">
                <select name="statusId" onchange="this.form.submit();">
                    <option value="none" selected disabled hidden>Set status</option>
                    <option value="${OrderStatus.IN_WORK.ordinal()}">${OrderStatus.IN_WORK}</option>
                    <option value="${OrderStatus.COMPLETED.ordinal()}">${OrderStatus.COMPLETED}</option>
                </select>
                <input type="hidden" name="command" value="setStatus">
                <input type="hidden" name="orderId" value="${order_info.get('id')}">
                <input type="submit" style="visibility: hidden">
            </form>
        </c:when>
    </c:choose>
</body>
</html>
