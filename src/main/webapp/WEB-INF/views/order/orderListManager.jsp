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
    <fmt:message key="filter.set_filter" var="setFilter"/>
    <fmt:message key="filter.reset_filter" var="resetFilter"/>
    <fmt:message key="filter.select_status" var="selectStatus"/>
    <fmt:message key="filter.select_repairer" var="selectRepairer"/>

    <fmt:message key="order.order_number" var="orderNo"/>
    <fmt:message key="order.order_date" var="orderDate"/>
    <fmt:message key="order.order_cost" var="orderCost"/>
    <fmt:message key="order.order_status" var="orderStatus"/>
    <fmt:message key="currency.uah" var="currency"/>

    <fmt:message key="download.download_report_in_pdf" var="downloadReportInPdf"/>
</head>
<body>
<%@include file="../nav/navbar.jsp"%>
<c:if test="${sessionScope.orders != null}">
    <form id="main-form" method="get" action="controller" style="display: block; padding-top: 50px;">
        <div class="d-inline-flex flex-row" style="margin-left: 3%">
            <input type="hidden" name="command" value="getOrders">

            <div class="flex flex-row">
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
                    <button class="btn btn-secondary" type="submit">${setFilter}</button>
                    <button class="btn btn-secondary" type="submit" name="resetFilter" value="true">${resetFilter}</button>
                </div>

                <div>
                    <button class="link-button" style="margin-top: 30px;" type="button" onclick="
                       var form = document.getElementById('main-form');
                       form[0].setAttribute('value', 'generateReport');
                       form.submit();
                       form[0].setAttribute('value', 'getOrders');"
                    >${downloadReportInPdf}</button>
                </div>
            </div>

            <div class="d-inline-flex flex-column filter-div">
                <div>
                    <select name="status_id">
                            <%-- TODO: label == localization --%>
                        <option value="none" selected disabled hidden>${selectStatus}</option>
                        <c:forEach items="${OrderStatus.values()}" var="item" varStatus="s">
                            <%-- change <item> to localized, don`t touch value --%>
                            <fmt:message key="order.status.${item}" var="statusLoc"/>
                            <c:choose>
                                <c:when test="${requestScope.status_id == s.index}">
                                    <option value="${s.index}" selected>${statusLoc}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${s.index}">${statusLoc}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>

                <div style="padding-top: 25px;">
                    <select name="worker_id">
                        <option value="none" selected disabled hidden>${selectRepairer}</option>
                        <c:forEach items="${repairers}" var="entry">
                            <c:choose>
                                <c:when test="${requestScope.worker_id == entry.key}">
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
                                    <c:otherwise><td>${item.cost} ${currency}</td></c:otherwise>
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
</c:if>

</body>
</html>
