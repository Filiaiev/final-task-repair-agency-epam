<%@include file="../lib.jsp"%>
<c:if test="${user != null}">
    <c:redirect url="/controller?command=toHome"/>
</c:if>

<html>
<head>
    <title>Login</title>
    <script src="script/script.js"></script>
    <fmt:message key="login_pattern" var="loginPattern"/>
    <fmt:message key="password_pattern" var="passwordPattern"/>
    <fmt:message key="login.form.log_in" var="logIn"/>
    <fmt:message key="register" var="register"/>
    <fmt:message key="login" var="login"/>
    <fmt:message key="password" var="password"/>
    <fmt:message key="login.form.authorization" var="authorization"/>

</head>
<body>
<%@include file="../nav/navbar.jsp"%>

<main class="login-form">
    <div class="cotainer">
        <div class="row justify-content-center" style="margin: auto;">
            <div class="col-md-6">
                <div class="card" style="margin-top: 100px">
                    <div class="card-header text-center">${authorization}</div>
                    <div class="card-body">
                        <form action="controller" method="post" onsubmit="return validateLoginForm()">
                            <div class="form-group row">
                                <label for="login" class="col-md-4 col-form-label text-md-right">${login}</label>
                                <div class="col-md-6">
                                    <input
                                            type="text"
                                            id="login"
                                            class="form-control"
                                            autocomplete="off"
                                            name="userLogin"
                                            title="${loginPattern}"
                                            required autofocus>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="password" class="col-md-4 col-form-label text-md-right">${password}</label>
                                <div class="col-md-6">
                                    <input
                                            type="password"
                                            id="password"
                                            class="form-control"
                                            name="userPass"
                                            title="${passwordPattern}" required>
                                </div>
                            </div>

                            <div class="col-md-6 offset-md-4">
                                <input type="hidden" name="command" value="login">
                                <input type="submit" class="btn btn-dark" value="${logIn}">
                                <a href="${pageContext.request.contextPath}/register" class="btn btn-dark ml-4">${register}</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<c:if test="${sessionScope.loginMessage != null}">
    <div class="d-flex w-100 justify-content-center" style="margin-top: 20px;">
        <fmt:message key="login.err.${sessionScope.loginMessage}" var="loginMsg"/>
        <p class="text-danger text-center w-25">${loginMsg}</p>
    </div>
</c:if>

</body>
</html>
