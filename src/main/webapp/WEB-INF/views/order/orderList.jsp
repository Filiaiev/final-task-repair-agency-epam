<%@include file="../lib.jsp"%>
<%@taglib prefix="tdc" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ct" uri="http://filiaiev.agency.com" %>
<html>
<head>
    <title>Orders</title>
    <c:set var="statuses" value="<%=OrderStatus.values()%>" scope="session"/>
    <link rel="stylesheet" href="style/core.css" type="text/css">
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>

    <fmt:message key="order.order_number" var="orderNo"/>
    <fmt:message key="order.order_date" var="orderDate"/>
    <fmt:message key="order.order_cost" var="orderCost"/>
    <fmt:message key="order.order_status" var="orderStatus"/>
    <fmt:message key="currency.uah" var="currency"/>
</head>
<body style="margin-bottom: 60px;">
<%@include file="../nav/navbar.jsp"%>
<form method="get" action="controller" style="display: block; padding-top: 50px;">
    <input type="hidden" name="command" value="getOrders">
        <div class="d-inline-flex w-100 justify-content-center">

        <div class="d-inline-flex flex-column justify-content-center">
            <c:forEach items="${sessionScope.orders}" var="item" varStatus="s">
                <div style="flex-direction: column">
                    <table
                            onclick="window.location.href = 'controller?command=getOrderInfo&orderId=${item.id}'"
                            class="table table-bordered"
                            style="cursor:pointer;">
                        <thead class="thead-light text-center">
                        <tr>
                            <th scope="col" colspan="2">${orderNo}${item.id}</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <th scope="row">${orderDate}</th>
                            <td><ct:sqlDateOut sqlDate="${item.orderDate}" locale="${pageContext.response.locale}"/></td>
                        </tr>

                        <tr>
                            <th scope="row">${orderCost}</th>
                            <c:choose>
                                <c:when test="${item.cost == null}">
                                    <td class="table-secondary">-</td>
                                </c:when>
                                <c:otherwise><td><c:out value="${item.cost}"/> ${currency}</td></c:otherwise>
                            </c:choose>
                        </tr>
                            <%--
                                 status-holder-$index is an id of an element
                                 whose bg color setStatusColor(id, status_id) function
                                 will change
                            --%>
                        <tr>
                            <th scope="row">${orderStatus}</th>
                            <tdc:tdStatusColor value="${item.statusName}"
                                               status="${statuses[item.statusId]}"/>
                        </tr>

                        </tbody>
                    </table>
                </div>
                <br/>
            </c:forEach>
        </div>

    </div>
<div class="pages-count-div d-flex w-100 justify-content-center">
    <c:forEach var="i" begin="1" end="${pagesCount}">
        <button class="link-button" type="submit" name="page" value="${i}">${i}</button>
    </c:forEach>
</div>
</form>

</body>
</html>
