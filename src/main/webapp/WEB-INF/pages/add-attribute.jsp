<%--
  Created by IntelliJ IDEA.
  User: snaric
  Date: 21-Aug-23
  Time: 7:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page errorPage="404.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="categoryBean" type="shop.ipwebshopadmin.beans.CategoryBean" scope="session"/>
<html>
<head>
    <title>Dodaj atribut</title>
    <link href="styles/form.css" rel="stylesheet">
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="container">
    <div class="login-frame">
        <div class="login-form">
            <form method="POST" action="?action=add-attribute&id=<%=categoryBean.getCategory().getId()%>">
                <div class="form-row">
                    Naziv atributa <br />
                    <label for="name">
                        <input type="text" name="name" id="name" /><br />
                    </label>
                </div>
                <button type="submit" value="Dodaj atribut" name="submit">Dodaj atribut</button>
                <h3><%=session.getAttribute("notification").toString() %></h3> <br />
            </form>
        </div>
    </div>
</div>
</body>
</html>
