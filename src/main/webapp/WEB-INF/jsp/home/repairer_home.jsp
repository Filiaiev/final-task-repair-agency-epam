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