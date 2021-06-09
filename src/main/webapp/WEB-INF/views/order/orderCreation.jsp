<%@include file="../lib.jsp"%>
<html>
<head>
    <title>Create</title>
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>
    <fmt:message key="creation_success" var="creationSuccess"/>
    <fmt:message key="creation_error" var="creationError"/>
    <fmt:message key="order.order" var="order"/>
    <fmt:message key="allowed_symbols" var="allowedSymbols"/>
    <fmt:message key="order.creation.describe_your_order" var="describeYourOrder"/>
    <fmt:message key="order.create" var="create"/>
</head>
<body>
<%@include file="../nav/navbar.jsp"%>
<div class="d-flex" id="creation-form" style="margin-top: 10%">
    <div class="d-flex w-100 justify-content-center align-self-center">
        <%--        <form class="form w-50" &lt;%&ndash;method="post" name="controller"&ndash;%&gt;>--%>
        <div class="text-center w-25">
            <label for="order-text-holder">${describeYourOrder}</label>
            <textarea
                    class="form-control"
                    id="order-text-holder"
                    name="orderText"
                    rows="6"
                    minlength="10"
                    maxlength="200"
                    style="min-height: 100px; max-height: 350px;"></textarea>
            <button
                    class="btn btn-dark mt-2"
                    onclick="createOrder('order-text-holder', '${order}',
                            '${creationError}', '${creationSuccess}', '${allowedSymbols}')">
                    ${create}</button>
            <%--                <input type="hidden" name="command" value="createOrder">--%>
        </div>
        <%--        </form>--%>
    </div>
</div>

<div class="d-flex w-100 justify-content-center align-self-center">
    <p id="message-holder" style="margin-top: 25px;"></p>
</div>

</body>
</html>
