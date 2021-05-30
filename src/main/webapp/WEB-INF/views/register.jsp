<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="home" value="Home"/>

<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">
    <link rel="stylesheet" href="style/core.css"/>
    <script src="script/script.js"></script>
    <fmt:setBundle basename="l18n.app_uk"/>
    <fmt:message key="login_pattern" var="login_pattern"/>
    <fmt:message key="password_pattern" var="password_pattern"/>
    <fmt:message key="name_pattern" var="name_pattern"/>
</head>
<body onload="setMaxDate();">
<nav class="navbar navbar-expand-xl navbar-dark bg-dark">
    <!-- <a class="navbar-brand" href="#">Navbar</a> -->
    <!-- <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button> -->

    <div class="container-fluid" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${pageContext.request.contextPath}/login">Home<span class="sr-only">(current)</span></a>
            </li>

        </ul>
    </div>
</nav>

<c:if test="${registerMessage != null}">
    <c:out value="${registerMessage}"/>
</c:if>

<main class="login-form">
    <div class="cotainer">
        <div class="row justify-content-center" style="margin: auto;">
            <div class="col-md-6">
                <div class="card mt-5">
                    <div class="card-header text-center">Registration</div>
                    <div class="card-body">
                        <form action="controller" method="post" onsubmit="return validateRegisterForm();">
                            <div class="form-group row">
                                <label for="login" class="col-md-4 col-form-label text-md-right">Login</label>
                                <div class="col-md-6">
                                    <input
                                            type="text"
                                            id="login"
                                            class="form-control"
                                            name="userLogin"
                                            title="${login_pattern}"  autofocus>
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
                                            title="${password_pattern}" >
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="email" class="col-md-4 col-form-label text-md-right">Email</label>
                                <div class="col-md-6">
                                    <input
                                            type="email"
                                            id="email"
                                            class="form-control"
                                            name="email"
                                            placeholder="user@example.com">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="last-name" class="col-md-4 col-form-label text-md-right">Last name</label>
                                <div class="col-md-6">
                                    <input type="text"
                                           id="last-name"
                                           class="form-control"
                                           name="lastName"
                                           title="${name_pattern}">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="first-name" class="col-md-4 col-form-label text-md-right">First name</label>
                                <div class="col-md-6">
                                    <input
                                            type="text"
                                            id="first-name"
                                            class="form-control"
                                            name="firstName"
                                            title="${name_pattern}">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="middle-name" class="col-md-4 col-form-label text-md-right">Middle name</label>
                                <div class="col-md-6">
                                    <input
                                            type="text"
                                            id="middle-name"
                                            class="form-control"
                                            name="middleName"
                                            title="${name_pattern}">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="birth-date" class="col-md-4 col-form-label text-md-right">Birth date</label>
                                <div class="col-md-6">
                                    <input
                                            type="date"
                                            id="birth-date"
                                            class="form-control"
                                            name="birthDate"
                                            min="1930-01-01">
                                </div>
                            </div>

                            <div class="col-md-6 offset-md-4">
                                <input
                                        type="hidden"
                                        name="command"
                                        value="register">
                                <input
                                        type="submit"
                                        class="btn btn-dark"
                                        value="Register">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
</body>
</html>
