<%@include file="lib.jsp"%>
<html>
<head>
    <title>Create</title>
    <script src="${pageContext.request.contextPath}/script/script.js" type="text/javascript"></script>
    <fmt:setBundle basename="l18n.app_uk"/>
    <fmt:message key="creation_success" var="creation_success"/>
    <fmt:message key="creation_error" var="creation_error"/>
    <fmt:message key="order" var="order"/>
    <fmt:message key="allowed_symbols" var="allowed_symbols"/>
</head>
<body>
<%@include file="nav/navbar.jsp"%>

<div class="d-flex" id="creation-form" style="margin-top: 10%">
    <div class="d-flex w-100 justify-content-center align-self-center">
        <%--        <form class="form w-50" &lt;%&ndash;method="post" name="controller"&ndash;%&gt;>--%>
        <div class="text-center w-25">
            <label for="order-text-holder">Order text</label>
            <textarea
                    class="form-control"
                    id="order-text-holder"
                    name="orderText"
                    rows="6"
                    style="min-height: 100px; max-height: 350px;"></textarea>
            <button
                    class="btn btn-dark mt-2"
                    onclick="createOrder('order-text-holder', '${order}',
                            '${creation_error}', '${creation_success}', '${allowed_symbols}')"
            >Submit</button>
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
