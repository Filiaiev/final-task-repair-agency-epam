<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">
<fmt:message key="nav.button.home" var="home"/>
<c:choose>
    <c:when test="${roleId == Role.CLIENT.ordinal()}">
        <fmt:message key="nav.button.user_orders" var="orders"/>
    </c:when>
    <c:when test="${roleId != Role.CLIENT.ordinal() && roleId != null}">
        <fmt:message key="nav.button.orders" var="orders"/>
    </c:when>
</c:choose>
<fmt:message key="nav.button.log_out" var="log_out"/>
<fmt:message key="nav.button.create" var="create"/>
<fmt:message key="currency.uah" var="currency"/>
<fmt:message key="profile" var="profile"/>

<%@page import="com.filiaiev.agency.entity.Role" %>
<nav class="navbar navbar-expand-xl navbar-dark bg-dark">
    <div class="container-fluid" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <c:if test="${roleId != null}">
                <li class="nav-item active">
                        <%--                <a class="nav-link" href="#"><h6 class="text-ligth">Home</h6><span class="sr-only">(current)</span></a>--%>
                    <a class="nav-link" href="controller?command=toHome">${home}<span class="sr-only">(current)</span></a>
                </li>
            </c:if>

            <c:if test="${roleId == Role.CLIENT.ordinal()}">
                <li class="nav-item ml-4 active">
                    <form method="get" action="controller" style="margin-bottom: 0">
                        <input type="hidden" name="command" value="toCreationForm">
                        <button class="btn btn-outline-light" type="submit">${create}</button>
                    </form>
                </li>
            </c:if>

            <c:if test="${roleId == null && !pageContext.request.requestURI.equals('/agency/')}">
                <li class="nav-item ml-4 active">
                        <button class="btn btn-outline-light"
                                onclick="window.location.href = '${pageContext.request.contextPath}/'">
                                ${log_in}</button>
                </li>
            </c:if>

            <c:if test="${roleId != null}">
                <li class="nav-item ml-4 active">
                    <form method="get" action="controller" style="margin-bottom: 0">
                        <input type="hidden" name="command" value="getOrders">
                        <input type="hidden" name="page" value="1">
                        <button class="btn btn-outline-light" type="submit">${orders}</button>
                    </form>
                </li>
            </c:if>
        </ul>

        <c:out value="${result}"/>
        <c:if test="${roleId != null}">
            <h6 class="text-light">${person.lastName} ${person.firstName}</h6>

            <c:if test="${roleId == Role.CLIENT.ordinal()}">
                <button class="btn btn-outline-light ml-3 my-sm-0"
                        onclick="window.location.href = '${pageContext.request.contextPath}/controller?command=toProfile'">${profile}</button>
            </c:if>

            <form class="form-inline my-2 my-xl-0" action="controller" method="post">
                <input type="hidden" name="command" value="logout">
                <button class="btn btn-outline-light ml-3 my-sm-0" type="submit">${log_out}</button>
            </form>
        </c:if>

        <c:forEach var="locale" items="${locales}">
            <a href="${pageContext.request.contextPath}/controller?command=setLocale&lang=${locale}"
               class="nav-link" style="color: white; padding-right: 2px;">${locale}</a>
        </c:forEach>
    </div>
</nav>
