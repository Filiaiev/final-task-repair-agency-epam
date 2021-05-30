<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Login</title>
    <script src="script/script.js"></script>
    <fmt:setBundle basename="l18n.app_uk"/>
    <fmt:message key="login_pattern" var="login_pattern"/>
    <fmt:message key="password_pattern" var="password_pattern"/>
</head>
<body>
<%@include file="nav/navbar.jsp"%>
<main class="login-form">
    <div class="cotainer">
        <div class="row justify-content-center" style="margin: auto;">
            <div class="col-md-6">
                <div class="card" style="margin-top: 100px">
                    <div class="card-header text-center">Authorization</div>
                    <div class="card-body">
                        <form action="controller" method="post" onsubmit="return validateLoginForm()">
                            <div class="form-group row">
                                <label for="login" class="col-md-4 col-form-label text-md-right">Login</label>
                                <div class="col-md-6">
                                    <input
                                            type="text"
                                            id="login"
                                            class="form-control"
                                            autocomplete="off"
                                            name="userLogin"
                                            title="${login_pattern}"
                                            required autofocus>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="password" class="col-md-4 col-form-label text-md-right">Password</label>
                                <div class="col-md-6">
                                    <input
                                            type="password"
                                            id="password"
                                            class="form-control"
                                            name="userPass"
                                            title="${password_pattern}" required>
                                </div>
                            </div>

                            <div class="col-md-6 offset-md-4">
                                <input type="hidden" name="command" value="login">
                                <input type="submit" class="btn btn-dark" value="Log in">
<%--                                <button onclick="location.href='${pageContext.request.contextPath}/register'" class="btn btn-dark ml-4">Register</button>--%>
                                <a href="${pageContext.request.contextPath}/register" class="btn btn-dark ml-4">Register</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<c:out value="${registerMessage}"/>
</body>
</html>
