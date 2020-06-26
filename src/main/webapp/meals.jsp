<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html lang="ru">
<head>
    <title>meals</title>

    <%--
        TD - Table Data
        TH - Table Header
        TR - Table Row
    --%>

    <style type="text/css">
        TABLE {
            border-collapse: collapse; /* убираем двойные линии между ячейками */
        }

        TH {
            background: #b0e0e6; /* цвет фона шапки */
        }

        TD, TH {
            padding: 5px; /* отступы внутри ячеек */
            border: 1px solid; /* чтобы рамка отображалась */
        }
    </style>

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <tr>
        <th>дата время</th>
        <th>описание</th>
        <th>калории</th>
        <th>превышение</th>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <tr>
            <td><c:out value="${fn:replace(meal.dateTime, 'T', ', ')}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><c:out value="${meal.excess}"/></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
