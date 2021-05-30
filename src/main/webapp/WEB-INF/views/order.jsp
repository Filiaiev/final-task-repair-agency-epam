<%@include file="lib.jsp"%>
<html>
<head>
    <link rel="stylesheet" href="style/order.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>
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
    <fmt:message key="creation_success" var="creation_success"/>
    <fmt:message key="creation_error" var="creation_error"/>
    <fmt:message key="allowed_symbols" var="allowed_symbols"/>
    <fmt:message key="create" var="create"/>

    <%-- Order statuses --%>
    <fmt:parseNumber var="status_id" type="number" value="${order_info.get('status_id')}"/>
    <%-- TODO: Why request scope doesnt work? When variable has req. scope --%>
    <fmt:message key="${OrderStatus.values()[status_id].toString()}" var="status_value"/>

    <%--  Payment statuses  --%>

    <c:set var="order_id" value="${order_info.get('id')}"/>
    <c:set var="statuses" value="${OrderStatus.values()}"/>
</head>
<body onload="setStatusColor('status-holder', ${status_id});">
    <%@ include file="nav/navbar.jsp"%>

    <div class="d-flex w-100 justify-content-center align-self-center">
        <table class="table table-bordered w-25" style="margin-top: 100px; margin-left: 0">
            <thead class="thead-dark text-center">
                <tr>
                    <th scope="col" colspan="2">${order_no} ${order_id}</th>
                </tr>
            </thead>

            <tbody>
                    <%--    Customer data
                           If role is manager
                   --%>
                <c:if test="${roleId == Role.MANAGER.ordinal()}">
                    <tr>
                        <th scope="row"><c:out value="${clients_full_name}"/></th>
                        <td>
                            <form action="controller" method="get" id="client-info-form">
                                <a role="link" onclick="document.getElementById('client-info-form').submit();"
                                   style="text-decoration: underline; cursor: pointer;">
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

                    <%-- Order number --%>
                <tr>
                    <th scope="row"><c:out value="${order_date}"/></th>
                    <td><c:out value="${order_info.get('order_date')}"/></td>
                </tr>

                <%-- Complete date --%>
                <c:if test="${statuses[status_id] == 'COMPLETED'}">
                    <tr>
                        <th scope="row"><c:out value="${complete_date}"/></th>
                        <td><c:out value="${order_info.get('complete_date')}"/></td>
                    </tr>
                </c:if>

                <%-- Cost --%>
                <c:if test="${roleId == Role.CLIENT.ordinal() || roleId == Role.MANAGER.ordinal()}">
                    <tr>
                        <th scope="row"><c:out value="${order_cost}"/></th>
                        <td><c:out value="${order_info.get('cost')}"/></td>
                    </tr>
                </c:if>

                <%-- Order text --%>
                <tr>
                    <th scope="row"><c:out value="${order_description}"/></th>
                    <td><c:out value="${order_info.get('description')}"/></td>
                </tr>

                <%-- Worker`s data --%>
                <c:if test="${order_info.get('worker_last_name') != null && roleId != 1}">
                    <tr>
                        <th scope="row"><c:out value="${workers_full_name}"/></th>
                        <td>
                            <c:out value="${order_info.get('worker_last_name')}"/><br>
                            <c:out value="${order_info.get('worker_first_name')}"/><br>
                            <c:out value="${order_info.get('worker_middle_name')}"/><br>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${statuses[status_id] == OrderStatus.COMPLETED}">
                    <tr>
                        <th scope="row"><c:out value="${comment}"/></th>
                        <td>
                            <c:out value="${order_info.get('comment')}"/><br>
                        </td>
                    </tr>
                </c:if>

                <%-- Status value --%>
                <tr>
                    <th scope="row"><c:out value="${order_status}"/></th>
                    <td id="status-holder"><c:out value="${status_value}"/></td>
                </tr>
            </tbody>
        </table>
    </div>

    <%-- Controllers --%>
    <c:choose>
        <%-- Client role elements --%>
        <c:when test="${roleId == Role.CLIENT.ordinal()}">
            <c:choose>
                <%--
                    Client create comment option
                --%>
                <c:when test="${statuses[status_id] == 'COMPLETED'}">
                    <div class="d-flex w-100 justify-content-center align-self-center">
                        <div class="text-center w-25">
                            <form
                                    action="controller"
                                    method="post"
                                    onsubmit="return validateCommentCreation('comment-text-holder', '${comment}', '${creation_error}', '${allowed_symbols}');">
                                <input type="hidden" name="command" value="createComment"/>
                                <input type="hidden" name="orderId" value="${order_id}"/>
                                <textarea
                                        class="w-100"
                                        id="comment-text-holder"
                                        name="commentText"
                                        placeholder="${comment}"
                                        style="max-height: 150px; min-height: 50px;"
                                ></textarea>
                                <input type="submit" value="${create}" style="margin-top: 10px"/>
                            </form>
                        </div>
                    </div>
                </c:when>

                <%--  Client pay option  --%>
                <c:when test="${statuses[status_id] == 'WAITING_FOR_PAYMENT'}">
                    <form action="controller" method="post">
<%--                        <input type="hidden" name="command" value="clientPay">--%>
                        <input type="hidden" name="orderId" value="${order_id}">
                            <%--
                                    TODO: SEND ORDER_INFO TO PAY COMMAND
                                    WHEN IT HAS REQUEST SCOPE
                            --%>
<%--                        <input type="submit" value="${pay}">--%>
                        <button type="submit" name="command" value="clientPay">${pay}</button>
                        <button type="submit" id="set-status-btn" name="command" value="setOrderStatus">Decline</button>
                        <select name="statusId" onchange="document.getElementById('set-status-btn').click()">
                            <option value="none" selected disabled hidden>Set status</option>
                            <option value="${OrderStatus.CANCELED.ordinal()}">${OrderStatus.CANCELED}</option>
                        </select>
                    </form>

                    <c:if test="${requestScope.payment_message_key != null}">
                        <fmt:message key="${requestScope.payment_message_key}" var="payment_status"/>
                        <h2>${payment_status}</h2>
                    </c:if>
                </c:when>
            </c:choose>
        </c:when>

        <%-- Manager role elements --%>
        <c:when test="${roleId == Role.MANAGER.ordinal()}">

            <form method="post" action="controller">
                <select name="statusId" onchange="this.form.submit();">
                    <option value="none" selected disabled hidden>Set status</option>
                    <option value="${OrderStatus.WAITING_FOR_PAYMENT.ordinal()}">${OrderStatus.WAITING_FOR_PAYMENT}</option>
                    <option value="${OrderStatus.PAID.ordinal()}">${OrderStatus.PAID}</option>
                    <option value="${OrderStatus.CANCELED.ordinal()}">${OrderStatus.CANCELED}</option>
                </select>
                <input type="hidden" name="command" value="setOrderStatus">
                <input type="hidden" name="orderId" value="${order_info.get('id')}">
                <input type="submit" style="visibility: hidden">
            </form>

            <c:choose>
                <%-- Set clien`t payment --%>
                <c:when test="${statuses[status_id] == OrderStatus.CREATED.name()}">
                    <form action="controller" method="post">
                        <input type="hidden" name="order_id" value="${order_id}">
                        <input type="hidden" name="command" value="setPayment"/>
                        <input type="number" name="cost_value"/>
                        <input type="submit" value="Встановити ціну"/>
                    </form>
                </c:when>

                <%-- Set repairer --%>
                <c:when test="${statuses[status_id] == OrderStatus.PAID.name()}">
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

        <c:when test="${roleId == Role.REPAIRER.ordinal()}">
            <form method="post" action="controller">
                <select name="statusId" onchange="this.form.submit();">
                    <option value="none" selected disabled hidden>Set status</option>
                    <option value="${OrderStatus.IN_WORK.ordinal()}">${OrderStatus.IN_WORK}</option>
                    <option value="${OrderStatus.COMPLETED.ordinal()}">${OrderStatus.COMPLETED}</option>
                </select>
                <input type="hidden" name="command" value="setOrderStatus">
                <input type="hidden" name="orderId" value="${order_info.get('id')}">
                <input type="submit" style="visibility: hidden">
            </form>
        </c:when>
    </c:choose>

    <div class="d-flex w-100 justify-content-center align-self-center">
        <p id="message-holder" style="margin-top: 25px;"></p>
    </div>
</body>
</html>
