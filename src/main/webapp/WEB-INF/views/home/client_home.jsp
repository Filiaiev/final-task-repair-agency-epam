<%--<div id="creation-form" style="margin-top: 10%; display: none">--%>
<%--    <div class="d-flex w-100 justify-content-center align-self-center">--%>
<%--&lt;%&ndash;        <form class="form w-50" &lt;%&ndash;method="post" name="controller"&ndash;%&gt;>&ndash;%&gt;--%>
<%--            <div class="text-center w-25">--%>
<%--                <label for="order-text-holder">Order text</label>--%>
<%--                <textarea class="form-control" id="order-text-holder" name="orderText" rows="6" style="min-height: 100px; max-height: 350px;"></textarea>--%>
<%--                <button class="btn btn-dark mt-2" onclick="createOrder()">Submit</button>--%>
<%--&lt;%&ndash;                <input type="hidden" name="command" value="createOrder">&ndash;%&gt;--%>
<%--            </div>--%>
<%--&lt;%&ndash;        </form>&ndash;%&gt;--%>
<%--    </div>--%>
<%--</div>--%>

<%--<div class="userMenuBar">--%>
<%--    <button onclick="createOrder()">Create</button><br>--%>
<%--    <button onclick="location.href='controller?command=getOrders'">My orders</button><br>--%>

    <%--  JS Don`t work with cyrillic. USE form with submitd  --%>
<%--    <form method="post" action="controller" class="creationForm">--%>
<%--        <textarea name="orderText" id="formTextArea" placeholder="fill form"></textarea>--%>
<%--        <button id="hideFormButton" onclick="hideCreationForm()">Hide</button>--%>
<%--&lt;%&ndash;        <button onclick="sendForm();">Send</button>&ndash;%&gt;--%>
<%--        <input type="submit" name="command" value="createOrder">--%>
<%--    </form>--%>
<%--</div>--%>

<table>
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
                <form method="get" action="controller">
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

