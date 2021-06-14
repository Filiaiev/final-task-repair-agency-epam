<%@include file="../lib.jsp"%>
<%@taglib prefix="ct" uri="http://filiaiev.agency.com" %>
<%@taglib prefix="tdc" tagdir="/WEB-INF/tags"%>
<html>
<head>
    <link rel="stylesheet" href="style/core.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>
    <title>Order</title>

    <fmt:message key="order.order_number" var="orderNo"/>
    <fmt:message key="order.order_date" var="orderDate"/>
    <fmt:message key="order.complete_date" var="completeDate"/>
    <fmt:message key="order.order_cost" var="orderCost"/>
    <fmt:message key="order.order_comment" var="orderComment"/>
    <fmt:message key="order.order_description" var="orderDescription"/>
    <fmt:message key="workers_full_name" var="workersFullName"/>
    <fmt:message key="client_full_name" var="clientsFullName"/>
    <fmt:message key="order.order_status" var="orderStatus"/>
    <fmt:message key="order.order_comment" var="comment"/>
    <fmt:message key="order.pay" var="pay"/>
    <fmt:message key="creation_success" var="creationSuccess"/>
    <fmt:message key="creation_error" var="creationError"/>
    <fmt:message key="allowed_symbols" var="allowedSymbols"/>
    <fmt:message key="order.create" var="create"/>
    <fmt:message key="order.decline" var="decline"/>
    <fmt:message key="order.set_status" var="setStatus"/>
    <fmt:message key="order.set_repairer" var="setRepairer"/>
    <fmt:message key="order.set_payment" var="setPayment"/>
    <fmt:message key="currency.uah" var="currency"/>

    <%-- Order statuses --%>
    <fmt:parseNumber var="statusId" type="number" value="${orderInfo.get('statusId')}"/>
    <%-- TODO: Why request scope doesnt work? When variable has req. scope --%>

    <%--  Payment statuses  --%>

    <c:set var="orderId" value="${orderInfo.get('id')}"/>
    <c:set var="statuses" value="${OrderStatus.values()}"/>
</head>
<body>
    <%@ include file="../nav/navbar.jsp"%>

    <div class="d-flex w-100 justify-content-center align-self-center">
        <table class="table table-bordered w-25" style="margin-top: 50px;">
            <thead class="thead-dark text-center">
                <tr>
                    <th scope="col" colspan="2">${orderNo}${orderId}</th>
                </tr>
            </thead>

            <tbody>
                    <%--    Customer data
                           If role is manager
                   --%>
                <c:if test="${sessionScope.role == Role.MANAGER}">
                    <tr>
                        <th scope="row">${clientsFullName}</th>
                        <td>
                            <form action="controller" method="get" id="client-info-form">
                                <a role="link" onclick="document.getElementById('client-info-form').submit();"
                                   style="text-decoration: underline; cursor: pointer;">
                                    <c:out value="${orderInfo.get('clientLastName')}"/><br>
                                    <c:out value="${orderInfo.get('clientFirstName')}"/><br>
                                    <c:out value="${orderInfo.get('clientMiddleName')}"/><br>
                                </a>
                                <input type="hidden" name="command" value="toClientInfo">
                                <input type="hidden" name="clientId" value="${orderInfo.get('clientId')}">
                            </form>
                        </td>
                    </tr>
                </c:if>

                <%-- Order date --%>
                <tr>
                    <th scope="row">${orderDate}</th>
                    <td><ct:sqlDateOut sqlDate="${orderInfo.get('orderDate')}" locale="${pageContext.response.locale}"/></td>
                </tr>

                <%-- Complete date --%>
                <c:if test="${statuses[statusId] == OrderStatus.COMPLETED}">
                    <tr>
                        <th scope="row">${completeDate}</th>
                        <td><ct:sqlDateOut sqlDate="${orderInfo.get('completeDate')}" locale="${pageContext.response.locale}"/></td>
                    </tr>
                </c:if>

                <%-- Cost --%>
                <c:if test="${sessionScope.role == Role.CLIENT || sessionScope.role == Role.MANAGER}">
                    <tr>
                        <th scope="row">${orderCost}</th>
                        <c:choose>
                            <c:when test="${orderInfo.get('cost') != null}">
                                <td><c:out value="${orderInfo.get('cost')}"/> ${currency}</td>
                            </c:when>
                            <c:otherwise>
                                <td class="table-secondary">-</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:if>

                <%-- Order text --%>
                <tr>
                    <th scope="row"><c:out value="${orderDescription}"/></th>
