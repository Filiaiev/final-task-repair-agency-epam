<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">
<%@page import="com.filiaiev.agency.entity.Role" %>
<nav class="navbar navbar-expand-xl navbar-dark bg-dark">
    <!-- <a class="navbar-brand" href="#">Navbar</a> -->
    <!-- <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button> -->

    <div class="container-fluid" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
<%--                <a class="nav-link" href="#"><h6 class="text-ligth">Home</h6><span class="sr-only">(current)</span></a>--%>
                <a class="nav-link" href="${pageContext.request.contextPath}/home">Home<span class="sr-only">(current)</span></a>
            </li>

            <c:if test="${roleId == Role.CLIENT.ordinal()}">
                <li class="nav-item ml-4 active">
                    <form method="get" action="controller" style="margin-bottom: 0">
                        <input type="hidden" name="command" value="goToCreationForm">
                        <button class="btn btn-outline-light" type="submit">Create</button>
                    </form>
                </li>
            </c:if>

            <c:if test="${roleId != null}">
                <li class="nav-item ml-4 active">
                    <form method="get" action="controller" style="margin-bottom: 0">
                        <input type="hidden" name="command" value="getOrders">
                        <button class="btn btn-outline-light" type="submit">Orders</button>
                    </form>
                </li>
            </c:if>
        </ul>

        <c:if test="${roleId != null}">
            <h6 class="text-light">${person.lastName} ${person.firstName}</h6>
            <c:if test="${roleId == Role.CLIENT.ordinal()}">
                <h6 class="text-light ml-2">${client.cash} UAH</h6>
            </c:if>
            <form class="form-inline my-2 my-xl-0" action="controller" method="post">
                <input type="hidden" name="command" value="logout">
                <button class="btn btn-outline-light ml-3 my-sm-0" type="submit">Log out</button>
            </form>
        </c:if>
    </div>
</nav>
