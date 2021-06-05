<%@include file="../lib.jsp"%>
<%@taglib prefix="ct" uri="http://filiaiev.agency.com" %>
<%@taglib prefix="tdc" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Orders</title>
    <c:set var="statuses" value="<%=OrderStatus.values()%>" scope="session"/>
    <link rel="stylesheet" href="style/core.css" type="text/css">
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>
    <fmt:message key="status" var="status"/>
    <fmt:message key="date" var="date"/>
    <fmt:message key="cost" var="cost"/>
    <fmt:message key="asc" var="asc"/>
    <fmt:message key="desc" var="desc"/>
    <fmt:message key="filter.set_filter" var="set_filter"/>
    <fmt:message key="filter.reset_filter" var="reset_filter"/>
    <fmt:message key="filter.select_status" var="select_status"/>
    <fmt:message key="filter.select_repairer" var="select_repairer"/>

    <fmt:message key="order.order_number" var="order_no"/>
    <fmt:message key="order.order_date" var="order_date"/>
    <fmt:message key="order.order_cost" var="order_cost"/>
    <fmt:message key="order.order_status" var="order_status"/>
    <fmt:message key="currency.uah" var="currency"/>
</head>
<body>
<%@include file="../nav/navbar.jsp"%>
<c:if test="${sessionScope.orders != null}">
    <form method="get" action="controller" style="display: block; padding-top: 50px;">
        <div class="d-inline-flex flex-row" style="margin-left: 3%">
            <input type="hidden" name="command" value="getOrders">

            <div class="flex flex-row">
                    <%--            <p>Sort by</p>--%>
                <div class="d-inline-flex flex-row filter-radio-div">
                    <input class="form-radio-input" id="status_id" type="radio" name="sortBy" value="status_id">
                    <label class="form-radio-label" for="status_id">${status}</label>

                    <input class="form-radio-input" id="order_date" type="radio" name="sortBy" value="order_date">
                    <label class="form-radio-label" for="order_date">${date}</label>

                    <input class="form-radio-input" id="cost" type="radio" name="sortBy" value="cost">
                    <label class="form-radio-label" for="cost">${cost}</label>
                </div>

                <div class="d-flex filter-radio-div">
                    <input class="form-radio-input" id="desc" type="radio" name="ordering" value="desc">
                    <label class="form-radio-label" for="desc">${desc}</label>

                    <input class="form-radio-input" id="asc" type="radio" name="ordering" value="asc">
                    <label class="form-radio-label" for="asc">${asc}</label>

                    <c:if test="${requestScope.sortBy != null || requestScope.ordering != null}">
                        <script>
                            document.getElementById('${requestScope.sortBy}').checked = true;
                            document.getElementById('${requestScope.ordering}').checked = true;
                        </script>
                    </c:if>
                </div>

                <div>
                    <button class="btn btn-secondary" type="submit">${set_filter}</button>
                    <button class="btn btn-secondary" type="submit" name="resetFilter" value="true">${reset_filter}</button>
                </div>
            </div>

            <div class="d-inline-flex flex-column filter-div">
                <div>
                    <select name="status_id">
                            <%-- TODO: label == localization --%>
                        <option value="none" selected disabled hidden>${select_status}</option>
                        <c:forEach items="${OrderStatus.values()}" var="item" varStatus="s">
                            <%-- change <item> to localized, don`t touch value --%>
                            <c:choose>
                                <c:when test="${status_id == s.index}">
                                    <option value="${s.index}" selected>${item}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${s.index}">${item}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>

                <div style="padding-top: 25px;">
                    <select name="worker_id">
                        <option value="none" selected disabled hidden>${select_repairer}</option>
                        <c:forEach items="${repairers}" var="entry">
                            <c:choose>
                                <c:when test="${worker_id == entry.key}">
                                    <option value="${entry.key}" selected><c:out value="${entry.value.lastName} ${entry.value.firstName} ${entry.value.middleName}"/></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${entry.key}"><c:out value="${entry.value.lastName} ${entry.value.firstName} ${entry.value.middleName}"/></option>

                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>
        <div class="d-inline-flex w-100 justify-content-center">

            <div class="d-inline-flex flex-column justify-content-center">
                <c:forEach items="${sessionScope.orders}" var="item" varStatus="loop_status">
                    <div style="flex-direction: column">
                        <table
                                onclick="window.location.href = 'controller?command=getOrderInfo&orderId=${item.id}'"
                                class="table table-bordered"
                                style="cursor:pointer;">
                            <thead class="thead-light text-center">
                            <tr>
                                <th scope="col" colspan="2">${order_no}${item.id}</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr>
                                <th scope="row">${order_date}</th>
                                <td><ct:sqlDateOut sqlDate="${item.orderDate}" locale="${pageContext.response.locale}"/></td>
                            </tr>

                            <tr>
                                <th scope="row">${order_cost}</th>
                                <c:choose>
                                    <c:when test="${item.cost == null}">
                                        <td class="table-secondary">-</td>
                                    </c:when>
                                    <c:otherwise><td>${item.cost} ${currency}</td></c:otherwise>
                                </c:choose>
                            </tr>

                                <%--
                                     status-holder-$index is an id of an element
                                     whose bg color setStatusColor(id, status_id) function
                                     will change
                                --%>
                            <tr>
                                <th scope="row">${order_status}</th>
                                <tdc:tdStatusColor value="${statuses[item.statusId]}"
                                                   status="${statuses[item.statusId]}"/>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                    <br/>
                </c:forEach>
            </div>

        </div>
        <div class="d-flex w-100 justify-content-center">
            <c:forEach var="i" begin="1" end="${pagesCount}">
                <button class="page-button" type="submit" name="page" value="${i}">${i}</button>
            </c:forEach>
        </div>
    </form>
</c:if>

</body>
</html>
