<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

    <title>Add new user</title>
    <link rel="stylesheet" href="myStyle.css" type="text/css">

</head>
<body>

<h2>Add or create meal</h2>

<%-- если прописать закомменченый тэг, то будет ошибка когда [addMeal], так как в атрибутах не будет такого бина --%>
<%-- <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>--%>

<form method="POST" action='meals_crud' name="frmEditOrCreateMeal">

    <table>

        <tr>
            <th>параметр</th>
            <th>поле ввода</th>
        </tr>

        <tr hidden>
            <td> id :</td>
            <td><input
                    type="text" readonly name="id"
                    value="<c:out value="${meal.id}" />"
            />
            </td>
        </tr>

        <tr>
            <td> dateTime :</td>
            <td><input
                    type="datetime-local" name="dateTime"
                    value="<c:out value="${meal.dateTime}" />"
            />
            </td>
        </tr>

        <%-- вариант со вводом даты и времени вручную --%>
        <%--    <input--%>
        <%--        type="text" name="dateTime"--%>
        <%--        value="<c:out value="${meal.dateTime}" />"/> <br/> --%>

        <tr>
            <td> description :</td>
            <td><input
                    type="text" name="description"
                    value="<c:out value="${meal.description}"/>"
            />
            </td>
        </tr>

        <tr>
            <td> calories :</td>
            <td><input
                    type="text" name="calories"
                    value="<c:out value="${meal.calories}" />"
            />
            </td>
        </tr>
    </table>

    <br>
    <button type="submit"> Submit</button>

</form>
</body>
</html>