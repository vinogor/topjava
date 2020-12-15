<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>

    <c:choose>
        <c:when test="${meal.id eq null}">
            <spring:message code="meal.create" var="headerMealForm"/>
        </c:when>
        <c:otherwise>
            <spring:message code="meal.update" var="headerMealForm"/>
        </c:otherwise>
    </c:choose>

    <%-- <h2>${empty meal.id ? ' Create meal' : 'Edit meal'}</h2> --%>
    <h2>${headerMealForm}</h2>

    <form method="post" action="<%=request.getContextPath()%>/meals/${empty meal.id ? 'create' : 'update'}">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.dateTime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" accept-charset="UTF-8" value="${meal.description}" size=40 name="description"
                       required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form>
</section>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
