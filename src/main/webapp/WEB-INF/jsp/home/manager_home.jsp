<form method="get" action="controller" class="filter-form" style="display: block; padding-top: 50px;">
    <div style="display: inline-flex;">
        <p>Статус</p>
        <p>Майстер</p>
    </div>

    <div></div>

    <div style="display: inline-flex; flex-direction: row;">
        <select name="filterTaskStatus" onchange="this.form.submit()">
            <%-- TODO: label == localization --%>
            <option value="none" selected disabled hidden>Select com.filiaiev.agency.filter</option>
            <c:forEach items="${OrderStatus.values()}" var="item">
                <option>${item}</option>
            </c:forEach>
        </select>

        <select name="filterTaskRepairer" onchange="this.form.submit()">
            <option value="none" selected disabled hidden>Select com.filiaiev.agency.filter</option>
            <c:forEach items="${repairers}" var="item">
                <option value="${item.id}">${item.lastName} ${item.firstName} ${item.middleName}</option>
            </c:forEach>
        </select>
    </div>
    <input type="hidden" name="command" value="filterBy">
    <input type="submit" value="com.filiaiev.agency.filter" style="visibility: hidden">
</form>

<form method="get" action="controller">
    <input id="order-by-1" type="radio" name="orderBy" value="status_id">
    <label for="order-by-1">status</label>
    <input id="order-by-2" type="radio" name="orderBy" value="order_date">
    <label for="order-by-2">date</label>
    <input id="order-by-3" type="radio" name="orderBy" value="cost">
    <label for="order-by-3">cost</label>
    <input type="hidden" name="command" value="sortOrders">
    <input type="submit" value="submit">
</form>

<table style="display: block;">
    <c:forEach items="${requestScope.orders}" var="item" varStatus="loop_status">
        <tr>
            <td><c:out value="Order number"/></td>
            <td><c:out value="${item.id}"/></td>
        </tr>

        <tr>
            <td><c:out value="Order date"/></td>
            <td><c:out value="${item.orderDate}"/></td>
        </tr>

        <%--
             status-holder-$index is an id of an element
             whose bg color setStatusColor(id, status_id) function
             will change
        --%>
        <tr>
            <td><c:out value="Status"/></td>
            <td id="status-holder-${loop_status.index}"><c:out value="${status_enum[item.statusId]}"/></td>
        </tr>

        <script>setStatusColor("status-holder-${loop_status.index}", ${item.statusId});</script>

        <tr>
            <td>
                <form method="post" action="controller">
                    <input type="hidden" name="command" value="getOrderInfo"/>
                    <input type="hidden" name="orderId" value="${item.id}">
                    <input type="submit" value="View order info"/>
                </form>
                    <%--                <button onclick="viewOrder(${item.id});">Order #</button>--%>
            </td>
        </tr>

        <tr>
            <td>${not loop_status.last ? '<hr/>' : '<br/>'}</td>
        </tr>
    </c:forEach>
</table>