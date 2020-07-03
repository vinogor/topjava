<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

<html lang="ru">
<head>

    <title>meals crud</title>
    <link rel="stylesheet" href="myStyle.css" type="text/css">

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals CRUD</h2>

<table>
    <tr>
        <%--        <th>id</th>--%>
        <th>дата время</th>
        <th>описание</th>
        <th>калории</th>
        <%--        <th>превышение</th>--%>
        <th>редактировать</th>
        <th>удалить</th>
    </tr>

    <%--
    - даёт автодополнение от IDEA
    - переменная становится доступной в java вставках
    - если реальный и прописанный типы не совпадают - exception
    - чисто для вывода в JSP это тэг не обязателен
    --%>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>

    <c:forEach var="meal" items="${meals}">
        <%-- задаём цвет строки в зависимости от превышения --%>
        <tr class=${ meal.excess == true ? "exceed" : "notExceed"}>
                <%-- <td><c:out value="${meal.id}"/></td>--%>
                <%-- костыльный вариант: --%>
                <%-- <td><c:out value="${fn:replace(meal.dateTime, 'T', ', ')}"/></td>  --%>
            <td><javatime:format value="${meal.dateTime}" pattern="uuuu-MM-dd', 'HH:mm"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
                <%-- <td><c:out value="${meal.excess}"/></td>--%>
            <td><a href="meals_crud?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals_crud?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>

<p><a href="meals_crud?action=insert">Add meal</a></p>

</body>
</html>
