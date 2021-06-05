<%@include file="../lib.jsp"%>
<%@taglib prefix="ct" uri="http://filiaiev.agency.com" %>
<%@taglib prefix="tdc" tagdir="/WEB-INF/tags"%>
<html>
<head>
    <link rel="stylesheet" href="style/core.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>
    <title>Order</title>

    <fmt:message key="order.order_number" var="order_no"/>
    <fmt:message key="order.order_date" var="order_date"/>
    <fmt:message key="order.complete_date" var="complete_date"/>
    <fmt:message key="order.order_cost" var="order_cost"/>
    <fmt:message key="order.order_comment" var="order_comment"/>
    <fmt:message key="order.order_description" var="order_description"/>
    <fmt:message key="workers_full_name" var="workers_full_name"/>
    <fmt:message key="client_full_name" var="clients_full_name"/>
    <fmt:message key="order.order_status" var="order_status"/>
    <fmt:message key="order.order_comment" var="comment"/>
    <fmt:message key="order.pay" var="pay"/>
    <fmt:message key="creation_success" var="creation_success"/>
    <fmt:message key="creation_error" var="creation_error"/>
    <fmt:message key="allowed_symbols" var="allowed_symbols"/>
    <fmt:message key="order.create" var="create"/>
    <fmt:message key="order.decline" var="decline"/>
    <fmt:message key="order.set_status" var="set_status"/>
    <fmt:message key="order.set_repairer" var="set_repairer"/>
    <fmt:message key="order.set_payment" var="set_payment"/>
    <fmt:message key="currency.uah" var="currency"/>

    <%-- Order statuses --%>
    <fmt:parseNumber var="status_id" type="number" value="${order_info.get('status_id')}"/>
    <%-- TODO: Why request scope doesnt work? When variable has req. scope --%>
    <fmt:message key="${OrderStatus.values()[status_id].toString()}" var="status_value"/>

    <%--  Payment statuses  --%>

    <c:set var="order_id" value="${order_info.get('id')}"/>
    <c:set var="statuses" value="${OrderStatus.values()}"/>
</head>
<body>
    <%@ include file="../nav/navbar.jsp"%>

    <div class="d-flex w-100 justify-content-center align-self-center">
        <table class="table table-bordered w-25" style="margin-top: 50px;">
            <thead class="thead-dark text-center">
                <tr>
                    <th scope="col" colspan="2">${order_no}${order_id}</th>
                </tr>
            </thead>

            <tbody>
                    <%--    Customer data
                           If role is manager
                   --%>
                <c:if test="${roleId == Role.MANAGER.ordinal()}">
                    <tr>
                        <th scope="row">${clients_full_name}</th>
                        <td>
                            <form action="controller" method="get" id="client-info-form">
                                <a role="link" onclick="document.getElementById('client-info-form').submit();"
                                   style="text-decoration: underline; cursor: pointer;">
                                    <c:out value="${order_info.get('client_last_name')}"/><br>
                                    <c:out value="${order_info.get('client_first_name')}"/><br>
                                    <c:out value="${order_info.get('client_middle_name')}"/><br>
                                </a>
                                <input type="hidden" name="command" value="goToClientInfo">
                                <input type="hidden" name="clientId" value="${order_info.get('client_id')}">
                            </form>
                        </td>
                    </tr>
                </c:if>

                <%-- Order date --%>
                <tr>
                    <th scope="row">${order_date}</th>
                    <td><ct:sqlDateOut sqlDate="${order_info.get('order_date')}" locale="${pageContext.response.locale}"/></td>
                </tr>

                <%-- Complete date --%>
                <c:if test="${statuses[status_id] == 'COMPLETED'}">
                    <tr>
                        <th scope="row">${complete_date}</th>
                        <td><ct:sqlDateOut sqlDate="${order_info.get('complete_date')}" locale="${pageContext.response.locale}"/></td>
                    </tr>
                </c:if>

                <%-- Cost --%>
                <c:if test="${roleId == Role.CLIENT.ordinal() || roleId == Role.MANAGER.ordinal()}">
                    <tr>
                        <th scope="row">${order_cost}</th>
                        <c:choose>
                            <c:when test="${order_info.get('cost') != null}">
                                <td><c:out value="${order_info.get('cost')}"/> ${currency}</td>
                            </c:when>
                            <c:otherwise>
                                <td class="table-secondary">-</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:if>

                <%-- Order text --%>
                <tr>
                    <th scope="row"><c:out value="${order_description}"/></th>
