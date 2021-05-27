<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
<%--    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">--%>
<%--    <script src="${pageContext.request.contextPath}/script/script.js"></script>--%>
<%--    <style>--%>
<%--        input:invalid{--%>
<%--            border: red solid 3px;--%>
<%--        }--%>
<%--    </style>--%>
</head>
<body>
<%@include file="nav/navbar.jsp"%>

<main class="login-form">
    <div class="cotainer">
        <div class="row justify-content-center" style="margin: auto;">
            <div class="col-md-6">
                <div class="card mt-5">
                    <div class="card-header text-center">Authorization</div>
                    <div class="card-body">
                        <form action="controller" method="post">
                            <div class="form-group row">
                                <label for="login" class="col-md-4 col-form-label text-md-right">Login</label>
                                <div class="col-md-6">
                                    <input type="text" id="login" class="form-control" autocomplete="off" name="userLogin" pattern="[a-z][a-z0-9]{5,15}" title="a-z0-9" required autofocus>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="password" class="col-md-4 col-form-label text-md-right">Password</label>
                                <div class="col-md-6">
                                    <input type="password" id="password" class="form-control" name="userPass" pattern="[A-z0-9]+" required>
                                </div>
                            </div>

                            <div class="col-md-6 offset-md-4">
                                <input type="hidden" name="command" value="login">
                                <input type="submit" class="btn btn-dark" value="Log in">
                                <button onclick="location.href='${pageContext.request.contextPath}/register'" class="btn btn-dark ml-4">Register</button>
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
