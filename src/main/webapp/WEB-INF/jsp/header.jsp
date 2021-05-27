<link rel="stylesheet" href="style/core.css" type="text/css"/>

<header>
    <div class="headerDiv">
        <a href="controller?command=go_to_home">Home</a>
        <h3>Welcome <c:out value="${sessionScope.person.lastName} ${sessionScope.person.firstName}"/></h3>
        <p>Cash: <c:out value="${sessionScope.client.cash}"/></p>
    </div>
</header>