<%--                    <td>${orderInfo.get('description')}</td>--%>
                    <td><c:out value="${orderInfo.get('description')}"/></td>
                </tr>

                <%-- Worker`s data --%>
                <c:if test="${orderInfo.get('workerId') != null && sessionScope.role != Role.REPAIRER}">
                    <tr>
                        <th scope="row">${workersFullName}</th>
                        <td>
                            <c:out value="${orderInfo.get('workerLastName')}"/><br>
                            <c:out value="${orderInfo.get('workerFirstName')}"/><br>
                            <c:out value="${orderInfo.get('workerMiddleName')}"/><br>
                        </td>
                    </tr>
                </c:if>

                <%-- Comment --%>
                <c:if test="${statuses[statusId] == OrderStatus.COMPLETED}">
                    <tr>
                        <th scope="row">${comment}</th>
                        <td><c:out value="${orderInfo.get('comment')}"/></td>
                    </tr>
                </c:if>

                <%-- Status value --%>
                <tr>
                    <th scope="row">${orderStatus}</th>
<%--                    <td id="status-holder"><c:out value="${status_value}"/></td>--%>
                    <tdc:tdStatusColor value="${orderInfo.get('statusName')}" status="${statuses[statusId]}"/>
                </tr>
            </tbody>
        </table>
    </div>

    <%-- Controllers --%>
    <c:choose>
        <%-- Client role elements --%>
        <c:when test="${sessionScope.role == Role.CLIENT}">
            <c:choose>
                <%--
                    Client create comment option
                --%>
                <c:when test="${statuses[statusId] == OrderStatus.COMPLETED}">
                    <div class="d-flex w-100 justify-content-center align-self-center">
                        <div class="text-center w-25">
                            <form
                                    action="controller"
                                    method="post"
                                    onsubmit="return validateCommentCreation('comment-text-holder',
                                            '${comment}', '${creationError}', '${allowedSymbols}');">
                                <input type="hidden" name="command" value="createComment"/>
                                <input type="hidden" name="orderId" value="${orderId}"/>
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
                <c:when test="${statuses[statusId] == OrderStatus.WAITING_FOR_PAYMENT}">
                    <div class="d-flex w-100 justify-content-center align-self-center">
                        <form action="controller" method="post">
                            <input type="hidden" name="orderId" value="${orderId}">
                            <button class="btn btn-success" type="submit" name="command" value="clientPay">${pay}</button>
                            <input type="hidden" name="statusId" value="${OrderStatus.CANCELED.ordinal()}">
                            <button class="btn btn-danger" type="submit" id="set-status-btn" name="command" value="setOrderStatus">${decline}</button>
                        </form>

<%--                        <c:if test="${requestScope.payment_message_key != null}">--%>
<%--                            <fmt:message key="${requestScope.payment_message_key}" var="paymentStatus"/>--%>
<%--                            <h2>${paymentStatus}</h2>--%>
<%--                        </c:if>--%>
                    </div>
                </c:when>
            </c:choose>
        </c:when>

        <%-- Manager role elements --%>
        <c:when test="${sessionScope.role == Role.MANAGER}">
            <c:choose>
                <%-- Set clien`t payment --%>
                <c:when test="${statuses[statusId] == OrderStatus.CREATED}">
                    <div class="d-flex w-100 justify-content-center align-self-center">
                        <form action="controller" method="post">
                            <input type="hidden" name="orderId" value="${orderId}">
                            <input type="hidden" name="command" value="setPayment"/>
                            <input type="number" name="costValue" placeholder="${currency}" autocomplete="no"/>
                            <button class="btn btn-secondary" type="submit" ><c:out value="${setPayment}"/></button>
                        </form>
                    </div>
                </c:when>

                <%-- Set repairer --%>
                <c:when test="${statuses[statusId] == OrderStatus.PAID}">
                    <div class="d-flex w-100 justify-content-center align-self-center">
                        <form action="controller" method="post">
                        <select name="repairer" onchange="this.form.submit();">
                            <option value="none" selected disabled hidden>${setRepairer}</option>
                            <c:forEach items="${repairers}" var="entry">
                                <option value="${entry.key}">${entry.value.lastName} ${entry.value.firstName} ${entry.value.middleName}</option>
                            </c:forEach>
                        </select>
                            <input type="hidden" name="command" value="setRepairer"/>
                        </form>
                    </div>
                </c:when>
            </c:choose>

            <c:if test="${statuses[statusId] != OrderStatus.COMPLETED}">
                <div class="d-flex w-100 justify-content-center align-self-center">
                    <form method="post" action="controller">
                        <select name="statusId" onchange="this.form.submit();">
                            <option value="none" selected disabled hidden>${setStatus}</option>
                            <option value="${OrderStatus.WAITING_FOR_PAYMENT.ordinal()}">${OrderStatus.WAITING_FOR_PAYMENT}</option>
                            <option value="${OrderStatus.PAID.ordinal()}">${OrderStatus.PAID}</option>
                            <option value="${OrderStatus.CANCELED.ordinal()}">${OrderStatus.CANCELED}</option>
                        </select>
                        <input type="hidden" name="command" value="setOrderStatus">
                        <input type="hidden" name="orderId" value="${orderInfo.get('id')}">
                    </form>
                </div>
            </c:if>
        </c:when>

        <c:when test="${sessionScope.role == Role.REPAIRER && statuses[statusId] != OrderStatus.COMPLETED}">
            <div class="d-flex w-100 justify-content-center align-self-center">
                    <form method="post" action="controller">
                        <select name="statusId" onchange="this.form.submit();">
                            <option value="none" selected disabled hidden>${setStatus}</option>
                            <option value="${OrderStatus.IN_WORK.ordinal()}">${OrderStatus.IN_WORK}</option>
                            <option value="${OrderStatus.COMPLETED.ordinal()}">${OrderStatus.COMPLETED}</option>
                        </select>
                        <input type="hidden" name="command" value="setOrderStatus">
                        <input type="hidden" name="orderId" value="${orderInfo.get('id')}">
                    </form>
            </div>
        </c:when>
    </c:choose>


<fmt:message key="payment.${requestScope.paymentStatus}" var="paymentStatus"/>
<div class="d-flex w-100 justify-content-center align-self-center">
    <p id="message-holder" style="margin-top: 10px; margin-bottom: 100px">
        <c:if test="${requestScope.paymentStatus != null}"><c:out value="${paymentStatus}"/></c:if>
    </p>
</div>
</body>
</html>
