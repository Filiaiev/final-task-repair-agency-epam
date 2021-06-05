<%@ tag import="com.filiaiev.agency.entity.OrderStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="value" required="true" rtexprvalue="true"  %>
<%@attribute name="status" rtexprvalue="true" type="com.filiaiev.agency.entity.OrderStatus"%>
<c:choose>
    <c:when test="${status == OrderStatus.CREATED}">
        <c:set var="className" value="table-secondary"/>
    </c:when>

    <c:when test="${status == OrderStatus.WAITING_FOR_PAYMENT}">
        <c:set var="className" value="table-warning"/>
    </c:when>

    <c:when test="${status == OrderStatus.PAID}">
        <c:set var="className" value="table-info"/>
    </c:when>

    <c:when test="${status == OrderStatus.CANCELED}">
        <c:set var="className" value="table-danger"/>
    </c:when>
    
    <c:when test="${status == OrderStatus.IN_WORK}">
        <c:set var="className" value="table-warning"/>
    </c:when>

    <c:when test="${status == OrderStatus.COMPLETED}">
        <c:set var="className" value="table-success"/>
    </c:when>
</c:choose>

<td class="${className}"><c:out value="${value}"/></td>