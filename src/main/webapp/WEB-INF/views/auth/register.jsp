<%@include file="../lib.jsp"%>
<c:set var="home" value="Home"/>
<c:if test="${user != null}">
    <c:redirect url="/controller?command=toHome"/>
</c:if>

<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">
    <link rel="stylesheet" href="style/core.css"/>
    <script src="script/script.js"></script>
    <fmt:message key="login_pattern" var="loginPattern"/>
    <fmt:message key="password_pattern" var="passwordPattern"/>
    <fmt:message key="name_pattern" var="namePattern"/>

    <fmt:message key="register" var="register"/>
    <fmt:message key="login" var="login"/>
    <fmt:message key="password" var="password"/>
    <fmt:message key="person.first_name" var="firstName"/>
    <fmt:message key="person.middle_name" var="middle“ame"/>
    <fmt:message key="person.last_name" var="lastName"/>
    <fmt:message key="person.birth_date" var="birthDate"/>
    <fmt:message key="login.form.log_in" var="logIn"/>
    <fmt:message key="register.registration" var="registration"/>
    <fmt:message key="email" var="email"/>

</head>
<body onload="setMaxDate();">
<nav class="navbar navbar-expand-xl navbar-dark bg-dark">

    <div class="container-fluid" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${pageContext.request.contextPath}/">${logIn}<span class="sr-only">(current)</span></a>
            </li>

        </ul>
    </div>
</nav>

<main class="login-form">
    <div class="cotainer">
        <div class="row justify-content-center" style="margin: auto;">
            <div class="col-md-6">
                <div class="card mt-5">
                    <div class="card-header text-center">${registration}</div>
                    <div class="card-body">
                        <form action="controller" method="post" onsubmit="return validateRegisterForm();">
                            <div class="form-group row">
                                <label for="login" class="col-md-4 col-form-label text-md-right">${login}</label>
                                <div class="col-md-6">
                                    <input
                                            type="text"
                                            id="login"
                                            class="form-control"
                                            name="userLogin"
                                            title="${loginPattern}" autofocus autocomplete="off">
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
                                            title="${passwordPattern}" autocomplete="off">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="email" class="col-md-4 col-form-label text-md-right">${email}</label>
                                <div class="col-md-6">
                                    <input
                                            type="email"
                                            id="email"
                                            class="form-control"
                                            name="email"
                                            placeholder="user@example.com" autocomplete="off">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="last-name" class="col-md-4 col-form-label text-md-right">${lastName}</label>
                                <div class="col-md-6">
                                    <input type="text"
                                           id="last-name"
                                           class="form-control"
                                           name="lastName"
                                           title="${namePattern}" autocomplete="off">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="first-name" class="col-md-4 col-form-label text-md-right">${firstName}</label>
                                <div class="col-md-6">
                                    <input
                                            type="text"
                                            id="first-name"
                                            class="form-control"
                                            name="firstName"
                                            title="${namePattern}" autocomplete="off">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="middle-name" class="col-md-4 col-form-label text-md-right">${middle“ame}</label>
                                <div class="col-md-6">
                                    <input
                                            type="text"
                                            id="middle-name"
                                            class="form-control"
                                            name="middleName"
                                            title="${namePattern}" autocomplete="off">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="birth-date" class="col-md-4 col-form-label text-md-right">${birthDate}</label>
                                <div class="col-md-6">
                                    <input
                                            type="date"
                                            id="birth-date"
                                            class="form-control"
                                            name="birthDate"
                                            min="1930-01-01" required>
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
                                        value="${register}">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<c:if test="${registerMessage != null}">
    <div class="d-flex w-100 justify-content-center align-self-center">
        <fmt:message key="register.message.${registerMessage}" var="registerMsg"/>
        <p style="margin-top: 25px;">${registerMsg}</p>
    </div>
</c:if>
</body>
</html>
