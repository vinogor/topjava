<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Add new user</title>
</head>
<body>

Add or create meal

<%-- если прописать закомменченый тэг, то будет ошибка когда [addMeal], так как в атрибутах не будет такого бина --%>
<%-- <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>--%>

<form method="POST" action='meals_crud' name="frmEditOrCreateMeal">

    id : <input
        type="text" name="id"
        value="<c:out value="${meal.id}" />"/> <br/>

    dateTime : <input
        type="datetime-local" name="dateTime"
        value="<c:out value="${meal.dateTime}" />"/> <br/>

    <%-- вариант со вводом даты и времени вручную --%>
    <%--    <input--%>
    <%--        type="text" name="dateTime"--%>
    <%--        value="<c:out value="${meal.dateTime}" />"/> <br/> --%>

    description : <input
        type=" text" name="description"
        value="<c:out value="${meal.description}"/>"/> <br/>

    calories : <input
        type="text" name="calories"
        value="<c:out value="${meal.calories}" />"/> <br/>

    <button type="submit"> Submit</button>

</form>
</body>
</html>