<%--                    <td>${order_info.get('description')}</td>--%>
                    <td><c:out value="${order_info.get('description')}"/></td>
                </tr>

                <%-- Worker`s data --%>
                <c:if test="${order_info.get('worker_last_name') != null && roleId != 1}">
                    <tr>
                        <th scope="row">${workers_full_name}</th>
                        <td>
                            <c:out value="${order_info.get('worker_last_name')}"/><br>
                            <c:out value="${order_info.get('worker_first_name')}"/><br>
                            <c:out value="${order_info.get('worker_middle_name')}"/><br>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${statuses[status_id] == OrderStatus.COMPLETED}">
                    <tr>
                        <th scope="row">${comment}</th>
                        <td><c:out value="${order_info.get('comment')}"/></td>
                    </tr>
                </c:if>

                <%-- Status value --%>
                <tr>
                    <th scope="row">${order_status}</th>
<%--                    <td id="status-holder"><c:out value="${status_value}"/></td>--%>
                    <tdc:tdStatusColor value="${status_value}" status="${statuses[status_id]}"/>
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
                <c:when test="${statuses[status_id] == OrderStatus.COMPLETED.name()}">
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
                <c:when test="${statuses[status_id] == OrderStatus.WAITING_FOR_PAYMENT.name()}">
                    <div class="d-flex w-100 justify-content-center align-self-center">
                        <form action="controller" method="post">
                            <input type="hidden" name="orderId" value="${order_id}">
                            <button class="btn btn-success" type="submit" name="command" value="clientPay">${pay}</button>
                            <input type="hidden" name="statusId" value="${OrderStatus.CANCELED.ordinal()}">
                            <button class="btn btn-danger" type="submit" id="set-status-btn" name="command" value="setOrderStatus">${decline}</button>
                        </form>

                        <c:if test="${requestScope.payment_message_key != null}">
                            <fmt:message key="${requestScope.payment_message_key}" var="payment_status"/>
                            <h2>${payment_status}</h2>
                        </c:if>
                    </div>
                </c:when>
            </c:choose>
        </c:when>

        <%-- Manager role elements --%>
        <c:when test="${roleId == Role.MANAGER.ordinal()}">
            <c:choose>
                <%-- Set clien`t payment --%>
                <c:when test="${statuses[status_id] == OrderStatus.CREATED}">
                    <div class="d-flex w-100 justify-content-center align-self-center">
                        <form action="controller" method="post">
                            <input type="hidden" name="order_id" value="${order_id}">
                            <input type="hidden" name="command" value="setPayment"/>
                            <input type="number" name="cost_value" placeholder="${currency}"/>
                            <button class="btn btn-secondary" type="submit" ><c:out value="${set_payment}"/></button>
                        </form>
                    </div>
                </c:when>

                <%-- Set repairer --%>
                <c:when test="${statuses[status_id] == OrderStatus.PAID}">
                    <div class="d-flex w-100 justify-content-center align-self-center">
                        <form action="controller" method="post">
                        <select name="repairer" onchange="this.form.submit();">
                            <option value="none" selected disabled hidden>${set_repairer}</option>
                            <c:forEach items="${repairers}" var="entry">
                                <option value="${entry.key}">${entry.value.lastName} ${entry.value.firstName} ${entry.value.middleName}</option>
                            </c:forEach>
                        </select>
                            <input type="hidden" name="command" value="setRepairer"/>
                        </form>
                    </div>
                </c:when>
            </c:choose>

            <c:if test="${statuses[status_id] != OrderStatus.COMPLETED}">
                <div class="d-flex w-100 justify-content-center align-self-center">
                    <form method="post" action="controller">
                        <select name="statusId" onchange="this.form.submit();">
                            <option value="none" selected disabled hidden>${set_status}</option>
                            <option value="${OrderStatus.WAITING_FOR_PAYMENT.ordinal()}">${OrderStatus.WAITING_FOR_PAYMENT}</option>
                            <option value="${OrderStatus.PAID.ordinal()}">${OrderStatus.PAID}</option>
                            <option value="${OrderStatus.CANCELED.ordinal()}">${OrderStatus.CANCELED}</option>
                        </select>
                        <input type="hidden" name="command" value="setOrderStatus">
                        <input type="hidden" name="orderId" value="${order_info.get('id')}">
                    </form>
                </div>
            </c:if>
        </c:when>

        <c:when test="${roleId == Role.REPAIRER.ordinal() && statuses[status_id] != OrderStatus.COMPLETED}">
            <div class="d-flex w-100 justify-content-center align-self-center">
                    <form method="post" action="controller">
                        <select name="statusId" onchange="this.form.submit();">
                            <option value="none" selected disabled hidden>${set_status}</option>
                            <option value="${OrderStatus.IN_WORK.ordinal()}">${OrderStatus.IN_WORK}</option>
                            <option value="${OrderStatus.COMPLETED.ordinal()}">${OrderStatus.COMPLETED}</option>
                        </select>
                        <input type="hidden" name="command" value="setOrderStatus">
                        <input type="hidden" name="orderId" value="${order_info.get('id')}">
                    </form>
            </div>
        </c:when>
    </c:choose>

    <c:if test="${statuses[status_id] == OrderStatus.WAITING_FOR_PAYMENT && sessionScope.paymentStatus != null}">
        <fmt:message key="payment.${sessionScope.paymentStatus}" var="payment_status"/>
        <div class="d-flex w-100 justify-content-center align-self-center">
            <p id="message-holder" style="margin-top: 25px;">${payment_status}</p>
        </div>
    </c:if>
</body>
</html>
